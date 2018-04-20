# veraPDF-validation-rest
Simple REST interface to VeraPDF validation using the Greenfield parser.

We (http://www.kb.dk) need a simple dockerized VeraPDF validator.   
This project is a WAR with a single REST endpoint for that purpose.

Used

    mvn archetype:generate -Dfilter=com.airhacks:javaee7-essentials-archetype
    
to generate basic web application project.

/tra 2018-04-19

Java EE 7 project works out of the box with Glassfish 4 and Wildfly 8.

TomEE 7.0.4 dislikes the url encoded Bitfinder URLs which we need.  See https://stackoverflow.com/a/36221241/53897 for fixes.

Servlet31 project works out of the box with Jetty 9.4.9.  Tomcat 8.5 needs the VM option

    -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true
    
(in a launch configuration or catalina.sh)

(Source https://communities.bmc.com/docs/DOC-68851)

/tra 2018-04-20
