/*
 * Copyright 2015 OSBI Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bi.meteorite.core.security;

import bi.meteorite.core.api.security.AdminLoginService;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import java.util.Dictionary;

/**
 * Config Admin Login Provider
 */
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
