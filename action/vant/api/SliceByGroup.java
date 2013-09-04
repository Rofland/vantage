package vant.api;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

import vant.Ensure;
import vant.Lang;
import vant.Usage;

public class SliceByGroup<T extends Tuple> extends Action {
	protected final Repo<T> _repo;
	protected final Grouping _group;
	protected int _offset;
	protected short _size;
	protected int _key;
	protected final short _capacity;

	public SliceByGroup(Repo<T> repo, Grouping g, short capacity) throws Usage {
		_repo = repo;
		_group = g;
		_capacity = (short) Ensure.range("ByGroup.capacity", capacity, 1,
				Short.MAX_VALUE);
	}

	public void key(int v) throws Usage {
		_key = Ensure.range("ByGroup.key", v, 0, Integer.MAX_VALUE);
	}

	public void offset(int v) throws Usage {
		_offset = Ensure.range("ByGroup.offset", v, 0, Integer.MAX_VALUE);
	}

	public void size(short v) throws Usage {
		_size = (short) Ensure.range("ByGroup.size", v, 0, _capacity);
	}

	@Override
	public int binaryLimit() {
		return 10;
	}

	@Override
	public void parse(JSONObject json) throws Usage {
		key(json.optInt("key"));
		offset(json.optInt("offset"));
		size((short) json.optInt("size"));
	}

	@Override
	public void parse(JSONArray json) throws Usage {
		key(json.optInt(1));
		offset(json.optInt(2));
		size((short) json.optInt(3));
	}

	@Override
	public void parse(ByteBuffer bin) throws Usage {
		key(bin.getInt());
		offset(bin.getInt());
		size(bin.getShort());
	}

	@Override
	public void array(Lang.Writer out) throws IOException {
		out.value(_key);
		out.value(_offset);
		out.value(_size);
	}

	@Override
	public void object(Lang.Writer out) throws IOException {
		out.key("key").value(_key);
		out.key("offset").value(_offset);
		out.key("size").value(_size);
	}

	@Override
	public void binary(DataOutput out) throws IOException {
		out.writeInt(_key);
		out.writeInt(_offset);
		out.writeShort(_size);
	}

	@Override
	public void perform() throws Exception {
		
	}

	@Override
	public void result(Lang.Writer out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void result(DataOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

}
