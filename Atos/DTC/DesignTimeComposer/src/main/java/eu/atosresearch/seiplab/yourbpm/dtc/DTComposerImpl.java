/**
 * 
 * Copyright (C) 2006-2010 ATOS ORIGIN
 * http://www.atosorigin.com
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the license LGPL.
 *
 * Authors :
 * Jesus Gorronogoitia. Atos Research and Innovation, Atos Origin SAE
 * @email jesus.gorronogoitia@atosresearch.eu
 * Mateusz Radzimski. Atos Research and Innovation, Atos Origin SAE
 * @email mateusz.radzimski@atosresearch.eu
 */
package eu.atosresearch.seiplab.yourbpm.dtc;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.service.Lifecycle;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thoughtworks.xstream.XStream;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.datamodel.util.BPMNValidationException;
import eu.atosresearch.seiplab.yourbpm.datamodel.util.BPMNValidator;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.Agent;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.BlackBoardControlAgent;
import eu.atosresearch.seiplab.yourbpm.dtc.configuration.ComposerConfiguration;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.AssignmentSet;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.ModelSolution;
import eu.atosresearch.seiplab.yourbpm.dtc.exception.DTComposerException;
import eu.atosresearch.seiplab.yourbpm.dtc.kb.KBProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.kb.KBProxyImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;
import eu.atosresearch.seiplab.yourbpm.dtc.utils.ModelIOUtils;
import eu.atosresearch.seiplab.yourbpm.dtc.viewer.DesignTimeComposerViewer;


public class DTComposerImpl implements IComposer, Lifecycle {

	// Logger
	static Logger logger = Logger.getLogger(DTComposerImpl.class);

	// Axis and DTC ClassLoader
	private static ClassLoader axis2Classloader = null;
	private static ClassLoader dtcClassloader = null;
	protected ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);
	protected KBProxy kbProxy = KBProxyImpl.getInstance();
	
//	public static ClassLoader getAxis2ClassLoader() {
//		return axis2Classloader;
//	}
	
	// Access to the singleton instance
	public static DTComposerImpl instance = null;

	private static ClassLoader getDTCClassLoader() {
		return dtcClassloader;
	}

	public static DTComposerImpl getInstance() {
		if (instance == null) {
			System.err
					.println("DTComposer not properly initialize with Spring configuration file in first invocation of getInstance method");
		}
		return instance;
	}

	List<Agent> registeredAgents = new ArrayList<Agent>();

	protected String springConfigurationFile;

	BlackBoardControlAgent blackBoardAgent = null;

//	public void setSpringConfigurationFile(String springConfigurationFile) {
//		this.springConfigurationFile = springConfigurationFile;
//	}

	ApplicationContext composerApplicationContext = null;

	DesignTimeComposerViewer viewer = null;

	ComposerConfiguration configuration;

	public DTComposerImpl() {
		dtcClassloader = this.getClass().getClassLoader();
	}


	@Override
	public String bindActivity(String sourceProcessModel,
			String activityTargetURI) throws DTComposerException {
		logger
				.debug("DTC-Invoked resolveGoalWithWS method with parameters sourceProcessModel: "
						+ sourceProcessModel
						+ ", activityTargetURI: "
						+ activityTargetURI);
		return processModel(sourceProcessModel, activityTargetURI,
				ComposerOperations.RESOLVE_GOAL_WITH_WS, 1).get(0);
	}
	
	public List<String> bindActivity(String sourceProcessModel,
			String activityTargetURI, int numberRequestedSolutions) throws DTComposerException {
		logger
				.debug("DTC-Invoked resolveGoalWithWS method with parameters sourceProcessModel: "
						+ sourceProcessModel
						+ ", activityTargetURI: "
						+ activityTargetURI);
		return processModel(sourceProcessModel, activityTargetURI,
				ComposerOperations.RESOLVE_GOAL_WITH_WS, numberRequestedSolutions);
	}
	
