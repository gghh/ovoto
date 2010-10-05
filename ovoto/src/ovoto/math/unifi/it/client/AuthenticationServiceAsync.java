package ovoto.math.unifi.it.client;

import com.google.appengine.api.users.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync {
	void getCurrentUser(String myUrl, AsyncCallback<User> callback);

}
