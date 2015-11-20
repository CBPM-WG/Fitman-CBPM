package eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IActivity;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParameter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IProcess;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.common.utils.cache.QueryCache;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLService;
import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLTemplate;



public class ReasonerAPIImpl implements ReasonerAPI {
	// Logger
	static Logger logger = Logger.getLogger(ReasonerAPIImpl.class);

	protected ReasonerClient reasonerClient;
	private QueryCache cache;
	
	
	
	public ReasonerAPIImpl(QueryCache cache) {
		this.cache = cache;
	}

	public synchronized List<WSLService> matchGoalMSM(IProcess process, IActivity activity, int maxFetched) {
		//get contextual annotations
		ArrayList<String> context = new ArrayList<String>();
		if (process.getSemanticAnnotations() != null) {
			for (ISemanticAnnotation sa : process.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.CONTEXTUAL_INFORMATION) {
					context.add(sa.getReferenceURI());
				}
			}
		}
		
		//TODO analyse if we need to use also Requirements and Constraints

		// get FC from activity
		ArrayList<String> fc = new ArrayList<String>();
		
		// get NFP from activity
		ArrayList<String> nfp = new ArrayList<String>();
		
		HashMap<String, ArrayList<String>> inputMsg = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> outputMsg = new HashMap<String, ArrayList<String>>();
		// HashMap<String, ArrayList<String>> outputMsg = new HashMap<String,
		// ArrayList<String>>();


		for (ISemanticAnnotation sa : activity.getSemanticAnnotations()) {
			if (sa.getType() == IAnnotationType.FUNCTIONAL_CLASSIFICATION) {
				// here we pass only reference URI, instead of whole
				// ISemanticAnnotation object
				fc.add(sa.getReferenceURI());
			}
		}
		
		for (ISemanticAnnotation sa : activity.getSemanticAnnotations()) {
			if (sa.getType() == IAnnotationType.NON_FUNCTIONAL_PROPERTY) {
				// here we pass only reference URI, instead of whole
				// ISemanticAnnotation object
				nfp.add(sa.getReferenceURI());
			}
		}

		for (IParameter par : activity.getInputParameters()) {
			ArrayList<String> inputParam = new ArrayList<String>();
			// be careful, we assume there is only 1 annotation per input.
			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.META_DATA) {
					// here we pass only reference URI, instead of whole
					// ISemanticAnnotation object
					// input.add(sa.getReferenceURI());
					inputParam.add(sa.getReferenceURI());
				}
			}
			inputMsg.put(par.getID(), inputParam);
		}
		
		for (IParameter par : activity.getOutputParameters()) {
			ArrayList<String> outputParam = new ArrayList<String>();
			// be careful, we assume there is only 1 annotation per input.
			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.META_DATA) {
					// here we pass only reference URI, instead of whole
					// ISemanticAnnotation object
					// input.add(sa.getReferenceURI());
					outputParam.add(sa.getReferenceURI());
				}
			}
			outputMsg.put(par.getID(), outputParam);
		}

//		for (IParameter par : activity.getOutputParameters()) {
//			// ArrayList<String> outputParam = new ArrayList<String>();
//			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
//				if (sa.getType() == IAnnotationType.META_DATA) {
//					// here we pass only reference URI, instead of whole
//					// ISemanticAnnotation object
//					// output.add(sa.getReferenceURI());
//					output.add(sa.getReferenceURI());
//				}
//			}
//		}

		// if nothing has been found:
		// ask about S/T using simple matchmaking, ignoring global R/C
		logger.debug("using simple wsmolite matching");
		List<WSLService> result = (List<WSLService>) reasonerClient.doMSMServiceMatch(fc, nfp, inputMsg, outputMsg, context, true, maxFetched);
		logger.debug(result);

		// return whole list of matches

		return result;
	}

	public synchronized List<WSLService> matchGoalMSM_WS(IProcess process, IActivity activity, int maxFetched) {
		//get contextual annotations
		ArrayList<String> context = new ArrayList<String>();
		if (process.getSemanticAnnotations() != null) {
			for (ISemanticAnnotation sa : process.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.CONTEXTUAL_INFORMATION) {
					context.add(sa.getReferenceURI());
				}
			}
		}
		
		//TODO analyse if we need to use also Requirements and Constraints

		// get FC from activity
		ArrayList<String> fc = new ArrayList<String>();
		
		// get NFP from activity
		ArrayList<String> nfp = new ArrayList<String>();
		
		// ArrayList<String> input = new ArrayList<String>();