//	@Override
//	public String checkIOSemanticCompatibility(String activity)
//			throws DTComposerException {
//		
//		//deserialize Activity
//		XStream xstream = new XStream();
//		IActivity activityLPML = (IActivity) xstream.fromXML(activity);
//		
//		//get list of activity's I/O annotations
//		List<OperationIO> activityInputParams = new LinkedList<OperationIO>();
//		List<OperationIO> activityOutputParams = new LinkedList<OperationIO>();
//		
//		for (IParameter p : activityLPML.getInputParameters()) {
//			OperationIO operation = new OperationIO();
//			operation.setParameter(p);
//			activityInputParams.add(operation);
//		}
//		for (IParameter p : activityLPML.getOutputParameters()) {
//			OperationIO operation = new OperationIO();
//			operation.setParameter(p);
//			activityOutputParams.add(operation);
//		}
//		
//		ReasonerAPIWSMLImpl reasoner = (ReasonerAPIWSMLImpl) composerApplicationContext.getBean("reasoner");
//		SemanticLinkOperatorAgent SLOAgent = (SemanticLinkOperatorAgent) composerApplicationContext.getBean("SemanticLinkOperatorAgent");
//			
//		Set<BindingCompatibilityDescriptor> bindingCompatibility = new HashSet<BindingCompatibilityDescriptor>();
//		//iterate over bindings
//		for (IBinding b : activityLPML.getBindings()) {
//			//get list of bound service I/O
//			List<OperationIO> serviceInputParams = new LinkedList<OperationIO>();
//			List<OperationIO> serviceOutputParams = new LinkedList<OperationIO>();
//
//			WSLService ws = reasoner.getServiceByEndpointAndOperation(b.getServiceReference().toString(), b.getOperation().toString());
//			for (WSLMessage msg : ws.getOperations().get(0).getInput()) {
//				OperationIO operation = new OperationIO();
//				IParameter parameter = new ParameterImpl();
//				for (String mr : msg.getModelReferences()) {
//					ISemanticAnnotation sa = new SemanticAnnotationImpl();
//					sa.setReferenceURI(mr);
//					sa.setType(IAnnotationType.META_DATA);
//					parameter.getSemanticAnnotations().add(sa);
//					parameter.setName("");
//				}
//				operation.setParameter(parameter);
//				serviceInputParams.add(operation);
//			}
//
//			WSLMessage msg = ws.getOperations().get(0).getOutput();
//			OperationIO operation = new OperationIO();
//			IParameter parameter = new ParameterImpl();
//			for (String mr : msg.getModelReferences()) {
//				ISemanticAnnotation sa = new SemanticAnnotationImpl();
//				sa.setReferenceURI(mr);
//				sa.setType(IAnnotationType.META_DATA);
//				parameter.getSemanticAnnotations().add(sa);
//				parameter.setName("");
//			}
//			operation.setParameter(parameter);
//			serviceOutputParams.add(operation);
//			
//			LinkedList<CompatibilityInfo> inputCompatInfo = null; 
//			LinkedList<CompatibilityInfo> outputCompatInfo = null; 
//			
//			//activityInputParams.addAll(activityOutputParams);
//			//serviceInputParams.addAll(serviceOutputParams);
//			
//			//Input
//			//invoke SLO to check semantic compatibility of inputs
//			inputCompatInfo = SLOApi.SLO(activityInputParams, serviceInputParams, SLOAgent.getOntologyKnowledge(), SLOAgent.getSLOEndpoint());
//			
//			//analyze response and create return structure
//			HashMap<OperationIO, Integer> hs = new HashMap<OperationIO, Integer>(); 
//			for (CompatibilityInfo co : inputCompatInfo) {
//				//get highest score for each input
//				if (!(hs.containsKey(co.getInput()) == true && hs.get(co.getInput()) > co.getCompatibilityScore()) ) {
//					hs.put(co.getInput(), co.getCompatibilityScore());
//				}
//			}
//			//compute compatibility score, and report reason of possible incompatibilities
//			Set<OperationIO> keys = hs.keySet();
//			int count = 0;
//			float score = 0;
//			StringBuilder reason = new StringBuilder();
//			for (OperationIO o : keys) {
//				score = ( score * count + hs.get(o) ) / (count + 1); 
//				count ++;
//				
//				if (hs.get(o) != 1) {
//					reason.append("Input \"" + o.getParameter().getName() + "\" is not satisfied, ");
//				}
//			}
//
//			//Output
//			//invoke SLO to check semantic compatibility of outputs
//			outputCompatInfo = SLOApi.SLO(activityOutputParams, serviceOutputParams, SLOAgent.getOntologyKnowledge(), SLOAgent.getSLOEndpoint());
//			//analyze response and create return structure
//			hs = new HashMap<OperationIO, Integer>(); 
//			for (CompatibilityInfo co : outputCompatInfo) {
//				//get highest score for each input
//				if (!(hs.containsKey(co.getInput()) == true && hs.get(co.getInput()) > co.getCompatibilityScore()) ) {
//					hs.put(co.getInput(), co.getCompatibilityScore());
//				}
//			}
//			//compute compatibility score, and report reason of possible incompatibilities
//			keys = hs.keySet();
//			for (OperationIO o : keys) {
//				score = ( score * count + hs.get(o) ) / (count + 1); 
//				count ++;
//				
//				if (hs.get(o) != 1) {
//					reason.append("Output \"" + o.getParameter().getName() + "\" is not satisfied, ");
//				}
//			}
//			
//			BindingCompatibilityDescriptor bcd = new BindingCompatibilityDescriptorImpl(b, reason.toString(), score);
//			
//			bindingCompatibility.add(bcd);
//		}
//		
//		//debug:
//		System.out.println(bindingCompatibility.size());
//		
////		for (BindingCompatibilityDescriptor b : bindingCompatibility) {
////			System.out.println(b);
////		}
//		
//		String result = xstream.toXML(bindingCompatibility);
//		logger
//		.info("DTC method: checkIOSemanticCompatibility: returned value " + result);
//		return result;
//	}
	
	private void cleanup() throws MalformedURLException {
		getBlackBoardControlAgent().cleanup();
	}
	// Axis2 Lifecycle
	@Override
	public void destroy(ServiceContext arg0) {
		// Ignored
	}

	public void exit() throws MalformedURLException {
		// TODO Cleanup
		Scanner keyIn = new Scanner(System.in);
		System.out.print("Press the enter key to exit");
		keyIn.nextLine();

		blackBoardAgent.cleanup();
		System.exit(0);
	}

	@Override
	public String generateDataFlow(String sourceProcessModel)
			throws DTComposerException {
//		throw new DTComposerException(new UnsupportedOperationException(
//				"MethodNotImplementedYet"));
		logger
		.debug("DTC-Invoked generateDataFlow method with parameters sourceProcessModel: "
				+ sourceProcessModel
				);
		System.out.println ("DTC-Invoked resolveProcess method with parameters sourceProcessModel: "
				+ sourceProcessModel);
				return processModel(sourceProcessModel, null,
						ComposerOperations.RESOLVE_DATAFLOW, 1).get(0);

	}

	private BlackBoardControlAgent getBlackBoardControlAgent() {
		return blackBoardAgent;
	}

	public ComposerConfiguration getConfiguration() {
		return configuration;
	}


	@Override
	public void init(ServiceContext sc) throws AxisFault {

		logger.debug("DTC-Invoke DTComposerImpl.init with configuration: dtcomposer-service-configuration.xml");
		try {
			axis2Classloader = sc.getAxisService().getClassLoader();
			init("dtcomposer-service-configuration.xml");

		} catch (Exception e) {
			logger.error ("DTC- Error in Initialization ", e);
		}
	}
	// Axis2 Lifecycle END


