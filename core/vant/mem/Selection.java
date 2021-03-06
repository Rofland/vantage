package vant.mem;

import java.util.Arrays;

import vant.Misc;

public class Selection extends vant.model.Selection {
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

	@Override
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

	@Override
	public boolean evict(int id) {
		int index = Arrays.binarySearch(_ids, id);
		if (index < 0)
			return false;
		_count--;
		System.arraycopy(_ids, index, _ids, index + 1, _count - index);
		return true;
	}

	@Override
	public boolean clear() {
		if (_count == 0)
			return false;
		_count = 0;
		return true;
	}

	@Override
	public boolean has(int id) {
		return Arrays.binarySearch(_ids, id) >= 0;
	}
}
