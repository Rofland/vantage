package vant.lang;

import java.util.HashMap;
import java.util.Map;

public class MapStruct implements Struct {
	public Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public Object value(String k) {
		return map.get(k);
	}

	@Override
	public Struct struct(String k, Struct alt) {
		Object v = map.get(k);
		return v instanceof Struct ? (Struct) v : alt;
	}

	@Override
	public Serie serie(String k, Serie alt) {
		Object v = map.get(k);
		return v instanceof Serie ? (Serie) v : alt;
	}

	@Override
	public void put(String k, Object v) {
		map.put(k, v);
	}
}
