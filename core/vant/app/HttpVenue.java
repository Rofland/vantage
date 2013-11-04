package vant.app;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vant.Norm;
import vant.lang.CatalogReader;
import vant.lang.Struct;
import vant.lang.Writer;
import vant.lang.json.Parser;

public class HttpVenue extends Venue {
	protected String _api;
	protected Struct _params;
	protected CatalogReader _paramReader = new CatalogReader();
	protected HttpServletRequest _rqst;
	protected HttpServletResponse _resp;

	Parser parser = new Parser();

	public void setup(HttpServletRequest rqst, HttpServletResponse resp) {
		this._rqst = rqst;
		this._resp = resp;

		String json = rqst.getParameter("rpc");
		Struct call = Struct.ZERO;
		try {
			call = parser.struct(new StringReader(json));
		} catch (IOException e) {
		}
		_api = Norm.STRING(call.value("method"), "");
		if (_api.isEmpty())
			return;
		_params = call.struct("params", Struct.ZERO);
	}

	@Override
	public String api() {
		return _api;
	}

	@Override
	public Struct params() {
		return _params;
	}

	@Override
	public void notFound() {
		try {
			_resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, _api);
		} catch (IOException e) {
		}
	}

	@Override
	public void misuse(String hint) {
		try {
			_resp.sendError(HttpServletResponse.SC_FORBIDDEN, hint);
		} catch (IOException e) {
		}
	}

	@Override
	public void violation(int type, String hint) {
		try {
			_resp.sendError(HttpServletResponse.SC_CONFLICT,
					Integer.toString(type));
		} catch (IOException e) {
		}
	}

	@Override
	public void fault(Exception cause) {
		try {
			_resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					cause.getMessage());
		} catch (IOException e) {
		}
	}

	@Override
	public Writer begin() throws IOException {
		return new vant.lang.json.Writer(_resp.getWriter());
	}

	@Override
	public void end() throws IOException {
		_resp.getWriter().flush();
	}
}
