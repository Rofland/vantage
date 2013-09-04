package vant;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Exchangable {
	int binaryLimit();

	void parse(JSONObject json) throws Usage;

	void parse(JSONArray json) throws Usage;

	void parse(ByteBuffer bin) throws Usage;

	void array(Lang.Writer out) throws IOException;

	void object(Lang.Writer out) throws IOException;

	void binary(DataOutput out) throws IOException;
}
