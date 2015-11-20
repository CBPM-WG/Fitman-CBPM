package eu.atosresearch.seiplab.yourbpm.dtc.client;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;


import com.thoughtworks.xstream.XStream;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcessElement;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerExceptionException0;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.BindActivity;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.BindActivityResponse;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.GenerateDataFlow;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.GenerateDataFlowResponse;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.ResolveActivity;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.ResolveActivityResponse;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.ResolveProcess;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.ResolveProcessResponse;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.SearchForActivityBindings;
import eu.atosresearch.seiplab.yourbpm.dtc.client.stub.DTComposerStub.SearchForActivityBindingsResponse;


public class DTComposerClient {
	long TIMEOUT = 10*60*1000;
	
	private DTComposerStub stub = null;
	
	public DTComposerClient (String endpoint, String proxyHost, int proxyPort) throws AxisFault{
		stub = new DTComposerStub (endpoint);
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(TIMEOUT);
//		if(proxyHost!=null) {
//			org.apache.axis2.transport.http.HttpTransportProperties.ProxyProperties proxyProps = new org.apache.axis2.transport.http.HttpTransportProperties.ProxyProperties ();
//			proxyProps.setProxyName(proxyHost);
//			proxyProps.setProxyPort(proxyPort);
//			proxyProps.setUserName("annonymous");
//			proxyProps.setPassWord("secret");
//			stub._getServiceClient().getOptions().setProperty(HTTPConstants.PROXY, proxyProps);	
//		}
	}
	
	public DTComposerClient (String endpoint) throws AxisFault{
		this(endpoint, null, -1);
	}
	
	public DTComposerClient () throws AxisFault{
		stub = new DTComposerStub ();
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(TIMEOUT);
	}
	
	// In order to find all solutions, set numberRequestedSolutions=-1
	
	// Specialized methods
	// [URI] DTC operations simplified. Some methods have been disabled and others renamed
	
