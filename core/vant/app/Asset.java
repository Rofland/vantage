package vant.app;

import vant.Mold;
import vant.model.Div;
import vant.model.Link;
import vant.model.Repo;
import vant.model.Tuple;

public interface Asset {
	<T extends Tuple> Repo<T> repo(String sym, Mold<T> m) throws Exception;

	Div div(String sym) throws Exception;

	Link link(String sym) throws Exception;
}