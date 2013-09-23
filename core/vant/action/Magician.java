package vant.action;

import java.io.IOException;
import java.util.Arrays;

import vant.Misc;
import vant.lang.Writer;

public abstract class Magician extends Action {
	protected final Magic _magic;
	protected int[] _ids = Misc.ZERO_INT;
	protected int _count;

	public Magician(Magic m, int n) {
		_magic = m;
		_ids = n == 0 ? Misc.ZERO_INT : new int[n];
	}

	protected void pledge(int id) {
		if (_count == _ids.length) {
			int n = _count < 10 ? 10 : _count + _count / 2;
			_ids = Arrays.copyOf(_ids, n);
		}
		_ids[_count++] = id;
	}

	protected void pledge(int[] ids, int off, int n) {
		int count = _count + n;
		if (count >= _ids.length)
			_ids = Arrays.copyOf(_ids, count);
		System.arraycopy(ids, off, _ids, _count, n);
		_count = count;
	}

	@Override
	public void result(String k, Writer w) throws IOException {
		w.array(k, _count);
		for (int i = 0; i < _count; i++) {
			w.object(k, true);
			w.INT("id", _ids[i]);
			_magic.prestige(i, w);
			w.end();
		}
		w.end();
	}

	@Override
	public void cleanup() {
		_count = 0;
		_magic.reset();
	}
}
