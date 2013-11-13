package vant.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import vant.Mold;
import vant.Usage;
import vant.app.Store;
import vant.model.Tuple;

public class Asset implements vant.app.Asset {
	protected final JDBC _conf;
	protected final Connection _conn;
	protected final Map<String, Store> _stores = new HashMap<String, Store>();

	public Asset(JDBC conf) throws SQLException {
		_conf = conf;
		_conn = conf.alloc();
	}

	@Override
	public <T extends Tuple> vant.model.Repo<T> repo(String sym, Mold<T> m)
			throws Exception {
		if (_stores.containsKey(sym))
			throw new Usage(sym, "Already exist.");
		Repo<T> repo = new Repo<T>(m, _conn, sym);
		ensure(repo);
		return repo;
	}

	@Override
	public vant.model.Grouping grouping(String sym) throws Exception {
		if (_stores.containsKey(sym))
			throw new Usage(sym, "Already exist.");
		Grouping g = new Grouping(_conn, sym);
		ensure(g);
		return g;
	}

	@Override
	public vant.model.Link link(String sym) throws Exception {
		if (_stores.containsKey(sym))
			throw new Usage(sym, "Already exist.");
		ThickLink link = new ThickLink(_conn, sym);
		ensure(link);
		return link;
	}

	protected static void ensure(Store store) throws Exception {
		try {
			store.check();
		} catch (Exception e) {
			store.setup();
		}
		store.open();
	}
}
