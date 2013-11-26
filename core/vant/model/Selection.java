package vant.model;

import java.util.Arrays;

import vant.Misc;

public class Selection implements Sliceable {
	protected int _count;
	protected int[] _ids = Misc.ZERO_INT;

	@Override
	public final int count() {
		return _count;
	}

	@Override
	public int slice(int from, int size, int[] ids) {
		int n = Math.min(size, _count - from);
		for (int i = 0; i < n; i++)
			ids[i] = _ids[from + i];
		return n;
	}

	public boolean add(int id) {
		int index = Arrays.binarySearch(_ids, id);
		if (index >= 0)
			return false;
		index = -index - 1;
		int[] dst = _ids;
		if (_count == _ids.length) {
			dst = new int[_count + 64];
			System.arraycopy(_ids, 0, dst, 0, index);
		}
		System.arraycopy(_ids, index, dst, index + 1, _count - index);
		dst[index] = id;
		_ids = dst;
		_count++;
		return true;
	}

	public boolean evict(int id) {
		int index = Arrays.binarySearch(_ids, id);
		if (index < 0)
			return false;
		_count--;
		System.arraycopy(_ids, index, _ids, index + 1, _count - index);
		return true;
	}

	public boolean clear() {
		if (_count == 0)
			return false;
		_count = 0;
		return true;
	}

	public boolean has(int... ids) {
		for (int id : ids)
			if (Arrays.binarySearch(_ids, id) < 0)
				return false;
		return true;
	}

	public void has(int[] ids, int f1, boolean[] presence, int f2, int n) {
		for (int i = 0; i < n; i++)
			presence[f2 + i] = Arrays.binarySearch(_ids, ids[f1 + i]) >= 0;
	}
}
