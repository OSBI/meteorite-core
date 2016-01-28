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


package bi.meteorite.util;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;

/**
 * Bootstrap for the integration tests.
 */
public class ITestBootstrap {

  private final Client client = ClientBuilder.newClient();//.register(JacksonJsonProvider.class);
  static final Long SERVICE_TIMEOUT = 30000L;
  @Inject
  protected BundleContext bundleContext;

  ExecutorService executor = Executors.newCachedThreadPool();

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
        KarafDistributionOption.logLevel(LogLevelOption.LogLevel.WARN),
        /**
         *
         * Uncomment to debug.
         */
        KarafDistributionOption.debugConfiguration("5005", false),

        configureConsole().ignoreLocalConsole(),


        CoreOptions.mavenBundle("bi.meteorite", "meteorite-core-api", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "meteorite-core-security-provider-scala", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "meteorite-core-security-scala", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "meteorite-core-persistence", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("bi.meteorite", "meteorite-core-model-scala", "1.0-SNAPSHOT"),
        CoreOptions.mavenBundle("org.apache.cxf", "cxf-rt-rs-security-cors", "3.1.2"),
        CoreOptions.mavenBundle("org.scala-lang", "scala-library", "2.11.7"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.jaxrs", "jackson-jaxrs-json-provider", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.jaxrs", "jackson-jaxrs-base", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.core", "jackson-core", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.core", "jackson-databind", "2.6.2"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.core", "jackson-annotations", "2.6.0"),
        CoreOptions.mavenBundle("com.fasterxml.jackson.module", "jackson-module-jaxb-annotations", "2.6.2"),
        CoreOptions.mavenBundle("com.google.guava", "guava", "18.0"),
        CoreOptions.mavenBundle("javax.transaction", "javax.transaction-api", "1.2"),
        CoreOptions.mavenBundle("org.hibernate.javax.persistence", "hibernate-jpa-2.1-api", "1.0.0.Final"),
        CoreOptions.mavenBundle("javax.interceptor", "javax.interceptor-api", "1.2"),

        editConfigurationFilePut("etc/users.properties", "admin",
            "admin,admin,manager,viewer,Operator, Maintainer, Deployer, Auditor, Administrator, SuperUser"),
        editConfigurationFilePut("etc/users.properties", "nonadmin",
            "nonadmin,ROLE_USER"),

        CoreOptions.junitBundles(),
        CoreOptions.cleanCaches()
    );
  }

  private static String karafVersion() {
    ConfigurationManager cm = new ConfigurationManager();
    return cm.getProperty("pax.exam.karaf.version", "4.0.1");
  }


  private String getBasicAuthentication(String user, String password) {
    String token = user + ":" + password;
    try {
      return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalStateException("Cannot encode with UTF-8", ex);
    }
  }

  protected Response get(String url, String type) {
    WebTarget target = client.target(url);
    return target.request(type).get();
  }

  protected Response get(String url, String user, String pass, String type) {
    WebTarget target = client.target(url);
    return target.request(type).header("Authorization", getBasicAuthentication(user, pass))
                 .get();
  }

  protected Response get(String url, String token, String type) {
    WebTarget target = client.target(url);
    return target.request(type).cookie("saiku_token", token).get();
  }

  protected Object post(String url, String user, String pass, String type, Object data, Class c) {


    String url2 = "https://selfsolve.apple.com/wcResults.do";
    URL obj = null;
    try {
      obj = new URL(url);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) obj.openConnection();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //add reuqest header
    try {
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("Authorization", getBasicAuthentication(user, pass));
    } catch (ProtocolException e) {
      e.printStackTrace();
    }
    //con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = null;
    try {
      wr = new DataOutputStream(con.getOutputStream());
      wr.writeBytes((String)"[\"bi.meteorite.objects.UserImpl\",{\"id\":0,\"username\":\"test\",\"password\":null,\"orgId\":0,\"email\":\"test@test.com\",\"roles\":[\"java.util.ArrayList\",[]]}]");
      wr.flush();
      wr.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    int responseCode = 0;
    try {
      responseCode = con.getResponseCode();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + ((String)data));
    System.out.println("Response Code : " + responseCode);



    /*JacksonJaxbJsonProvider prov = new JacksonJaxbJsonProvider();
    ObjectMapper obj = new ObjectMapper();
    obj.enableDefaultTyping();
    prov.setMapper(obj);
    List<Object> providers = new ArrayList<Object>();
    providers.add( prov );
    WebClient wc = WebClient.create(url, providers);

    Object resp = wc.header("Authorization", getBasicAuthentication(user, pass)).header("Accept",
        "application/json").type(MediaType.APPLICATION_JSON).post(data, c);



    WebTarget target = client.target(url);
    return target.request(type).header("Authorization", getBasicAuthentication(user, pass)).header("Accept", "application/json")
                 .post(Entity.entity(data, MediaType.APPLICATION_JSON),
                     c);*/
    return null;
  }


  protected String executeCommand(final String command, final Long timeout, final Boolean silent) {
    String response;
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final PrintStream printStream = new PrintStream(byteArrayOutputStream);
    final CommandProcessor commandProcessor = getOsgiService(CommandProcessor.class);
    final CommandSession commandSession = commandProcessor.createSession(System.in, printStream, System.err);
    FutureTask<String> commandFuture = new FutureTask<String>(new Callable<String>() {
      public String call() {
        try {
          if (!silent) {
            System.err.println(command);
          }
          commandSession.execute(command);
        } catch (Exception e) {
          e.printStackTrace(System.err);
        }
        printStream.flush();
        return byteArrayOutputStream.toString();
      }
    });

    try {
      executor.submit(commandFuture);
      response = commandFuture.get(timeout, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      e.printStackTrace(System.err);
      response = "SHELL COMMAND TIMED OUT: ";
    }

    return response;
  }

  protected <T> T getOsgiService(Class<T> type, long timeout) {
    return getOsgiService(type, null, timeout);
  }

  protected <T> T getOsgiService(Class<T> type) {
    return getOsgiService(type, null, SERVICE_TIMEOUT);
  }

  protected <T> T getOsgiService(Class<T> type, String filter, long timeout) {
    ServiceTracker tracker = null;
    try {
      String flt;
      if (filter != null) {
        if (filter.startsWith("(")) {
          flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")" + filter + ")";
        } else {
          flt = "(&(" + Constants.OBJECTCLASS + "=" + type.getName() + ")(" + filter + "))";
        }
      } else {
        flt = "(" + Constants.OBJECTCLASS + "=" + type.getName() + ")";
      }
      Filter osgiFilter = FrameworkUtil.createFilter(flt);
      tracker = new ServiceTracker(bundleContext, osgiFilter, null);
      tracker.open(true);
      // Note that the tracker is not closed to keep the reference
      // This is buggy, as the service reference may change i think
      Object svc = type.cast(tracker.waitForService(timeout));
      if (svc == null) {
        Dictionary dic = bundleContext.getBundle().getHeaders();
        //System.err.println("Test bundle headers: " + TestUtility.explode(dic));

        //for (ServiceReference ref : TestUtility.asCollection(bundleContext.getAllServiceReferences(null, null))) {
        //System.err.println("ServiceReference: " + ref);
        //}

        //for (ServiceReference ref : TestUtility.asCollection(bundleContext.getAllServiceReferences(null, flt))) {
//          System.err.println("Filtered ServiceReference: " + ref);
        //      }

        throw new RuntimeException("Gave up waiting for service " + flt);
      }
      return type.cast(svc);
    } catch (InvalidSyntaxException e) {
      throw new IllegalArgumentException("Invalid filter", e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
