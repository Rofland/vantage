package vant;

/**
 * Violation refers to semantic violation detected during execution. Upstream
 * user/program is supposed to understand it and take actions.
 */
public class Violation extends Exception {
	private static final long serialVersionUID = -7175175363520725380L;
	public final int type;

	public Violation(int type, String msg) {
		super(msg);
		this.type = type;
	}
}
