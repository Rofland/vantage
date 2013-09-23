package vant.lang.json;

import java.io.IOException;

public class Json {
	public static final String LANG = "json";

	public static Object parsePrimitive(String string) {
		Double d;
		if (string.equals("")) {
			return string;
		}
		if (string.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		}
		if (string.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		}
		if (string.equalsIgnoreCase("null")) {
			return null;
		}

		/*
		 * If it might be a number, try converting it. If a number cannot be
		 * produced, then the value will just be a string. Note that the plus
		 * and implied string conventions are non-standard. A JSON parser may
		 * accept non-JSON forms as long as it accepts all correct JSON forms.
		 */

		char b = string.charAt(0);
		if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
			try {
				if (string.indexOf('.') > -1 || string.indexOf('e') > -1
						|| string.indexOf('E') > -1) {
					d = Double.valueOf(string);
					if (!d.isInfinite() && !d.isNaN()) {
						return d;
					}
				} else {
					Long myLong = new Long(string);
					if (myLong.longValue() == myLong.intValue()) {
						return new Integer(myLong.intValue());
					} else {
						return myLong;
					}
				}
			} catch (Exception ignore) {
			}
		}
		return string;
	}

	public static void quote(CharSequence string, Appendable dst)
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
