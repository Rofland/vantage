package vant.model;

import java.io.IOException;

import vant.Usage;
import vant.lang.Exchange;

public abstract class Tuple {
	public static final Tuple[] ZERO = new Tuple[0];
	
	public abstract void validate() throws Usage;

	public abstract int binaryLimit();

	public abstract void decode(Exchange r) throws IOException, Usage;

	public abstract void encode(Exchange w) throws IOException;
}
