package vant.model;

import vant.Mold;

public abstract class Repo<T extends Tuple> implements Countable {
	public final Mold<T> mold;
	public final T proto;

	public Repo(Mold<T> m) {
		this.mold = m;
		this.proto = m.create();
	}

	public abstract int create(T t);

	public abstract boolean update(int id, T t);

	public abstract void read(int[] ids, int f1, T[] ts, int f2, int n);

	public abstract void read(int id, T t);
}