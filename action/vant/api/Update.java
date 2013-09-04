package vant.api;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

import vant.Ensure;
import vant.Lang;
import vant.Silently;
import vant.Usage;

public class Update<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected int _id;
	protected final T _tuple;

	public Update(Repo<T> repo) {
		_repo = repo;
		_tuple = Silently.create(repo.clazz);
	}

	@Override
	public int binaryLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void parse(JSONArray json) throws Usage {
		_id = Ensure.id("update.id", json.optInt(0));
		_tuple.parse(json.optJSONArray(1));

	}

	@Override
	public void parse(JSONObject json) throws Usage {
		_id = Ensure.id("update.id", json.optInt("id"));
		_tuple.parse(json);
	}

	@Override
	public void parse(ByteBuffer bin) throws Usage {
		_id = Ensure.id("update.id", bin.getInt());
		_tuple.parse(bin);
	}

	@Override
	public void array(Lang.Writer out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void object(Lang.Writer out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void binary(DataOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void perform() throws Exception {
		_repo.update(_id, _tuple);
	}

	@Override
	public void result(Lang.Writer out) throws IOException {
		out.value(true);
	}

	@Override
	public void result(DataOutput out) throws IOException {
		out.writeBoolean(true);
	}
}