package vant.spi;

import java.util.Arrays;

import vant.Silently;
import vant.api.Peek;
import vant.api.Repo;
import vant.api.Tuple;

public class MemRepo<T extends Tuple> extends Repo<T> {
	protected T[] _tuples;

	public MemRepo(Class<T> clazz) {
		super(clazz);
		_tuples = Silently.create(clazz, _count);
	}

	protected void save(int id, T tuple) throws Exception {
	}

	@Override
	public int create(T tuple) throws Exception {
		int id = _count + 1;
		save(id, tuple);
		ensureCapacity(_count + 1);
		T fresh = Silently.create(clazz);
		fresh.copy(tuple);
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
		proto.copy(_tuples[id - 1]);
		_tuples[id - 1].copy(tuple);
	}

	@Override
	public void load(int[] ids, T[] tuples, int n) {
		for (int i = 0; i < n; i++) {
			int index = ids[i] - 1;
			if (index < _count)
				tuples[i].copy(_tuples[index]);
			else
				tuples[i] = null;
		}
	}

	@Override
	public boolean read(int id, T tuple) {
		if (id <= 0 || id > _count)
			return false;
		tuple.copy(_tuples[id - 1]);
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
		T tuple = Silently.create(clazz);
		for (int i = offset; i < end; i++) {
			int id = i + 1;
			tuple.copy(_tuples[i]);
			peek.at(id, tuple);
		}
	}
}
