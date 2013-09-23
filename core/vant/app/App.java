package vant.app;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import vant.Norm;
import vant.lang.Struct;

public class App {
	protected final Portalet portalet = new Portalet();

	public Portal portal(String ap, Struct conf) {
		Portal portal = portalet.portals.get(ap);
		if (portal == null) {
			SyntacticPortal sportal = new JsonPortalRPC();
			portalet.portals.put(ap, sportal);
			portal = sportal;
		}
		return portal;
	}

	public void launch(String root, Struct conf) throws Exception {
		Server server = new Server(Norm.INT(conf.value("port"), 8100));

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
