/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn;


/**
 * 
 * In a business process, a flow represents a transition from one 
 * process element to another. It has one source and one destination
 * process element
 *  
 */
public interface IFlow extends IProcessElement {
		
	/**
	 * The source from which this flow originates. Business process execution
	 * flows from the source to the destination of the flow.
	 * 
	 * @param source
	 */
	void setSource(IProcessElement source);
	IProcessElement getSource();

	/**
	 * 
	 * @param destination
	 * @see #setSource(IProcessElement)
	 */
	void setDestination(IProcessElement destination);
	IProcessElement getDestination();
	
	
	/**
	 * 
	 * Flows can have a condition. The condition is evaluated at 
	 * runtime to a boolean value. If this value is not <code>true</code> the
	 * flow cannot be traversed. Then, either another flow, which condition
	 * is <code>true</code> shall be traversed or the process execution halts.
	 * 
	 * That is why, a valid LPML process must always provide a unconditional flow, 
	 * that is taken in case the conditions of all predecessor flows evaluate to
	 * <code>false</code>
	 *  
	 *  LPML API is orthogonal to the language used to express the condition.
	 *  It is up to the respective design and runtimes to agree on a specific format
	 *  
	 *  @return the actual condition expression. It must be null for default flows
	 */
	String getCondition();

	/**
	 * Sets the condition of this flow
	 * @see #getCondition()
	 */
	void setCondition(String value);
	
	
	/**
	 * In LPML the flows have ordering. This is needed in order to ensure
	 * determinism when evaluating of the conditions of several flows going out
	 * of a process element. The flow with the lowest sequence number shall be
	 * attempted first. 
	 * 
	 * There shall always be exactly one default flow going out of a process
	 * element or no flows at all (for incomplete processes). The default flow
	 * has a sequence number of Integer.MAX_VALUE and hence is the last one being
	 * attempted.
	 *  
	 * @return the sequence number of this flow.
	 */
	int getSequenceNumber();
	
	/**
	 * The sequence number is mutable because users may reorder the flows
	 * 
	 * @param number
	 */
	void setSequenceNumber(int number);
	
}

