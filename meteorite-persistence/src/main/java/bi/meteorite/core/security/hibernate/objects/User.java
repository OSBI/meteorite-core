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

package bi.meteorite.core.security.hibernate.objects;

import bi.meteorite.core.api.objects.MeteoriteUser;

/**
 * A User Object
 */
public class User implements MeteoriteUser {
  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public void setUsername(String username) {

  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public void setPassword(String password) {

  }

  @Override
  public String[] getRoles() {
    return new String[0];
  }

  @Override
  public void setRoles(String[] roles) {

  }

  @Override
  public String getEmail() {
    return null;
  }

  @Override
  public void setEmail(String email) {

  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public void setId(int id) {

  }

  @Override
  public int getOrgId() {
    return 0;
  }

  @Override
  public void setOrgId(int orgId) {

  }
}
