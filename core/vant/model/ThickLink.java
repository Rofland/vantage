package vant.model;

import java.util.Arrays;

import vant.Misc;
import vant.model.Link;

public class ThickLink extends Link {
	public int[][] _groups = new int[0][];
	public int[] _sizes = Misc.ZERO_INT;

	protected void save(int k, int v) throws Exception {
	}

	protected void delete(int k, int v) throws Exception {
	}

	@Override
	public void put(int k, int v) throws Exception {
		int oldLength = _groups.length;
		if (k > _groups.length) {
			int n = (k & 0xffffffc0) + 64;
			_groups = Arrays.copyOf(_groups, n);
			_sizes = Arrays.copyOf(_sizes, n);
			while (oldLength < n)
				_groups[oldLength++] = Misc.ZERO_INT;
		}
		int[] group = _groups[k - 1];
		int index = Arrays.binarySearch(group, v);
		if (index >= 0)
			return;
		save(k, v);
		index = -index - 1;
		group = Misc.reserve(group, _sizes[k - 1], index, 1, 16);
		group[index] = v;
		_groups[k - 1] = group;
		_sizes[k - 1]++;
	}

	@Override
	public void cut(int k, int v) throws Exception {
		if (k > _groups.length)
			return;
		int[] group = _groups[k - 1];
		int index = Arrays.binarySearch(group, 0, _sizes[k - 1], v);
		if (index < 0)
			return;
		delete(k, v);
		_sizes[k - 1]--;
		System.arraycopy(group, index + 1, group, index, _sizes[k - 1] - index);
	}

	@Override
	public int countOf(int k) {
		return k > _sizes.length ? 0 : _sizes[k - 1];
	}

	@Override
	public int list(int k, int from, int size, int[] vs) throws Exception {
		if (k > _sizes.length || _sizes[k - 1] <= from)
			return 0;
		int[] group = _groups[k - 1];
		int count = Math.min(size, _sizes[k - 1] - from);
		for (int i = 0; i < count; i++)
			vs[i] = group[from + i];
		return count;
	}
}