//	private void saveAgentWorkingMemory(String agent_URI) {
//		final DesignModificationRuleAgent designModificationAgent = (DesignModificationRuleAgent) composerApplicationContext
//				.getBean(agent_URI);
//		designModificationAgent.saveLogFile();
//	}

	public void init(String springConfigurationFile) throws DTComposerException {
		System.out.println("Starting DTC with configuration file " + springConfigurationFile);
		
		this.springConfigurationFile = springConfigurationFile;
		initConfiguration();
	}

//	@Init
	private void initConfiguration() throws DTComposerException {
		logger.info("DTC-Initializing");
		initializeSpring(springConfigurationFile);
		initializeBlackBoard();
		registerAgents();
		initializeDirectories();
		instance = this;
		logger.info("DTC-Initialized");
	}

	private void initializeBlackBoard() {
		blackBoardAgent = (BlackBoardControlAgent) composerApplicationContext
				.getBean("blackBoardControlAgent");
	}

	private void initializeDirectories() {
		logger.info("DTC-Initializing directories");
		ModelIOUtils.createDir(configuration.getOUTPUT_MODELS_DIR());
		ModelIOUtils.createDir(configuration.getTEMP_DIR());
	}

	private void initializeSpring(String springConfiguration) {
		logger.info("DTC-Initializing Spring with configuration "
				+ springConfiguration);

		Thread.currentThread().setContextClassLoader(getDTCClassLoader());
		composerApplicationContext = new ClassPathXmlApplicationContext(
				springConfiguration);

		configuration = (ComposerConfiguration) composerApplicationContext
				.getBean("composerConfiguration");
		logger.debug("DTC-Initialized Spring configuration");
	}

	private void initializeViewer() {
		viewer = new DesignTimeComposerViewer(blackBoardAgent.getBlackBoard());
	}

