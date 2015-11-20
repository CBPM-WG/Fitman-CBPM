package eu.atosresearch.seiplab.yourbpm.dtc.client;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;



/**
 * A binding compatibility descriptor to be used in operations like
 * check compatibility and resolve
 * 
 * @author pavel.genevski@sap.com
 */
public interface BindingCompatibilityDescriptor {

	/**
	 * 
	 * @return the incompatible binding
	 */
	IBinding getBinding();
	
	/**
	 * 
	 * @return a message describing why the binding is not 100% compatible
	 */
	String getMessage();
	
	/**
	 * 
	 * @return a numerical form of the compatibility
	 * 1 means 100% compatibility, 0.5 is 50% etc.
	 */
	float getCompatibility();
}
