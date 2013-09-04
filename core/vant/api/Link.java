package vant.api;

public abstract class Link implements Countable {
	public abstract void join(int src, int dst) throws Exception;

	public abstract void chop(int src, int dst) throws Exception;

	public abstract int list(int src, int from, int size, int[] dsts)
			throws Exception;
}
