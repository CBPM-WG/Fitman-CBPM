## Prerequisites

  - Java JDK 6 (tested with version 1.6.0_32) – Note: Current version does not work with Open JDK.
  - Maven 3.X (tested with version 3.0.4).
  - Tomcat 6.X or above (tested with version 6.0.35, current version does not work in Tomcat 7.X).
  - Postgresql 8.3.X or above (tested with version 8.3).

## Installation
### Configuration

Configure the following modules. Change Tomcat ``<host>:<port>`` in following configuration files:

- Compel module:
    - $COMPEL/Compel/src/main/resources/compel.properties: modify Tomcat ``<host>:<port>`` for Oryx URL, ontologies and DTC endpoint
- KBClient module:
    - $COMPEL/KBClient/src/main/resources/sesame.properties: modify Tomcat ``<host>:<port>`` for sesame.server property
    - $COMPEL/KBClient/src/test/resources/ServiceDescriptions directory: modify Tomcat ``<host>:<port>`` in each test service description in (*.posm files) rdfs:isDefinedBy property pointing at the service wsdl url.
- activiti-webapp-explorer2:
    - $COMPEL/activiti/activiti-webapp-explorer2/src/main/resources/yourbpm.properties: modify Tomcat ``<host>:<port>`` for compel.root and bpmnservice.endpoint properties.

In the following we refer to the server and port where COMPEL is deployed as $COMPEL_SERVER and $COMPEL_PORT

Later configuration can be modified by changing the corresponding property files in $TOMCAT_HOME/webapps/ directory for each deployed war modules that requires to be configured: compel-<version>/ and KBClient.jar/WEB_INF/classes/sesame.properties for each module that use this library.

1.	Avoid using localhost as host. Default port is 8080 (Tomcat default port)
2.	Not all ontologies are hosted locally in Tomcat.

### Compilation

Compile modules:
```sh
	$COMPEL> mvn –Dmaven.test.skip=true clean install
```

War files are created for some modules in their target directories.

### Deployment

**Activiti Database setup**
- start PostgreSQL as root:
```sh
    [LINUX]: > sudo /etc/init.d/postgresql start.
```
```sh
    [Windows]: start Postgresql as Windows service or using Postgresql script provided by the installation
```

- Create activiti user:
```sh
createuser -U postgres -e -P -E activiti
```
When prompted:

  1. Enter password for new role: activiti
  2. Enter it again: activiti
  3. Shall the new role be a superuser? (y/n) n
  4. Shall the new role be allowed to create databases? (y/n) y
  5. Shall the new role be allowed to create more new roles? (y/n) y

```
Output: 
CREATE ROLE activiti ENCRYPTED PASSWORD 'md54fae7683b3e8809f364b0026c885af8c' NOSUPERUSER CREATEDB CREATEROLE INHERIT LOGIN;
```
- Create activiti database:
```
createdb -U postgres -O activiti -E utf8 -e activiti
```
```
Output: 
CREATE DATABASE activiti OWNER activiti ENCODING 'utf8';CREATE DATABASE
```
- Populate Activiti database:

    It is done automatically when you start up the Activiti context.

