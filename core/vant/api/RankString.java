package vant.api;

import java.util.Arrays;

import vant.Misc;

public class RankString extends Rank {
	protected String[] _keys = Misc.ZERO_STRING;

	public final void put(int id, String key) {
		int index = Arrays.binarySearch(_ids, 0, _count, id);
		if (index >= 0) {
			int cmp = key.compareTo(_keys[index]);
			if (cmp == 0)
				return;
			if (cmp < 0)
				backward(index, key);
			else
				forward(index, key);
			return;
		}

		if (_count == _keys.length) {
			int n = _count + 256;
			String[] keys2 = new String[n];
			int[] ids2 = new int[n];
			System.arraycopy(_keys, 0, keys2, 0, _count);
			System.arraycopy(_ids, 0, ids2, 0, _count);
			_keys = keys2;
			_ids = ids2;
		}
		_keys[_count] = key;
		_ids[_count] = id;
		_count++;
		backward(_count - 1, key);
	}

	public final boolean contains(String key) {
		return Arrays.binarySearch(_keys, 0, _count, key) >= 0;
	}

	public final String keyAt(int rank) {
		return _keys[rank];
	}

	public final void includeLower(String[] keys, int[] ids, int n) {
		Arrays.sort(keys, 0, n);
		int i = 0, j = 0;
		while (i < _count && j < n) {
			if (_keys[i].compareTo(keys[j]) < 0)
				i++;
			else
				ids[j++] = i;
		}
		while (j < n)
			ids[j++] = _count;
	}

	public final void includeUpper(String[] keys, int[] ids, int n) {
		Arrays.sort(keys, 0, n);
		int i = _count - 1, j = n - 1;
		while (i >= 0 && j >= 0) {
			if (_keys[i].compareTo(keys[j]) > 0)
				i--;
			else
				ids[j--] = i;
		}
		while (j >= 0)
			ids[j--] = -1;
	}

	private final void backward(int index, String key) {
		int id = _ids[index];
		for (; index > 0; index--) {
			String prevKey = _keys[index - 1];
			int prevID = _ids[index - 1];
			int cmp = prevKey.compareTo(key);
			if (cmp < 0 || (cmp == 0 && id < prevID))
				break;
			_keys[index] = prevKey;
			_ids[index] = prevID;
		}
		_keys[index] = key;
		_ids[index] = id;
	}

	private final void forward(int index, String key) {
		int id = _ids[index];
		int limit = _count - 1;
		for (; index < limit; index++) {
			String nextKey = _keys[index + 1];
			int nextID = _ids[index + 1];
			int cmp = key.compareTo(nextKey);
			if (cmp < 0 || (cmp == 0 && id < nextID))
				break;
			_keys[index] = nextKey;
			_ids[index] = nextID;
		}
		_keys[index] = key;
		_ids[index] = id;
	}
}
