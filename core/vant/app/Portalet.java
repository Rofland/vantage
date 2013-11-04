package vant.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Portalet extends HttpServlet {
	private static final long serialVersionUID = 4446185988812535088L;

	public final Map<String, Portal> portals = new HashMap<String, Portal>();
	protected final HttpVenue _session = new HttpVenue();

	@Override
	protected void doGet(HttpServletRequest rqst, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(rqst, resp);
	}

	@Override
	protected void doPost(HttpServletRequest rqst, HttpServletResponse resp)
			throws ServletException, IOException {
		Portal portal = getPortal(rqst);
		if (portal == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		resp.setContentType("text/plain;charset=utf-8");
		synchronized (this) {
			_session.setup(rqst, resp);
			portal.exec(_session);
		}
	}

	protected Portal getPortal(HttpServletRequest rqst) {
		String portal = rqst.getServletPath();
		int end = portal.indexOf('/', 1);
		portal = end > 0 ? portal.substring(1, end) : portal.substring(1);
		return portals.get(portal);
	}
}
