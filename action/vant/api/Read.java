package vant.api;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

import vant.Ensure;
import vant.Lang;
import vant.Silently;
import vant.Usage;

public class Read<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected final int[] _ids = new int[1];
	protected final T _tuple;
	protected final T[] _tuples;

	@SuppressWarnings("unchecked")
	public Read(Repo<T> repo) {
		_repo = repo;
		_tuple = Silently.create(repo.clazz);
		_tuples = (T[]) Array.newInstance(repo.clazz, 1);
	}

	@Override
	public int binaryLimit() {
		return _tuple.binaryLimit();
	}

	@Override
	public void parse(JSONArray json) throws Usage {
		_ids[0] = Ensure.id("read.params[0]", json.optInt(0));
	}

	@Override
	public void parse(JSONObject json) throws Usage {
		_ids[0] = Ensure.id("read.params.id", json.optInt("id"));
	}

	@Override
	public void parse(ByteBuffer bin) throws Usage {
		_ids[0] = Ensure.id("read.params", bin.getInt());
	}

	@Override
	public void array(Lang.Writer out) throws IOException {
		out.value(_ids[0]);
	}

	@Override
	public void object(Lang.Writer out) throws IOException {
		out.value(_ids[0]);
	}

	@Override
	public void binary(DataOutput out) throws IOException {
		out.writeInt(_ids[0]);
	}

	@Override
	public void perform() throws Exception {
		_tuples[0] = _tuple;
		_repo.load(_ids, _tuples, 1);
	}

	@Override
	public void result(Lang.Writer out) throws IOException {
		if (_tuples[0] == null)
			out.value(null);
		else {
			out.array();
			_tuple.array(out);
			out.end();
		}
	}

	@Override
	public void result(DataOutput out) throws IOException {
		if (_tuples[0] == null)
			out.writeInt(-_ids[0]);
		else {
			out.writeInt(_ids[0]);
			_tuple.binary(out);
		}
	}
}
