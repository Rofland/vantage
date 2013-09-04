package vant.spi;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vant.cpi.SyntacticPortal;

public class Portalet extends HttpServlet {
	private static final long serialVersionUID = 4446185988812535088L;

	public final Map<String, SyntacticPortal> portals = new HashMap<String, SyntacticPortal>();

	@Override
	protected void doGet(HttpServletRequest rqst, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(rqst, resp);
	}

	@Override
	protected void doPost(HttpServletRequest rqst, HttpServletResponse resp)
			throws ServletException, IOException {
		SyntacticPortal portal = getPortal(rqst);
		if (portal == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String rpc = rqst.getParameter("rpc");
		if (rpc == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Reader r = new StringReader(rpc);
		Writer w = new OutputStreamWriter(resp.getOutputStream());
		try {
			synchronized (portal) {
				resp.setContentType("text/plain;charset=utf-8");
				portal.exec(r, w);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected SyntacticPortal getPortal(HttpServletRequest rqst) {
		String portal = rqst.getServletPath();
		int end = portal.indexOf('/', 1);
		portal = end > 0 ? portal.substring(1, end) : portal.substring(1);
		return portals.get(portal);
	}
}
