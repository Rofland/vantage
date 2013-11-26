package vant.action;

import java.io.IOException;

import vant.Ensure;
import vant.Usage;
import vant.Violation;
import vant.lang.Exchange;
import vant.lang.Writer;
import vant.mem.Repo;
import vant.model.Relation;

public class Put extends Action {
	protected final Relation _relation;
	protected final Repo<?> _kRepo, _vRepo;
	protected int _k, _v;

	public Put(Relation r, Repo<?> kr, Repo<?> vr) {
		this._relation = r;
		this._kRepo = kr;
		this._vRepo = vr;
	}

	@Override
	public void perform() throws Exception {
		if (_k > _kRepo.count())
			throw new Violation(0, "Relation source not exist.");
		if (_v > _vRepo.count())
			throw new Violation(1, "Relation target not exist.");
		this._relation.put(_k, _v);
	}

	@Override
	public int binaryLimit() {
		return 8;
	}

	@Override
	public void validate() throws Usage {
	}

	@Override
	public void decode(Exchange r) throws IOException, Usage {
		_k = Ensure.id("key", r.INT("key", 0));
		_v = Ensure.id("value", r.INT("value", 0));
	}

	@Override
	public void encode(Exchange w) throws IOException {
		w.INT("key", _k);
		w.INT("value", _v);
	}

	@Override
	public void result(Writer w) throws IOException {
		w.BOOL(true);
	}
}
