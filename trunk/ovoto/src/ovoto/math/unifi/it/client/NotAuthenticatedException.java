package ovoto.math.unifi.it.client;

public class NotAuthenticatedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String  authUrl;
	
	protected NotAuthenticatedException() {}
	
	public NotAuthenticatedException(String url) {
		super("Authentication Informations not found");
		authUrl = url;
	}
	
	public String getAuthUrl() {
		return authUrl;
	}
	
}



