package vant.cpi;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import vant.Corruption;
import vant.Json;
import vant.Usage;
import vant.Violation;
import vant.api.Action;

public class JsonPortalRPC extends SyntacticPortal {
	protected static final JSONObject PARAM_EMPTY = new JSONObject();
	protected int _rpcID;
	protected JSONObject _rqst, _params;
	protected String _method;
	protected Json.Writer _resp;
	protected Writer _writer;

	protected final void beginResponse(boolean ok) throws IOException {
		_resp.object();
		_resp.key("jsonrpc").value("2.0");
		_resp.key("id").value(_rpcID);
		_resp.key(ok ? "result" : "error");
	}

	protected final void endResponse() throws IOException {
		_resp.end();
		_writer.flush();
	}

	@Override
	public void exec(Reader in, Writer out) throws IOException {
		_writer = out;
		_resp = new Json.Writer(_writer);
		Action action = null;
		try {
			_rqst = null;
			_rqst = new JSONObject(new JSONTokener(in));
		} catch (JSONException e) {
			if (e.getCause() instanceof IOException)
				throw (IOException) e.getCause();
			else
				parseFailure(e.getMessage());
			return;
		}

		try {
			if (!"2.0".equals(_rqst.getString("jsonrpc")))
				throw new JSONException("Field 'jsonrpc' must be '2.0'");
			_rpcID = _rqst.getInt("id");
			_method = _rqst.getString("method");
			action = actions.get(_method);
			if (action == null) {
				notFound(_method);
				return;
			}
			_params = _rqst.optJSONObject("params");
			if (_params == null && _rqst.has("params")) {
				notRequest("Field 'params' must be object, if present.");
				return;
			}
			if (_params == null)
				_params = PARAM_EMPTY;
			action.parse(_params);
		} catch (JSONException e) {
			notRequest(e.getMessage());
			return;
		} catch (Usage e) {
			misuse(e.getMessage());
			return;
		}

		try {
			action.perform();
		} catch (Violation e) {
			violation(e.type, e.getMessage());
			return;
		} catch (Corruption e) {
			corruption(e.getMessage());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			fault(e.getClass().getName() + ": " + e.getMessage());
			return;
		}

		beginResponse(true);
		action.result(_resp);
		endResponse();
	}

	@Override
	protected void parseFailure(String hint) throws IOException {
		error(-32700, "Parse error", hint);
	}

	@Override
	protected final void notRequest(String hint) throws IOException {
		error(-32600, "Invalid Request", hint);
	}

	@Override
	protected final void notFound(String hint) throws IOException {
		error(-32601, "Method not found", hint);
	}

	@Override
	protected final void misuse(String hint) throws IOException {
		error(-32602, "Invalid params", hint);
	}

	@Override
	protected final void violation(int type, String hint) throws IOException {
		beginResponse(false);
		_resp.object();
		_resp.key("code").value(-32603);
		_resp.key("message").value(hint);
		_resp.key("data").value(type);
		_resp.end();
		endResponse();
	}

	@Override
	protected final void fault(String hint) throws IOException {
		error(-32604, "Internal error", hint);
	}

	@Override
	protected final void corruption(String hint) throws IOException {
		error(-32605, "Internal error", hint);
	}

	protected final void error(int code, String msg, String data)
			throws IOException {
		beginResponse(false);
		_resp.object();
		_resp.key("code").value(code);
		_resp.key("message").value(msg);
		_resp.key("data").value(data);
		_resp.end();
		endResponse();
	}
}
