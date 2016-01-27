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

import bi.meteorite.core.api.persistence.UserService;
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
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.util.tracker.ServiceTracker;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;
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

  @Filter(timeout = 30000L)
  @Inject
  private UserAuthentication authenticationService;

  @Filter(timeout = 30000L)
  @Inject
  private UserService userService;

  @Inject
  @Filter(value = "(osgi.jdbc.driver.class=org.h2.Driver)", timeout = 30000L)
  private DataSourceFactory dsf;

  @Inject
  BundleContext context;

  /**
   * Test that a user can login
   *
   * @throws Exception
   */
  @Test
  public void testLoginService() throws Exception {
    assertNotNull(caService);
    assertNotNull(authenticationService);
    assertNotNull(dsf);
    ServiceTracker<DataSource, Object> tracker = new ServiceTracker<DataSource, Object>(
        context, DataSource.class, null);
    tracker.open();
    DataSource dataSource = (DataSource) tracker.waitForService(10000);

    Response response = get("http://localhost:8181/cxf/rest/core/user", MediaType.APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.CLIENT_ERROR));
    //assertThat(response.readEntity(String.class), containsString("Username can not be null"));

    response = get("http://localhost:8181/cxf/rest/core/user", "admin", "admin", MediaType.APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

  }

  /**
   * Test that an admin user can login and get a response from a admin only, and non admin endpoint.
   *
   * @throws TokenProviderException
   */
  @Test
  public void testRoleRestrictedEndpoints() throws TokenProviderException, SQLException, InterruptedException {
    assertNotNull(caService);
    assertNotNull(authenticationService);
    assertNotNull(dsf);
    ServiceTracker<DataSource, Object> tracker = new ServiceTracker<DataSource, Object>(
        context, DataSource.class, null);
    tracker.open();
    DataSource dataSource = (DataSource) tracker.waitForService(10000);
    Response response = get("http://localhost:8181/cxf/rest/core/user/whoami", "admin", "admin", MediaType
        .APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

    assertThat(response.readEntity(String.class), containsString("test3"));

    response = get("http://localhost:8181/cxf/rest/core/user", "admin", "admin", MediaType.APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

  }

  /**
   * Test that a non admin user can login but not hit an admin only endpoint.
   *
   * @throws MeteoriteSecurityException
   */
  @Test
  public void testNonAdminLockDown() throws MeteoriteSecurityException, SQLException, InterruptedException {
    assertNotNull(caService);
    assertNotNull(authenticationService);
    assertNotNull(dsf);
    ServiceTracker<DataSource, Object> tracker = new ServiceTracker<DataSource, Object>(
        context, DataSource.class, null);
    tracker.open();
    DataSource dataSource = (DataSource) tracker.waitForService(10000);
    Response response = get("http://localhost:8181/cxf/rest/core/user/whoami", "smith", "smith", MediaType
        .APPLICATION_JSON);

    assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));

    response = get("http://localhost:8181/cxf/rest/core/user", "smith", "smith", MediaType
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
        } catch (SQLException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
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
        } catch (SQLException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    thread1.start();
    thread2.start();
  }
}
