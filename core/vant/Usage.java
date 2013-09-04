package vant;

/**
 * Misuse refers to exceptions caused by upstream code. It could be avoided by
 * improving the quality of those code. Upstream developers are the targeted
 * audiences.
 */
public class Usage extends Exception {
	private static final long serialVersionUID = -7118035386212453444L;

	public final String what;
	public final String how;

	public Usage(String what, String how) {
		super(what + ": " + how);
		this.what = what;
		this.how = how;
	}
}
