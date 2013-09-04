package vant;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Misc {
	public static int compare(double x, double y) {
		return x == y ? 0 : x < y ? -1 : 1;
	}

	public static int compare(int x, int y) {
		return x == y ? 0 : x < y ? -1 : 1;
	}

	public static int compare(long x, long y) {
		return x == y ? 0 : x < y ? -1 : 1;
	}

	public static int unique(int[] array, int begin, int end) {
		if (begin >= end)
			return begin;
		Arrays.sort(array, begin, end);
		int prev = array[begin];
		int w = begin + 1;
		for (int r = w; r < end; r++)
			if (array[r] != prev)
				prev = array[w++] = array[r];
		return w; // w is the new end;
	}

	public static int unique(String[] array, int begin, int end) {
		if (begin >= end)
			return begin;
		Arrays.sort(array, begin, end);
		String prev = array[begin];
		int w = begin + 1;
		for (int r = w; r < end; r++)
			if (array[r] != prev)
				prev = array[w++] = array[r];
		return w; // w is the new end;
	}

	@SuppressWarnings("unchecked")
	public static <A> A reserve(A a, int count, int from, int shift,
			int extension) {
		A b = null;
		int newCount = count + shift;
		if (newCount >= Array.getLength(a)) {
			int newLength = (newCount - newCount % extension) + extension;
			b = (A) Array.newInstance(a.getClass().getComponentType(),
					newLength);
			System.arraycopy(a, 0, b, 0, from);
		} else
			b = a;
		System.arraycopy(a, from, b, from + shift, count - from);
		return b;
	}

	public static InputStream input(final ByteBuffer bb) {
		return new InputStream() {
			@Override
			public int read() {
				return bb.hasRemaining() ? bb.get() : -1;
			}
		};
	}

	public static void copy(ByteBuffer src, ByteBuffer dst) {
		int pos = src.position();
		src.rewind();
		dst.put(src);
		src.position(pos);
	}

	public static JSONObject opt(JSONObject json, String key) {
		JSONObject obj = json.optJSONObject(key);
		return obj == null ? new JSONObject() : obj;
	}

	public static boolean equals(CharSequence a, int apos, CharSequence b,
			int bpos, int n) {
		if (n > a.length() - apos || n > b.length() - bpos)
			return false;
		for (int i = 0; i < n; i++)
			if (a.charAt(apos + i) != b.charAt(bpos + i))
				return false;
		return true;
	}

	public static boolean equalsIgnoreCase(CharSequence a, int apos,
			CharSequence b, int bpos, int n) {
		if (n > a.length() - apos || n > b.length() - bpos)
			return false;
		for (int i = 0; i < n; i++) {
			char aCh = Character.toLowerCase(a.charAt(apos + i));
			char bCh = Character.toLowerCase(b.charAt(bpos + i));
			if (aCh != bCh)
				return false;
		}
		return true;
	}

	public static final byte[] ZERO_BYTE = new byte[0];
	public static final short[] ZERO_SHORT = new short[0];
	public static final int[] ZERO_INT = new int[0];
	public static final long[] ZERO_LONG = new long[0];
	public static final float[] ZERO_FLOAT = new float[0];
	public static final double[] ZERO_DOUBLE = new double[0];
	public static final String[] ZERO_STRING = new String[0];
	public static final Map<String, Object> ZERO_MAP = Collections
			.unmodifiableMap(new HashMap<String, Object>());
}
