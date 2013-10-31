package vant.model;

import vant.index.Countable;

public abstract class Link implements Relation, Countable {
	public abstract void put(int k, int v) throws Exception;

	public abstract void cut(int k, int v) throws Exception;

	public abstract int list(int k, int from, int size, int[] vs)
			throws Exception;
}
