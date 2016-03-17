/*
 * Copyright 2016 OSBI Ltd
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

package bi.meteorite.core.api.security.rest

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing

//import com.qmino.miredot.annotations.ReturnType
import javax.annotation.security.RolesAllowed
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

/**
  * User creation and manipulation for administrators of Meteorite core.
  */
@CrossOriginResourceSharing(allowAllOrigins = true,allowCredentials = true)
@Path("/user") trait UserService {
  /**
    * Add a user to Meteorite core.
    *
    * @param u The Meteorite User object
    * @return an HTTP response.
    * @throws MeteoriteSecurityException
    */
  @POST
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
 // @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @Path("/")
  @throws(classOf[MeteoriteSecurityException])
  def addUser(u: MeteoriteUser): Response

  /**
    * Update a user in Meteorite core.
    *
    * @param u The Meteorite User object.
    * @return an HTTP response.
    * @throws MeteoriteSecurityException
    */
  @PUT
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/")
  //@ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @throws(classOf[MeteoriteSecurityException])
  def modifyUser(u: MeteoriteUser): Response

  /**
    * Remove a user from Meteorite core.
    *
    * @param u The Meteorite User object.
    * @return An HTTP response code.
    * @throws MeteoriteSecurityException
    */
  @DELETE
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/")
  //@ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: MeteoriteUser): Response

  /**
    * Remove a user from Meteorite core.
    *
    * @param id The User ID.
    * @return An HTTP response code.
    * @throws MeteoriteSecurityException
    */
  @DELETE
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/{id}")
  //@ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(@PathParam("id") id: Int): Response

  /**
    * Add user to a group.
    *
    * @param group The group
    * @return An HTTP response.
    * @throws MeteoriteSecurityException
    */
  @POST
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/lookup/{id}/group/{gid}")
  @throws(classOf[MeteoriteSecurityException])
  def addRole(@PathParam("id") id: String, @PathParam("gid") group: Int): Response

  /**
    * Add user to a group by group name.
    *
    * @param group The group
    * @return An HTTP response.
    * @throws MeteoriteSecurityException
    */
  @POST
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/lookup/{id}/group/{group}")
  @throws(classOf[MeteoriteSecurityException])
  def addRole(@PathParam("id") id: String, @PathParam("group") group: String): Response

  /**
    * Get a list of existing users.
    *
    * @return a list of existing users.
    * @throws MeteoriteSecurityException
    */
  @GET
  @Produces(Array("application/json"))
  @Path("/")
  //@ReturnType("java.util.List<SaikuUser>")
  @RolesAllowed(Array("admin"))
  @throws(classOf[MeteoriteSecurityException])
  def getExistingUsers: Response

  /**
    * Get a user by id.
    *
    * @param id the user id.
    * @return an HTTP response code.
    * @throws MeteoriteSecurityException
    */
  @GET
  @Produces(Array("application/json"))
  @Path("/lookup/{id}")
  //@ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @throws(classOf[MeteoriteSecurityException])
  def getUser(@PathParam("id") id: Int): Response

  /**
    * Discover who is logged in.
    *
    * @return an HTTP response code.
    * @throws MeteoriteSecurityException
    */
  @GET
  @Produces(Array("application/json"))
  @Path("/whoami")
  //@ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @throws(classOf[MeteoriteSecurityException])
  def whoami: Response
}