NOTE: If you set up a different activiti user password (default: activiti) or the postgresql service url is different from the standard one, configure:
```
$COMPEL/resources/myconfiguration.properties
````
And recompile activiti-webapp-explorer2 module.
Deploy the following required YourBPM modules (war files) in TOMCAT_HOME/webapps directory:
- $COMPEL/activiti/activiti-webapp-explorer2/target/ActivitiCompelExplorer2.war
- $COMPEL/Compel/target/compel-1.0.war
- $COMPEL/DTC/DesignTimeComposer/target/DesignTimeComposer.war
- $COMPEL/BPMN20/BPMN20Module/target/BPMN20Module.war
- $COMPEL/third party dependencies/Knowledge Base/Software/Sesame/openrdf-sesame.war
- $COMPEL/third party dependencies/Knowledge Base/Software/Sesame/openrdf-workbench.war

Deploy optional test services (war files) in $TOMCAT_HOME/webapps directory:

- $COMPEL/MockServices/AirCongo/target/AirCongo.war
- $COMPEL/MockServices/BrusselAirlines/target/brusselAirlinesService.war
- $COMPEL/MockServices/firstHotelService/target/fisrtHotelService.war
- $COMPEL/MockServices/HostelWorld/target/HostelWorldService.war
- $COMPEL/MockServices/HotelFind/target/HotelFindService.war
- $COMPEL/MockServices/demo-app/target/demo-app-1.0.war

Deploy optional domain specific ontologies in $TOMCAT_HOME/webapps/ROOT/ontologies/ directory:

- $COMPEL/third party dependencies/Knowledge Base/Ontologies/pizza.owl
- $COMPEL/third party dependencies/Knowledge Base/Ontologies/wine.owl
- $COMPEL/third party dependencies/Knowledge Base/Ontologies/wp7-businessregistration-fc.wsml.rdf.xml
- $COMPEL/third party dependencies/Knowledge Base/Ontologies/wp7-io.wsml.rdf.xml
- $COMPEL/third party dependencies/Knowledge Base/Ontologies/Turismo.owl.xml
- $COMPEL/third party dependencies/Knowledge Base/Ontologies/eTourism.owl
- $COMPEL/third party dependencies/Knowledge Base/Ontologies/logisticOntology_v2.5.owl
- $COMPEL/third party dependencies/Knowledge Base/Ontologies/logisticOntologyClassif_v2.5.owl

Launch Tomcat:
```sh
$TOMCAT_HOME/bin>startup.sh
````

Deploy COMPEL ontologies. Access this URL in a Web browser:

http://<$COMPEL_SERVER>[2]:<$COMPEL_PORT>/openrdf-workbench/repositories/NONE/create?type=owlim-lite

This action will create the KB repository named WebN1.
- Select WebN1 repository in the Sesame Workbench left menu, in Repositories option

For each ontology in below table, follow the following procedure:
- Select menu Add
- Fulfill the form with the following data:
    - Base URI: Ontology_URI
    - Context: <[Ontology_URI]>
    - Select option "Select the file containing the RDF data you wish to upload"
    - Choose File: browse to the ontology RDF file in $YOUR_BPM/third party dependencies/Knowledge Base/Ontologies/ directory
    - Click on upload


Ontology table:

| Ontology File     | Ontology_URI    | 
| --------|---------|
|$COMPEL/third party dependencies/Knowledge Base/Ontologies/OntoBPMN.owl  | http://dkm.fbk.eu/index.php/BPMN_Ontology   | 
| $COMPEL/third party dependencies/Knowledge Base/Ontologies/POSM.rdf.owl | http://www.wsmo.org/ns/posm/0.2 | 
| $COMPEL/third party dependencies/Knowledge Base/Ontologies/bridge.owl | http://bridge-webn1.atosresearch.eu/bridge | 
| $COMPEL/third party dependencies/Knowledge Base/Ontologies/integra.owl | http://bridge-webn1.atosresearch.eu/bridge | 

Deploying Semantic Web Service (SWS) descriptions
```sh
	$COMPEL> mvn install –f KBClient/pom.xml
```
The execution of KBClient tests will deploy the SWS descriptions once that Sesame KB is configured and running. Every time that you need to deploy a new Semantic Web Service, it is necessary to create a new description file (posm) and execute again the KBClient.

Access Activiti/YourBPM using Firefox browser at
http://<$COMPEL_SERVER>:<$COMPEL_PORT>/ActivitiCompelExplorer2

### Licence
GPL v3  Licence

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

 
   [PlDb]: <https://github.com/CBPM-WG/Fitman-CBPM/blob/master/ENG/FITMAN-BPM/README.md>




