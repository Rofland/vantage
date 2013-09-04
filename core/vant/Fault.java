package vant;

/**
 * Fault is the umbrella of all exceptions happening during the execution,
 * whether it is a bug, a hardware/network failure or cascaded exception from
 * downstream services. Normally, in the event of Fault, user session will
 * terminate with apology and developers/operators will be notified for
 * investigation and recovery.
 */
public class Fault extends Exception {
	private static final long serialVersionUID = -6646451927979387176L;

	public Fault(Exception cause) {
		super(cause);
	}
}
