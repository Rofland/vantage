package vant.app;

import vant.Mold;
import vant.model.Grouping;
import vant.model.Link;
import vant.model.Repo;
import vant.model.Tuple;

public interface Asset {
	<T extends Tuple> Repo<T> repo(String sym, Mold<T> m) throws Exception;

	Grouping grouping(String sym) throws Exception;

	Link link(String sym) throws Exception;
}