//		ArrayList<String> output = new ArrayList<String>();

		HashMap<String, ArrayList<String>> inputMsg = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> outputMsg = new HashMap<String, ArrayList<String>>();
		// HashMap<String, ArrayList<String>> outputMsg = new HashMap<String,
		// ArrayList<String>>();

		for (ISemanticAnnotation sa : activity.getSemanticAnnotations()) {
			if (sa.getType() == IAnnotationType.FUNCTIONAL_CLASSIFICATION) {
				// here we pass only reference URI, instead of whole
				// ISemanticAnnotation object
				fc.add(sa.getReferenceURI());
			}
		}
		
		for (ISemanticAnnotation sa : activity.getSemanticAnnotations()) {
			if (sa.getType() == IAnnotationType.NON_FUNCTIONAL_PROPERTY) {
				// here we pass only reference URI, instead of whole
				// ISemanticAnnotation object
				nfp.add(sa.getReferenceURI());
			}
		}

		// Bug fixed: only added parameters if the are semantically annotated.
		// Convention: regardless the number of input parameters and annotations
		// we only add one input parameter (input message) with multiple annotations, where every annotation is understood as a message part
		
		String in = null;
		ArrayList<String> inputParam = new ArrayList<String>();
		for (IParameter par : activity.getInputParameters()) {
			// be careful, we assume there is only 1 annotation per input.
			if (par.getSemanticAnnotations().isEmpty()){
				continue;
			}
			if (in == null){
				in = par.getID(); // We use as input id the first input
			}
			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.META_DATA) {
					// here we pass only reference URI, instead of whole
					// ISemanticAnnotation object
					// input.add(sa.getReferenceURI());
					inputParam.add(sa.getReferenceURI());
				}
			}
		}
		inputMsg.put(in, inputParam);
		
		// Bug fixed: only added parameters if the are semantically annotated.
		// Convention: regardless the number of output parameters and annotations
		// we only add one out parameter (output message) with multiple annotations, where every annotation is understood as a message part
		String out = null;
		ArrayList<String> outputParam = new ArrayList<String>();
		
		for (IParameter par : activity.getOutputParameters()) {
			
			// be careful, we assume there is only 1 annotation per input.
			if (par.getSemanticAnnotations().isEmpty()){
				continue;
			}
			if (out == null){
				out = par.getID(); // We use as output id the first output
			}
			
			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.META_DATA) {
					// here we pass only reference URI, instead of whole
					// ISemanticAnnotation object
					// input.add(sa.getReferenceURI());
					outputParam.add(sa.getReferenceURI());
				}
			}
		}
		outputMsg.put(out, outputParam);

//		for (IParameter par : activity.getOutputParameters()) {
//			// ArrayList<String> outputParam = new ArrayList<String>();
//			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
//				if (sa.getType() == IAnnotationType.META_DATA) {
//					// here we pass only reference URI, instead of whole
//					// ISemanticAnnotation object
//					// output.add(sa.getReferenceURI());
//					output.add(sa.getReferenceURI());
//				}
//			}
//		}

		//if annotation set is empty - skip query
		if (fc.isEmpty() && inputMsg.isEmpty() && outputMsg.isEmpty()) {
			logger.debug("annotation set is empty!");
			return null;
		} else { 
			logger.debug("using simple wsmolite matching (WS)");
			List<WSLService> result = (List<WSLService>) reasonerClient.doMSMServiceMatch(fc, nfp, inputMsg, outputMsg, context, true, maxFetched);
			logger.debug(result);
			return result;
		}
		// if nothing has been found:
		// ask about S/T using simple matchmaking, ignoring global R/C

		// return whole list of matches

		//return result;

	}


	public synchronized List<WSLTemplate> matchGoalMSM_Template(IProcess process, IActivity activity, int maxFetched) {
		//get contextual annotations
		ArrayList<String> context = new ArrayList<String>();
		if (process.getSemanticAnnotations() != null) {
			for (ISemanticAnnotation sa : process.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.CONTEXTUAL_INFORMATION) {
					context.add(sa.getReferenceURI());
				}
			}
		}
		
		//TODO analyse if we need to use also Requirements and Constraints

		//get FC from activity
		ArrayList<String> fc = new ArrayList<String>();
		//get NFP from activity
		ArrayList<String> nfp = new ArrayList<String>();
		
		// ArrayList<String> input = new ArrayList<String>();
