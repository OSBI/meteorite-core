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

package bi.meteorite.core.security.tokenprovider;

import bi.meteorite.core.api.security.tokenprovider.TokenProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

import java.io.File;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

/**
 * Created by bugg on 30/09/15.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TestCheck {

  @Inject
  private TokenProvider helloService;

  @Configuration
  public Option[] config() {

    MavenArtifactUrlReference karafUrl = maven()
        .groupId("org.apache.karaf")
        .artifactId("apache-karaf")
        .version(karafVersion())
        .type("zip");

    MavenUrlReference karafStandardRepo = maven()
        .groupId("org.apache.karaf.features")
        .artifactId("standard")
        .version(karafVersion())
        .classifier("features")
        .type("xml");
    systemProperty("org.ops4j.pax.url.mvn.repositories").value("+http://repo1.maven.org/maven2/,http://nexus.qmino"
                                                               + ".com/content/repositories/miredot");

    MavenUrlReference karafCellarrepo = maven().groupId("org.apache.karaf.cellar")
                                               .artifactId("apache-karaf-cellar")
                                               .version("4.0.0").classifier("features").type("xml");

    return options(
        karafDistributionConfiguration()
            .frameworkUrl(karafUrl)
            .unpackDirectory(new File("target", "exam"))
            .useDeployFolder(false),
        keepRuntimeFolder(),
        configureConsole().ignoreLocalConsole(),
        features(karafStandardRepo, "scr"),
        features(karafCellarrepo, "cellar"),
        mavenBundle("bi.meteorite", "api", "1.0-SNAPSHOT"),
        mavenBundle("bi.meteorite", "security-provider", "1.0-SNAPSHOT"),
        mavenBundle("javax.ws.rs", "javax.ws.rs-api", "2.0.1"),
        mavenBundle("commons-codec", "commons-codec", "1.9"),
        mavenBundle("com.hazelcast", "hazelcast", "3.2.3"),
        editConfigurationFileExtend("etc/config.properties", "org.apache.aries.blueprint.synchronous", "true"),
        editConfigurationFilePut("etc/org.apache.karaf.features.cfg", "featuresBoot",
            "(aries-blueprint, bundle, config, deployer, diagnostic, feature, instance, jaas, kar, log, management, package, service, shell, shell-compat, ssh, system, wrap)"),
        wrappedBundle(mavenBundle("javax.servlet", "servlet-api", "2.5")),
        //bundle("http://www.example.com/repository/foo-1.2.3.jar"),
        junitBundles(),
        cleanCaches()
    );
  }

  public static String karafVersion() {
    ConfigurationManager cm = new ConfigurationManager();
    String karafVersion = cm.getProperty("pax.exam.karaf.version", "4.0.1");
    return karafVersion;
  }

  @Test
  public void getHelloService() {
    assertNotNull(helloService);
    assertEquals("Hello Pax!", helloService.getClass());
  }
}
