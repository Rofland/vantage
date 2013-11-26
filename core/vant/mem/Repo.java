package vant.mem;

import java.util.Arrays;

import vant.Mold;
import vant.model.Tuple;

public class Repo<T extends Tuple> extends vant.model.Repo<T> {
	public final Mold<T> mold;
	public final T proto;
	protected T[] _tuples;
	protected int _count;

	public Repo(Mold<T> mold) {
		this.mold = mold;
		this.proto = mold.create();
		_tuples = mold.array(0);
	}

	@Override
	public int count() {
		return _count;
	}

	@Override
	public int create(T t) {
		int id = _count + 1;
		ensureCapacity(_count + 1);
		T fresh = mold.create();
		mold.copy(t, fresh);
		_tuples[_count] = fresh;
		_count++;
		return id;
	}

	@Override
	public boolean update(int id, T t) {
		read(id, proto);
		if (t.equals(proto))
			return false;
		mold.copy(_tuples[id - 1], proto);
		mold.copy(t, _tuples[id - 1]);
		return true;
	}

	@Override
	public void read(int[] ids, int f1, T[] ts, int f2, int n) {
		for (int i = 0; i < n; i++) {
			int k1 = f1 + i, k2 = f2 + i;
			int index = ids[k1] - 1;
			if (index < _count)
				mold.copy(_tuples[index], ts[k2]);
			else
				ts[k2] = null;
		}
	}

	@Override
	public void read(int id, T tuple) {
		mold.copy(_tuples[id - 1], tuple);
	}

	protected final void ensureCapacity(int n) {
		if (n <= _tuples.length)
			return;
		n = Math.max(n, _tuples.length + 128);
		_tuples = Arrays.copyOf(_tuples, n);
	}
}
