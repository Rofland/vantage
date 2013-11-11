package vant.jdbc;

import vant.Mold;
import vant.app.Persisted;
import vant.lang.Struct;
import vant.model.Link;
import vant.model.Repo;
import vant.model.Tuple;

public class Asset implements vant.app.Asset {
	protected final Struct conf;

	public Asset(Struct conf) {
		this.conf = conf;
	}

	@Override
	public <T extends Tuple> Repo<T> repo(String sym, Mold<T> m)
			throws Exception {
		RepoJDBC<T> repo = new RepoJDBC<T>(m);
		conf.put("table", sym);
		repo.conf().extract(conf);
		ensure(repo);
		return repo;
	}

	@Override
	public vant.model.Div div(String sym) throws Exception {
		Div div = new Div();
		conf.put("table", sym);
		div.conf().extract(conf);
		ensure(div);
		return div;
	}

	@Override
	public Link link(String sym) throws Exception {
		ThickLinkJDBC link = new ThickLinkJDBC();
		conf.put("table", sym);
		link.conf().extract(conf);
		ensure(link);
		return link;
	}

	protected static void ensure(Persisted<?> p) throws Exception {
		switch (p.check()) {
		case INVALID:
			throw new Exception("Invalid store.");
		case NOT_EXIST:
			p.setup();
		case OK:
			p.open();
		}
	}

}
