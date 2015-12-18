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

package bi.meteorite.core.security;

import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;
import bi.meteorite.core.api.security.exceptions.TokenProviderException;
import bi.meteorite.core.api.security.rest.UserAuthentication;
import bi.meteorite.util.ITestBootstrap;

import org.apache.karaf.features.FeaturesService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.service.cm.ConfigurationAdmin;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the authentication mechanism for Meteorite Core.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TestSecurity extends ITestBootstrap {

  @Inject
  protected FeaturesService featuresService;

  @Inject
  private ConfigurationAdmin caService;

  @Inject
  private UserAuthentication authenticationService;

  /**
   * Test that a user can login
   *
   * @throws Exception
   */
  @Test
  public void testLoginService() throws Exception {
    assertNotNull(caService);
    assertNotNull(authenticationService);

    Response response = get("http://localhost:8181/cxf/rest/core/user", MediaType.APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SERVER_ERROR));
    assertThat(response.readEntity(String.class), containsString("Username can not be null"));

    response = get("http://localhost:8181/cxf/rest/core/user", "karaf", "karaf", MediaType.APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

  }

  /**
   * Test that an admin user can login and get a response from a admin only, and non admin endpoint.
   *
   * @throws TokenProviderException
   */
  @Test
  public void testRoleRestrictedEndpoints() throws TokenProviderException {
    assertNotNull(caService);
    assertNotNull(authenticationService);

    Response response = get("http://localhost:8181/cxf/rest/core/user/whoami", "karaf", "karaf", MediaType
        .APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

    assertThat(response.readEntity(String.class), containsString("test3"));

    response = get("http://localhost:8181/cxf/rest/core/user", "karaf", "karaf", MediaType.APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

  }

  /**
   * Test that a non admin user can login but not hit an admin only endpoint.
   *
   * @throws MeteoriteSecurityException
   */
  @Test
  public void testNonAdminLockDown() throws MeteoriteSecurityException {
    assertNotNull(caService);
    assertNotNull(authenticationService);

    Response response = get("http://localhost:8181/cxf/rest/core/user/whoami", "nonadmin", "nonadmin", MediaType
        .APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

    response = get("http://localhost:8181/cxf/rest/core/user", "nonadmin", "nonadmin", MediaType
        .APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.CLIENT_ERROR));
  }


  /**
   * Test that an admin and a non admin user login but the non admin user cannot imitate the admin user.
   *
   * @throws MeteoriteSecurityException
   */
  @Test
  public void testCheckDualLogins() throws MeteoriteSecurityException {
    Thread thread1 = new Thread() {
      public void run() {
        try {
          testNonAdminLockDown();
        } catch (MeteoriteSecurityException e) {
          e.printStackTrace();
        }
      }
    };

    Thread thread2 = new Thread() {
      public void run() {
        try {
          testRoleRestrictedEndpoints();
        } catch (TokenProviderException e) {
          e.printStackTrace();
        }
      }
    };

    thread1.start();
    thread2.start();
  }
}
