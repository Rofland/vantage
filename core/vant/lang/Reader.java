package vant.lang;

import java.io.IOException;

public interface Reader extends Exchange {
	int depth();

	boolean object(String k) throws IOException;

	int array(String k) throws IOException;

	void end() throws IOException;
}
