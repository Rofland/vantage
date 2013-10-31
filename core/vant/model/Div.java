package vant.model;

import java.util.Arrays;

import vant.Misc;

public class Div implements Relation {
	protected int[] _divs = Misc.ZERO_INT; // division of a key
	protected int[] _kCounts = Misc.ZERO_INT; // size of a division
	protected int _nonZero;

	protected void create(int k, int v) throws Exception {
	}

	protected void update(int k, int v) throws Exception {
	}

	protected void delete(int k) throws Exception {
	}

	@Override
	public void put(int k, int v) throws Exception {
		if (k > _divs.length)
			_divs = Arrays.copyOf(_divs, k + 256);
		int vOld = _divs[k - 1];
		if (vOld == v)
			return;
		if (vOld == 0) {
			create(k, v);
			_nonZero ++;
		} else {
			update(k, v);
			_kCounts[vOld - 1]--;
		}
		_divs[k - 1] = v;
		if (v > _kCounts.length)
			_kCounts = Arrays.copyOf(_kCounts, v + 32);
		_kCounts[v - 1]++;
	}

	@Override
	public void cut(int k, int v) throws Exception {
		int vOld = _divs[k - 1];
		if (vOld == 0 || vOld != v)
			return;
		delete(k);
		_divs[k - 1] = 0;
		_kCounts[v - 1]--;
		_nonZero--;
	}

	public int value(int id) throws Exception {
		return _divs[id - 1];
	}

	public int countOf(int v) throws Exception {
		return v < _kCounts.length ? _kCounts[v-1] : 0;
	}
	
	public int count() {
		return _nonZero;
	}

	public final int rlist(int v, int from, int size, int[] ks)
			throws Exception {
		size = v < _kCounts.length ? Math.min(size, _kCounts[v - 1] - from) : 0;
		if (from >= size)
			return 0;
		int index = 0;
		int toSkip = from;
		for (int i = 0; i < _divs.length; i++) {
			if (_divs[i] != v)
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
