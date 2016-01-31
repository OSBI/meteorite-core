/*
 * Copyright 2016 OSBI Ltd
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
package bi.meteorite.core.security.authentication

import org.apache.cxf.Bus
import org.apache.cxf.feature.AbstractFeature
import org.apache.cxf.interceptor.InterceptorProvider
import org.apache.cxf.interceptor.security.JAASLoginInterceptor

/**
  * Feature to do JAAS authentication with defaults for karaf integration
  */
object TokenAuthenticationFeature {
  val ID: String = "jaas"
}

class TokenAuthenticationFeature extends AbstractFeature {
  private var contextName: String = "meteorite-realm"
  private var reportFault: Boolean = false

  override def getID: String = TokenAuthenticationFeature.ID

  protected override def initializeProvider(provider: InterceptorProvider, bus: Bus) {
    val jaasLoginInterceptor: JAASLoginInterceptor = new JAASLoginInterceptor
    jaasLoginInterceptor.setRoleClassifierType(JAASLoginInterceptor.ROLE_CLASSIFIER_CLASS_NAME)
    jaasLoginInterceptor.setRoleClassifier("org.apache.karaf.jaas.boot.principal.RolePrincipal")
    jaasLoginInterceptor.setContextName(contextName)
    jaasLoginInterceptor.setReportFault(reportFault)
    provider.getInInterceptors.add(jaasLoginInterceptor)
    super.initializeProvider(provider, bus)
  }

  def setContextName(contextName: String) = this.contextName = contextName

  def setReportFault(reportFault: Boolean) this.reportFault = reportFault
}
