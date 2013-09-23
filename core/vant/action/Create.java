package vant.action;

import java.io.IOException;

import vant.Usage;
import vant.lang.Reader;
import vant.lang.Writer;
import vant.model.Repo;
import vant.model.Tuple;

public class Create<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected int _id;
	protected final T _tuple;

	public Create(Repo<T> repo) {
		_repo = repo;
		_tuple = repo.mold.create();
	}

	@Override
	public void perform() throws Exception {
		_id = _repo.create(_tuple);
	}

	@Override
	public void result(String k, Writer w) throws IOException {
		w.BOOL(k, true);
	}

	@Override
	public int binaryLimit() {
		return 4;
	}

	@Override
	public void validate() throws Usage {
		_tuple.validate();
	}

	@Override
	public void decode(Reader r) throws IOException, Usage {
		_tuple.decode(r);
	}

	@Override
	public void encode(Writer w) throws IOException {
		_tuple.encode(w);
	}
}
