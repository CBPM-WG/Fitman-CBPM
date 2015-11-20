/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;

import java.util.Set;


/**
 * 
 * Defines the data flow mapping between one or more outputs and
 * a single input. There might be a transformation applied to the
 * output(s) in order to adapt to the input. The transformation is
 * expressed in the form of a SPARQL query on the outputs
 *
 */
public interface IConnector extends IIdentifiable {
	
	/**
	 * 
	 * @return the SPARQL transformation
	 */
	String getSemanticMapping();
	
	/**
	 * 
	 * @param value the SPARQL transformation
	 */
	void setSemanticMapping(String value);

//	/**
//	 * @TODO clarify
//	 * @return
//	 */
//	IParameter getLoopParameter();
	
	
	/**
	 * One connector always maps to exactly one input parameter
	 * of the owning activity.
	 * 
	 * @return the output parameter of the connector that corresponds to one
	 * input parameter of the owning activity
	 */
	IParameter getOutputParameter();
	void setOutputParameter(IParameter p);

	/**
	 * 
	 * @return the set of input parameters for this connector
	 * that are mapped to the input parameter of the owning activity
	 */
	Set<? extends IParameter> getInputParameters();
}
