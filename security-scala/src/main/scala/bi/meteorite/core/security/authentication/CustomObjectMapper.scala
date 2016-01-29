package bi.meteorite.core.security.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
  * Created by bugg on 27/01/16.
  */
class CustomObjectMapper extends ObjectMapper{

  //this.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
  this.registerModule(DefaultScalaModule)
  this.registerModule(new Hibernate4Module())
}
