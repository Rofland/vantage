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

public final class Load<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected final int[] _ids;
	protected int _count;
	protected final T[] _tuples, _returns;

	@SuppressWarnings("unchecked")
	public Load(Repo<T> repo, int limit) {
		_repo = repo;
		_ids = new int[limit];
		_tuples = Silently.create(repo.clazz, limit);
		_returns = (T[]) Array.newInstance(repo.clazz, limit);
	}

	@Override
	public int binaryLimit() {
		return 4 * _ids.length;
	}

	@Override
	public void parse(JSONArray ids) throws Usage {
		if (ids == null)
			throw new Usage("read.params", "Must be array of ID");
		_count = Ensure.range("read.params", ids.length(), 1, _ids.length);
		for (int i = 0; i < _count; i++) {
			_ids[i] = ids.optInt(i);
			_returns[i] = _tuples[i];
		}
	}

	@Override
	public void parse(JSONObject json) throws Usage {
		_count = 0;
		JSONArray ids = json.optJSONArray("ids");
		if (ids == null)
			throw new Usage("read.ids", "Must be array of ID");
		_count = Ensure.range("read.ids", ids.length(), 1, _ids.length);
		for (int i = 0; i < _count; i++) {
			_ids[i] = ids.optInt(i);
			_returns[i] = _tuples[i];
		}
	}

	@Override
	public void parse(ByteBuffer bin) throws Usage {
		_count = 0;
		if (bin == null)
			throw new Usage("read.ids", "Must be array of ID");
		_count = Ensure.range("read.ids", bin.getInt(), 1, _ids.length);
		for (int i = 0; i < _count; i++) {
			_ids[i] = bin.getInt();
			_returns[i] = _tuples[i];
		}
	}

	@Override
	public void array(Lang.Writer out) throws IOException {
		for (int i = 0; i < _count; i++)
			out.value(_ids[i]);
	}

	@Override
	public void object(Lang.Writer out) throws IOException {
		out.key("ids").array();
		for (int i = 0; i < _count; i++)
			out.value(_ids[i]);
		out.end();
	}

	@Override
	public void binary(DataOutput out) throws IOException {
		out.writeShort(_count);
		for (int i = 0; i < _count; i++)
			out.writeInt(_ids[i]);
	}

	@Override
	public void perform() throws Exception {
		for (int i = 0; i < _count; i++)
			_returns[i] = _tuples[i];
		_repo.load(_ids, _returns, _count);
	}

	@Override
	public void result(Lang.Writer out) throws IOException {
		out.array();
		for (int i = 0; i < _count; i++) {
			T tuple = _returns[i];
			if (tuple == null)
				out.value(null);
			else {
				out.array();
				tuple.array(out);
				out.end();
			}
		}
		out.end();
	}

	@Override
	public void result(DataOutput out) throws IOException {
		for (int i = 0; i < _count; i++) {
			T tuple = _returns[i];
			if (tuple != null) {
				out.writeInt(_ids[i]);
				tuple.binary(out);
			}
		}
	}
}