package vant.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vant.Mold;
import vant.app.Asset;
import vant.app.Persisted;
import vant.lang.Struct;
import vant.model.Link;
import vant.model.Repo;
import vant.model.Tuple;

public class JDBCAsset extends Asset {
	public final List<Persisted<JDBC>> stores = new ArrayList<Persisted<JDBC>>();
	public final Map<String, Repo<?>> repos = new HashMap<String, Repo<?>>();

	@Override
	public <T extends Tuple> Repo<T> repo(Mold<T> m, String name, Struct s)
			throws Exception {
		MemRepoJDBC<T> repo = new MemRepoJDBC<T>(m);
		repo.conf().extract(s);
		stores.add(repo);
		repos.put(name, repo);

		switch (repo.check()) {
		case INVALID:
			throw new Exception("Invalid store.");
		case NOT_EXIST:
			repo.setup();
		case OK:
			repo.open();
		}
		return repo;
	}

	@Override
	public Link link(String name, Struct s) {
		// TODO Auto-generated method stub
		return null;
	}
}
