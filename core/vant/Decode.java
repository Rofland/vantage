package vant;

public final class Decode {
	public static long LONG(CharSequence nm, int start, int end) {
		int radix = 10;
		int index = start;
		boolean negative = false;
		if (nm == null || start < 0 || end <= start || end > nm.length())
			throw new NumberFormatException("" + nm.subSequence(start, end));

		char firstChar = nm.charAt(index);
		if (firstChar == '-') {
			negative = true;
			index++;
		}

		// Handle radix specifier, if present
		if (Misc.equalsIgnoreCase(nm, index, "0x", 0, 2)) {
			index += 2;
			radix = 16;
		} else if (nm.charAt(index) == '#') {
			index++;
			radix = 16;
		} else if (nm.charAt(index) == '0' && end > 1 + index) {
			index++;
			radix = 8;
		}

		long limit = negative ? Long.MIN_VALUE : -Long.MAX_VALUE;
		long result = parse(limit, radix, nm, index, end);
		return negative ? result : -result;
	}

	private static long parse(long limit, int radix, CharSequence s, int start,
			int end) {
		int result = 0;
		long multmin;
		int digit;

		multmin = limit / radix;
		for (int index = start; index < end; index++) {
			// Accumulating negatively avoids surprises near MAX_VALUE
			digit = Character.digit(s.charAt(index), radix);
			if (digit < 0 || result < multmin)
				throw new NumberFormatException("" + s.subSequence(start, end));
			result *= radix;
			if (result < limit + digit)
				throw new NumberFormatException("" + s.subSequence(start, end));
			result -= digit;
		}
		return result;
	}
}
