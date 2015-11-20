/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;



/**
 * 
 * A generic interface that all process elements inherit. 
 * Provides basic support for identification, naming and 
 * semantic annotations
 * 
 * The ID is a "process local" identifier and shall be unique inside
 * the process in which this element exists. 
 * It is up to the users of LPML to ensure unique element IDs inside 
 * the process. In order to ensure global uniqueness, for example in some collaboration scenarios,
 * where process elements of different processes might have to be referred to, the process id 
 * may be used as well to identify the process element globally  
 * 
 */
public interface ProcessElement extends IProcessElement {

}
