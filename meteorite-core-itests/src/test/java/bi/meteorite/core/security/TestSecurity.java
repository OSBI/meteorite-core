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
import bi.meteorite.core.api.security.rest.objects.Login;

import org.apache.karaf.features.FeaturesService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.File;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;

/**
 * Created by bugg on 30/09/15.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TestSecurity {

  @Inject
  protected FeaturesService featuresService;

  @Inject
  private ConfigurationAdmin caService;

  @Filter(timeout = 60000)
  @Inject
  private UserAuthentication helloService;

  /*@Inject
  private UserService usermanagement;*/

  @Configuration
  public Option[] config() {


    MavenArtifactUrlReference karafUrl = maven()
        .groupId("bi.meteorite")
        .artifactId("meteorite-engine")
        .version("1.0-SNAPSHOT")
        .type("zip");

    MavenUrlReference karafStandardRepo = maven()
        .groupId("org.apache.karaf.features")
        .artifactId("standard")
        .version(karafVersion())
        .classifier("features")
        .type("xml");
    CoreOptions.systemProperty("org.ops4j.pax.url.mvn.repositories")
               .value("+http://repo1.maven.org/maven2/,http://nexus.qmino"
                      + ".com/content/repositories/miredot");

    MavenUrlReference karafCellarrepo = maven().groupId("org.apache.karaf.cellar")
                                               .artifactId("apache-karaf-cellar")
                                               .version("4.0.0").classifier("features").type("xml");

    return CoreOptions.options(
        KarafDistributionOption.karafDistributionConfiguration()
                               .frameworkUrl(karafUrl)
                               .unpackDirectory(new File("target", "exam"))
                               .useDeployFolder(false),
        KarafDistributionOption.keepRuntimeFolder(),
        KarafDistributionOption.logLevel(LogLevelOption.LogLevel.ERROR),
        /**
         *
         * Uncomment to debug.
         */
        //KarafDistributionOption.debugConfiguration("5005", true),

        configureConsole().ignoreLocalConsole(),


        CoreOptions.mavenBundle("bi.meteorite", "api", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "security-provider", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "security", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.jaxrs", "jackson-jaxrs-json-provider", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.jaxrs", "jackson-jaxrs-base", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.core", "jackson-core", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.core", "jackson-databind", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.core", "jackson-annotations", "2.6.0"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.module", "jackson-module-jaxb-annotations", "2.6.2"),
        CoreOptions.mavenBundle("com.google.guava", "guava", "18.0"),

        editConfigurationFilePut("etc/system.properties", "javax.ws.rs.ext.RuntimeDelegate", "org.apache.cxf.jaxrs"
                                                                                             + ".impl.RuntimeDelegateImpl"),
        editConfigurationFilePut("etc/users.properties", "admin",
            "admin,admin,manager,viewer,Operator, Maintainer, Deployer, Auditor, Administrator, SuperUser"),
        CoreOptions.junitBundles(),
        CoreOptions.cleanCaches()
    );
  }

  public static String karafVersion() {
    ConfigurationManager cm = new ConfigurationManager();
    return cm.getProperty("pax.exam.karaf.version", "4.0.1");
  }

  @Test
  public void testLoginService() throws Exception {
    assertNotNull(caService);
    assertNotNull(helloService);


 //   assertThat(helloService.login(new Login("karaf", "karaf")).getStatus(), is(200));

  }

  @Test
  public void testGetUsername() throws TokenProviderException {
    assertNotNull(caService);
    assertNotNull(helloService);

  //  Response s = helloService.login(new Login("karaf", "karaf"));

    assertThat(s.getStatus(), is(200));

//    Response who = helloService.whoami(s.getCookies().get(TokenProvider.TOKEN_COOKIE_NAME).getValue());

//    assertThat((String)who.getEntity(), containsString("karaf"));

  }

  @Ignore
  @Test
  public void testAddUser() throws MeteoriteSecurityException {
    //usermanagement.addUser("test", "test");
  }

}
