package vant.index;

import vant.model.Sliceable;

public abstract class Rank implements Sliceable {
	protected int _count;
	protected int[] _ids = new int[0];

	@Override
	public final int slice(int from, int size, int[] ids) {
		int count = 0;
		int end = Math.min(from + size, _count);
		for (int i = from; i < end; i++, count++)
			ids[count] = _ids[i];
		return count;
	}

	@Override
	public int count() {
		return _count;
	}
}
