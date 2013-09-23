package vant;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class UTF8 {
	private UTF8() {
	};

	public static final Charset CHARSET = Charset.forName("UTF-8");

	public static ThreadLocal<StringBuilder> builder = new ThreadLocal<StringBuilder>() {
		@Override
		protected StringBuilder initialValue() {
			return new StringBuilder();
		}
	};

	public static int length(CharSequence s, int start, int end) {
		int length = 0;
		for (int i = start; i < end; i++) {
			char c = s.charAt(i);
			if (c < 0x80)
				length += 1;
			else if (c < 0x800)
				length += 2;
			else
				length += 3;
		}
		return length;
	}

	public static String read(ByteBuffer bin) {
		short n = bin.getShort();
		if (n == 0)
			return "";
		if (bin.hasArray())
			return new String(bin.array(), bin.arrayOffset() + bin.position(),
					n, CHARSET);
		byte[] bytes = new byte[n];
		bin.get(bytes);
		return new String(bytes, CHARSET);
	}

	public static String read(DataInput in) throws IOException {
		short n = in.readShort();
		if (n == 0)
			return "";
		StringBuilder sb = builder.get();
		sb.setLength(0);
		for (int i = 0; i < n; i++) {
			char c = 0;
			byte b = in.readByte();
			if ((b & 0xe0) == 0xe0) {
				c |= b & 0x0f;
				c <<= 6;
				c |= in.readByte() & 0x3f;
				c <<= 6;
				c |= in.readByte() & 0x3f;
			} else if ((b & 0xc0) == 0xc0) {
				c |= b & 0x1f;
				c <<= 6;
				c |= in.readByte() & 0x3f;
			} else if (b > 0)
				c = (char) b;
			sb.append(c);
		}
		return sb.toString();
	}

	public static void write(ByteBuffer bin, CharSequence s, int start, int end) {
		bin.putShort((short) length(s, start, end));
		for (int i = start; i < end; i++) {
			char c = s.charAt(i);
			if (c < 0x80) {
				bin.put((byte) c);
			} else if (c < 0x800) {
				bin.put((byte) (((c >> 6) & 0x1f) | 0xc0));
				bin.put((byte) ((c & 0x3f) | 0x80));
			} else {
				bin.put((byte) (c >> 12 | 0xe0));
				bin.put((byte) (((c >> 6) & 0x3f) | 0x80));
				bin.put((byte) ((c & 0x3f) | 0x80));
			}
		}
	}

	public static void write(DataOutput out, CharSequence s, int start, int end)
			throws IOException {
		out.writeShort((short) length(s, start, end));
		for (int i = start; i < end; i++) {
			char c = s.charAt(i);
			if (c < 0x80) {
				out.write(c);
			} else if (c < 0x800) {
				out.write(((c >> 6) & 0x1f) | 0xc0);
				out.write((c & 0x3f) | 0x80);
			} else {
				out.write(c >> 12 | 0xe0);
				out.write(((c >> 6) & 0x3f) | 0x80);
				out.write((c & 0x3f) | 0x80);
			}
		}
	}

	public static InputStream asStream(final CharSequence cs) {
		return new InputStream() {
			private final int length = cs.length();
			private int index = 0;
			private int residue = 0;

			@Override
			public int read() throws IOException {
				if (residue > 0) {
					int code = residue | 0x80;
					residue >>= 8;
					return code;
				} else if (index == length)
					return -1;

				char c = cs.charAt(index++);
				if (c < 0x80)
					return c;
				residue |= c & 0x3f;
				c >>= 6;
				if (c < 0x20)
					return c | 0xc0;
				residue <<= 8;
				residue |= c & 0x3f;
				return (c >> 6) | 0xe0;
			}
		};
	}
}
