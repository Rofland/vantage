package vant.action;

import java.io.IOException;

import vant.Ensure;
import vant.Usage;
import vant.lang.Reader;
import vant.lang.Writer;

public final class Load extends Magician {
	public Load(Magic m) {
		super(m, Byte.MAX_VALUE);
	}

	@Override
	public void perform() throws Exception {
		_magic.turn(_ids, 0, _count);
	}

	@Override
	public int binaryLimit() {
		return 4 * _ids.length;
	}

	@Override
	public void validate() throws Usage {
		Ensure.range("count", _count, 0, _ids.length);
		for (int i = 0; i < _count; i++)
			Ensure.id("id", _ids[i]);
	}

	@Override
	public void decode(Reader r) throws IOException {
		_count = r.array("ids");
		int n = Math.min(_count, _ids.length);
		int i = 0;
		for (; i < n; i++)
			_ids[i] = r.INT("id", 0);
		for (; i < _count; i++)
			r.INT("id", 0);
		r.end();
	}

	@Override
	public void encode(Writer w) throws IOException {
		w.array("ids", _count);
		for (int i = 0; i < _count; i++)
			w.INT("id", _ids[i]);
	}
}