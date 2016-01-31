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

package bi.meteorite.core.api.security.tokenprovider


/**
  * An authentication token.
  */
trait IToken {
  def getToken: String

  def setToken(token: String)

  def getTokenSecret: String

  def setTokenSecret(tokenSecret: String)

  def getTimestamp: Long

  def setTimestamp(timestamp: Long)

  def getProperties: Map[String, String]

  def getProperty(key: String): String

  def setProperties(properties: Map[String, String])

  def setProperty(key: String, value: String)

  def isExpired(validityDuration: Long): Boolean
}

