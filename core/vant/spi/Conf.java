package vant.spi;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Conf {
	public String url;

	public abstract void parse(JSONObject json) throws JSONException;
}
