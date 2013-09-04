package vant.spi;

import org.json.JSONObject;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import vant.cpi.JsonPortalRPC;
import vant.cpi.Portal;
import vant.cpi.SyntacticPortal;

public class App {
	protected final Portalet portalet = new Portalet();

	public Portal portal(String ap, JSONObject conf) {
		Portal portal = portalet.portals.get(ap);
		if (portal == null) {
			SyntacticPortal sportal = new JsonPortalRPC();
			portalet.portals.put(ap, sportal);
			portal = sportal;
		}
		return portal;
	}

	public void launch(String root, JSONObject conf) throws Exception {
		Server server = new Server(conf.optInt("port", 8100));

		Context servletContext = new Context();
		servletContext.setAllowNullPathInfo(true);
		servletContext.setContextPath("/api");
		servletContext.addServlet(new ServletHolder(portalet), "/");
		server.addHandler(servletContext);

		WebAppContext ui = new WebAppContext(root, "/");
		server.addHandler(ui);

		server.start();
	}
}
