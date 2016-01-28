package bi.meteorite.core.api.objects

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
  * A user object for Meteorite core.
  */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
trait MeteoriteUser {
  /**
    * Get the username.
    *
    * @return the username
    */
  def getUsername: String

  /**
    * Set the username.
    *
    * @param username the username.
    */
  def setUsername(username: String)

  /**
    * Get the user password.
    *
    * @return the password.
    */
  def getPassword: String

  /**
    * Set the user password.
    *
    * @param password the password.
    */
  def setPassword(password: String)

  /**
    * Get an array of roles.
    *
    * @return an array of roles.
    */
  def getRoles: java.util.List[MeteoriteRole]

  /**
    * Set the roles.
    *
    * @param roles an array of roles.
    */
  def setRoles(roles: java.util.List[MeteoriteRole])

  /**
    * Get the user email address.
    *
    * @return the email address.
    */
  def getEmail: String

  /**
    * Set the email address.
    *
    * @param email the email address.
    */
  def setEmail(email: String)

  /**
    * Get the users unique id
    *
    * @return the id
    */
  def getId: Long

  /**
    * Set the users id.
    *
    * @param id the id.
    */
  def setId(id: Long)

  /**
    * Get the organization id.
    *
    * @return the org id.
    */
  def getOrgId: Int

  /**
    * Set the organization id.
    *
    * @param orgId the organization id.
    */
  def setOrgId(orgId: Int)
}


