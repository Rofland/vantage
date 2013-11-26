package vant.app;

import vant.Mold;
import vant.action.Action;
import vant.mem.Grouping;
import vant.mem.IS;
import vant.model.Repo;
import vant.model.Tuple;

public interface App {
	public interface Access {
		void action(String sym, Action a);

		Action action(String sym);
	}

	public interface Asset {
		<T extends Tuple> Repo<T> repo(String sym, Mold<T> m, boolean runtime)
				throws Exception;

		Grouping grouping(String sym, boolean runtime) throws Exception;

		IS selection(String sym, boolean runtime) throws Exception;
	}

	void init(Access access, Asset asset) throws Exception;
}
