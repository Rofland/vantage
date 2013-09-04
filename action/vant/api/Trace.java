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

public abstract class Trace<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected final Extender<T> _ext;
	protected int _seed;
	protected int _hop;
	protected final int[] _ids;
	protected final T[] _tuples;
	protected int _count;

	public Trace(Repo<T> repo, Extender<T> ext, int capacity) throws Usage {
		_repo = Ensure.notNull("Trace.repo", repo);
		_ext = ext == null ? Extender.<T> NOOP() : ext;
		Ensure.range("Trace.capacity", capacity, 1, 100);
		_ids = new int[capacity + 1];
		_tuples = Silently.create(_repo.clazz, capacity + 1);
	}

	@Override
	public void perform() throws Exception {
		int id = _seed;
		for (_count = 0; id > 0 && _count <= _hop; _count++) {
			T tuple = _tuples[_count];
			if (!_repo.read(id, tuple))
				break;
			_ids[_count] = id;
			id = trace(tuple);
		}
	}

	protected abstract int trace(T tuple);

	protected void seed(int v) throws Usage {
		_seed = Ensure.id("Trace.seed", v);
	}

	protected void hop(byte v) throws Usage {
		_hop = (byte) Ensure.range("Trace.hop", v, 1, _ids.length);
	}

	@Override
	public int binaryLimit() {
		return 5;
	}

	@Override
	public void parse(JSONObject json) throws Usage {
		seed(json.optInt("seed"));
		hop((byte) json.optInt("hop", 2));
	}

	@Override
	public void parse(JSONArray json) throws Usage {
		seed(json.optInt(0));
		hop((byte) json.optInt(1, 2));
	}

	@Override
	public void parse(ByteBuffer bin) throws Usage {
		seed(bin.getInt());
		hop(bin.get());
	}

	@Override
	public void array(Lang.Writer out) throws IOException {
		out.value(_seed);
		out.value(_hop);
	}

	@Override
	public void object(Lang.Writer out) throws IOException {
		out.key("seed").value(_seed);
		out.key("hop").value(_hop);
	}

	@Override
	public void binary(DataOutput out) throws IOException {
		out.writeInt(_seed);
		out.writeByte(_hop);
	}

	@Override
	public void result(Lang.Writer out) throws IOException {
		out.array();
		for (int i = 0; i < _count; i++) {
			out.array();
			out.value(_ids[i]);
			T tuple = _tuples[i];
			tuple.array(out);
			_ext.bind(tuple);
			_ext.array(out);
			out.end();
		}
		out.end();
	}

	@Override
	public void result(DataOutput out) throws IOException {
		out.writeInt(_count);
		for (int i = 0; i < _count; i++) {
			out.writeInt(_ids[i]);
			T tuple = _tuples[i];
			tuple.binary(out);
			_ext.bind(tuple);
			_ext.binary(out);
		}
	}
}
