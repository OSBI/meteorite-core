package bi.meteorite.core.security.rest

import org.ops4j.pax.cdi.api.OsgiServiceProvider

/**
  * Created by brunogamacatao on 17/03/16.
  */
@OsgiServiceProvider(classes = Array(classOf[ReportEngine]))
class ReportEngineImpl extends ReportEngine {
  override def getReport(filename: String): String = {
    return "It works"
  }
}
