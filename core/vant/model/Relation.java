package vant.model;

public abstract class Relation implements Countable {
	public abstract boolean put(int k, int v);

	public abstract boolean cut(int k, int v);
	
	public abstract int cut(int k);
	
	public abstract int list(int v, int from, int size, int[] ks);
	
	public abstract boolean has(int k, int v);
	
	public abstract int count(int v);
}

