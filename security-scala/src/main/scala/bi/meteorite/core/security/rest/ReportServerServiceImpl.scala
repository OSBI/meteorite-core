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

import javax.inject.Singleton
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

import bi.meteorite.core.api.report.rest.ReportServerService
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException
import org.ops4j.pax.cdi.api.OsgiServiceProvider


/**
  * Created by brunogamacatao on 17/03/16.
  */

@OsgiServiceProvider(classes = Array(classOf[ReportServerService]))
@Singleton
class ReportServerServiceImpl extends ReportServerService {

  @throws(classOf[MeteoriteSecurityException])
  override def getReport(@PathParam("filename") filename: String): Response = {
    Response.ok("Hello World").build()
  }

}
