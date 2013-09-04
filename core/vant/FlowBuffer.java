package vant;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class FlowBuffer {
	public ByteBuffer buffer;

	public class Input extends FlowBuffer implements Flow.Reader {
		@Override
		public boolean BOOL(boolean alt) {
			return buffer.get() != 0;
		}

		@Override
		public byte BYTE(byte alt) {
			return buffer.get();
		}

		@Override
		public char CHAR(char alt) {
			return buffer.getChar();
		}

		@Override
		public short SHORT(short alt) {
			return buffer.getShort();
		}

		@Override
		public int INT(int alt) {
			return buffer.getInt();
		}

		@Override
		public long LONG(long alt) {
			return buffer.getLong();
		}

		@Override
		public float FLOAT(float alt) {
			return buffer.getFloat();
		}

		@Override
		public double DOUBLE(double alt) {
			return buffer.getDouble();
		}

		@Override
		public String STRING(String alt) {
			return UTF8.read(buffer);
		}
	}

	public class Output extends FlowBuffer implements Flow.Writer {

		@Override
		public void value(boolean v) throws IOException {
			buffer.put((byte) (v ? 1 : 0));
		}

		@Override
		public void value(byte v) throws IOException {
			buffer.put(v);
		}

		@Override
		public void value(char v) throws IOException {
			buffer.putChar(v);

		}

		@Override
		public void value(short v) throws IOException {
			buffer.putShort(v);
		}

		@Override
		public void value(int v) throws IOException {
			buffer.putInt(v);
		}

		@Override
		public void value(long v) throws IOException {
			buffer.putLong(v);
		}

		@Override
		public void value(float v) throws IOException {
			buffer.putFloat(v);
		}

		@Override
		public void value(double v) throws IOException {
			buffer.putDouble(v);
		}

		@Override
		public void value(CharSequence v) throws IOException {
			UTF8.write(buffer, v);
		}

	}
}