package vant.action;

import java.io.IOException;

import vant.Usage;
import vant.lang.Exchange;
import vant.lang.Writer;

public abstract class Action {
	public abstract void perform() throws Exception;

	public void cleanup() {
	}

	public abstract int binaryLimit();

	public abstract void validate() throws Usage;

	public abstract void decode(Exchange r) throws IOException, Usage;

	public abstract void encode(Exchange w) throws IOException;

	public abstract void result(Writer w) throws IOException;
}
