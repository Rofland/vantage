package vant.spi;

public interface Persisted<C extends Conf> {
	C conf();

	void open() throws Exception;

	void setup() throws Exception;

	void erase() throws Exception;

	State check() throws Exception;

	public enum State {
		OK, NOT_EXIST, INVALID
	}
}
