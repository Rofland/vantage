package vant.lang.json;

import java.io.IOException;
import java.util.BitSet;

import vant.Misc;
import vant.lang.LangError;

public class Writer implements vant.lang.Writer {
	private static final int maxdepth = 200;
	private boolean _comma = false;
	protected char _mode = 'i';
	private final BitSet _stack = new BitSet();
	private int _depth = 0;
	protected Appendable _dst;

	public Writer(Appendable appender) {
		this._dst = appender;
	}

	private void newline() throws IOException {
		_dst.append('\n');
		for (int i = 0; i < _depth; i++)
			_dst.append('\t');
	}

	private void value(String k, String v, boolean quote) throws IOException {
		if (_comma)
			_dst.append(',');
		switch (_mode) {
		case 'o':
			if (k == null || k.length() == 0)
				throw new LangError(Json.LANG, "Key must be non-empty string.");
			Json.quote(k, _dst);
			_dst.append(':');
		case 'a':
			if (quote)
				Json.quote(v, _dst);
			else
				_dst.append(v);
			break;
		default:
			throw new LangError(Json.LANG, "Misplaced primitive.");
		}
		_comma = true;
	}

	@Override
	public boolean BOOL(String k, boolean v) throws IOException {
		value(k, Boolean.toString(v), false);
		return v;
	}

	@Override
	public byte BYTE(String k, byte v) throws IOException {
		value(k, Byte.toString(v), false);
		return v;
	}

	@Override
	public char CHAR(String k, char v) throws IOException {
		value(k, Character.toString(v), true);
		return v;
	}

	@Override
	public short SHORT(String k, short v) throws IOException {
		value(k, Short.toString(v), false);
		return v;
	}

	@Override
	public int INT(String k, int v) throws IOException {
		value(k, Integer.toString(v), false);
		return v;
	}

	@Override
	public long LONG(String k, long v) throws IOException {
		boolean quote = v < Misc.MIN_CLONG || v > Misc.MAX_CLONG;
		value(k, Long.toString(v), quote);
		return v;
	}

	@Override
	public float FLOAT(String k, float v) throws IOException {
		value(k, Float.toString(v), false);
		return v;
	}

	@Override
	public double DOUBLE(String k, double v) throws IOException {
		value(k, Double.toString(v), false);
		return v;
	}

	@Override
	public String STRING(String k, String v) throws IOException {
		value(k, v, true);
		return v;
	}

	@Override
	public int depth() {
		return _depth;
	}

	@Override
	public void object(String k, boolean compact) throws IOException {
		begin(k, compact ? 'a' : 'o');
	}

	@Override
	public void array(String k, int n) throws IOException {
		begin(k, 'a');
	}

	private void begin(String k, char mode) throws IOException {
		if (_depth == maxdepth)
			throw new LangError(Json.LANG, "Nesting too deep.");
		if (_comma)
			_dst.append(',');
		switch (_mode) {
		case 'o':
			if (k == null || k.length() == 0)
				throw new LangError(Json.LANG, "Key must be non-empty string.");
			Json.quote(k, _dst);
			_dst.append(':');
		case 'i':
		case 'a':
			_dst.append(mode == 'o' ? '{' : '[');
			_stack.set(_depth, mode == 'o');
			_depth++;
			_comma = false;
			_mode = mode;
			if (mode == 'o')
				newline();
			break;
		default:
			throw new LangError(Json.LANG, "Misplaced object.");
		}
	}

	@Override
	public void end() throws IOException {
		if (_mode != 'o' && _mode != 'a')
			throw new LangError(Json.LANG, "Nesting error.");
		_depth--;
		if (_mode == 'o')
			newline();
		_dst.append(_mode == 'o' ? '}' : ']');
		_mode = _depth == 0 ? 'd' : _stack.get(_depth - 1) ? 'o' : 'a';
		_comma = true;
	}

	@Override
	public void flush() throws IOException {
	}
}