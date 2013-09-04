package vant.cpi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vant.api.Action;

public abstract class Portal {
	public final Map<String, Action> actions = new HashMap<String, Action>();
	
	protected abstract void parseFailure(String hint) throws IOException;

	protected abstract void notRequest(String hint) throws IOException;

	protected abstract void notFound(String hint) throws IOException;

	protected abstract void misuse(String hint) throws IOException;

	protected abstract void violation(int type, String hint) throws IOException;

	protected abstract void fault(String hint) throws IOException;

	protected abstract void corruption(String hint) throws IOException;
}
