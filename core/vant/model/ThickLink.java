package vant.model;

import java.util.Arrays;

import vant.Misc;
import vant.model.Link;

public class ThickLink extends Link {
	public int[][] _groups = new int[0][];
	public int[] _sizes = Misc.ZERO_INT;

	protected void save(int src, int dst) throws Exception {
	}

	protected void delete(int src, int dst) throws Exception {
	}

	@Override
	public void join(int src, int dst) throws Exception {
		int oldLength = _groups.length;
		if (src > _groups.length) {
			int n = (src & 0xffffffc0) + 64;
			_groups = Arrays.copyOf(_groups, n);
			_sizes = Arrays.copyOf(_sizes, n);
			while (oldLength < n)
				_groups[oldLength++] = Misc.ZERO_INT;
		}
		int[] group = _groups[src - 1];
		int index = Arrays.binarySearch(group, dst);
		if (index >= 0)
			return;
		save(src, dst);
		index = -index - 1;
		group = Misc.reserve(group, _sizes[src - 1], index, 1, 16);
		group[index] = dst;
		_groups[src - 1] = group;
		_sizes[src - 1]++;
	}

	@Override
	public void chop(int src, int dst) throws Exception {
		if (src > _groups.length)
			return;
		int[] group = _groups[src - 1];
		int index = Arrays.binarySearch(group, 0, _sizes[src - 1], dst);
		if (index < 0)
			return;
		delete(src, dst);
		_sizes[src - 1]--;
		System.arraycopy(group, index + 1, group, index, _sizes[src - 1]
				- index);
	}

	@Override
	public int countOf(int src) {
		return src > _sizes.length ? 0 : _sizes[src - 1];
	}

	@Override
	public int list(int src, int from, int size, int[] dsts) throws Exception {
		if (src > _sizes.length || _sizes[src - 1] <= from)
			return 0;
		int[] group = _groups[src - 1];
		int count = Math.min(size, _sizes[src - 1] - from);
		for (int i = 0; i < count; i++)
			dsts[i] = group[from + i];
		return count;
	}
}