//	private boolean isAgentRegistered(String uri) {
//		boolean result = false;
//		for (Agent agent : registeredAgents) {
//			if (agent.getIdentifier().toString().equalsIgnoreCase(uri)) {
//				result = true;
//				break;
//			}
//		}
//		return result;
//	}

	private DesignModel loadDesignModel(String sourceProcessModel)
			throws URISyntaxException {
		IProcess process = ModelIOUtils.loadModelFromXML(sourceProcessModel);
		DesignModel dm = new DesignModel();
		dm.setIdentifier(new URI(process.getID()));
		dm.setDesignStructure(process);
		return dm;
	}

	private void pressAnyKey() {
		Scanner keyIn = new Scanner(System.in);
		System.out.print("Press the enter key to continue");
		keyIn.nextLine();
	}

	// IComposer interface

//	@Override
//	public ModelSolution[] resolveGoalMS(String sourceProcessModel,
//			String activityTargetURI, int numberRequestedSolutions)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveGoalMS method with parameters sourceProcessModel: "
//						+ sourceProcessModel
//						+ ", activityTargetURI: "
//						+ activityTargetURI
//						+ ", numberRequestedSolutions: "
//						+ numberRequestedSolutions);
//		return processModel(sourceProcessModel, activityTargetURI,
//				numberRequestedSolutions, ComposerOperations.RESOLVE_GOAL);
//	}

	private List<String> processModel(String sourceProcessModel,
			String goalTargetURI, ComposerOperations operation, int numberRequestedSolutions)
			throws DTComposerException {
		List<String> result = new ArrayList<String>();
		
		//Checking input model with BPMNValidator
		try {
			new BPMNValidator().validate((IProcess)new XStream().fromXML(sourceProcessModel));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DTComposerException("Invalid received process model", e);
		}
		
		//Processing input model
		ModelSolution[] solutions = processModel(sourceProcessModel,
				goalTargetURI, numberRequestedSolutions, operation);
		
		//Checking returned model wiht BPMNValidator
		try {
			if (solutions.length != 0){
				new BPMNValidator().validate((IProcess)new XStream().fromXML(solutions[0].getModel()));
				for (ModelSolution ms: solutions){
					result.add (ms.getModel());
				}
			}
			
		} catch (BPMNValidationException e) {
			e.printStackTrace();
			throw new DTComposerException("Invalid process model created by Design Time Composer service. Please report to jesus.gorronogoitia@atosresearch.eu", e);
		}
		
		logger.info("DTC: Returned model: " + result);
		logger.info("DTC has sucessfully processed the received model. Processed model has been returned");
		
		return result;
	}

//	@Override
//	public ModelSolution[] resolveGoalWithTemplateMS(String sourceProcessModel,
//			String activityTargetURI, int numberRequestedSolutions)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveGoalWithTemplateMS method with parameters sourceProcessModel: "
//						+ sourceProcessModel
//						+ ", activityTargetURI: "
//						+ activityTargetURI
//						+ ", numberRequestedSolutions: "
//						+ numberRequestedSolutions);
//		return processModel(sourceProcessModel, activityTargetURI,
//				numberRequestedSolutions,
//				ComposerOperations.RESOLVE_GOAL_WITH_TEMPLATE);
//	}

