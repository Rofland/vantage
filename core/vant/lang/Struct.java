package vant.lang;

public interface Struct {
	Object value(String k);

	Struct struct(String k, Struct alt);

	Serie serie(String k, Serie alt);

	void put(String k, Object v);

	public static final Struct ZERO = new Struct() {

		@Override
		public Object value(String k) {
			return null;
		}

		@Override
		public Struct struct(String k, Struct alt) {
			return alt;
		}

		@Override
		public Serie serie(String k, Serie alt) {
			return alt;
		}

		@Override
		public void put(String k, Object v) {
		}
	};
}
