package vant;

public abstract class FlowStrings {
	protected CharSequence[] array;
	protected int start;
	protected int index;

	public void bind(CharSequence[] array, int offset) {
		this.array = array;
		this.start = offset;
		this.index = offset;
	}

	public class Reader extends FlowStrings implements Flow.Reader {
		@Override
		public boolean BOOL(boolean alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			if (Misc.equalsIgnoreCase(v, 0, "true", 0, 4)
					|| Misc.equalsIgnoreCase(v, 0, "yes", 0, 3))
				return true;
			if (Misc.equalsIgnoreCase(v, 0, "false", 0, 5)
					|| Misc.equalsIgnoreCase(v, 0, "no", 0, 2))
				return false;
			return alt;
		}

		@Override
		public byte BYTE(byte alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			return Byte.decode(v.toString());
		}

		@Override
		public char CHAR(char alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			return v.charAt(0);
		}

		@Override
		public short SHORT(short alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			return Short.decode(v.toString());
		}

		@Override
		public int INT(int alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			return Integer.decode(v.toString());
		}

		@Override
		public long LONG(long alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			return Long.decode(v.toString());
		}

		@Override
		public float FLOAT(float alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			return Float.parseFloat(v.toString());
		}

		@Override
		public double DOUBLE(double alt) {
			CharSequence v = array[index++];
			if (v == null || v.length() == 0)
				return alt;
			return Double.parseDouble(v.toString());
		}

		@Override
		public String STRING(String alt) {
			CharSequence v = array[index++];
			return v == null || v.length() == 0 ? alt : v.toString();
		}
	}

	public class Writer extends FlowStrings implements Flow.Writer {
		@Override
		public void value(boolean v) {
			array[index++] = Boolean.toString(v);
		}

		@Override
		public void value(byte v) {
			array[index++] = Byte.toString(v);
		}

		@Override
		public void value(char v) {
			array[index++] = Character.toString(v);
		}

		@Override
		public void value(short v) {
			array[index++] = Short.toString(v);
		}

		@Override
		public void value(int v) {
			array[index++] = Integer.toString(v);
		}

		@Override
		public void value(long v) {
			array[index++] = Long.toString(v);
		}

		@Override
		public void value(float v) {
			array[index++] = Float.toString(v);
		}

		@Override
		public void value(double v) {
			array[index++] = Double.toString(v);
		}

		@Override
		public void value(CharSequence v) {
			array[index++] = v.toString();
		}
	}
}
