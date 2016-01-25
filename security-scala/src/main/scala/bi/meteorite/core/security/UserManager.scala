package bi.meteorite.core.security

import java.util

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.security.IUserManagement

class UserManager extends IUserManagement {

  override def addUser(u: MeteoriteUser): MeteoriteUser = null

  override def deleteUser(u: MeteoriteUser): Boolean = false

  override def setUser(u: MeteoriteUser): MeteoriteUser = null

  override def getUser(id: Int): MeteoriteUser = null

  override def getRoles(u: MeteoriteUser): Array[String] = {
    Array.ofDim[String](0)
  }

  override def addRole(u: MeteoriteUser) {
  }

  override def removeRole(u: MeteoriteUser) {
  }

  override def removeUser(username: String) {
  }

  override def updateUser(u: MeteoriteUser): MeteoriteUser = null

  override def isAdmin: Boolean = false

  override def getAdminRoles: List[String] = null
}