//	@Override
//	public String resolveGoalWithTemplate(String sourceProcessModel,
//			String activityTargetURI) throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveGoalWithTemplate method with parameters sourceProcessModel: "
//						+ sourceProcessModel
//						+ ", activityTargetURI: "
//						+ activityTargetURI);
//		return processModel(sourceProcessModel, activityTargetURI,
//				ComposerOperations.RESOLVE_GOAL_WITH_TEMPLATE);
//	}

//	@Override
//	public ModelSolution[] resolveGoalWithWSMS(String sourceProcessModel,
//			String activityTargetURI, int numberRequestedSolutions)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveGoalWithWSMS method with parameters sourceProcessModel: "
//						+ sourceProcessModel
//						+ ", activityTargetURI: "
//						+ activityTargetURI
//						+ ", numberRequestedSolutions: "
//						+ numberRequestedSolutions);
//		return processModel(sourceProcessModel, activityTargetURI,
//				numberRequestedSolutions,
//				ComposerOperations.RESOLVE_GOAL_WITH_WS);
//	}

	private ModelSolution[] processModel(String sourceProcessModel,
			String goalTargetURI, int numberRequestedSolutions,
			ComposerOperations operation) throws DTComposerException {

		try {
			Collection<ModelSolution> solutions;
			// Viewer for debugging
			if (getConfiguration().isDebug()) {
				initializeViewer();
			}

			// Load model
			AssignmentSet as = new AssignmentSet();
			as.setAssignments(new ArrayList<ISemanticAnnotation>());

			DesignModel initialModel = loadDesignModel(sourceProcessModel);
			initialModel.setAssignmentSet(as);

			getBlackBoardControlAgent().setFindAllSolutions(
					numberRequestedSolutions < 0 ? true : false);
			getBlackBoardControlAgent().setNumberRequestedSolutions(
					numberRequestedSolutions);
			getBlackBoardControlAgent().setGoalTarget(
					goalTargetURI != null ? new URI(goalTargetURI) : null);
			getBlackBoardControlAgent().setOperation(operation);

			// Post Initial model
			getBlackBoardControlAgent().setInitialModel(initialModel);

			// Once the blackboard has been full processed we obtained the
			// solutions
			solutions = getBlackBoardControlAgent().getSolutions();

			// CleaningUp
			if (getConfiguration().isDebug() && !getConfiguration().isService()) {
				pressAnyKey();
			}
			cleanup();

			return solutions.toArray(new ModelSolution[0]);
		} catch (Exception e) {
			logger.error("Error processing activity " + goalTargetURI + " in model " +  sourceProcessModel, e);
			throw new DTComposerException("Error processing model in Design Time Composer", e);
		}
	}

//	@Override
//	public ModelSolution[] resolveProcessMS(String sourceProcessModel,
//			int numberRequestedSolutions) throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveProcessMS method with parameters sourceProcessModel: "
//						+ sourceProcessModel
//						+ ", numberRequestedSolutions: "
//						+ numberRequestedSolutions);
//		return processModel(sourceProcessModel, null, numberRequestedSolutions,
//				ComposerOperations.RESOLVE_PROCESS);
//	}

	private void registerAgent(Agent agent, boolean refreshContext)
			throws DTComposerException {
		logger.debug("DTC-Registering agent " + agent.getIdentifier());
		
		if (agent instanceof ApplicationListener){
			((ClassPathXmlApplicationContext) composerApplicationContext)
				.addApplicationListener((ApplicationListener)agent);
		}
		if (refreshContext){
			((ClassPathXmlApplicationContext) composerApplicationContext).refresh();
		}
		agent.setComposerApplicationContext(composerApplicationContext);
		registeredAgents.add(agent);
		
//		if (agent instanceof DesignModificationAgent){
//			try {
//				agent.registerTerminologiesInDAA ();
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new DTComposerException (e);
//			} 
//		}
	}

