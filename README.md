# oskari-server-extensions

The geoportal for http://arctic-sdi.org/

Builds the on top of the oskariorg/oskari-server project:
- http://oskari.org/
- https://github.com/oskariorg/oskari-server

## To setup Jetty 9

Download the Jetty-Oskari bundle from http://oskari.org/download.
You can delete everything under {JETTY_HOME=oskari-server}/webapps and replace the oskari-map.war with the version build from this repository.

Add/modify  {JETTY_HOME=oskari-server}/resources/oskari-ext.properties with:

    ##################################
    # Flyway config
    ##################################
    
    # initialized the layer srs (also updated by setup.war if used to generate GeoServer config)
    oskari.native.srs=EPSG:3575


## To build the webapp for map

Run 'mvn clean install' in this directory. 
A deployable WAR-file will be compiled to ./webapp-map/target/oskari-map.war
