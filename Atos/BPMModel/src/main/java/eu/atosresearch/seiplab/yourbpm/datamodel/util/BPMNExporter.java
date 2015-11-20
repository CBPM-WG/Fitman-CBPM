package eu.atosresearch.seiplab.yourbpm.datamodel.util;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;

public interface BPMNExporter {
	public Object exportProcess (IProcess process);
}