//	@Override
//	public ModelSolution[] resolveProcessWithTemplateMS(
//			String sourceProcessModel, int numberRequestedSolutions)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveProcessWithTemplateMS method with parameters sourceProcessModel: "
//						+ sourceProcessModel
//						+ ", numberRequestedSolutions: "
//						+ numberRequestedSolutions);
//		return processModel(sourceProcessModel, null, numberRequestedSolutions,
//				ComposerOperations.RESOLVE_PROCESS_WITH_TEMPLATE);
//	}

//	@Override
//	public String resolveProcessWithTemplate(String sourceProcessModel)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveProcessWithTemplate method with parameters sourceProcessModel: "
//						+ sourceProcessModel);
//		return processModel(sourceProcessModel, null,
//				ComposerOperations.RESOLVE_PROCESS_WITH_TEMPLATE);
//	}

//	@Override
//	public ModelSolution[] resolveProcessWithWSMS(String sourceProcessModel,
//			int numberRequestedSolutions) throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveProcessWithWSMS method with parameters sourceProcessModel: "
//						+ sourceProcessModel
//						+ ", numberRequestedSolutions: "
//						+ numberRequestedSolutions);
//		return processModel(sourceProcessModel, null, numberRequestedSolutions,
//				ComposerOperations.RESOLVE_PROCESS_WITH_WS);
//	}

//	@Override
//	public String resolveProcessWithWS(String sourceProcessModel)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked resolveProcessWithWS method with parameters sourceProcessModel: "
//						+ sourceProcessModel);
//		return processModel(sourceProcessModel, null,
//				ComposerOperations.RESOLVE_PROCESS_WITH_WS);
//	}

	private void registerAgents() throws DTComposerException {
		Map dmas = composerApplicationContext.getBeansOfType(Agent.class);
		Iterator iter = dmas.keySet().iterator();
		while (iter.hasNext()) {
			Agent agent = (Agent) dmas.get(iter.next());
			registerAgent(agent, false);
		}
	}
	
	@Override
	public String resolveActivity(String sourceProcessModel,
			String activityTargetURI) throws DTComposerException {
		logger
				.debug("DTC-Invoked resolveGoal method with parameters sourceProcessModel: "
						+ sourceProcessModel
						+ ", activityTargetURI: "
						+ activityTargetURI);
		return processModel(sourceProcessModel, activityTargetURI,
				ComposerOperations.RESOLVE_GOAL, 1).get(0);
	}

//	public void registerDesignAnalysisAgent(String uri, String knowledgePath)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked registerDesignAnalysisAgent method with parameters uri: "
//						+ uri + ", knowledgePath: " + knowledgePath);
//
//		if (isAgentRegistered(uri)) {
//			System.err.println("Agent already registered. URI: " + uri);
//			throw new DTComposerException(new Exception(
//					"Agent already registered. URI: " + uri));
//		}
//
//		try {
//			// Creates the new Design Modification Agent
//			// TODO: Complete Reasoner specification
//			ReasonerAPI reasoner = null;
//			DesignAnalysisAgent agent = new DesignAnalysisAgent(knowledgePath,
//					getConfiguration().getDSLR_DAA(), reasoner);
//			agent.setComposerApplicationContext(composerApplicationContext);
//			agent.setIdentifier(new URI(uri));
//			// Register the agent and refresh the context to apply that change
//			registerAgent(agent, true);
//		} catch (URISyntaxException e) {
//			logger.error ("Error registering DAA", e);
//			throw new DTComposerException(e);
//		}
//	}

//	public void registerDesignModificationRuleAgent(String uri,
//			String knowledgePath) throws DTComposerException {
//		logger
//				.debug("DTC-Invoked registerDesignModificationRuleAgent method with parameters uri: "
//						+ uri + ", knowledgePath: " + knowledgePath);
//		if (isAgentRegistered(uri)) {
//			logger.error("Agent already registered. URI: " + uri);
//			throw new DTComposerException(new Exception(
//					"Agent already registered. URI: " + uri));
//		}
//
//		// Creates the new Design Modification Agent
//		try {
//			// TODO: Complete Reasoner specification
//			ReasonerAPI reasoner = null;
//			DesignModificationRuleAgent agent = new DesignModificationRuleAgent(
//					knowledgePath, getConfiguration().getDSLR_DMA(), reasoner);
//			agent.setComposerApplicationContext(composerApplicationContext);
//			agent.setIdentifier(new URI(uri));
//			// Register the agent and refresh the context to apply that change
//			registerAgent(agent, true);
//		} catch (Exception e) {
//			logger.error ("Error registering DMA " + uri, e);
//			throw new DTComposerException(e);
//		}
//	}

