package vant.api;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

import vant.Flow;
import vant.Lang;
import vant.Usage;

public abstract class Tuple {
	public abstract void copy(Tuple src);

	public abstract int traitCount();

	public abstract void parse(Flow.Reader in) throws Usage, IOException;

	// ByteBuffer support

	public abstract int binaryLimit();

	public abstract void parse(ByteBuffer bin) throws Usage;

	public abstract void encode(ByteBuffer bin);

	public abstract void decode(ByteBuffer bin);

	// JSON array support

	public abstract void parse(JSONArray json) throws Usage;

	public abstract void encode(JSONArray json);

	// JSON object support

	public abstract void parse(JSONObject json) throws Usage;

	public abstract void encode(JSONObject json);

	// streaming support

	public abstract void binary(DataOutput out) throws IOException;

	public abstract void array(Lang.Writer out) throws IOException;

	public abstract void object(Lang.Writer out) throws IOException;
}
