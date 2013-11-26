package vant.model;

public abstract class Repo<T extends Tuple> implements Countable {

	public abstract int create(T t);

	public abstract boolean update(int id, T t);

	public abstract void read(int[] ids, int f1, T[] ts, int f2, int n);

	public abstract void read(int id, T t);
}