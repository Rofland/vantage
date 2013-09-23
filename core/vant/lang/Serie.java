package vant.lang;

public interface Serie {
	int count();

	Object value(int i);

	Struct struct(int i, Struct alt);

	Serie serie(int i, Serie alt);

	void put(int i, Object v);

	void push(Object v);

	public static final Serie ZERO = new Serie() {
		@Override
		public int count() {
			return 0;
		}

		@Override
		public Object value(int i) {
			return null;
		}

		@Override
		public Struct struct(int i, Struct alt) {
			return alt;
		}

		@Override
		public Serie serie(int i, Serie alt) {
			return alt;
		}

		@Override
		public void put(int i, Object v) {
		}

		@Override
		public void push(Object v) {
		}
	};
}