package vant.action;

import java.io.IOException;

import vant.lang.Exchange;

public abstract class Magic {
	public abstract void turn(int[] ids, int offset, int count)
			throws Exception;

	public abstract void prestige(int i, Exchange w) throws IOException;

	public abstract void reset();
}