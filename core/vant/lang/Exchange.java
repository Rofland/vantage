package vant.lang;

import java.io.IOException;

public interface Exchange {
	boolean BOOL(String k, boolean v) throws IOException;

	byte BYTE(String k, byte v) throws IOException;

	char CHAR(String k, char v) throws IOException;

	short SHORT(String k, short v) throws IOException;

	int INT(String k, int v) throws IOException;

	long LONG(String k, long v) throws IOException;

	float FLOAT(String k, float v) throws IOException;

	double DOUBLE(String k, double v) throws IOException;

	String STRING(String k, String v) throws IOException;
}
