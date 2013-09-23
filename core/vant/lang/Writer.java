package vant.lang;

import java.io.IOException;

public interface Writer extends Exchange {
	int depth();

	void object(String k, boolean compact) throws IOException;

	void array(String k, int n) throws IOException;

	void end() throws IOException;
	
	void flush() throws IOException;
}
