package vant;

import java.util.regex.Pattern;

public class Ensure {
	public static <T> T notNull(String what, T ref) throws Usage {
		if (ref == null)
			throw new Usage(what, "Should not be null.");
		return ref;
	}

	public static <T extends CharSequence> T ascii(String what, T s)
			throws Usage {
		int n = s.length();
		for (int i = 0; i < n; i++)
			if (s.charAt(i) > 0x7f)
				throw new Usage(what, "Only ASCII characters allowed.");
		return s;
	}

	public static <T extends CharSequence> T symbol(String what, T s)
			throws Usage {
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z')
				continue;
			if (c == '.' && i > 0)
				continue;
			throw new Usage(what,
					"Symbol allows a-z, 0-9 and dot(.), not starting with dot.");
		}
		return s;
	}

	public static <T extends CharSequence> T pattern(String what, Pattern p, T s)
			throws Usage {
		if (p.matcher(s).matches())
			return s;
		throw new Usage(what, "Must follow pattern " + p.toString());
	}

	@SuppressWarnings("unchecked")
	public static <T> T type(Class<T> clazz, Object obj) {
		if (clazz.isInstance(obj))
			return (T) obj;
		throw new RuntimeException("Expecting " + clazz.getName() + ". Got "
				+ obj.getClass().getName());
	}

	public static int id(String what, int id) throws Usage {
		if (id < 1)
			throw new Usage(what, "Must be a positive integer.");
		return id;
	}

	public static int range(String what, int v, int min, int max) throws Usage {
		if (v < min || v > max)
			throw new Usage(what, "Must range in [" + min + "," + max + "].");
		return v;
	}

	public static long range(String what, long v, long min, long max)
			throws Usage {
		if (v < min || v > max)
			throw new Usage(what, "Must range in [" + min + "," + max + "].");
		return v;
	}
}
