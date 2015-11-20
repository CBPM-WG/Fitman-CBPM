/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext;

import java.net.URI;
import java.net.URL;



/**
 * This interface represents the executable entity that sits behind an
 * executable process element, such as activity or human task. It groups
 * together both the service and the operation that will be invoked 
 * when the activity is executed.
 * 
 * IBinding is based on the concepts of the MSM model in which the services are
 * identified by URLs and their operations are identified by URIs.
 * 
 * In case the service represents a business process that
 * has been deployed to the execution engine and exposed as
 * a service, it may be possible to discover the process URL  by looking 
 * for a rdfs:seeAlso annotation in the MSM model.
 * 
 */
public interface IBinding extends IIdentifiable, INameable {
	
	URL getServiceReference();
	void setServiceReference(URL value);
	
	URI getOperation();
	void setOperation(URI operation);

}
