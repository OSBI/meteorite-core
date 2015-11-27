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

import org.apache.karaf.jaas.modules.BackingEngineService;

import java.util.List;

/**
 * Created by bugg on 03/07/15.
 */
public interface IUserManagementProvider {

  void addUser(String u, String p) throws MeteoriteSecurityException;

  void deleteUser(String u) throws MeteoriteSecurityException;

  //MeteoriteUser setUser(MeteoriteUser u) throws MeteoriteSecurityException;

  //MeteoriteUser getUser(int id) throws MeteoriteSecurityException;

  List<String> getRoles(String u) throws MeteoriteSecurityException;

  void addRole(String u, String r) throws MeteoriteSecurityException;

  void removeRole(String u, String r) throws MeteoriteSecurityException;

  void removeUser(String u) throws MeteoriteSecurityException;

  MeteoriteUser updateUser(MeteoriteUser u) throws MeteoriteSecurityException;

  boolean isAdmin(String u) throws MeteoriteSecurityException;

  List<String> getAdminRoles() throws MeteoriteSecurityException;

  void setBackingEngineService(BackingEngineService backingEngineService);
}
