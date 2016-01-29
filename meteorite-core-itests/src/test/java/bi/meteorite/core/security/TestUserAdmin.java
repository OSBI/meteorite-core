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

import bi.meteorite.core.api.objects.MeteoriteUser;
import bi.meteorite.core.api.persistence.UserService;
import bi.meteorite.core.api.security.rest.UserAuthentication;
import bi.meteorite.core.security.rest.objects.UserObj;
import bi.meteorite.objects.UserImpl;
import bi.meteorite.util.ITestBootstrap;

import org.apache.karaf.features.FeaturesService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.jdbc.DataSourceFactory;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by bugg on 27/01/16.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TestUserAdmin extends ITestBootstrap {

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

  @Test
  public void testAddUser() {
    MeteoriteUser u = new UserObj();
    u.setUsername("testuser");
    u.setEmail("testemail@test.com");
    u.setPassword("testpassword");


    Response response = post("http://localhost:8181/cxf/rest/core/user", "admin", "admin", MediaType
        .APPLICATION_JSON, u, MeteoriteUser.class);


    assertNotNull(response.readEntity(String.class));

    System.out.println("here");

  }

  @Ignore
  @Test
  public void testDeleteUser() {

  }

  @Ignore
  @Test
  public void testAddRoles() {

  }

  @Ignore
  @Test
  public void testDeleteRoles() {

  }
}
