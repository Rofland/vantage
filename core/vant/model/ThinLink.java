package vant.model;

import java.util.Arrays;

import vant.Misc;
import vant.model.Link;

public class ThinLink extends Link {
	protected int _count;
	protected long[] _pairs = Misc.ZERO_LONG;
	protected int[] _sizes = Misc.ZERO_INT;

	protected void save(int src, int dst) throws Exception {
	}

	protected void delete(int src, int dst) throws Exception {
	}

	@Override
	public void join(int src, int dst) throws Exception {
		long pair = (((long) src) << 32) | dst;
		int index = Arrays.binarySearch(_pairs, pair);
		if (index >= 0)
			return;
		save(src, dst);
		index = -index - 1;
		_pairs = Misc.reserve(_pairs, _count, index, 1, 512);
		_pairs[index] = pair;
		_count++;
		if (src > _sizes.length)
			_sizes = Arrays.copyOf(_sizes, (src & 0xffffff00) + 256);
		_sizes[src - 1]++;
	}

	@Override
	public void chop(int src, int dst) throws Exception {
		long pair = ((long) src) << 32 | dst;
		int index = Arrays.binarySearch(_pairs, 0, _count, pair);
		if (index < 0)
			return;
		delete(src, dst);
		_count--;
		System.arraycopy(_pairs, index + 1, _pairs, index, _count - index);
		_sizes[src - 1]--;
	}

	@Override
	public int countOf(int src) {
		return src > _sizes.length ? 0 : _sizes[src - 1];
	}

	@Override
	public int list(int src, int from, int size, int[] dsts) {
		if (src > _sizes.length || _sizes[src - 1] <= from)
			return 0;
		long pairHead = ((long) src) << 32;
		int offset = Arrays.binarySearch(_pairs, pairHead) + from;
		int count = Math.min(size, _sizes[src - 1] - from);
		for (int i = 0; i < count; i++)
			dsts[i] = (int) _pairs[offset + i];
		return count;
	}
}
