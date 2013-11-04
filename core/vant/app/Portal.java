package vant.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vant.Usage;
import vant.Violation;
import vant.action.Action;
import vant.lang.CatalogReader;

public class Portal {
	public final Map<String, Action> actions = new HashMap<String, Action>();
	protected final CatalogReader _paramReader = new CatalogReader();

	public synchronized void exec(Venue s) throws IOException {
		Action action = actions.get(s.api());
		if (action == null)
			s.notFound();
		action.cleanup();
		_paramReader.root(s.params());
		try {
			action.decode(_paramReader);
			action.validate();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Usage e) {
			s.misuse(e.getMessage());
			return;
		} catch (RuntimeException e) {
			s.fault(e);
			return;
		}

		try {
			action.perform();
		} catch (Violation e) {
			s.violation(e.type, e.getMessage());
		} catch (Exception e) {
			s.fault(e);
		}

		action.result(s.begin());
		s.end();
	}
}
