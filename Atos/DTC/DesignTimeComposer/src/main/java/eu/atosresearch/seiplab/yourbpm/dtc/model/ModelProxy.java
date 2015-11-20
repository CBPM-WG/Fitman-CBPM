package eu.atosresearch.seiplab.yourbpm.dtc.model;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IFlow;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public interface ModelProxy {
	public <T> T cloneLPMLObject(T cloneObject) throws URISyntaxException;
	public IProcess cloneProcess(IProcess process) throws Exception;
	
	public ArrayList<IActivity> findAllActivities(IProcess process);
	public Collection<? extends IActivity> findAllNonHTActivities(IProcess process);
	public ArrayList<IActivity> findAllUnboundActivities(IProcess process);
	public ArrayList<IActivity> findAllUnboundActivities2 (IProcess process);
	
	public IProcessElement getProcessElementByID(IProcess process, String ID);
	
	public IActivity getActivityById(IProcess process, URI id);
	public IActivity getActivityByName(IProcess process, String name);
	
	public Collection<IProcessElement> getFirstActivities(IProcess process);
	public Collection<IProcessElement> getLastActivities(IProcess process);
	
	public IProcessElement getFlowObjectByIdentifier(IProcess process, URI uri);
	public Iterator<IFlow> getFlows(IProcess processTemplate, IProcessElement a);
	
	public Collection<IFlow> getIncommingSequenceFlowObjects(IProcess process, IActivity activity);
	public Collection<IFlow> getOutgoingSequenceFlowObjects(IProcess process, IActivity activity);
	
	public Collection<IProcessElement> getOutgoingActivities(IProcess process, IActivity activity);
	
	public Collection<ISemanticAnnotation> getPreferences(IProcess process);
	public Collection<ISemanticAnnotation> getRequirements(IProcess process);
	
	public IActivity getStartElement(IProcess process);
	public IActivity getEndElement(IProcess process);
	
	public boolean substitute(IProcess process, IActivity activity, IBinding binding);
	public void substitute(IProcess process, IActivity activity, IProcess template);
	
	public String createProcessWithActivityAnnotations(
			List<ISemanticAnnotation> gsas, List<ISemanticAnnotation> sas, 
			List<ISemanticAnnotation> isas, List<ISemanticAnnotation> osas, String activityTargetURI);
	public List<IBinding> getBindings(String model, String activityTargetURI) throws URISyntaxException;
}
