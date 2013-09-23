package vant.lang;

import java.io.IOException;
import java.nio.ByteBuffer;

import vant.UTF8;

public class BufferWriter implements Writer {
	public ByteBuffer buffer;
	protected int _depth = 0;

	public BufferWriter(ByteBuffer b) {
		buffer = b;
	}
	
	@Override
	public boolean BOOL(String k, boolean v) throws IOException {
		buffer.put((byte) (v ? 1 : 0));
		return v;
	}

	@Override
	public byte BYTE(String k, byte v) throws IOException {
		buffer.put(v);
		return v;
	}

	@Override
	public char CHAR(String k, char v) throws IOException {
		buffer.putChar(v);
		return v;
	}

	@Override
	public short SHORT(String k, short v) throws IOException {
		buffer.putShort(v);
		return v;
	}

	@Override
	public int INT(String k, int v) throws IOException {
		buffer.putInt(v);
		return v;
	}

	@Override
	public long LONG(String k, long v) throws IOException {
		buffer.putLong(v);
		return v;
	}

	@Override
	public float FLOAT(String k, float v) throws IOException {
		buffer.putFloat(v);
		return v;
	}

	@Override
	public double DOUBLE(String k, double v) throws IOException {
		buffer.putDouble(v);
		return v;
	}

	@Override
	public String STRING(String k, String v) throws IOException {
		if (v == null)
			v = "";
		UTF8.write(buffer, v, 0, v.length());
		return v;
	}

	@Override
	public int depth() {
		return _depth;
	}

	@Override
	public void object(String k, boolean compact) throws IOException {
		_depth++;
	}

	@Override
	public void array(String k, int n) throws IOException {
		buffer.putInt(n);
		_depth++;
	}

	@Override
	public void end() throws IOException {
		if (_depth > 0)
			_depth--;
	}

	@Override
	public void flush() throws IOException {
	}
}