package vant;

import java.io.IOException;

public abstract class Lang {
	public static class SyntaxError extends RuntimeException {
		private static final long serialVersionUID = 5904865438980858240L;

		public final String lang;
		public final String hint;

		public SyntaxError(String lang, String hint) {
			super(String.format("[%s] %s", lang, hint));
			this.lang = lang;
			this.hint = hint;
		}
	}

	public interface Writer extends Flow.Writer {
		Writer object() throws IOException;

		Writer array() throws IOException;

		Writer end() throws IOException;

		Writer key(CharSequence k) throws IOException;
	}
}