//		ArrayList<String> output = new ArrayList<String>();

		HashMap<String, ArrayList<String>> inputMsg = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> outputMsg = new HashMap<String, ArrayList<String>>();
		// HashMap<String, ArrayList<String>> outputMsg = new HashMap<String,
		// ArrayList<String>>();

		for (ISemanticAnnotation sa : activity.getSemanticAnnotations()) {
			if (sa.getType() == IAnnotationType.FUNCTIONAL_CLASSIFICATION) {
				// here we pass only reference URI, instead of whole
				// ISemanticAnnotation object
				fc.add(sa.getReferenceURI());
			}
		}
		
		for (ISemanticAnnotation sa : activity.getSemanticAnnotations()) {
			if (sa.getType() == IAnnotationType.NON_FUNCTIONAL_PROPERTY) {
				// here we pass only reference URI, instead of whole
				// ISemanticAnnotation object
				nfp.add(sa.getReferenceURI());
			}
		}

		// Bug fixed: only added parameters if the are semantically annotated.
		// Convention: regardless the number of input parameters and annotations
		// we only add one input parameter (input message) with multiple annotations, where every annotation is understood as a message part
		String in = null;
		ArrayList<String> inputParam = new ArrayList<String>();
		for (IParameter par : activity.getInputParameters()) {
			// be careful, we assume there is only 1 annotation per input.
			if (par.getSemanticAnnotations().isEmpty()){
				continue;
			}
			if (in == null){
				in = par.getID(); // We use as input id the first input
			}
			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.META_DATA) {
					// here we pass only reference URI, instead of whole
					// ISemanticAnnotation object
					// input.add(sa.getReferenceURI());
					inputParam.add(sa.getReferenceURI());
				}
			}
		}
		inputMsg.put(in, inputParam);
		
		// Bug fixed: only added parameters if the are semantically annotated.
		// Convention: regardless the number of output parameters and annotations
		// we only add one out parameter (output message) with multiple annotations, where every annotation is understood as a message part
		String out = null;
		ArrayList<String> outputParam = new ArrayList<String>();
		for (IParameter par : activity.getOutputParameters()) {
			
			// be careful, we assume there is only 1 annotation per input.
			if (par.getSemanticAnnotations().isEmpty()){
				continue;
			}
			if (out == null){
				out = par.getID(); // We use as output id the first output
			}
			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
				if (sa.getType() == IAnnotationType.META_DATA) {
					// here we pass only reference URI, instead of whole
					// ISemanticAnnotation object
					// input.add(sa.getReferenceURI());
					outputParam.add(sa.getReferenceURI());
				}
			}
			
		}
		outputMsg.put(out, outputParam);

//		for (IParameter par : activity.getOutputParameters()) {
//			// ArrayList<String> outputParam = new ArrayList<String>();
//			for (ISemanticAnnotation sa : par.getSemanticAnnotations()) {
//				if (sa.getType() == IAnnotationType.META_DATA) {
//					// here we pass only reference URI, instead of whole
//					// ISemanticAnnotation object
//					// output.add(sa.getReferenceURI());
//					output.add(sa.getReferenceURI());
//				}
//			}
//		}

		// if nothing has been found:
		// ask about S/T using simple matchmaking, ignoring global R/C
		
		//if annotation set is empty - skip query
		if (fc.isEmpty() && inputMsg.isEmpty() && outputMsg.isEmpty()) {
			logger.debug("annotation set is empty!");
			return null;
		} else { 
			logger.debug("using simple wsmolite matching (PF)");
			// List<WSLTemplate> result = (List<WSLTemplate>)
			// reasonerClient.doMSMServiceMatch(fc, input, output, null, false);
			List<WSLTemplate> result = (List<WSLTemplate>) reasonerClient.doMSMServiceMatch(fc, nfp, inputMsg, outputMsg, context, false, maxFetched);
			logger.debug(result);
			return result;
		}

		// return whole list of matches

		//return result;
	}
	
	public synchronized boolean match(ISemanticAnnotation sa1, ISemanticAnnotation sa2) {
		return reasonerClient.matchClasses(sa1.getReferenceURI(), sa2.getReferenceURI());
	}

//	public synchronized boolean matchOperationInstances(String op1, String op2) {
//		return reasonerClient.matchOperationInstances(op1, op2);
//	}

	public synchronized boolean matchClasses(String[] c1s, String[] c2s) {
		return reasonerClient.matchClasses(c1s, c2s);
	}
	
	
	public void cleanUp() {
		cache.cleanCache();
	}
	
}
