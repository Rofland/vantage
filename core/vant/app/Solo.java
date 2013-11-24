package vant.app;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import vant.Usage;
import vant.Violation;
import vant.action.Action;
import vant.lang.CatalogReader;

public class Solo {
	public static void start(App.Access access, String webRoot)
			throws Exception {
		Servlet servlet = new AccessServlet(access);
		Server server = new Server(8100);

		Context servletContext = new Context();
		servletContext.setAllowNullPathInfo(true);
		servletContext.setContextPath("/api");
		servletContext.addServlet(new ServletHolder(servlet), "/");
		server.addHandler(servletContext);

		WebAppContext ui = new WebAppContext(webRoot, "/");
		server.addHandler(ui);

		server.start();
	}
}

class AccessServlet extends HttpServlet {
	private static final long serialVersionUID = 5557358343745233663L;

	private final App.Access _access;
	protected final HttpVenue _venue = new HttpVenue();
	protected CatalogReader _paramReader = new CatalogReader();

	public AccessServlet(App.Access acs) {
		_access = acs;
	}

	@Override
	protected void doGet(HttpServletRequest rqst, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(rqst, resp);
	}

	@Override
	protected void doPost(HttpServletRequest rqst, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain;charset=utf-8");
		synchronized (this) {
			_venue.setup(rqst, resp);
			Action action = _access.action(_venue.api());
			if (action == null) {
				_venue.notFound();
				return;
			}
			action.cleanup();
			_paramReader.root(_venue.params());
			try {
				action.decode(_paramReader);
				action.validate();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (Usage e) {
				_venue.misuse(e.getMessage());
				return;
			} catch (NumberFormatException e) {
				_venue.misuse("Unrecognizable as number." + e.getMessage());
				return;
			}

			try {
				action.perform();
			} catch (Violation e) {
				_venue.violation(e.type, e.getMessage());
			} catch (Exception e) {
				_venue.fault(e);
			}

			action.result(_venue.begin());
			_venue.end();
		}
	}
}