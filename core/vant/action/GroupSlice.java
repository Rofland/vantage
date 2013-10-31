package vant.action;

import java.io.IOException;

import vant.Ensure;
import vant.Usage;
import vant.lang.Exchange;
import vant.lang.Reader;
import vant.lang.Writer;
import vant.model.Div;

public class GroupSlice extends Magician {
	protected final Div _div;
	protected int _offset;
	protected byte _size;
	protected int _group;

	public GroupSlice(Div div, Magic m) {
		super(m, Byte.MAX_VALUE);
		_div = div;
	}

	@Override
	public void perform() throws Exception {
		_count = _div.rlist(_group, _offset, _size, _ids);
		_magic.turn(_ids, 0, _count);
	}

	@Override
	public int binaryLimit() {
		return 9;
	}

	@Override
	public void validate() throws Usage {
		Ensure.range("group", _group, 0, Integer.MAX_VALUE);
		Ensure.range("offset", _offset, 0, Integer.MAX_VALUE);
		Ensure.range("size", _size, 0, _ids.length);
	}

	@Override
	public void decode(Reader r) throws IOException {
		exchange(r, 0, 0, (byte) 10);
	}

	@Override
	public void encode(Writer w) throws IOException {
		exchange(w, _group, _offset, _size);
	}

	protected void exchange(Exchange x, int group, int offset, byte size)
			throws IOException {
		_group = x.INT("group", group);
		_offset = x.INT("offset", offset);
		_size = x.BYTE("size", size);
	}
}
