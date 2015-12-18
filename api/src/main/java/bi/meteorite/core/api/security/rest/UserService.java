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

package bi.meteorite.core.api.security.rest;

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;

import com.qmino.miredot.annotations.ReturnType;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * User creation and manipulation for administrators of Meteorite core.
 */
@Path("/")
public interface UserService {

  /**
   * Add a user to Meteorite core.
   *
   * @param u The Meteorite User object
   * @return an HTTP response.
   * @throws MeteoriteSecurityException
   */
  @POST
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @Path("/user")
  Response addUser(MeteoriteUser u) throws MeteoriteSecurityException;

  /**
   * Update a user in Meteorite core.
   *
   * @param u The Meteorite User object.
   * @return an HTTP response.
   * @throws MeteoriteSecurityException
   */
  @PUT
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @Path("/user")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response modifyUser(MeteoriteUser u) throws MeteoriteSecurityException;

  /**
   * Remove a user from Meteorite core.
   *
   * @param u The Meteorite User object.
   * @return An HTTP response code.
   * @throws MeteoriteSecurityException
   */
  @DELETE
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @Path("/user")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response deleteUser(MeteoriteUser u) throws MeteoriteSecurityException;

  /**
   * Remove a user from Meteorite core by user id.
   *
   * @param id The user id.
   * @return an HTTP response code.
   * @throws MeteoriteSecurityException
   */
  @DELETE
  @Produces({ "application/json" })
  @Path("/user/lookup/{userid}")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response deleteUser(@PathParam("userid") String id) throws MeteoriteSecurityException;

  /**
   * Add user to a group.
   *
   * @param group The group
   * @return An HTTP response.
   * @throws MeteoriteSecurityException
   */
  @POST
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @Path("/user/lookup/{id}/group/{gid}")
  Response addRole(@PathParam("id") String id, @PathParam("gid") int group) throws MeteoriteSecurityException;

  /**
   * Add user to a group by group name.
   *
   * @param group The group
   * @return An HTTP response.
   * @throws MeteoriteSecurityException
   */
  @POST
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @Path("/user/lookup/{id}/group/{group}")
  Response addRole(@PathParam("id") String id, @PathParam("group") String group) throws MeteoriteSecurityException;

  /**
   * Get a list of existing users.
   *
   * @return a list of existing users.
   * @throws MeteoriteSecurityException
   */
  @GET
  @Produces({ "application/json" })
  @Path("/user")
  @ReturnType("java.util.List<SaikuUser>")
  @RolesAllowed("admin")
  Response getExistingUsers() throws MeteoriteSecurityException;

  /**
   * Get a user by id.
   *
   * @param id the user id.
   * @return an HTTP response code.
   * @throws MeteoriteSecurityException
   */
  @GET
  @Produces({ "application/json" })
  @Path("/user/lookup/{id}")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response getUser(@PathParam("id") int id) throws MeteoriteSecurityException;

  /**
   * Discover who is logged in.
   *
   * @return an HTTP response code.
   * @throws MeteoriteSecurityException
   */
  @GET
  @Produces({ "application/json" })
  @Path("/user/whoami")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response whoami() throws MeteoriteSecurityException;
}
