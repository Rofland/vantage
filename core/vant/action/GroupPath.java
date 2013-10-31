package vant.action;

import java.io.IOException;

import vant.Ensure;
import vant.Usage;
import vant.lang.Exchange;
import vant.lang.Reader;
import vant.lang.Writer;
import vant.model.Div;

public class GroupPath extends Magician {
	protected final Div _tree;
	protected int _seed;
	protected byte _hop;

	public GroupPath(Div div, Magic m) {
		super(m, Byte.MAX_VALUE);
		_tree = div;
	}

	@Override
	public void perform() throws Exception {
		_count = 0;
		int id = _tree.value(_seed);
		while (id > 0 && _count < _hop) {
			_ids[_count] = id;
			id = _tree.value(id);
			_count++;
		}
		_magic.turn(_ids, 0, _count);
	}

	@Override
	public int binaryLimit() {
		return 5;
	}

	@Override
	public void validate() throws Usage {
		Ensure.id("seed", _seed);
		Ensure.range("hop", _hop, 1, _ids.length);
	}

	@Override
	public void decode(Reader r) throws IOException {
		exchange(r, 0, (byte) 10);
	}

	@Override
	public void encode(Writer w) throws IOException {
		exchange(w, _seed, _hop);
	}

	protected void exchange(Exchange x, int seed, byte hop) throws IOException {
		_seed = x.INT("seed", seed);
		_hop = x.BYTE("hop", hop);
	}
}
