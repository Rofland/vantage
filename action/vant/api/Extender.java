package vant.api;

import java.io.DataOutput;
import java.io.IOException;

import vant.Lang;

public abstract class Extender<T extends Tuple> {
	protected T _tuple;

	public void bind(T tuple) {
		extend(tuple);
		_tuple = tuple;

	}

	public abstract int traitCount();

	public abstract int binaryLimit();

	protected abstract void extend(T tuple);

	public abstract void array(Lang.Writer out) throws IOException;

	public abstract void object(Lang.Writer out) throws IOException;

	public abstract void binary(DataOutput out) throws IOException;

	protected static final Extender<? extends Tuple> nil = new Extender<Tuple>() {
		public int traitCount() {
			return 0;
		}

		public int binaryLimit() {
			return 0;
		}

		protected void extend(Tuple tuple) {
		}

		public void array(Lang.Writer out) {
		}

		public void object(Lang.Writer out) {
		}

		public void binary(DataOutput out) {
		}
	};

	@SuppressWarnings("unchecked")
	public static <T extends Tuple> Extender<T> NOOP() {
		return (Extender<T>) nil;
	}
}
