package vant.api;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONObject;

import vant.Ensure;
import vant.Lang;
import vant.Usage;

public abstract class Slice extends Action {

	protected int _offset;
	protected short _size;

	protected final short _capacity;

	public Slice(short capacity) throws Usage {
		_capacity = (short) Ensure.range("Slice.capacity", capacity, 1,
				Short.MAX_VALUE);
	}

	public int binaryLimit() {
		return 6;
	}

	public void offset(int v) throws Usage {
		_offset = Ensure.range("Slice.offset", v, 0, Integer.MAX_VALUE);
	}

	public void size(short v) throws Usage {
		_size = (short) Ensure.range("Slice.size", v, 0, _capacity);
	}

	@Override
	public void parse(JSONObject json) throws Usage {
		offset(json.optInt("offset"));
		size((short) json.optInt("size"));
	}

	@Override
	public void parse(JSONArray json) throws Usage {
		offset(json.optInt(1));
		size((short) json.optInt(2));
	}
	

	@Override
	public void parse(ByteBuffer bin) throws Usage {
		offset(bin.getInt());
		size(bin.getShort());
	}

	public void array(Lang.Writer out) throws IOException {
		out.value(_offset);
		out.value(_size);
	}

	public void object(Lang.Writer out) throws IOException {
		out.key("offset").value(_offset);
		out.key("size").value(_size);
	}

	public void binary(DataOutput out) throws IOException {
		out.writeInt(_offset);
		out.writeShort(_size);
	}
}
