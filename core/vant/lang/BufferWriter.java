package vant.lang;

import java.io.IOException;
import java.nio.ByteBuffer;

import vant.UTF8;

public class BufferWriter extends Writer {
	public ByteBuffer buffer;
	protected int _depth = 0;

	public BufferWriter(ByteBuffer b) {
		buffer = b;
	}

	@Override
	public Writer key(String k) throws IOException {
		return this;
	}

	@Override
	public Writer BOOL(boolean v) throws IOException {
		buffer.put((byte) (v ? 1 : 0));
		return this;
	}

	@Override
	public Writer BYTE(byte v) throws IOException {
		buffer.put(v);
		return this;
	}

	@Override
	public Writer CHAR(char v) throws IOException {
		buffer.putChar(v);
		return this;
	}

	@Override
	public Writer SHORT(short v) throws IOException {
		buffer.putShort(v);
		return this;
	}

	@Override
	public Writer INT(int v) throws IOException {
		buffer.putInt(v);
		return this;
	}

	@Override
	public Writer LONG(long v) throws IOException {
		buffer.putLong(v);
		return this;
	}

	@Override
	public Writer FLOAT(float v) throws IOException {
		buffer.putFloat(v);
		return this;
	}

	@Override
	public Writer DOUBLE(double v) throws IOException {
		buffer.putDouble(v);
		return this;
	}

	@Override
	public Writer STRING(String v) throws IOException {
		if (v == null)
			v = "";
		UTF8.write(buffer, v, 0, v.length());
		return this;
	}

	@Override
	public int depth() {
		return _depth;
	}

	@Override
	public Writer object() throws IOException {
		_depth++;
		return this;
	}
	
	@Override
	public Writer tuple() throws IOException {
		_depth++;
		return this;
	}

	@Override
	public Writer array(int n) throws IOException {
		buffer.putInt(n);
		_depth++;
		return this;
	}

	@Override
	public Writer end() throws IOException {
		if (_depth > 0)
			_depth--;
		return this;
	}

	@Override
	public Writer flush() throws IOException {
		return this;
	}
}