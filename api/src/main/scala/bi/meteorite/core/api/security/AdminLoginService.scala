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

package bi.meteorite.core.api.security

import javax.security.auth.login.LoginContext

/**
  * Admin Api Interface for user management.
  */
trait AdminLoginService {
  /**
    * Method that performs user authentication in a multi tenant environment.
    * @param company The company where user is registered.
    * @param username The user name (it should be unique for each company).
    * @param password The password that matches to the company + username pair.
    * @return true if user is authorized, false otherwise.
    */
  def login(company: String, username: String, password: String): Boolean

  /**
    * Method that unregisters user session (perform logout).
    * @param company The company where user is registered.
    * @param username The user name (it should be unique for each company).
    * @return true if user is logged out successfully, false otherwise.
    */
  def logout(company: String, username: String): Boolean

  def getCompany: String

  def getUsername: String

  def getRoles: List[String]

  /**
    * Set the JaaS realm.
    *
    * @param realm The JAAS Realm
    */
  def setRealm(realm: String)

  def setLoginContext(loginContext: LoginContext)
}

