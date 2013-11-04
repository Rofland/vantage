package vant.app;

import java.io.IOException;

import vant.lang.Struct;
import vant.lang.Writer;

public abstract class Venue {
	public abstract String api();

	public abstract Struct params();

	public abstract void notFound();

	public abstract void misuse(String hint);

	public abstract void violation(int type, String hint);

	public abstract void fault(Exception e);

	public abstract Writer begin() throws IOException;

	public abstract void end() throws IOException;
}
