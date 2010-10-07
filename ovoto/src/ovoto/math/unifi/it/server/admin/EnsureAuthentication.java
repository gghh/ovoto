package ovoto.math.unifi.it.server.admin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import ovoto.math.unifi.it.client.AuthenticationService;
import ovoto.math.unifi.it.client.NotAuthenticatedException;
import ovoto.math.unifi.it.client.NotAuthorizedException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EnsureAuthentication extends RemoteServiceServlet implements Filter, AuthenticationService  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	//Filter
	@Override
	public void destroy() {
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {


		UserService us = UserServiceFactory.getUserService();

		User u = us.getCurrentUser();

		if(u==null) {
			//interrupt
			HttpServletResponse httpResp = (HttpServletResponse)resp;
			httpResp.sendError(HttpServletResponse.SC_FORBIDDEN, "Auth Required");
		} else 
			chain.doFilter(req, resp);
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	
	
	
	//Service
	@Override
	public User getCurrentUser(String myUrl) throws NotAuthenticatedException, NotAuthorizedException {
		UserService us = UserServiceFactory.getUserService();
		User u = us.getCurrentUser();
		if(u==null) {
			throw new NotAuthenticatedException(us.createLoginURL(myUrl));
		} else {
			if(us.isUserAdmin())
				return u;
			else
				throw new NotAuthorizedException(u.getEmail() +" is not authorized for this service (i.e. it is not an administrator)", us.createLogoutURL(myUrl));
		}
	}
	
	

}
