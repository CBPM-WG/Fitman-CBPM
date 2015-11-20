This Reasoner client is used by DTComposer and provides wrapping methods
over soa4all.service.annotation reasoner service used by soa4all.service.location/service.discovery

Dependencies: Running soa4all.service.annotation reasoner service

Installation of soa4all.service.annotation reasoner service
-----------------------------------------------------------

check out the project
svn co https://svn.sti2.at/soa4all/trunk/soa4all-service-annotation/soa4all-service-annotation-repository-reasoner

disable using the semantic spaces repository in the file
soa4all-service-annotation-repository-reasoner/src/reasoner.properties
> useServiceAnnotationRepository = false

run the reasoner
> ant publish-webservice

Reasoner service running at: http://127.0.0.1:8765/soa4all/reasoner
Service descriptions located in directory: ontologies. See examples.wsml 

Configuration of DTC Reasoner Client
------------------------------------
Edit src/main/resources/reasoner.properties. Set reasoner service data (url, port):

reasoner.url = http://127.0.0.1
reasoner.port = 8765
reasoner.path = /soa4all/reasoner
reasoner.namespaceURI = http://service.repositoryreasoner.soa4all.eu/
reasoner.localpart = ReasonerServiceService

DTC Reasoner Client Test
------------------------
They work with examples.wsml ontology under src/main/resources/examples.wsml
Therefore, to make those test work, replace soa4all.service.annotation reasoner service ontologies
under soa4all-service-annotation/soa4all-service-annotation-repository-reasoner/ontologies directory
with our examples.wsml and restart the reasoner 