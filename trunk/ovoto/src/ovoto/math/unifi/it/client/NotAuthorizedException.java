package ovoto.math.unifi.it.client;

public class NotAuthorizedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logoutUrl;
	
	protected NotAuthorizedException() {}
	
	public NotAuthorizedException(String m,String logoutUrl) {
		super(m);
		this.logoutUrl = logoutUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}
	
	
	
	
}



