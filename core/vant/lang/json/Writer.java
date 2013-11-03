package vant.lang.json;

import java.io.IOException;
import java.util.Arrays;

import vant.Misc;
import vant.lang.LangError;

public class Writer extends vant.lang.Writer {
	protected boolean _comma = false;
	protected boolean _kv = false;
	protected boolean[] _nest = new boolean[8];
	protected int _depth = 0;
	protected Appendable _dst;

	public Writer(Appendable appender) {
		this._dst = appender;
		_nest[0] = false;
	}

	private void value(String v, boolean quote) throws IOException {
		if (_kv)
			throw new LangError(Json.LANG, "Unexpected value");
		if (_comma)
			_dst.append(',');
		if (quote)
			Json.quote(v, _dst);
		else
			_dst.append(v);
		finish();
	}

	private void finish() throws IOException {
		if (_depth == 0) {
			_comma = _kv = false;
			_dst.append('\n');
		} else {
			_comma = true;
			_kv = _nest[_depth - 1];
		}
	}

	@Override
	public Writer key(String k) throws IOException {
		if (k == null || k.length() == 0)
			throw new LangError(Json.LANG, "Key must be non-empty string.");
		if (_comma)
			_dst.append(',');
		if (_kv) {
			Json.quote(k, _dst);
			_dst.append(':');
		}
		_comma = _kv = false;
		return this;
	}

	@Override
	public Writer BOOL(boolean v) throws IOException {
		value(Boolean.toString(v), false);
		return this;
	}

	@Override
	public Writer BYTE(byte v) throws IOException {
		value(Byte.toString(v), false);
		return this;
	}

	@Override
	public Writer CHAR(char v) throws IOException {
		value(Character.toString(v), true);
		return this;
	}

	@Override
	public Writer SHORT(short v) throws IOException {
		value(Short.toString(v), false);
		return this;
	}

	@Override
	public Writer INT(int v) throws IOException {
		value(Integer.toString(v), false);
		return this;
	}

	@Override
	public Writer LONG(long v) throws IOException {
		boolean quote = v < Misc.MIN_CLONG || v > Misc.MAX_CLONG;
		value(Long.toString(v), quote);
		return this;
	}

	@Override
	public Writer FLOAT(float v) throws IOException {
		value(Float.toString(v), false);
		return this;
	}

	@Override
	public Writer DOUBLE(double v) throws IOException {
		value(Double.toString(v), false);
		return this;
	}

	@Override
	public Writer STRING(String v) throws IOException {
		value(v, true);
		return this;
	}

	@Override
	public int depth() {
		return _depth;
	}

	@Override
	public Writer object() throws IOException {
		return begin(true);
	}

	@Override
	public Writer tuple() throws IOException {
		return begin(false);
	}

	@Override
	public Writer array(int n) throws IOException {
		return begin(false);
	}

	private Writer begin(boolean kv) throws IOException {
		if (_depth == _nest.length)
			_nest = Arrays.copyOf(_nest, _depth + 8);
		if (_kv)
			throw new LangError(Json.LANG, "Expecting array/object.");
		if (_comma)
			_dst.append(',');
		_comma = false;
		_nest[_depth] = kv;
		_depth++;
		_kv = kv;
		_dst.append(kv ? '{' : '[');
		return this;
	}

	@Override
	public Writer end() throws IOException {
		if (_depth == 0)
			throw new LangError(Json.LANG, "Unexpected end at depth 0.");
		_depth--;
		boolean kv = _nest[_depth];
		if (kv != _kv)
			throw new LangError(Json.LANG, "Unexpected end after key.");
		_dst.append(kv ? '}' : ']');
		finish();
		return this;
	}

	@Override
	public Writer flush() throws IOException {
		return this;
	}
}