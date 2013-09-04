package vant.api;

import java.io.DataOutput;
import java.io.IOException;

import vant.Lang;
import vant.Silently;
import vant.Usage;

public class SliceByRank<T extends Tuple> extends Slice {
	protected final Repo<T> _repo;
	protected final Extender<T> _ext;
	protected final Rank _rank;
	protected final Countable[] _countables;

	protected final int[] _ids;
	protected final T[] _tuples;
	protected int _count;

	public SliceByRank(Repo<T> repo, Extender<T> ext, Rank rank, short capacity,
			Countable... cs) throws Usage {
		super(capacity);
		_repo = repo;
		_ext = ext == null ? Extender.<T> NOOP() : ext;
		_rank = rank;
		_ids = new int[capacity];
		_tuples = Silently.create(repo.clazz, capacity);
		_countables = cs;
	}

	@Override
	public void perform() throws Exception {
		_count = _rank.slice(_offset, _size, _ids);
		_repo.load(_ids, _tuples, _count);
	}

	@Override
	public void result(Lang.Writer out) throws IOException {
		out.array();
		for (int i = 0; i < _count; i++) {
			int id = _ids[i];
			out.array();
			out.value(id);
			_tuples[i].array(out);
			_ext.bind(_tuples[i]);
			_ext.array(out);
			for (Countable c : _countables)
				out.value(c.countOf(id));
			out.end();
		}
		out.end();
	}

	@Override
	public void result(DataOutput out) throws IOException {
		for (int i = 0; i < _count; i++) {
			int id = _ids[i];
			out.writeInt(id);
			_tuples[i].binary(out);
			for (Countable c : _countables)
				out.writeInt(c.countOf(id));
			_ext.bind(_tuples[i]);
			_ext.binary(out);
		}
	}
}
