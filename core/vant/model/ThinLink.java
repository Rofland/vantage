package vant.model;

import java.util.Arrays;

import vant.Misc;
import vant.model.Link;

public class ThinLink extends Link {
	protected int _count;
	protected long[] _pairs = Misc.ZERO_LONG;
	protected int[] _sizes = Misc.ZERO_INT;

	protected void save(int k, int v) throws Exception {
	}

	protected void delete(int k, int v) throws Exception {
	}

	@Override
	public void put(int k, int v) throws Exception {
		long pair = (((long) k) << 32) | v;
		int index = Arrays.binarySearch(_pairs, pair);
		if (index >= 0)
			return;
		save(k, v);
		index = -index - 1;
		_pairs = Misc.reserve(_pairs, _count, index, 1, 512);
		_pairs[index] = pair;
		_count++;
		if (k > _sizes.length)
			_sizes = Arrays.copyOf(_sizes, (k & 0xffffff00) + 256);
		_sizes[k - 1]++;
	}

	@Override
	public void cut(int k, int v) throws Exception {
		long pair = ((long) k) << 32 | v;
		int index = Arrays.binarySearch(_pairs, 0, _count, pair);
		if (index < 0)
			return;
		delete(k, v);
		_count--;
		System.arraycopy(_pairs, index + 1, _pairs, index, _count - index);
		_sizes[k - 1]--;
	}

	@Override
	public int countOf(int k) {
		return k > _sizes.length ? 0 : _sizes[k - 1];
	}

	@Override
	public int list(int k, int from, int size, int[] vs) {
		if (k > _sizes.length || _sizes[k - 1] <= from)
			return 0;
		long pairHead = ((long) k) << 32;
		int offset = Arrays.binarySearch(_pairs, pairHead) + from;
		int count = Math.min(size, _sizes[k - 1] - from);
		for (int i = 0; i < count; i++)
			vs[i] = (int) _pairs[offset + i];
		return count;
	}
}
