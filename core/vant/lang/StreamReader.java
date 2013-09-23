package vant.lang;

import java.io.DataInput;
import java.io.IOException;

public class StreamReader implements Reader {
	public DataInput in;
	protected int _depth;

	public StreamReader(DataInput in) {
		this.in = in;
	}
	
	@Override
	public int depth() {
		return _depth;
	}

	@Override
	public boolean object(String k) throws IOException {
		_depth++;
		return true;
	}

	@Override
	public int array(String k) throws IOException {
		_depth++;
		return in.readInt();
	}

	@Override
	public void end() throws IOException {
		if (_depth > 0)
			_depth--;
	}

	@Override
	public boolean BOOL(String k, boolean alt) throws IOException {
		return in.readBoolean();
	}

	@Override
	public byte BYTE(String k, byte alt) throws IOException {
		return in.readByte();
	}

	@Override
	public char CHAR(String k, char alt) throws IOException {
		return in.readChar();
	}

	@Override
	public short SHORT(String k, short alt) throws IOException {
		return in.readShort();
	}

	@Override
	public int INT(String k, int alt) throws IOException {
		return in.readInt();
	}

	@Override
	public long LONG(String k, long alt) throws IOException {
		return in.readLong();
	}

	@Override
	public float FLOAT(String k, float alt) throws IOException {
		return in.readFloat();
	}

	@Override
	public double DOUBLE(String k, double alt) throws IOException {
		return in.readDouble();
	}

	@Override
	public String STRING(String k, String alt) throws IOException {
		return in.readUTF();
	}
}