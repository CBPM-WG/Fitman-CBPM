package eu.atosresearch.seiplab.yourbpm.dtc.model.bpmn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IFlow;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;

public class BPMNProxy implements ModelProxy {

	@Override
	public Collection<ISemanticAnnotation> getRequirements(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ISemanticAnnotation> getPreferences(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void substitute(IProcess process, IActivity activity, IProcess template) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<IFlow> getFlows(IProcess processTemplate, IProcessElement a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProcessElement getProcessElementByID(IProcess process, String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActivity getActivityByName(IProcess process, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActivity getActivityById(IProcess process, URI id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IProcessElement> getFirstActivities(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IProcessElement> getLastActivities(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActivity getStartElement(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActivity getEndElement(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IFlow> getIncommingSequenceFlowObjects(IProcess process,
			IActivity activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IFlow> getOutgoingSequenceFlowObjects(IProcess process,
			IActivity activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IProcessElement> getOutgoingActivities(IProcess process,
			IActivity activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProcessElement getFlowObjectByIdentifier(IProcess process, URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IActivity> findAllActivities(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends IActivity> findAllNonHTActivities(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IActivity> findAllUnboundActivities(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IActivity> findAllUnboundActivities2(IProcess process) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean substitute(IProcess process, IActivity activity,
			IBinding binding) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IProcess cloneProcess(IProcess process) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T cloneLPMLObject(T cloneObject) throws URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createProcessWithActivityAnnotations(
			List<ISemanticAnnotation> gsas, List<ISemanticAnnotation> sas,
			List<ISemanticAnnotation> isas, List<ISemanticAnnotation> osas,
			String activityTargetURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IBinding> getBindings(String model, String activityTargetURI)
			throws URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}


}
