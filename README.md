# meteorite-core
Core for Saiku and other stuff, provides interfaces for JCR and Security


To build

Check out and run

mvn clean install

to skip tests run

mvn clean install -DskipTests


To deploy:

extract the meteorite-engine zip file from karaf/target

run ./bin/karaf

or ./bin/karaf debug

once started run

feature:install meteorite-core-features


To update modules over the wire:


To run a specific integration test:


