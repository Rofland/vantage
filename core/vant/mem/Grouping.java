package vant.mem;

import java.util.Arrays;

import vant.Misc;

public class Grouping extends vant.model.Grouping {
	protected int[] _groups = Misc.ZERO_INT; // group of a key
	protected int[] _kCounts = Misc.ZERO_INT; // size of a division
	protected int _nonZero;

	@Override
	public boolean put(int k, int v) {
		if (k > _groups.length)
			_groups = Arrays.copyOf(_groups, k + 256);
		int vOld = _groups[k - 1];
		if (vOld == v)
			return false;
		if (vOld == 0)
			_nonZero++;
		else
			_kCounts[vOld - 1]--;
		_groups[k - 1] = v;
		if (v > _kCounts.length)
			_kCounts = Arrays.copyOf(_kCounts, v + 32);
		_kCounts[v - 1]++;
		return true;
	}

	@Override
	public boolean cut(int k, int v) {
		int vOld = _groups[k - 1];
		if (vOld == 0 || vOld != v)
			return false;
		_groups[k - 1] = 0;
		_kCounts[v - 1]--;
		_nonZero--;
		return true;
	}

	@Override
	public int cut(int k) {
		return cut(k, _groups[k - 1]) ? 1 : 0;
	}

	@Override
	public boolean has(int k, int v) {
		return _groups[k - 1] == v;
	}

	@Override
	public int value(int k) {
		return _groups[k - 1];
	}

	@Override
	public int count(int v) {
		return v < _kCounts.length ? _kCounts[v - 1] : 0;
	}

	@Override
	public int count() {
		return _nonZero;
	}

	@Override
	public int list(int v, int from, int size, int[] ks) {
		size = v < _kCounts.length ? Math.min(size, _kCounts[v - 1] - from) : 0;
		if (from >= size)
			return 0;
		int index = 0;
		int toSkip = from;
		for (int i = 0; i < _groups.length; i++) {
			if (_groups[i] != v)
				continue;
			if (toSkip > 0) {
				toSkip--;
				continue;
			}
			ks[index++] = i + 1;
			if (index == size)
				break;
		}
		return size;
	}
}
