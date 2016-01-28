# meteorite-core
Core for Saiku and other stuff, provides interfaces for JCR and Security

[a API Examples](API_Examples.md)

## To build

Check out and run

    mvn clean install

to skip tests run

    mvn clean install -DskipTests


## To deploy:

extract the meteorite-engine zip file from karaf/target and run

    ./bin/karaf

or 

    ./bin/karaf debug

once started run

    feature:install meteorite-core-features


## To update modules over the wire:

    bundle:update id
    
For example:

    bundle:update 294
    
Or 

     bundle:update bi.meteorite.core-api
     bundle:update bi.meteorite.core-model-scala
     bundle:update bi.meteorite.core-persistence
     bundle:update bi.meteorite.core-security-provider-scala
     bundle:update bi.meteorite.core-security-scala
     bundle:update bi.meteorite.ui-shared
     bundle:update bi.meteorite.ui
     
## To run a specific integration test:

To run a specific test class run 

    mvn clean install -Dtest=TestSecurity
    
To run a specific test class method run

    mvn clean install -Dtest=TestSecurity#testNonAdminLockDown

