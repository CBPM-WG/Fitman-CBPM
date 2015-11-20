/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;



/**
 * 
 * A parameter represents a given input or output of an Activity,
 * human task or another I/O consuming or producing process element 
 */
public interface Parameter extends IParameter {

	/**
	 * A temp hack !!!
	 * @return TODO @Yosu specify the usage of this property
	 */
	@Override
        String getValue();
	@Override
        void setValue(String value);
	
}
