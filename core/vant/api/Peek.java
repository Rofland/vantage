package vant.api;

public interface Peek<T extends Tuple> {
	void at(int id, T tuple);
}