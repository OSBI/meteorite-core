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

import java.util.List;

/**
 * Created by bugg on 03/07/15.
 */
public interface IUserManagement {

  MeteoriteUser addUser(MeteoriteUser u) throws MeteoriteSecurityException;

  boolean deleteUser(MeteoriteUser u)  throws MeteoriteSecurityException;

  MeteoriteUser setUser(MeteoriteUser u)  throws MeteoriteSecurityException;

  MeteoriteUser getUser(int id)  throws MeteoriteSecurityException;

  String[] getRoles(MeteoriteUser u)  throws MeteoriteSecurityException;

  void addRole(MeteoriteUser u)  throws MeteoriteSecurityException;

  void removeRole(MeteoriteUser u)  throws MeteoriteSecurityException;

  void removeUser(String username) throws MeteoriteSecurityException;

  MeteoriteUser updateUser(MeteoriteUser u)  throws MeteoriteSecurityException;

  boolean isAdmin()  throws MeteoriteSecurityException;

  List<String> getAdminRoles()  throws MeteoriteSecurityException;


}
