/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml;

import java.util.Set;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;


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
public interface Process extends IProcess
{
	
	
	/**
	 * @return all process elements of this process including the start and end events
	 */
        @Override
        Set<IProcessElement> getProcessElements();

	@Override
        Set<Flow> getFlows();

	/**
	 * 
	 * @return the start element of a process or null if none is present. 
	 * In case more than one start elements are added to the process the method
	 * might return an arbitrary element
	 */
	@Override
        Activity getStartElement();

	/**
	 * 
	 * @return the end element of a process or null if none is present. 
	 * In case more than one end elements are added to the process the method
	 * might return an arbitrary element
	 */
	@Override
        Activity getEndElement();
}
