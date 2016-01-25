package bi.meteorite.core.api.persistence

/**
  * Created by bugg on 22/01/16.
  */

import bi.meteorite.core.api.objects.MeteoriteUser
import bi.meteorite.core.api.objects.MeteoriteRole

/**
  * Interface for persisting users.
  */
trait UserService {
  def getUser(id: String): MeteoriteUser

  def addUser(user: MeteoriteUser): MeteoriteUser

  def getUsers: Iterable[MeteoriteUser]

  def updateUser(user: MeteoriteUser)

  def deleteUser(id: String)

  def addRole(r: MeteoriteRole): MeteoriteRole

  def deleteRole(id: String)

  def getRole(id: String): MeteoriteRole
}
