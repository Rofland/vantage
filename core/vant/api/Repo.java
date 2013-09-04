package vant.api;

import vant.Silently;

public abstract class Repo<T extends Tuple> {
	public final Class<T> clazz;
	public final T proto;
	protected int _count;

	public Repo(Class<T> clazz) {
		this.clazz = clazz;
		this.proto = Silently.create(clazz);
	}

	public final int count() {
		return _count;
	}

	public abstract int create(T tuple) throws Exception;

	public abstract void update(int id, T tuple) throws Exception;

	public abstract void load(int[] ids, T[] tuples, int n) throws Exception;

	public abstract boolean read(int id, T tuple) throws Exception;

	public abstract void scan(int offset, int length, Peek<T> op)
			throws Exception;
}
