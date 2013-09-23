package vant.lang;

import java.nio.ByteBuffer;

import vant.UTF8;

public class BufferReader implements Reader {
	public ByteBuffer buffer;
	protected int _depth = 0;

	@Override
	public int depth() {
		return _depth;
	}

	@Override
	public boolean object(String k) {
		_depth++;
		return true;
	}

	@Override
	public int array(String k) {
		_depth++;
		return buffer.getInt();
	}

	@Override
	public void end() {
		if (_depth > 0)
			_depth--;
	}

	@Override
	public boolean BOOL(String k, boolean alt) {
		return buffer.get() != 0;
	}

	@Override
	public byte BYTE(String k, byte alt) {
		return buffer.get();
	}

	@Override
	public char CHAR(String k, char alt) {
		return buffer.getChar();
	}

	@Override
	public short SHORT(String k, short alt) {
		return buffer.getShort();
	}

	@Override
	public int INT(String k, int alt) {
		return buffer.getInt();
	}

	@Override
	public long LONG(String k, long alt) {
		return buffer.getLong();
	}

	@Override
	public float FLOAT(String k, float alt) {
		return buffer.getFloat();
	}

	@Override
	public double DOUBLE(String k, double alt) {
		return buffer.getDouble();
	}

	@Override
	public String STRING(String k, String alt) {
		return UTF8.read(buffer);
	}
}