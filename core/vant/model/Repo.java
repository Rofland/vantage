package vant.model;

import vant.Mold;

public abstract class Repo<T extends Tuple> {
	public final Mold<T> mold;
	public final T proto;
	protected int _count;

	public Repo(Mold<T> mold) {
		this.mold = mold;
		this.proto = mold.create();
	}

	public final int count() {
		return _count;
	}

	public abstract int create(T tuple) throws Exception;

	public abstract void update(int id, T tuple) throws Exception;

	public abstract void load(int[] ids, int i1, T[] tuples, int i2, int n)
			throws Exception;

	public abstract boolean read(int id, T tuple) throws Exception;
}
