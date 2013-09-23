package vant;

public abstract class Mold<T> {
	public abstract T create();

	public abstract T[] array(int n);

	public abstract void copy(T src, T dst);

	public void fill(T[] array, int off, int n) {
		int end = off + n;
		for (int i = off; i < end; i++)
			array[i] = create();
	}
}
