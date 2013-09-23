package vant.app;

import vant.lang.Struct;

public abstract class Conf {
	public String url;

	public abstract void extract(Struct s);
}
