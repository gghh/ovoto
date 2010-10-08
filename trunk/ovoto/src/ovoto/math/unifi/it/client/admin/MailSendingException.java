package ovoto.math.unifi.it.client.admin;

public class MailSendingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected MailSendingException() {}
	
	public MailSendingException(String msg) {
		super(msg);
	}
}
