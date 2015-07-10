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
import bi.meteorite.core.api.security.MeteoriteSecurityException;

import com.qmino.miredot.annotations.ReturnType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by bugg on 03/07/15.
 */
@Path("/core")
public interface UserService {

  @POST
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  @Path("/user")
  Response addUser(MeteoriteUser u) throws MeteoriteSecurityException;

  @PUT
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @Path("/user")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response modifyUser(MeteoriteUser u) throws MeteoriteSecurityException;

  @DELETE
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @Path("/user")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response deleteUser(MeteoriteUser u) throws MeteoriteSecurityException;

  @DELETE
  @Produces({ "application/json" })
  @Path("/user")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response deleteUser(@PathParam("userid") int id) throws MeteoriteSecurityException;

  @POST
  @Produces({ "application/json" })
  @Consumes({ "application/json" })
  @Path("/user")
  Response addGroup(String group) throws MeteoriteSecurityException;

  @DELETE
  @Produces({ "application/json" })
  @Path("/user")
  Response deleteGroup(int id) throws MeteoriteSecurityException;

  @GET
  @Produces({ "application/json" })
  @Path("/user")
  @ReturnType("java.util.List<SaikuUser>")
  Response getExistingUsers() throws MeteoriteSecurityException;

  @GET
  @Produces({ "application/json" })
  @Path("/user")
  @ReturnType("bi.meteorite.core.api.objects.MeteoriteUser")
  Response getUser(int id) throws MeteoriteSecurityException;
}
