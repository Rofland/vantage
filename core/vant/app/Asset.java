package vant.app;

import vant.Mold;
import vant.lang.Struct;
import vant.model.Link;
import vant.model.Repo;
import vant.model.Tuple;

public abstract class Asset {
	public abstract <T extends Tuple> Repo<T> repo(Mold<T> m, String name,
			Struct s) throws Exception;

	public abstract Link link(String name, Struct s);
}