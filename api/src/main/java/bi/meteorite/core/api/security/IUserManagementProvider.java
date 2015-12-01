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

package bi.meteorite.core.api.security;

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;

import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.jaas.modules.BackingEngineService;

import java.util.List;

/**
 * User Management Provider.
 * API for the main user managaement interface for Meteorite Core.
 */
public interface IUserManagementProvider {

  /**
   * Add a new user to the platform.
   * @param u username
   * @param p password
   * @throws MeteoriteSecurityException
   */
  void addUser(String u, String p) throws MeteoriteSecurityException;

  /**
   * Delete a user from the platform.
   * @param u username
   * @throws MeteoriteSecurityException
   */
  void deleteUser(String u) throws MeteoriteSecurityException;

  /**
   * Get a list of users on the platform.
   * @return a list of usernames.
   * @throws MeteoriteSecurityException
   */
  List<String> getUsers() throws MeteoriteSecurityException;

  //MeteoriteUser setUser(MeteoriteUser u) throws MeteoriteSecurityException;

  //MeteoriteUser getUser(int id) throws MeteoriteSecurityException;

  /**
   * Get a list of roles applied to a user.
   * @param u username
   * @return a list of roles.
   * @throws MeteoriteSecurityException
   */
  List<String> getRoles(String u) throws MeteoriteSecurityException;

  /**
   * Add a role to a user.
   * @param u username
   * @param r role name
   * @throws MeteoriteSecurityException
   */
  void addRole(String u, String r) throws MeteoriteSecurityException;

  /**
   * Remove role from a user
   * @param u username
   * @param r role
   * @throws MeteoriteSecurityException
   */
  void removeRole(String u, String r) throws MeteoriteSecurityException;

  /**
   * Update a user on the platform.
   * @param u user object
   * @return a updated user object
   * @throws MeteoriteSecurityException
   */
  MeteoriteUser updateUser(MeteoriteUser u) throws MeteoriteSecurityException;

  /**
   * Discover is the user is an administrator or not.
   * @param u username
   * @return true if the user is admin.
   * @throws MeteoriteSecurityException
   */
  boolean isAdmin(String u) throws MeteoriteSecurityException;

  /**
   * Get a list of roles that define an administrator.
   * @return a list of administrative roles
   * @throws MeteoriteSecurityException
   */
  List<String> getAdminRoles() throws MeteoriteSecurityException;

  /**
   * Set the backing service engine that drives the security.
   * @param backingEngineService
   */
  void setBackingEngineService(BackingEngineService backingEngineService);

  /**
   * Set the JaaS realm.
   * @param realm
   */
  void setRealm(JaasRealm realm);
}
