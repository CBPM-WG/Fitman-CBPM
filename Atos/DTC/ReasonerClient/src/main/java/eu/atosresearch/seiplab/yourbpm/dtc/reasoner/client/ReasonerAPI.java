package eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client;

import java.util.HashMap;
import java.util.List;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLService;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLTemplate;



public interface ReasonerAPI {
	public void cleanUp();
	public List<WSLService> matchGoalMSM_WS (IProcess process, IActivity activity, int maxFetched);
	public List<WSLTemplate> matchGoalMSM_Template(IProcess process, IActivity activity, int maxFetched);
	public boolean matchClasses(String[] c1s, String[] c2s);
	public boolean match(ISemanticAnnotation sa1, ISemanticAnnotation sa2);
}
