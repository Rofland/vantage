package vant;

import java.io.IOException;
import java.util.BitSet;

public class Json {
	public static final String LANG = "json";

	public static class Writer implements Lang.Writer {
		private static final int maxdepth = 200;
		private boolean _comma = false;
		protected char _mode = 'i';
		private final BitSet _stack = new BitSet();
		private int _depth = 0;
		protected Appendable _dst;

		public Writer(Appendable appender) {
			this._dst = appender;
		}

		@Override
		public Writer object() throws IOException {
			if (_depth >= maxdepth)
				throw new Lang.SyntaxError(LANG, "Nesting too deep.");
			if (_mode != 'i' && _mode != 'o' && _mode != 'a')
				throw new Lang.SyntaxError(LANG, "Misplaced object.");
			_stack.set(_depth, true);
			_depth += 1;
			contingency(false, 'k');
			_dst.append("{");
			return this;
		}

		@Override
		public Writer array() throws IOException {
			if (this._mode != 'i' && this._mode != 'o' && this._mode != 'a')
				throw new Lang.SyntaxError(LANG, "Misplaced array.");
			if (_depth >= maxdepth)
				throw new Lang.SyntaxError(LANG, "Nesting too deep.");
			_stack.set(_depth, false);
			_depth += 1;
			contingency(false, 'a');
			_dst.append('[');
			return this;
		}

		@Override
		public Writer end() throws IOException {
			if (_mode != 'a' && _mode != 'k')
				throw new Lang.SyntaxError(LANG, "Nesting error.");
			_depth--;
			if (_mode == 'k') {
				_dst.append('\n');
				for (int i = 0; i < _depth; i++)
					_dst.append('\t');
			}
			_dst.append(_mode == 'k' ? '}' : ']');

			_mode = _depth == 0 ? 'd' : _stack.get(_depth - 1) ? 'k' : 'a';
			_comma = true;
			return this;
		}

		@Override
		public Writer key(CharSequence string) throws IOException {
			if (_mode != 'k')
				throw new Lang.SyntaxError(LANG, "Misplaced key.");
			if (string == null || string.length() == 0)
				throw new Lang.SyntaxError(LANG,
						"Key must be non-empty string.");
			contingency(false, 'o');
			_dst.append('\n');
			for (int i = 0; i < _depth; i++)
				_dst.append('\t');
			quote(string, _dst);
			_dst.append(':');
			return this;
		}

		@Override
		public void value(boolean v) throws IOException {
			preValue();
			_dst.append(Boolean.toString(v));
		}

		@Override
		public void value(byte v) throws IOException {
			preValue();
			_dst.append(Byte.toString(v));

		}

		@Override
		public void value(char v) throws IOException {
			value(Character.toString(v));
		}

		@Override
		public void value(short v) throws IOException {
			preValue();
			_dst.append(Short.toString(v));
		}

		@Override
		public void value(int v) throws IOException {
			preValue();
			_dst.append(Integer.toString(v));
		}

		public void value(long v) throws IOException {
			preValue();
			_dst.append(Long.toString(v));
		}

		@Override
		public void value(float v) throws IOException {
			preValue();
			_dst.append(Float.isInfinite(v) || Float.isNaN(v) ? "null" : Float
					.toString(v));
		}

		@Override
		public void value(double v) throws IOException {
			preValue();
			_dst.append(Double.isInfinite(v) || Double.isNaN(v) ? "null"
					: Double.toString(v));
		}

		@Override
		public void value(CharSequence s) throws IOException {
			preValue();
			if (s == null)
				_dst.append("null");
			else
				quote(s, _dst);
		}

		private final void preValue() throws IOException {
			if (_mode != 'a' && _mode != 'o')
				throw new Lang.SyntaxError(LANG, "Misplaced primitive.");
			contingency(true, _mode == 'o' ? 'k' : _mode);
		}

		private void contingency(boolean more, char mode) throws IOException {
			if (_comma)
				_dst.append(',');
			_comma = more;
			_mode = mode;
		}

		private static void quote(CharSequence string, Appendable dst)
				throws IOException {
			if (string == null || string.length() == 0) {
				dst.append("\"\"");
				return;
			}

			char b;
			char c = 0;
			String hhhh;
			int i;
			int len = string.length();

			dst.append('"');
			for (i = 0; i < len; i += 1) {
				b = c;
				c = string.charAt(i);
				switch (c) {
				case '\\':
				case '"':
					dst.append('\\');
					dst.append(c);
					break;
				case '/':
					if (b == '<') {
						dst.append('\\');
					}
					dst.append(c);
					break;
				case '\b':
					dst.append("\\b");
					break;
				case '\t':
					dst.append("\\t");
					break;
				case '\n':
					dst.append("\\n");
					break;
				case '\f':
					dst.append("\\f");
					break;
				case '\r':
					dst.append("\\r");
					break;
				default:
					if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
							|| (c >= '\u2000' && c < '\u2100')) {
						dst.append("\\u");
						hhhh = Integer.toHexString(c);
						dst.append("0000", 0, 4 - hhhh.length());
						dst.append(hhhh);
					} else {
						dst.append(c);
					}
				}
			}
			dst.append('"');
		}
	}
}