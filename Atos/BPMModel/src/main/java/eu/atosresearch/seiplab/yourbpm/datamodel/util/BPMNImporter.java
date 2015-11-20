package eu.atosresearch.seiplab.yourbpm.datamodel.util;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;

public interface BPMNImporter {
	public IProcess importProcess(String lpmlData);
}
