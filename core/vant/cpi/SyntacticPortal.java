package vant.cpi;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public abstract class SyntacticPortal extends Portal {
	public abstract void exec(Reader input, Writer output) throws IOException;
}
