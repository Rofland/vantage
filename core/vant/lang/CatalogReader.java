package vant.lang;

import java.io.IOException;
import java.util.Stack;

import vant.Norm;

public class CatalogReader implements Reader {
	protected static class SerieX implements Struct {
		public Serie serie;
		public int index;

		public SerieX(Serie s) {
			serie = s;
		}

		@Override
		public Object value(String k) {
			return serie.value(index++);
		}

		@Override
		public Struct struct(String k, Struct alt) {
			return serie.struct(index++, alt);
		}

		@Override
		public Serie serie(String k, Serie alt) {
			return serie.serie(index++, alt);
		}

		@Override
		public void put(String k, Object v) {
		}
	}

	protected Stack<Struct> _stack = new Stack<Struct>();
	protected Struct _top;

	public void root(Struct s) {
		_top = s;
	}

	@Override
	public boolean BOOL(String k, boolean alt) throws IOException {
		return Norm.BOOL(_top.value(k), alt);
	}

	@Override
	public byte BYTE(String k, byte alt) throws IOException {
		return Norm.BYTE(_top.value(k), alt);
	}

	@Override
	public char CHAR(String k, char alt) throws IOException {
		return Norm.CHAR(_top.value(k), alt);
	}

	@Override
	public short SHORT(String k, short alt) throws IOException {
		return Norm.SHORT(_top.value(k), alt);
	}

	@Override
	public int INT(String k, int alt) throws IOException {
		return Norm.INT(_top.value(k), alt);
	}

	@Override
	public long LONG(String k, long alt) throws IOException {
		return Norm.LONG(_top.value(k), alt);
	}

	@Override
	public float FLOAT(String k, float alt) throws IOException {
		return Norm.FLOAT(_top.value(k), alt);
	}

	@Override
	public double DOUBLE(String k, double alt) throws IOException {
		return Norm.DOUBLE(_top.value(k), alt);
	}

	@Override
	public String STRING(String k, String alt) throws IOException {
		return Norm.STRING(_top.value(k), alt);
	}

	@Override
	public int depth() {
		return _stack.size() + 1;
	}

	@Override
	public boolean object(String k) throws IOException {
		_stack.push(_top);
		_top = _top.struct(k, null);
		return _top != null;
	}

	@Override
	public int array(String k) throws IOException {
		_stack.push(_top);
		Serie s = _top.serie(k, null);
		_top = (s == null || s.count() == 0) ? null : new SerieX(s);
		return s == null ? 0 : s.count();
	}

	@Override
	public void end() throws IOException {
		if (!_stack.isEmpty())
			_top = _stack.pop();
	}
}