//	public void registerDesignModificationSemanticAgent(String uri,
//			String[] knowledgeOntologies, String[] knownServices,
//			String[] contextOntologies, String[] goals)
//			throws DTComposerException {
//		logger
//				.debug("DTC-Invoked registerDesignModificationSemanticAgent method with parameters uri: "
//						+ uri
//						+ ", knowledgeOntologies: "
//						+ knowledgeOntologies
//						+ ", knownServices: "
//						+ knownServices
//						+ ", contextOntologies: "
//						+ contextOntologies
//						+ ", knownServices: " + knownServices);
//		if (isAgentRegistered(uri)) {
//			System.err.println("Agent already registered. URI: " + uri);
//			throw new DTComposerException(new Exception(
//					"Agent already registered. URI: " + uri));
//		}
//
//		try {
//			// Creates the new Design Modification Agent
//			DesignModificationSemanticAgent agent = new DesignModificationSemanticAgent(
//					knowledgeOntologies, knownServices, contextOntologies,
//					goals);
//			agent.setIdentifier(new URI(uri));
//			agent.initializeAgent();
//
//			// Register the agent and refresh the context to apply that change
//			registerAgent(agent, true);
//		} catch (Exception e) {
//			logger.error("Error registering DMSA " + uri, e);
//			throw new DTComposerException(e);
//		}
//	}


//	@Override
//	public void registerSemanticLinkOperatorAgent(URI identifier)
//			throws DTComposerException {
//		SemanticLinkOperatorAgent agent = new SemanticLinkOperatorAgent(identifier);
//		registerAgent(agent, true);
//	}

//	@Override
//	public void unregisterDesignAnalysisAgent(String identifier)
//			throws DTComposerException {
//		logger.debug("DTC-Invoked unregisterDesignAnalysisAgent method with parameters identifier: "
//						+ identifier);
//		// Register the agent and refresh the context to apply that change
//		try {
//			unregisterAgent(new URI(identifier), DesignAnalysisAgent.class);
//		} catch (URISyntaxException e) {
//			throw new DTComposerException(e);
//		}
//	}

//	@Override
//	public void unregisterDesignModificationRuleAgent(String identifier)
//			throws DTComposerException {
//		logger.debug("DTC-Invoked unregisterDesignModificationRuleAgent method with parameters identifier: "
//						+ identifier);
//		// Register the agent and refresh the context to apply that change
//		try {
//			unregisterAgent(new URI(identifier),
//					DesignModificationRuleAgent.class);
//		} catch (URISyntaxException e) {
//			throw new DTComposerException(e);
//		}
//	}

//	@Override
//	public void unregisterDesignModificationSemanticAgent(String identifier)
//			throws DTComposerException {
//		logger.debug("DTC-Invoked unregisterDesignModificationSemanticAgent method with parameters identifier: "
//						+ identifier);
//		// Register the agent and refresh the context to apply that change
//		try {
//			unregisterAgent(new URI(identifier),
//					DesignModificationSemanticAgent.class);
//		} catch (URISyntaxException e) {
//			throw new DTComposerException(e);
//		}
//	}

