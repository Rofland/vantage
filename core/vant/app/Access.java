package vant.app;

import java.util.HashMap;
import java.util.Map;

import vant.action.Action;

public class Access implements App.Access {
	protected final Map<String, Action> _actions = new HashMap<String, Action>();
	protected Map<String, Action> _tmp = _actions;

	@Override
	public void action(String sym, Action a) {
		if (_tmp != null)
			_tmp.put(sym, a);
	}

	@Override
	public Action action(String sym) {
		return _actions.get(sym);
	}

	public void seal() {
		_tmp = null;
	}
}