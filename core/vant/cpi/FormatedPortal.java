package vant.cpi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class FormatedPortal extends Portal {
	public abstract void exec(InputStream in, OutputStream out)
			throws IOException;
}
