package vant.model;

public abstract class Selection implements Sliceable {
	public abstract boolean add(int id);

	public abstract boolean evict(int id);

	public abstract boolean clear();

	public abstract boolean has(int id);
}
