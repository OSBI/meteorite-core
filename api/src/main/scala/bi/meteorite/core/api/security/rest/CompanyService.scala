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

import bi.meteorite.core.api.objects.MeteoriteCompany
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
@Path("/company") trait CompanyService {
  /**
    * Add a company to Meteorite core.
    *
    * @param company The Meteorite Company object
    * @return an HTTP response.
    * @throws MeteoriteSecurityException
    */
  @POST
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/")
  @throws(classOf[MeteoriteSecurityException])
  def addCompany(company: MeteoriteCompany): Response

  /**
    * Update a company in Meteorite core.
    *
    * @param company The Meteorite Company object.
    * @return an HTTP response.
    * @throws MeteoriteSecurityException
    */
  @PUT
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/")
  @throws(classOf[MeteoriteSecurityException])
  def modifyCompany(company: MeteoriteCompany): Response

  /**
    * Remove a company from Meteorite core.
    *
    * @param company The Meteorite Company object.
    * @return An HTTP response code.
    * @throws MeteoriteSecurityException
    */
  @DELETE
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/")
  @throws(classOf[MeteoriteSecurityException])
  def deleteCompany(company: MeteoriteCompany): Response

  /**
    * Remove a company from Meteorite core.
    *
    * @param id The Company ID.
    * @return An HTTP response code.
    * @throws MeteoriteSecurityException
    */
  @DELETE
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/{id}")
  @throws(classOf[MeteoriteSecurityException])
  def deleteCompany(@PathParam("id") id: Long): Response

  /**
    * Add user to a company.
    *
    * @param user The user
    * @return An HTTP response.
    * @throws MeteoriteSecurityException
    */
  @POST
  @Produces(Array("application/json"))
  @Consumes(Array("application/json"))
  @Path("/lookup/{companyId}/user/{username}")
  @throws(classOf[MeteoriteSecurityException])
  def addUser(@PathParam("companyId") companyId: Long, @PathParam("username") username: String): Response

  /**
    * Get a list of companies.
    *
    * @return a list of existing companies.
    * @throws MeteoriteSecurityException
    */
  @GET
  @Produces(Array("application/json"))
  @Path("/")
  @throws(classOf[MeteoriteSecurityException])
  def getCompanies: Response
}
