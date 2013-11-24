package vant.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import vant.Mold;
import vant.Usage;
import vant.app.Store;
import vant.model.Tuple;

public class Asset implements vant.app.App.Asset {
	protected final JDBC _conf;
	protected final Connection _conn;
	protected final Map<String, Store> _stores = new HashMap<String, Store>();

	public Asset(JDBC conf) throws SQLException {
		_conf = conf;
		_conn = conf.alloc();
	}

	@Override
	public <T extends Tuple> vant.model.Repo<T> repo(String sym, Mold<T> m,
			boolean runtime) throws Exception {
		if (_stores.containsKey(sym))
			throw new Usage(sym, "Already exist.");
		return runtime ? new vant.model.Repo<T>(m) : ensure(sym, new Repo<T>(m,
				_conn, sym));
	}

	@Override
	public vant.model.Grouping grouping(String sym, boolean runtime)
			throws Exception {
		if (_stores.containsKey(sym))
			throw new Usage(sym, "Already exist.");
		return runtime ? new vant.model.Grouping() : ensure(sym, new Grouping(
				_conn, sym));
	}

	@Override
	public vant.model.Link link(String sym, boolean runtime) throws Exception {
		if (_stores.containsKey(sym))
			throw new Usage(sym, "Already exist.");
		return runtime ? new vant.model.ThickLink() : ensure(sym,
				new ThickLink(_conn, sym));
	}

	@Override
	public vant.model.Selection selection(String sym, boolean runtime)
			throws Exception {
		if (_stores.containsKey(sym))
			throw new Usage(sym, "Already exist.");
		return runtime ? new vant.model.Selection() : ensure(sym,
				new Selection(_conn, sym));
	}

	protected <S extends Store> S ensure(String sym, S store) throws Exception {
		try {
			store.check();
		} catch (Exception e) {
			store.setup();
		}
		store.open();
		_stores.put(sym, store);
		return store;
	}
}
