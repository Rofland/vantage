package vant.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import vant.api.Link;
import vant.api.Repo;
import vant.api.Tuple;

public class JDBCAsset extends Asset {
	public final List<Persisted<JDBC>> stores = new ArrayList<Persisted<JDBC>>();
	public final Map<String, Repo<?>> repos = new HashMap<String, Repo<?>>();

	@Override
	public <T extends Tuple> Repo<T> repo(Class<T> clazz, String name,
			JSONObject conf) throws Exception {
		MemRepoJDBC<T> repo = new MemRepoJDBC<T>(clazz);
		repo.conf().parse(conf);
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
	public Link link(String name, JSONObject conf) {
		// TODO Auto-generated method stub
		return null;
	}
}
