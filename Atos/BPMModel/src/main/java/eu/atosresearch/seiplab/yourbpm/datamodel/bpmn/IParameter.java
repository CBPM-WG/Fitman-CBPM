/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn;



/**
 * 
 * A parameter represents a given input or output of an Activity,
 * human task or another I/O consuming or producing process element 
 */
public interface IParameter extends IProcessElement {

	/**
	 * A temp hack !!!
	 * @return TODO @Yosu specify the usage of this property
	 */
	String getValue();
	void setValue(String value);
	
}
