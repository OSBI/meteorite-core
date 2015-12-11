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

import bi.meteorite.core.api.security.AdminLoginService;
import bi.meteorite.core.api.security.IUserManagementProvider;
import bi.meteorite.core.api.security.exceptions.MeteoriteSecurityException;

import org.apache.karaf.features.FeaturesService;

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

import java.io.File;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;

/**
 * Created by bugg on 30/09/15.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TestCheck {

  @Inject
  protected FeaturesService featuresService;


  @Inject
  private AdminLoginService helloService;

  @Inject
  private IUserManagementProvider usermanagement;

  @Configuration
  public Option[] config() {

    MavenArtifactUrlReference karafUrl = CoreOptions.maven()
                                                    .groupId("org.apache.karaf")
                                                    .artifactId("apache-karaf")
                                                    .version(karafVersion())
                                                    .type("zip");

    MavenUrlReference karafStandardRepo = CoreOptions.maven()
                                                     .groupId("org.apache.karaf.features")
                                                     .artifactId("standard")
                                                     .version(karafVersion())
                                                     .classifier("features")
                                                     .type("xml");
    CoreOptions.systemProperty("org.ops4j.pax.url.mvn.repositories")
               .value("+http://repo1.maven.org/maven2/,http://nexus.qmino"
                      + ".com/content/repositories/miredot");

    MavenUrlReference karafCellarrepo = CoreOptions.maven().groupId("org.apache.karaf.cellar")
                                                   .artifactId("apache-karaf-cellar")
                                                   .version("4.0.0").classifier("features").type("xml");

    return CoreOptions.options(
        KarafDistributionOption.karafDistributionConfiguration()
                               .frameworkUrl(karafUrl)
                               .unpackDirectory(new File("target", "exam"))
                               .useDeployFolder(false),
        KarafDistributionOption.keepRuntimeFolder(),
        KarafDistributionOption.logLevel(LogLevelOption.LogLevel.WARN),
        /**
         *
         * Uncomment to debug.
         */
        //KarafDistributionOption.debugConfiguration("5005", true),
        editConfigurationFilePut("etc/org.apache.karaf.features.cfg", "featuresBoot", "(aries-blueprint,bundle,"
                                                                                      + "config,wrap, "
                                                                                      + "cellar-hazelcast,jaas)"),

        configureConsole().ignoreLocalConsole(),
        KarafDistributionOption.features(karafStandardRepo, "scr"),
        KarafDistributionOption
            .features(karafCellarrepo, "cellar-hazelcast"),

        CoreOptions.mavenBundle("bi.meteorite", "api", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "security-provider", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("javax.ws.rs", "javax.ws.rs-api", "2.0.1"),
        CoreOptions.mavenBundle("commons-codec", "commons-codec", "1.9"),
        wrappedBundle(CoreOptions.mavenBundle("com.atlassian.clover", "clover", "4.0.6")),
        CoreOptions.mavenBundle("com.google.guava", "guava", "18.0"),
        /*editConfigurationFilePut("etc/org.apache.karaf.features.cfg", "featuresBoot",
            "(aries-blueprint, bundle, config, cellar, deployer, diagnostic, feature, instance, jaas, kar, log, "
            + "management, package, service, shell, shell-compat, ssh, system, wrap)"),*/
        wrappedBundle(CoreOptions.mavenBundle("javax.servlet", "servlet-api", "2.5")),
        wrappedBundle(CoreOptions.mavenBundle("com.atlassian.clover", "clover", "4.0.2")),
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

    assertNotNull(helloService);


    assertThat(helloService.login("karaf", "karaf"), is(true));

  }

  @Test
  public void testGetUsername() {
    assertNotNull(helloService);

    assertThat(helloService.login("karaf", "karaf"), is(true));

    assertThat(helloService.getUsername(), equalTo("karaf"));

  }

  @Test
  public void testAddUser() throws MeteoriteSecurityException {
    usermanagement.addUser("test", "test");
  }

}
