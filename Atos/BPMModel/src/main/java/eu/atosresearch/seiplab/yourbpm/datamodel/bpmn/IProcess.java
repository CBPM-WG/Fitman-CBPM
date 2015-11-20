/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotatable;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IIdentifiable;

import java.util.Set;


/**
 * Represents an LPML process. A process has an ID and binds together all process
 * elements and their relationships. There are two distinct sets of process elements
 * 
 * How processes are identified is 
 * application specific. In any case, being globally unique recommended. 
 * One solution to this problem could be REST style URI identifiers, like
 * http://mycompany.com/processes/234234
 * 
 * Another way could be to use reversed FQDN similar to Java, i.e.
 * com.mycompany.processes.hr.BasicHiringProcess
 * 
 */
public interface IProcess extends IIdentifiable, IAnnotatable {
	
	
	/**
	 * @return all process elements of this process including the start and end events
	 */
	Set<? extends IProcessElement> getProcessElements();

	Set<? extends IFlow> getFlows();

	/**
	 * 
	 * @return the start element of a process or null if none is present. 
	 * In case more than one start elements are added to the process the method
	 * might return an arbitrary element
	 */
	IActivity getStartElement();

	/**
	 * 
	 * @return the end element of a process or null if none is present. 
	 * In case more than one end elements are added to the process the method
	 * might return an arbitrary element
	 */
	IActivity getEndElement();

}
