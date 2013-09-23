package vant.index;

import java.util.Arrays;

import vant.Misc;

public class RankLong extends Rank {
	protected long[] _keys = Misc.ZERO_LONG;

	public final void put(int id, long key) {
		int index = Arrays.binarySearch(_ids, 0, _count, id);
		if (index >= 0) {
			long oldKey = _keys[index];
			if (oldKey == key)
				return;
			if (key < oldKey)
				backward(index, key);
			else
				forward(index, key);
			return;
		} else if (_count == _keys.length) {
			int n = _count + 256;
			long[] keys2 = new long[n];
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

	public final boolean contains(long key) {
		return Arrays.binarySearch(_keys, 0, _count, key) >= 0;
	}

	public final long keyAt(int rank) {
		return _keys[rank];
	}

	public final void includeLower(long[] keys, int[] indexes, int n) {
		Arrays.sort(keys, 0, n);
		int i = 0, j = 0;
		while (i < _count && j < n) {
			if (_keys[i] < keys[j])
				i++;
			else
				indexes[j++] = i;
		}
		while (j < n)
			indexes[j++] = _count;
	}

	public final void includeUpper(long[] keys, int[] indexes, int n) {
		Arrays.sort(keys, 0, n);
		int i = _count - 1, j = n - 1;
		while (i >= 0 && j >= 0) {
			if (_keys[i] > keys[j])
				i--;
			else
				indexes[j--] = i;
		}
		while (j >= 0)
			indexes[j--] = -1;
	}

	private final void backward(int index, long key) {
		int id = _ids[index];
		for (; index > 0; index--) {
			long prevKey = _keys[index - 1];
			int prevID = _ids[index - 1];
			if (prevKey < key || prevKey == key && prevID < id)
				break;
			_keys[index] = prevKey;
			_ids[index] = prevID;
		}
		_keys[index] = key;
		_ids[index] = id;
	}

	private final void forward(int index, long key) {
		int id = _ids[index];
		int limit = _count - 1;
		for (; index < limit; index++) {
			long nextKey = _keys[index + 1];
			int nextID = _ids[index + 1];
			if (key < nextKey || (key == nextKey && id < nextID))
				break;
			_keys[index] = nextKey;
			_ids[index] = nextID;
		}
		_keys[index] = key;
		_ids[index] = id;
	}
}
