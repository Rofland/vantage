package vant.api;

import java.util.Arrays;

import vant.Misc;

/**
 * Group is an index for M-to-1 relationship between repos.
 */
public final class Grouping implements Countable {
	protected int[] _keys = Misc.ZERO_INT; // group of an ID
	protected int[] _sizes = Misc.ZERO_INT; // size of a group
	protected int _count;

	public void put(int id, int key) {
		if (id <= _count) {
			int oldKey = _keys[id - 1];
			if (oldKey == key)
				return;
			_sizes[oldKey]--;
		} else if (_count == _keys.length)
			_keys = Arrays.copyOf(_keys, _count + 256);
		_count++;
		_keys[id - 1] = key;

		if (key >= _sizes.length)
			_sizes = Arrays.copyOf(_sizes, key + 32);
		_sizes[key]++;
	}

	public int value(int id) {
		return _keys[id - 1];
	}

	@Override
	public int countOf(int key) {
		return key < _sizes.length ? _sizes[key] : 0;
	}

	public final int list(int key, int from, int size, int[] ids) {
		size = Math.min(size, _sizes[key] - from);
		if (from >= size)
			return 0;
		int index = 0;
		int toSkip = from;
		for (int i = 0; i < _count; i++) {
			if (_keys[i] != key)
				continue;
			if (toSkip > 0) {
				toSkip--;
				continue;
			}
			ids[index++] = i + 1;
			if (index == size)
				break;
		}
		return size;
	}
}
