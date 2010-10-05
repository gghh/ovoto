package ovoto.math.unifi.it.client;

import com.google.appengine.api.users.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("authenticate")
public interface AuthenticationService  extends RemoteService {
		User getCurrentUser(String myUrl) throws NotAuthenticatedException, NotAuthorizedException;
	}
