package vant.action;

import java.io.IOException;

import vant.Ensure;
import vant.Usage;
import vant.index.Rank;
import vant.lang.Exchange;
import vant.lang.Reader;
import vant.lang.Writer;

public class RankSlice extends Magician {
	protected final Rank _rank;
	protected int _offset;
	protected byte _size;

	public RankSlice(Rank rank, Magic m) {
		super(m, Byte.MAX_VALUE);
		_rank = rank;
	}

	@Override
	public void perform() throws Exception {
		_count = _rank.slice(_offset, _size, _ids);
		_magic.turn(_ids, 0, _count);
	}

	@Override
	public int binaryLimit() {
		return 5;
	}

	@Override
	public void validate() throws Usage {
		Ensure.range("offset", _offset, 0, Integer.MAX_VALUE);
		Ensure.range("size", _size, 0, _ids.length);
	}

	@Override
	public void decode(Reader r) throws IOException {
		exchange(r, 0, (byte) 10);
	}

	@Override
	public void encode(Writer w) throws IOException {
		exchange(w, _offset, _size);
	}

	public void exchange(Exchange x, int offset, byte size) throws IOException {
		_offset = x.INT("offset", offset);
		_size = x.BYTE("size", size);
	}
}
