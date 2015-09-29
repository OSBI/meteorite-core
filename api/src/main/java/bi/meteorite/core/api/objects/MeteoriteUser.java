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

package bi.meteorite.core.api.objects;

/**
 * A user object for Meteorite core.
 */
public interface MeteoriteUser {

  /**
   * Get the username.
   * @return the username
   */
  String getUsername();

  /**
   * Set the username.
   * @param username the username.
   */
  void setUsername(String username);

  /**
   * Get the user password.
   * @return the password.
   */
  String getPassword();

  /**
   * Set the user password.
   * @param password the password.
   */
  void setPassword(String password);

  /**
   * Get an array of roles.
   * @return an array of roles.
   */
  String[] getRoles();

  /**
   * Set the roles.
   * @param roles an array of roles.
   */
  void setRoles(String[] roles);

  /**
   * Get the user email address.
   * @return the email address.
   */
  String getEmail();

  /**
   * Set the email address.
   * @param email the email address.
   */
  void setEmail(String email);

  /**
   * Get the users unique id
   * @return the id
   */
  int getId();

  /**
   * Set the users id.
   * @param id the id.
   */
  void setId(int id);

  /**
   * Get the organization id.
   * @return the org id.
   */
  int getOrgId();

  /**
   * Set the organization id.
   * @param orgId the organization id.
   */
  void setOrgId(int orgId);
}

