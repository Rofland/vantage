package vant.api;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

import vant.Lang;
import vant.Silently;
import vant.Usage;

public class Create<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected int _id;
	protected final T _tuple;

	public Create(Repo<T> repo) {
		_repo = repo;
		_tuple = Silently.create(repo.clazz);
	}

	@Override
	public int binaryLimit() {
		return 4;
	}

	@Override
	public void parse(JSONArray json) throws Usage {
		_tuple.parse(json);
	}

	@Override
	public void parse(JSONObject json) throws Usage {
		_tuple.parse(json);
	}

	@Override
	public void parse(ByteBuffer bin) throws Usage {
		_tuple.parse(bin);
	}

	@Override
	public void array(Lang.Writer out) throws IOException {
		_tuple.array(out);
	}

	@Override
	public void object(Lang.Writer out) throws IOException {
		_tuple.object(out);
	}

	@Override
	public void binary(DataOutput out) throws IOException {
		_tuple.binary(out);
	}
	
	
	@Override
	public void perform() throws Exception {
		_id = _repo.create(_tuple);
	}

	@Override
	public void result(Lang.Writer out) throws IOException {
		out.value(_id);
	}

	@Override
	public void result(DataOutput out) throws IOException {
		out.writeInt(_id);
	}
}
