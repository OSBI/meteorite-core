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

package bi.meteorite.core.security.rest;

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.security.IUserManagementProvider;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;
import bi.meteorite.core.api.security.rest.UserService;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

/**
 * Created by bugg on 10/07/15.
 */
@OsgiServiceProvider(classes = { UserService.class })
@Singleton
public class UserServiceImpl implements UserService {

  private IUserManagementProvider iUserManagementProvider;


  @Override
  public Response addUser(MeteoriteUser u) throws MeteoriteSecurityException {
    iUserManagementProvider.addUser(u.getUsername(), u.getPassword());
    //TODO add other information
    return Response.ok().build();
  }

  @Override
  public Response modifyUser(MeteoriteUser u) throws MeteoriteSecurityException {
    iUserManagementProvider.updateUser(u);
    return Response.ok().build();
  }

  @Override
  public Response deleteUser(MeteoriteUser u) throws MeteoriteSecurityException {
    iUserManagementProvider.deleteUser(u.getUsername());
    return Response.ok().build();
  }

  @Override
  public Response deleteUser(int id) throws MeteoriteSecurityException {
    return null;
  }

  @Override
  public Response addGroup(int id, int group) throws MeteoriteSecurityException {
    return null;
  }

  @Override
  public Response addGroup(int id, String group) throws MeteoriteSecurityException {
    return null;
  }

  @Override
  public Response getExistingUsers() throws MeteoriteSecurityException {
    return Response.ok("{\"login\":{\"password\":\"pass\",\"username\":\"test\"}}").build();
  }

  @Override
  public Response getUser(int id) throws MeteoriteSecurityException {
    return null;
  }
}
