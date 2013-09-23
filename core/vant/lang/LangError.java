package vant.lang;

public class LangError extends RuntimeException {
	private static final long serialVersionUID = 5904865438980858240L;

	public final String lang;
	public final String hint;

	public LangError(String lang, String hint) {
		super(String.format("[%s] %s", lang, hint));
		this.lang = lang;
		this.hint = hint;
	}
}
