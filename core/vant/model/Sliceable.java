package vant.model;

public interface Sliceable extends Countable {
	int slice(int from, int size, int[] ids);
}
