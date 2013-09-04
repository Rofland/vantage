package vant;

import java.io.IOException;

public final class Flow {
	private Flow() {
	}

	public interface Reader {
		boolean BOOL(boolean alt) throws IOException;

		byte BYTE(byte alt) throws IOException;

		char CHAR(char alt) throws IOException;

		short SHORT(short alt) throws IOException;

		int INT(int alt) throws IOException;

		long LONG(long alt) throws IOException;

		float FLOAT(float alt) throws IOException;

		double DOUBLE(double alt) throws IOException;

		String STRING(String alt) throws IOException;
	}

	public interface Writer {
		void value(boolean v) throws IOException;

		void value(byte v) throws IOException;

		void value(char v) throws IOException;

		void value(short v) throws IOException;

		void value(int v) throws IOException;

		void value(long v) throws IOException;

		void value(float v) throws IOException;

		void value(double v) throws IOException;

		void value(CharSequence v) throws IOException;
	}
}
