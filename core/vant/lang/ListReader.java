package vant.lang;

import java.util.List;

import vant.Norm;

public class ListReader implements Exchange {
	protected List<?> _list;
	protected int _index;

	@Override
	public boolean BOOL(String k, boolean alt) {
		return Norm.BOOL(_list.get(_index++), alt);

	}

	@Override
	public byte BYTE(String k, byte alt) {
		return Norm.BYTE(_list.get(_index++), alt);
	}

	@Override
	public char CHAR(String k, char alt) {
		return Norm.CHAR(_list.get(_index++), alt);
	}

	@Override
	public short SHORT(String k, short alt) {
		return Norm.SHORT(_list.get(_index++), alt);
	}

	@Override
	public int INT(String k, int alt) {
		return Norm.INT(_list.get(_index++), alt);
	}

	@Override
	public long LONG(String k, long alt) {
		return Norm.LONG(_list.get(_index++), alt);
	}

	@Override
	public float FLOAT(String k, float alt) {
		return Norm.FLOAT(_list.get(_index++), alt);
	}

	@Override
	public double DOUBLE(String k, double alt) {
		return Norm.DOUBLE(_list.get(_index++), alt);
	}

	@Override
	public String STRING(String k, String alt) {
		return Norm.STRING(_list.get(_index++), alt);
	}
}