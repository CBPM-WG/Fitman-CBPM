/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IConnector;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IPositionable;

import java.util.List;
import java.util.Set;

/**
 * 
 * An activity represents a unit of work in a business process. In LPML
 * activities can be bound to one or more compatible pairs of service+operation. 
 *
 */
public interface IActivity extends IProcessElement, IPositionable {
	
	/**
	 * 
	 * An activity has a list with one or more bindings. All bindings shall be compatible with each other
	 * so that if one of them doesn't work it can be replaced with the next one during runtime.
	 * The order of the list reflects the invocation preference. That is during runtime the first binding 
	 * shall be attempted first and only if it doesn't work the next one shall be attemted.
	 * 
	 * @return a list of the current bindings for this activity.
	 * @see IBinding 
	 */
	List<IBinding> getBindings();
	
	
	/**
	 * I/O parameters are needed for the following use cases
	 * 
	 * 1. Execution related 
	 * 	- IParameter flow mapping (IParameter info is also kept in the data flow IConnector)
	 *  - Control flow (IParameter info is also used in the Flow's condition
	 *  - Loops (there is a loop parameter)
	 *  
	 * 2. Composition related (i.e. the user specifies the desired I/O of the activity or a 
	 *  human task. In the case of activity, DTC binds the conforming service+operation pairs.     
	 * In the case of human task modeling the specified inputs and outputs may be used in 
	 * order to automatically render the human task in the respective human task client 
	 */
	Set<? extends IParameter> getInputParameters();
	Set<? extends IParameter> getOutputParameters();
	

	boolean isStartElement();
	void setStartElement(boolean value);

	/**
	 * TODO decide if it is possible to have more than
	 * one end event. This would be convenient for the user
	 * but it will make it much harder to define the process
	 * outputs.
	 * @return true if the activity is an end element
	 */
	boolean isEndElement();
	void setEndElement(boolean value);

	
	boolean isHumanTask();
	void setHumanTask(boolean value);

	/**
	 * 
	 * TODO @Yosu - what is the use case for this property. Why shall the modeler
	 * bother if the activity is synchronous or asynchronous?
	 * What does it mean to be synchronous?
	 */
	boolean isSynchronous();
	void setSynchronous(boolean value);

	
	/**
	 * This is needed to support loops. If set, this parameter must be of
	 * a collection type. During execution the 
	 * activity has to be executed once for each of the values of the
	 * collection.
	 * 
	 * If this parameter is null this means that this is a regular activity
	 * without a loop. 
	 * @param p
	 */
	void setLoopParameter(IParameter p);
	
	/**
	 * 
	 * @return the loop parameter
	 * @see #setLoopParameter(IParameter)
	 */
	IParameter getLoopParameter();
	
	/**
	 * 
	 * An activity has a set of connectors that define the data flow mapping for
	 * this activity. There are zero to one connectors for each input parameter of
	 * the activity. 
	 * 
	 * TODO @UKARL - this is just my humble view of the model. Please clarify
	 * the details how data flow shall be presented on an LPML level
	 * 
	 * @return the list of data flow mapping connectors for this activity
	 */
	Set<IConnector> getConnectors();
	
}