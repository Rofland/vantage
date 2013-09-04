package vant.spi;

import org.json.JSONObject;

import vant.api.Link;
import vant.api.Repo;
import vant.api.Tuple;

public abstract class Asset {
	public abstract <T extends Tuple> Repo<T> repo(Class<T> clazz, String name,
			JSONObject conf) throws Exception;

	public abstract Link link(String name, JSONObject conf);
}