package bi.meteorite.core.security;

import bi.meteorite.core.api.security.AdminLoginService;
import bi.meteorite.core.api.security.rest.UserService;


import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import java.util.Dictionary;

@OsgiServiceProvider(classes = { AdminLoginService.class })
public class ConfigAdminLoginProvider implements AdminLoginService {

  private volatile String username;
  private volatile String password;


  @Override
  public String getUsername() {
	return username;
  }

  public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws Exception {
	String usernameProperty = (String) properties.get("username");
	String passwordProperty = (String) properties.get("password");

	if (usernameProperty == null) {
	  //throw new ConfigurationException("username", "Required property username missing");
	}

	if (passwordProperty == null) {
	  //throw new ConfigurationException("password", "Required property passsword missing");
	}

	username = usernameProperty;
	password = passwordProperty;
  }

  public boolean login(String username, String password) {
	return username.equals(this.username) && password.equals(this.password);
  }
}
