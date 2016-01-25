package bi.meteorite.core.api.objects

/**
  * Created by bugg on 29/12/15.
  */
trait MeteoriteRole {
  def getId: Int

  def setId(id: Int)

  def getUserId: MeteoriteUser

  def setUserId(id: MeteoriteUser)

  def getRole: String

  def setRole(role: String)
}

