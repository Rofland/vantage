package vant.app;

import org.json.JSONException;

import vant.lang.Struct;

public abstract class Conf {
	public String url;

	public abstract void extract(Struct s) throws JSONException;
}
