package vant.action;

import java.io.IOException;

import vant.Ensure;
import vant.Usage;
import vant.lang.Exchange;
import vant.lang.Writer;
import vant.model.Repo;
import vant.model.Tuple;

public class Update<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected int _id;
	protected final T _tuple;

	public Update(Repo<T> repo) {
		_repo = repo;
		_tuple = repo.mold.create();
	}

	@Override
	public void perform() throws Exception {
		_repo.update(_id, _tuple);
	}

	@Override
	public void result(Writer w) throws IOException {
		w.BOOL(true);
	}

	@Override
	public int binaryLimit() {
		return 4 + _tuple.binaryLimit();
	}

	@Override
	public void validate() throws Usage {
		Ensure.id("id", _id);
	}

	@Override
	public void decode(Exchange r) throws IOException, Usage {
		_id = r.INT("id", 0);
		_tuple.decode(r);
	}

	@Override
	public void encode(Exchange w) throws IOException {
		w.INT("id", _id);
		_tuple.encode(w);
	}
}