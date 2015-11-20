package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IPositionable;


/**
 * A gateway is a non-executable element of a process that lets the
 * user split one flow into two or more flows and merge them back.
 * 
 * The semantics of the split/merge shall be specified in sub interfaces.
 * 
 *
 */
public interface IGateway extends IProcessElement,IPositionable {

	boolean isSplit();
} 
