package vant.app;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import vant.Corruption;
import vant.Norm;
import vant.Usage;
import vant.Violation;
import vant.action.Action;
import vant.lang.CatalogReader;
import vant.lang.LangError;
import vant.lang.MapStruct;
import vant.lang.Struct;

public class JsonPortalRPC extends SyntacticPortal {
	protected static final Struct PARAM_EMPTY = new MapStruct();
	protected Object _rpcID;
	protected Struct _rqst, _params;
	protected String _method;
	protected vant.lang.Writer _resp;
	protected vant.lang.json.Parser _parser = new vant.lang.json.Parser();
	protected CatalogReader _catalog = new CatalogReader();

	protected final void beginResponse() throws IOException {
		_resp.object("response", false);
		_resp.STRING("jsonrpc", "2.0");
		if (_rpcID instanceof String)
			_resp.STRING("id", _rpcID.toString());
		else
			_resp.LONG("id", ((Number) _rpcID).longValue());
	}

	protected final void endResponse() throws IOException {
		_resp.end();
		_resp.flush();
	}

	@Override
	public void exec(Reader in, Writer out) throws IOException {
		_rqst = null;
		_resp = new vant.lang.json.Writer(out);
		Action action = null;
		try {
			_rqst = _parser.struct(in);
		} catch (LangError e) {
			parseFailure(e.getMessage());
			return;
		}

		String version = Norm.STRING(_rqst.value("jsonrpc"), null);
		_rpcID = Norm.INT(_rqst.value("id"), -1);
		_method = Norm.STRING(_rqst.value("method"), null);

		if (!"2.0".equals(version)) {
			notRequest("Field 'jsonrpc' must be '2.0'");
			return;
		}
		if (_rpcID == null) {
			notRequest("Field 'id' must integer or non-empty string.");
			return;
		} else if (_rpcID instanceof String && _rpcID.toString().isEmpty()) {
			notRequest("Field 'id' must integer or non-empty string.");
			return;
		} else if (_rpcID instanceof Number) {
			Number id = (Number) _rpcID;
			if (id.doubleValue() - id.longValue() != 0) {
				notRequest("Field 'id' must integer or non-empty string.");
				return;
			}
		} else if (_method == null) {
			notRequest("Field 'method' is missing.");
			return;
		}
		action = actions.get(_method);
		if (action == null) {
			notFound(_method);
			return;
		}
		_params = _rqst.struct("params", PARAM_EMPTY);
		_catalog.root(_params);

		try {
			action.decode(_catalog);
			action.validate();
			action.perform();
		} catch (Usage e) {
			misuse(e.getMessage());
			return;
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

		beginResponse();
		action.result("result", _resp);
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
		beginResponse();
		_resp.object("error", false);
		_resp.INT("code", -32603);
		_resp.STRING("message", hint);
		_resp.INT("data", type);
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
		beginResponse();
		_resp.object("error", false);
		_resp.INT("code", code);
		_resp.STRING("message", msg);
		_resp.STRING("data", data);
		_resp.end();
		endResponse();
	}
}
