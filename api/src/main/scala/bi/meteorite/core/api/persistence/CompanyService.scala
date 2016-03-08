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
package bi.meteorite.core.api.persistence

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.objects.MeteoriteRole

/**
  * Interface for persisting companies.
  */
trait CompanyService {
  /**
    * Update an existing company.
    * @param company the updated company object
    * @return The merged company.
    */
  def mergeCompany(company: MeteoriteCompany) : MeteoriteCompany

  /**
    * Get company by ID.
    * @param id The Id of the company object.
    * @return The MeteoriteCompany.
    */
  def getCompany(id: Long): MeteoriteCompany

  /**
    * Add a new MeteoriteCompany.
    * @param company The MeteoriteCompany object.
    * @return The persisted MeteoriteCompany.
    */
  def addCompany(company: MeteoriteCompany): MeteoriteCompany

  /**
    * Get all companies.
    * @return A list of MeteoriteCompany objects.
    */
  def getCompanies: Iterable[MeteoriteCompany]

  /**
    * Update a company.
    * @param company The MeteoriteCompany to update.
    */
  def updateCompany(company: MeteoriteCompany)

  /**
    * Delete a company.
    * @param company The MeteoriteCompany to delete.
    */
  def deleteCompany(company: MeteoriteCompany)

  /**
    * Delete Company by ID.
    * @param id The ID of the company you wish to remove.
    */
  def deleteCompany(id: Long)

  /**
    * Add a user to an existing company.
    * @param user The user object
    * @return The persisted user.
    */
  def addUser(user: MeteoriteUser): MeteoriteUser

  /**
    * Delete a user by username assigned to a company.
    * @param username The username.
    */
  def deleteUser(username: String)

  /**
    * Get a user assigned to a company by username.
    * @param username The username.
    * @return The MeteoriteUser.
    */
  def getUser(username: String): MeteoriteUser
}
