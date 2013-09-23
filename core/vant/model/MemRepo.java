package vant.model;

import java.util.Arrays;

import vant.Mold;

public class MemRepo<T extends Tuple> extends Repo<T> {
	protected T[] _tuples;

	public MemRepo(Mold<T> m) {
		super(m);
		_tuples = m.array(0);
	}

	protected void save(int id, T tuple) throws Exception {
	}

	@Override
	public int create(T tuple) throws Exception {
		int id = _count + 1;
		save(id, tuple);
		ensureCapacity(_count + 1);
		T fresh = mold.create();
		mold.copy(tuple, fresh);
		_tuples[_count] = fresh;
		_count++;
		return id;
	}

	@Override
	public void update(int id, T tuple) throws Exception {
		read(id, proto);
		if (tuple.equals(proto))
			return;
		save(id, tuple);
		mold.copy(_tuples[id - 1], proto);
		mold.copy(tuple, _tuples[id - 1]);
	}

	@Override
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

	@Override
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

	@Override
	public void scan(int offset, int length, Peek<T> peek) throws Exception {
		offset = Math.max(0, offset);
		int end = Math.min(_count, offset + length);
		T tuple = mold.create();
		for (int i = offset; i < end; i++) {
			int id = i + 1;
			mold.copy(_tuples[i], tuple);
			peek.at(id, tuple);
		}
	}
}
