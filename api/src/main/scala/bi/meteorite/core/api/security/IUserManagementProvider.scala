package bi.meteorite.core.api.security

import javax.xml.bind.annotation.{XmlRootElement, XmlType}

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException
import org.apache.karaf.jaas.config.JaasRealm
import org.apache.karaf.jaas.modules.BackingEngineService

/**
  * User Management Provider.
  * API for the main user managaement interface for Meteorite Core.
  */
trait IUserManagementProvider {
  /**
    * Add a new user to the platform.
    *
    * @param u username
    * @param p password
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def addUser(u: MeteoriteUser)

  /**
    * Delete a user from the platform.
    *
    * @param u username
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: String)

  /**
    * Get a list of users on the platform.
    *
    * @return a list of usernames.
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def getUsers: List[String]

  /**
    * Get a list of roles applied to a user.
    *
    * @param u username
    * @return a list of roles.
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def getRoles(u: String): List[String]

  /**
    * Add a role to a user.
    *
    * @param u username
    * @param r role name
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def addRole(u: String, r: String)

  /**
    * Remove role from a user
    *
    * @param u username
    * @param r role
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def removeRole(u: String, r: String)

  /**
    * Update a user on the platform.
    *
    * @param u user object
    * @return a updated user object
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def updateUser(u: MeteoriteUser): MeteoriteUser

  /**
    * Discover is the user is an administrator or not.
    *
    * @param u username
    * @return true if the user is admin.
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def isAdmin(u: String): Boolean

  /**
    * Get a list of roles that define an administrator.
    *
    * @return a list of administrative roles
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def getAdminRoles: List[String]

  /**
    * Get a single user.
    *
    * @return a Meteorite User.
    * @throws MeteoriteSecurityException
    */
  @throws(classOf[MeteoriteSecurityException])
  def getUser(id: Int): MeteoriteUser

  /**
    * Set the backing service engine that drives the security.
    *
    * @param backingEngineService the backing engine service.
    */
  def setBackingEngineService(backingEngineService: BackingEngineService)

  /**
    * Set the JaaS realm.
    *
    * @param realm the JAAS Realm
    */
  def setRealm(realm: JaasRealm)
}

