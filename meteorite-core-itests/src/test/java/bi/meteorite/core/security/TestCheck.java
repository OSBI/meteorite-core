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

import bi.meteorite.core.api.security.tokenprovider.TokenProvider;

import org.apache.karaf.features.FeaturesService;

import org.junit.Assert;
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

/**
 * Created by bugg on 30/09/15.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TestCheck {

  @Inject
  protected FeaturesService featuresService;


  @Inject
  private TokenProvider helloService;

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
    CoreOptions.systemProperty("org.ops4j.pax.url.mvn.repositories").value("+http://repo1.maven.org/maven2/,http://nexus.qmino"
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

        //configureConsole().ignoreLocalConsole(),
        KarafDistributionOption.features(karafStandardRepo, "scr"),
        KarafDistributionOption
            .features(karafCellarrepo, "cellar-hazelcast", "cellar-shell", "cellar-config", "cellar-bundle",
                "cellar-features"),
        CoreOptions.mavenBundle("bi.meteorite", "api", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "security-provider", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("javax.ws.rs", "javax.ws.rs-api", "2.0.1"),
        CoreOptions.mavenBundle("commons-codec", "commons-codec", "1.9"),
        //mavenBundle("com.hazelcast", "hazelcast", "3.2.3"),
        //mavenBundle("org.apache.karaf.cellar", "org.apache.karaf.cellar.core", "4.0.0"),
        /*editConfigurationFilePut("etc/org.apache.karaf.features.cfg", "featuresBoot",
            "(aries-blueprint, bundle, config, cellar, deployer, diagnostic, feature, instance, jaas, kar, log, "
            + "management, package, service, shell, shell-compat, ssh, system, wrap)"),*/
        KarafDistributionOption
            .editConfigurationFilePut("etc/org.apache.karaf.features.cfg", "featuresBoot", "(aries-blueprint,bundle,"
                                                                                           + "config,wrap)"),
        //editConfigurationFileExtend("etc/config.properties", "org.apache.aries.blueprint.synchronous", "true"),
        CoreOptions.wrappedBundle(CoreOptions.mavenBundle("javax.servlet", "servlet-api", "2.5")),
        CoreOptions.junitBundles(),
        CoreOptions.cleanCaches()
    );
  }

  public static String karafVersion() {
    ConfigurationManager cm = new ConfigurationManager();
    String karafVersion = cm.getProperty("pax.exam.karaf.version", "4.0.1");
    return karafVersion;
  }

  @Test
  public void getHelloService() throws Exception {
    Assert.assertTrue(featuresService.isInstalled(featuresService.getFeature("cellar-hazelcast")));
   // assertNotNull(helloService);
   // assertEquals("Hello Pax!", helloService.getClass());
  }


  /*@ProbeBuilder
  public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {
    System.out.println("TestProbeBuilder gets called");
    probe.setHeader(Constants.DYNAMICIMPORT_PACKAGE, "*");
    probe.setHeader(Constants.EXPORT_PACKAGE, "bi.meteorite.core.security");
    return probe;
  }*/
}
