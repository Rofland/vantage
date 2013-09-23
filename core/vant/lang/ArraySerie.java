package vant.lang;

import java.util.Arrays;

import vant.Misc;

public class ArraySerie implements Serie {
	protected Object[] array = Misc.ZERO_OBJECT;
	protected int count;

	public final int count() {
		return count;
	}

	@Override
	public Object value(int i) {
		return array[i];
	}

	@Override
	public Struct struct(int i, Struct alt) {
		Object v = array[i];
		return v instanceof Struct ? (Struct) v : alt;
	}

	@Override
	public Serie serie(int i, Serie alt) {
		Object v = array[i];
		return v instanceof Serie ? (Serie) v : alt;
	}

	@Override
	public void push(Object v) {
		if (count == array.length) {
			int n = count < 10 ? 10 : count + count / 2;
			array = Arrays.copyOf(array, n);
		}
		array[count] = v;
		count++;
	}

	@Override
	public void put(int i, Object v) {
		array[i] = v;
	}
}
