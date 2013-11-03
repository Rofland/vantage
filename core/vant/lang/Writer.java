package vant.lang;

import java.io.IOException;

public abstract class Writer implements Exchange {
	public abstract int depth();

	public abstract Writer key(String k) throws IOException;

	public abstract Writer object() throws IOException;

	public abstract Writer tuple() throws IOException;

	public abstract Writer array(int n) throws IOException;

	public abstract Writer BOOL(boolean v) throws IOException;

	public abstract Writer BYTE(byte v) throws IOException;

	public abstract Writer CHAR(char v) throws IOException;

	public abstract Writer SHORT(short v) throws IOException;

	public abstract Writer INT(int v) throws IOException;

	public abstract Writer LONG(long v) throws IOException;

	public abstract Writer FLOAT(float v) throws IOException;

	public abstract Writer DOUBLE(double v) throws IOException;

	public abstract Writer STRING(String v) throws IOException;

	public abstract Writer end() throws IOException;

	public abstract Writer flush() throws IOException;

	@Override
	public boolean BOOL(String k, boolean v) throws IOException {
		key(k).BOOL(v);
		return v;
	}

	@Override
	public byte BYTE(String k, byte v) throws IOException {
		key(k).BYTE(v);
		return v;
	}

	@Override
	public char CHAR(String k, char v) throws IOException {
		key(k).CHAR(v);
		return v;
	}

	@Override
	public short SHORT(String k, short v) throws IOException {
		key(k).SHORT(v);
		return v;
	}

	@Override
	public int INT(String k, int v) throws IOException {
		key(k).INT(v);
		return v;
	}

	@Override
	public long LONG(String k, long v) throws IOException {
		key(k).LONG(v);
		return v;
	}

	@Override
	public float FLOAT(String k, float v) throws IOException {
		key(k).FLOAT(v);
		return v;
	}

	@Override
	public double DOUBLE(String k, double v) throws IOException {
		key(k).DOUBLE(v);
		return v;
	}

	@Override
	public String STRING(String k, String v) throws IOException {
		key(k).STRING(v);
		return v;
	}
}
