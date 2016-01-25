package bi.meteorite.core.api.security

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException

/**
  * User Management Interface.
  */
trait IUserManagement {
  @throws(classOf[MeteoriteSecurityException])
  def addUser(u: MeteoriteUser): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def deleteUser(u: MeteoriteUser): Boolean

  @throws(classOf[MeteoriteSecurityException])
  def setUser(u: MeteoriteUser): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def getUser(id: Int): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def getRoles(u: MeteoriteUser): Array[String]

  @throws(classOf[MeteoriteSecurityException])
  def addRole(u: MeteoriteUser)

  @throws(classOf[MeteoriteSecurityException])
  def removeRole(u: MeteoriteUser)

  @throws(classOf[MeteoriteSecurityException])
  def removeUser(username: String)

  @throws(classOf[MeteoriteSecurityException])
  def updateUser(u: MeteoriteUser): MeteoriteUser

  @throws(classOf[MeteoriteSecurityException])
  def isAdmin: Boolean

  @throws(classOf[MeteoriteSecurityException])
  def getAdminRoles: List[String]
}

