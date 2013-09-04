package vant.api;

import java.io.DataOutput;
import java.io.IOException;

import vant.Exchangable;
import vant.Lang;

public abstract class Action implements Exchangable {
	public abstract void perform() throws Exception;

	public abstract void result(Lang.Writer out) throws IOException;

	public abstract void result(DataOutput out) throws IOException;
}
