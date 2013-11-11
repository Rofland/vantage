package vant.model;

import java.util.Arrays;

import vant.Misc;

public class Selection implements Sliceable {
	protected int _count;
	protected int[] _ids = Misc.ZERO_INT;

	public final int count() {
		return _count;
	}

	protected void _add(int id) throws Exception {
	}

	protected void _evict(int id) throws Exception {
	}

	protected void _clear() throws Exception {
	}

	public int slice(int from, int size, int[] ids) throws Exception {
		int n = Math.min(size, _count - from);
		for (int i = 0; i < n; i++)
			ids[i] = _ids[from + i];
		return n;
	}

	public void add(int id) throws Exception {
		int index = Arrays.binarySearch(_ids, id);
		if (index >= 0)
			return;
		_add(id);
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
	}

	public void evict(int id) throws Exception {
		int index = Arrays.binarySearch(_ids, id);
		if (index < 0)
			return;
		_evict(id);
		_count--;
		System.arraycopy(_ids, index, _ids, index + 1, _count - index);
	}

	public void clear() throws Exception {
		if (_count == 0)
			return;
		_clear();
		_count = 0;
	}

	public boolean has(int... ids) throws Exception {
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
