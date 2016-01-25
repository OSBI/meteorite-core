package bi.meteorite.core.api.security.rest.objects

import javax.xml.bind.annotation.XmlRootElement

/**
  * Login Object.
  */
@XmlRootElement class Login {
  private var username: String = null
  private var password: String = null

  def this(username: String, password: String) {
    this()
    this.username = username
    this.password = password
  }

  def getUsername = username

  def setUsername(username: String) = this.username = username

  def getPassword = password

  def setPassword(password: String) = this.password = password

}
