package vant;

public class Norm {
	public static boolean BOOL(Object raw) {
		return BOOL(raw, false);
	}

	public static short SHORT(Object raw) {
		return SHORT(raw, (short) 0);
	}

	public static int INT(Object raw) {
		return INT(raw, 0);
	}

	public static long LONG(Object raw) {
		return LONG(raw, 0L);
	}

	public static float FLOAT(Object raw) {
		return FLOAT(raw, 0f);
	}

	public static double DOUBLE(Object raw) {
		return DOUBLE(raw, 0.0);
	}

	public static String STRING(Object raw) {
		return STRING(raw, "");
	}

	public static boolean BOOL(Object raw, boolean opt) {
		if (raw == null)
			return opt;
		else if (raw instanceof Boolean)
			return ((Boolean) raw).booleanValue();
		else if (raw instanceof Number)
			return raw.equals(0) == false;
		else if (raw instanceof String) {
			String bool = (String) raw;
			if (bool.equalsIgnoreCase("true") || bool.equals("yes"))
				return true;
			if (bool.equalsIgnoreCase("false") || bool.equals("no"))
				return false;
			else
				return opt;
		} else
			return opt;
	}

	public static short SHORT(Object raw, short opt) {
		if (raw == null)
			return opt;
		else if (raw instanceof Number)
			return ((Number) raw).shortValue();
		else if (raw instanceof String)
			return Short.decode((String) raw);
		else
			return opt;
	}

	public static int INT(Object raw, int opt) {
		if (raw == null)
			return opt;
		else if (raw instanceof Number)
			return ((Number) raw).intValue();
		else if (raw instanceof String)
			return Integer.decode((String) raw);
		else
			return opt;
	}

	public static long LONG(Object raw, long opt) {
		if (raw == null)
			return opt;
		else if (raw instanceof Number)
			return ((Number) raw).longValue();
		else if (raw instanceof String)
			return Long.decode((String) raw);
		else
			return opt;
	}

	public static float FLOAT(Object raw, float opt) {
		if (raw == null)
			return opt;
		else if (raw instanceof Number)
			return ((Number) raw).floatValue();
		else if (raw instanceof String)
			return Float.parseFloat((String) raw);
		else
			return opt;

	}

	public static double DOUBLE(Object raw, double opt) {
		if (raw == null)
			return opt;
		else if (raw instanceof Number)
			return ((Number) raw).doubleValue();
		else if (raw instanceof String)
			return Double.parseDouble((String) raw);
		else
			return opt;
	}

	public static String STRING(Object raw, String opt) {
		return raw == null ? opt : raw.toString();
	}
}