	/**
	 * Request to resolve an unbound Activity (described by a goal annotations) 
	 * either binding it with found WS or expanding it with a process fragment or template
	 * This method is more generic than bindActivity since it may replace the activity by a subprocess (template)
	 * @param process LPML process model that includes the activity to resolve
	 * @param activity LPML Activity to resolve
	 * @return Resolve LPML process 
	 * @throws DTComposerExceptionException 
	 * @throws RemoteException 
	 * @see SOA4ALL D6.4.2, D6.4.3
	 */
	public IProcess resolveActivity(IProcess process, IActivity activity) throws DTComposerExceptionException0{
		try {
			ResolveActivity rg = new ResolveActivity();
			rg.setSourceProcessModel(new XStream().toXML(process));
			rg.setActivityTargetURI(activity.getID());
			ResolveActivityResponse r = stub.resolveActivity(rg);
			String resolvedProcess = r.get_return();
			if(resolvedProcess !=null)
				return (IProcess) new XStream().fromXML(resolvedProcess);
			else
				return null;
		} catch (RemoteException e) {
			throw new DTComposerExceptionException0(e.getMessage());
		}
	}
	
//	/**
//	 * Request to resolve an unbound Activity (described by a goal annotations) 
//	 * expanding it with a process fragment or template
//	 * @param model URI
//	 * @param activity 
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public String resolveGoalWithTemplate(String sourceProcessModel, String activity) throws RemoteException, DTComposerExceptionException{
//		ResolveGoalWithTemplate rgwt = new ResolveGoalWithTemplate();
//		rgwt.setSourceProcessModel(sourceProcessModel);
//		rgwt.setActivityTargetURI(activity);
//		ResolveGoalWithTemplateResponse r = stub.resolveGoalWithTemplate(rgwt);
//		return r.get_return();
//	}
	
//	/**
//	 * Request to loopup for additional compatible bindings of given activity.
//	 * @param process LPML process model that includes the activity to bind
//	 * @param activity LPML Activity to bind
//	 * @return Set of BindingCompatibilityDescriptor describing new suggested bindings
//	 * @throws DTComposerExceptionException 
//	 * @throws RemoteException 
//	 * @see SOA4ALL D6.4.2, D6.4.3
//	 */
//	
//	public Set<BindingCompatibilityDescriptor> bindActivity (IProcess process, IActivity activity) throws DTComposerExceptionException{
//		try {
//			BindActivity rgww = new BindActivity();
//			rgww.setSourceProcessModel(new XStream().toXML(process));
//			rgww.setActivityTargetURI(activity.getID());
//			BindActivityResponse r = stub.bindActivity(rgww);
//			// Lookup activity in returned model
//			Set<BindingCompatibilityDescriptor> bcds = new HashSet<BindingCompatibilityDescriptor>();
//			if (r.get_return() != null){
//				IProcess p = (IProcess) new XStream().fromXML(r.get_return());
//				IActivity a = lookupActivity (p, activity.getID());
//				// Detect new binding by comparison
//				bcds = detectBindingSetChanges (a, activity);
//			}
//			// Returned modified bindings
//			return bcds; // TODO include semantic compatibility between bound services and desired IO in return structure.
//		} catch (RemoteException e) {
//			throw new DTComposerExceptionException(e.getMessage());
//		}
//	}
	
//	/**
//	 * Populates the Set<BindingCompatibilityDescriptor> comparing the binding of the input and output Activities
//	 * Current implementation only detects 100% compatible new bindings
//	 * TODO: Current implementation detects new binding, not binding changes or not 100% bindings
//	 * @param newActivity Activity processed by DTC, including new bindings
//	 * @param oldActivity Former activity sent to DTC for binding.
//	 * @return
//	 */
//	private Set<BindingCompatibilityDescriptor> detectBindingSetChanges(
//			IActivity newActivity, IActivity oldActivity) {
//		Set<BindingCompatibilityDescriptor> result = new HashSet<BindingCompatibilityDescriptor>();
//		for (IBinding b:newActivity.getBindings()){
//			if (!hasBinding (oldActivity.getBindings(), b)){
//				//TODO Detect not full compatible bindings at DTC and serialize this information in the return value.
//				BindingCompatibilityDescriptor 
//					bcd = new BindingCompatibilityDescriptorImpl(b, "Binding compatible", 100.0f);
//				result.add(bcd);
//			}
//		}
//		return result;
//	}

	private boolean hasBinding(List<IBinding> bindings, IBinding b) {
		boolean result = false;
		for (IBinding binding: bindings){
			if (binding.getOperation().toString().equals(b.getOperation().toString()) &&
				binding.getServiceReference().toString().equals(b.getServiceReference().toString())){
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Look up for an activity within the given process by id
	 * @param process
	 * @param id
	 * @return
	 */
	private IActivity lookupActivity(IProcess process, String id) {
		IActivity a = null;
		for (IProcessElement pe: process.getProcessElements()){
			if (pe instanceof IActivity){
				if (((IActivity)pe).getID().equals(id)){
					a = (IActivity)pe;
					break;
				}
			}
		}
		return a;
	}

	/**
	 * Request to resolve a model
	 * either binding all its activities with found WS or expanding them with a process fragment or template
	 * @param process LPML process model to resolve
	 * @return Resolved LPML process model.
	 * @throws DTComposerExceptionException 
	 * @throws RemoteException 
	 * @see SOA4ALL D6.4.2, D6.4.3
	 */
	public IProcess resolveProcess(IProcess process) throws DTComposerExceptionException0{
		try {
			ResolveProcess rp = new ResolveProcess();
			rp.setSourceProcessModel(new XStream().toXML(process));
			ResolveProcessResponse r = stub.resolveProcess(rp);
			String resolvedProcess = r.get_return();
			if(resolvedProcess !=null)
				return (IProcess) new XStream().fromXML(resolvedProcess);
			else
				return null;
		} catch (RemoteException e) {
			throw new DTComposerExceptionException0(e.getMessage());
		}
	}
	
//	/**
//	 * Request to resolve a process 
//	 * expanding all its activities with a process fragments or templates
//	 * @param model URI
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public String resolveProcessWithTemplate(String sourceProcessModel) throws RemoteException, DTComposerExceptionException{
//		ResolveProcessWithTemplate rpwt = new ResolveProcessWithTemplate();
//		rpwt.setSourceProcessModel(sourceProcessModel);
//		ResolveProcessWithTemplateResponse r = stub.resolveProcessWithTemplate(rpwt);
//		return r.get_return();
//	}
	
//	/**
//	 * Request to resolve a model
//	 * expanding all its activities with a process fragment or template
//	 * @param model URI
//	 * @param minimun number of different model solution requested
//	 * @return Collection found solutions
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public String resolveProcessWithWS(String sourceProcessModel) throws RemoteException, DTComposerExceptionException{
//		ResolveProcessWithWS rpww = new ResolveProcessWithWS();
//		rpww.setSourceProcessModel(sourceProcessModel);
//		ResolveProcessWithWSResponse r = stub.resolveProcessWithWS(rpww);
//		return r.get_return();
//	}
	
//	/**
//	 * Request to check the semantic compatibility between inputs and outputs of activities 
//	 * logically connected through the work flow by causality.
//	 * NOT IMPLEMENTED YET
//	 * @param process LPML process model to check
//	 * @return Checked LPML process model. Some incompatible bindings could be dropped out.
//	 * @throws DTComposerExceptionException 
//	 * @throws RemoteException 
//	 * @see SOA4ALL D6.4.2, D6.4.3
//	 */
//	@SuppressWarnings("unchecked")
//	public Set<BindingCompatibilityDescriptor> checkIOSemanticCompatibility (IActivity activity) throws DTComposerExceptionException{
//		try {
//			CheckIOSemanticCompatibility ciosc = new CheckIOSemanticCompatibility();
//			ciosc.setActivity(new XStream().toXML(activity));
//			CheckIOSemanticCompatibilityResponse r = stub.checkIOSemanticCompatibility(ciosc);
//			XStream xstream = new XStream();
//			String result = r.get_return();
//			System.out.println (result);
//			Set<BindingCompatibilityDescriptor> sbcd = 
//				(Set<BindingCompatibilityDescriptor>) xstream.fromXML(result);
//			
//			return sbcd;
//		} catch (RemoteException e) {
//			throw new DTComposerExceptionException(e.getMessage());
//		}
//	}
	
	/**
	 * Request to generate the process model data flow. It is required a complete process model, 
	 * That is, a process model without abstract activities: all activities bound.
	 * NOT IMPLEMENTED YET
	 * @param process LPML process model to generate the data flow for
	 * @return Updated LPML process model thats includes the generated data flow.
	 * @throws DTComposerExceptionException 
	 * @throws RemoteException 
	 * @see SOA4ALL D6.4.2, D6.4.3
	 */
	public IProcess generateDataFlow (IProcess process) throws DTComposerExceptionException0{
		try {
			GenerateDataFlow gdf = new GenerateDataFlow();
			gdf.setSourceProcessModel(new XStream().toXML(process));
			GenerateDataFlowResponse r = stub.generateDataFlow(gdf);
			String resolvedProcess = r.get_return();
			if(resolvedProcess !=null)
				return (IProcess) new XStream().fromXML(resolvedProcess);
			else
				return null;
		} catch (RemoteException e) {
			throw new DTComposerExceptionException0(e.getMessage());
		}
	}

	
	// Management methods
	// [Yosu] Management methods disabled
//	/**
//	 * Register within the DTComposer a new DesignModificationAgent (DMA) based on Drools Rules, 
//	 * with concrete domain specific knowledge for modifications (set of Drools rules described by specific SOA4ALL WP6 DSL)
//	 * @param Unique identifier for this DMA
//	 * @param classpath based path or a URL to the domain specific knowledge (rules), based on SOA4ALL DSL.
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public void registerDesignModificationRuleAgent(String uri, String knowledgePath) throws RemoteException, DTComposerExceptionException{
//		RegisterDesignModificationRuleAgent rdma = new RegisterDesignModificationRuleAgent();
//		rdma.setUri(uri);
//		rdma.setKnowledgePath(knowledgePath);
//		stub.registerDesignModificationRuleAgent(rdma);
//	}
	
//	/**
//	 * Register within the DTComposer a new DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * with concrete domain specific knowledge for analysis (set of Drools rules described by specific SOA4ALL WP6 DSL)
//	 * @param Unique identifier for this DMA
//	 * @param classpath based path or a URL to the domain specific knowledge (rules), based on SOA4ALL DSL. 
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public void registerDesignAnalysisAgent(String uri, String knowledgePath) throws RemoteException, DTComposerExceptionException{
//		RegisterDesignAnalysisAgent rdma = new RegisterDesignAnalysisAgent();
//		rdma.setUri(uri);
//		rdma.setKnowledgePath(knowledgePath);
//		stub.registerDesignAnalysisAgent(rdma);
//	}
//	
//	/**
//	 * Register within the DTComposer a new semantic DesignAnalysisAgent (DMA) based on WSMO rules, 
//	 * @param Unique identifier for this DMA
//	 * @param classpath based path or a URL pointing at domain specific knowledge described by  WSMO ontologies 
//	 * @param classpath based path or a URL pointing at domain specific services described by WSMO ontologies 
//	 * @param classpath based path or a URL pointing at domain specific context described by  WSMO ontologies
//	 * @param classpath based path or a URL pointing at domain specific goals described by WSMO ontologies 
//	 * @param domain specific namespace
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public void registerDesignModificationSemanticAgent(String identifier, String[] knowledgeOntologies, 
//			String[] knownServices, String[] contextOntologies,  String[] goals)
//		throws RemoteException, DTComposerExceptionException{
//		RegisterDesignModificationSemanticAgent rdma = new RegisterDesignModificationSemanticAgent();
//		rdma.setUri(identifier);
//		rdma.setKnowledgeOntologies(knowledgeOntologies);
//		rdma.setKnownServices(knownServices);
//		rdma.setContextOntologies(contextOntologies);
//		rdma.setGoals(goals);
//		stub.registerDesignModificationSemanticAgent(rdma);
//	}
//	
//	/**
//	 * Unregister within the DTComposer an identified DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * @param Unique identifier for this DMA
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public void unregisterDesignModificationRuleAgent(String identifier) throws RemoteException, DTComposerExceptionException{
//		UnregisterDesignModificationRuleAgent urdma = new UnregisterDesignModificationRuleAgent();
//		urdma.setIdentifier(identifier);
//		stub.unregisterDesignModificationRuleAgent(urdma);
//	}
//	
//	/**
//	 * Unregister within the DTComposer an identified DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * @param Unique identifier for this DMA
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public void unregisterDesignAnalysisAgent(String identifier) throws RemoteException, DTComposerExceptionException{
//		UnregisterDesignAnalysisAgent urdma = new UnregisterDesignAnalysisAgent();
//		urdma.setIdentifier(identifier);
//		stub.unregisterDesignAnalysisAgent(urdma);
//	}
//	
//	/**
//	 * Unregister within the DTComposer an identified DesignAnalysisAgent (DMA) based on Drools Rules, 
//	 * @param Unique identifier for this DMA
//	 * @throws 
//	 * @see SOA4ALL D5.4.2
//	 */
//	public void unregisterDesignModificationSemanticAgent(String identifier)
//	throws RemoteException, DTComposerExceptionException{
//		UnregisterDesignModificationSemanticAgent urdma = new UnregisterDesignModificationSemanticAgent();
//		urdma.setIdentifier(identifier);
//		stub.unregisterDesignModificationSemanticAgent(urdma);
//	}

	
// Mateusz: resolve() functionality is covered by bindActivity(), 
// we only need to add compatibility descriptor to bindActivity's return structure 
//	
//	/**
//	 * 
//	 * Propose binding for the activity based on its I/O parameters.
//	 * DTC might return bindings that are not 100% compatible.
//	 * 
//	 * @param a the activity to be resolved
//	 * @return a set of compatibility descriptors with bindings inside them
//	 * these bindings are just suggestions. 
//	 */
//	Set<BindingCompatibilityDescriptor> resolve(Activity a){
//		return null;
//	}
	
//	/**
//	 * 
//	 * Check the compatibility of the existing activity bindings
//	 * with its I/O parameters
//	 * NOT IMPLEMENTED YET
//	 * @param a the activity to be checked
//	 * @return a set of compatibility descriptors
//	 * @throws DTComposerExceptionException 
//	 * @throws RemoteException 
//	 * @see BindingCompatibilityDescriptor
//	 */
//	public Set<BindingCompatibilityDescriptor> checkBindingCompatibility(IActivity activity) throws DTComposerExceptionException{
//		return checkIOSemanticCompatibility(activity);
//	}
	
	/**
	 * Request to bind a given activity (task) for a given process
	 * @param processId Id of process that includes the activity to bind
	 * @param activityId Id of the activity to bind.
	 * @return Boolean True if binding was successful.
	 * @throws DTComposerExceptionException 
	 * @throws RemoteException 
	 * @see SOA4ALL D6.4.2, D6.4.3
	 */
	
	public boolean searchForActivityBindings (String processId, String taskId) throws DTComposerExceptionException0{
		try {
			SearchForActivityBindings sfab = new SearchForActivityBindings();
			sfab.setActivityURI(taskId);
			sfab.setProcessURI(processId);			
			SearchForActivityBindingsResponse r = stub.searchForActivityBindings(sfab);
			return r.get_return();
		} catch (RemoteException e) {
			throw new DTComposerExceptionException0(e.getMessage());
		}
	}
}
