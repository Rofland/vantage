package vant.index;


public abstract class Rank {
	protected int _count;
	protected int[] _ids = new int[0];

	public final int slice(int from, int size, int[] ids) {
		int count = 0;
		int end = Math.min(from + size, _count);
		for (int i = from; i < end; i++, count++)
			ids[count] = _ids[i];
		return count;
	}
}
