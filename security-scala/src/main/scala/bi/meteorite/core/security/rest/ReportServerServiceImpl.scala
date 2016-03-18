/*
 * Copyright 2015 OSBI Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bi.meteorite.core.security.rest

import javax.inject.{Inject, Singleton}

import bi.meteorite.core.api.report.rest.ReportServerService
import org.ops4j.pax.cdi.api.{OsgiService, OsgiServiceProvider}


/**
  * Created by brunogamacatao on 17/03/16.
  */

@OsgiServiceProvider(classes = Array(classOf[ReportServerService]))
@Singleton
class ReportServerServiceImpl extends ReportServerService {
  @Inject
  @volatile
  @OsgiService
  private var reportEngine : ReportEngine = _

  override def getReport(filename: String): String = {
    return reportEngine.getReport(filename)
  }

  def getReportEngine() : ReportEngine = reportEngine
  def setReportEngine(reportEngine : ReportEngine) = this.reportEngine = reportEngine
}
