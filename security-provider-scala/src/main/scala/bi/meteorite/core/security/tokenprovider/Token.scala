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
package bi.meteorite.core.security.tokenprovider

import bi.meteorite.core.api.security.tokenprovider.IToken
import java.io.Serializable

import scala.collection.mutable

/**
  * This class represents a generic token. A token is a unique and randomly generated String, associated with a secret
  * (which is also a unique and randomly generated String). A timestamp is assigned to a token to facilitate token
  * expiration. Arbitrary String properties can be assigned to a token.
  */
class Token extends Serializable with IToken {
  private var mtoken: String = null
  private var mtokenSecret: String = null
  private var mtimestamp = 0L
  private var mproperties = collection.mutable.Map[String, String]()

  /**
    * Constructor. Creates a new token
    *
    * @param token       The uniquely random generated String
    * @param tokenSecret The secret, that is supposed to authenticate the owner of the token. The token may be publicly
    *                    available (i.e. send to the client in a http response) but the secret is only known by the
    *                    owner.
    * @param timestamp   The time at which the token was generated
    */
  def this(token: String, tokenSecret: String, timestamp: Long) {
    this()
    mtoken = token
    mtokenSecret = tokenSecret
    mtimestamp = timestamp
  }

  /**
    * Returns the token. The token is publicly known.
    *
    * @return the token.
    */
  def getToken: String = {
    mtoken
  }

  /**
    * Sets the token.
    *
    * @param token the token to set
    */
  def setToken(token: String) {
    mtoken = token
  }

  /**
    * Returns the token secret. The token secret associated with this token is only known by the owner.
    *
    * @return the secret that only the token owner knows
    */
  def getTokenSecret: String = {
    mtokenSecret
  }

  /**
    * Sets the token secret.
    *
    * @param tokenSecret the token secret to set
    */
  def setTokenSecret(tokenSecret: String) {
    mtokenSecret = tokenSecret
  }

  /**
    * Returns the timestamp in milliseconds of the time at which the token was created.
    *
    * @return the timestamp in milliseconds
    */
  def getTimestamp: Long = {
    System.currentTimeMillis
    mtimestamp
  }

  /**
    * Sets the timestamp.
    *
    * @param timestamp the timestamp.
    */
  def setTimestamp(timestamp: Long) {
    mtimestamp = timestamp
  }

  /**
    * Returns the map of arbitrary properties.
    *
    * @return a property map.
    */
  def getProperties: Map[String, String] = {
    mproperties.toMap
  }

  def getProperty(key: String): String = {
    if (mproperties == null) {
      return null
    }
    mproperties.get(key).get
  }

  def setProperties(properties: Map[String, String]) {
    mproperties = collection.mutable.Map(properties.toSeq: _*)
  }

  def setProperty(key: String, value: String) {
    if (mproperties == null) {
      mproperties = collection.mutable.Map[String, String]()
    }
    mproperties.put(key, value)
  }

  /**
    * Verifies if the token is expired comparing its timestamp with the system time. Returns true of the timestamp of
    * this token + the provided validity duration equals or is higher then the current time.
    *
    * @param validityDuration The amount of milliseconds the token is valid. Can be 0. If the validityDuration is smaller
    *                         then 0, this method always returns false.
    * @return an expired flag.
    */
  def isExpired(validityDuration: Long): Boolean = {
    if (validityDuration < 0) {
      return false
    }
    else if (validityDuration == 0) {
      return true
    }
    val expiryDate: Long = mtimestamp + validityDuration
    System.currentTimeMillis >= expiryDate
  }

  override def clone: Token = {
    val clone: Token = new Token(mtoken, mtokenSecret, mtimestamp)
    if (getProperties != null) {
      clone.setProperties(clone.getProperties.toMap)
    }
    clone
  }

  private def clone(map: collection.mutable.Map[String, String]): collection.mutable.Map[String, String] = {
    val newMap = collection.mutable.Map[String, String]()
    for (key <- map.keySet) {
      newMap.put(key, map.get(key).get)
    }
    newMap
  }
}
