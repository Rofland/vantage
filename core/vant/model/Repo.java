package vant.model;

import java.util.Arrays;

import vant.Mold;

public class Repo<T extends Tuple> {
	public final Mold<T> mold;
	public final T proto;
	protected T[] _tuples;
	protected int _count;

	public Repo(Mold<T> mold) {
		this.mold = mold;
		this.proto = mold.create();
		_tuples = mold.array(0);
	}

	public final int count() {
		return _count;
	}

	public int create(T tuple) {
		int id = _count + 1;
		ensureCapacity(_count + 1);
		T fresh = mold.create();
		mold.copy(tuple, fresh);
		_tuples[_count] = fresh;
		_count++;
		return id;
	}

	public boolean update(int id, T tuple) {
		read(id, proto);
		if (tuple.equals(proto))
			return false;
		mold.copy(_tuples[id - 1], proto);
		mold.copy(tuple, _tuples[id - 1]);
		return true;
	}

	public void load(int[] ids, int off1, T[] tuples, int off2, int n) {
		for (int i = 0; i < n; i++) {
			int k1 = off1 + i, k2 = off2 + i;
			int index = ids[k1] - 1;
			if (index < _count)
				mold.copy(_tuples[index], tuples[k2]);
			else
				tuples[k2] = null;
		}
	}

	public boolean read(int id, T tuple) {
		if (id <= 0 || id > _count)
			return false;
		mold.copy(_tuples[id - 1], tuple);
		return true;
	}

	protected final void ensureCapacity(int n) {
		if (n <= _tuples.length)
			return;
		n = Math.max(n, _tuples.length + 128);
		_tuples = Arrays.copyOf(_tuples, n);
	}
}
