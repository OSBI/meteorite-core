package bi.meteorite.core.security.rest

/**
  * Created by brunogamacatao on 17/03/16.
  */
trait ReportEngine {
  def getReport(filename : String) : String
}