//	@Override
//	public void unregisterSemanticLinkOperatorAgent(String identifier)
//			throws DTComposerException {
//		logger.debug("DTC-Invoked unregisterSemanticLinkOperatorAgent method with parameters identifier: "
//				+ identifier);
//		// Register the agent and refresh the context to apply that change
//		try {
//			unregisterAgent(new URI(identifier),
//					SemanticLinkOperatorAgent.class);
//		} catch (URISyntaxException e) {
//			throw new DTComposerException(e);
//		}
//		
//	}
	
	@Override
	public String resolveProcess(String sourceProcessModel)
			throws DTComposerException {
		logger
				.debug("DTC-Invoked resolveProcess method with parameters sourceProcessModel: "
						+ sourceProcessModel);
		System.out.println ("DTC-Invoked resolveProcess method with parameters sourceProcessModel: "
		+ sourceProcessModel);
		return processModel(sourceProcessModel, null,
				ComposerOperations.RESOLVE_PROCESS, 1).get(0);
	}

	@Override
	public boolean searchForActivityBindings(String processURI, String activityURI)
			throws DTComposerException {
		boolean result = false;
		logger
		.debug("DTC-Invoked searchForActivityBindings method with parameters processURI: "
				+ processURI + " and activityURI: " + activityURI);
		try {
			//Retrieve Global Semantic Annotations for given process
			List<ISemanticAnnotation> gsas = kbProxy.getAnnotations(processURI);
			
			//Retrieve Semantic Annotations for given activity
			List<ISemanticAnnotation> sas = new ArrayList<ISemanticAnnotation>();
			sas.addAll(kbProxy.getAnnotations(activityURI, IAnnotationType.FUNCTIONAL_CLASSIFICATION));
			sas.addAll(kbProxy.getAnnotations(activityURI, IAnnotationType.NON_FUNCTIONAL_PROPERTY));
			
			debugSA (sas, "FC and NFP semantic annotations for activity " + activityURI + ": ");
			
			//List<SemanticAnnotation> sas = KBAPI.getAnnotations (activityURI);
			if (sas.isEmpty()){ //No annotations obtained.
				logger
				.error("No FC annotations found for activityURI: " + activityURI);
				return false;
			}
			
			//Retrieve I/O Semantic Annotations for given activity
			List<ISemanticAnnotation> isas = kbProxy.getInputAnnotations(activityURI);
			List<ISemanticAnnotation> osas = kbProxy.getOutputAnnotations(activityURI);
			if (isas.isEmpty() || osas.isEmpty()){ //No annotations obtained.
				logger
				.error("No I/O annotations found for activityURI: " + activityURI);
				return false;
			}
			
			debugSA (isas, "Input semantic annotations for activity " + activityURI + ": ");
			debugSA (osas, "Output semantic annotations for activity " + activityURI + ": ");
			
			//Build a process model containing that activity
			String model = modelProxy.createProcessWithActivityAnnotations (gsas, sas, isas, osas, activityURI);
			
			//Bind Activity
			//TODO Set numberRequestedSolutions by configuration
			int numberRequestedSolutions = 10;
			List<String> models = bindActivity(model, activityURI, numberRequestedSolutions);
			
			//Retrieve bindings
			List<IBinding> bindings = new ArrayList<IBinding>();
			for (String m:models){
				bindings.addAll(modelProxy.getBindings (m, activityURI));
			}
			
			//Store bindings in KB. Avoid to duplicate bindings
			if (!bindings.isEmpty()){
				result = true;
				kbProxy.bindServices(activityURI, bindings);
			}
		} catch (URISyntaxException e) {
			throw new DTComposerException(e.getMessage());
		}
		//Notify
		return result;
	}

	private void debugSA(List<ISemanticAnnotation> sas,
			String message) {
		StringBuffer sb = new StringBuffer (message);
		sb.append("[");
		for (ISemanticAnnotation sa: sas){
			sb.append(sa.getReferenceURI() + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		
		logger.debug(sb.toString());
	}

//	private void unregisterAgent(URI agentURI, Class type)
//			throws DTComposerException {
//		Iterator beans = ((ClassPathXmlApplicationContext) composerApplicationContext)
//				.getApplicationListeners().iterator();
//		Agent agent = null;
//		while (beans.hasNext()) {
//			Agent a = (Agent) beans.next();
//			if (a.getIdentifier().compareTo(agentURI) == 0) {
//				agent = a;
//				break;
//			}
//		}
//		if (agent == null) {
//			throw new DTComposerException("Agent with URI " + agentURI + " not found");
//		}
//		((ClassPathXmlApplicationContext) composerApplicationContext)
//				.getApplicationListeners().remove(agent);
//		((ClassPathXmlApplicationContext) composerApplicationContext).refresh();
//		registeredAgents.remove(agent);
//	}

}
