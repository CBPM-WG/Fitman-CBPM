package eu.atosresearch.seiplab.yourbpm.dtc.kb;


import java.util.ArrayList;
import java.util.List;

import org.soa4all.lpml.impl.SemanticAnnotationImpl;

import eu.atosresearch.seiplab.kb.businesslogic.AnnotationHandler;
import eu.atosresearch.seiplab.kb.types.Service;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;

public class KBProxyImpl implements KBProxy {
	private AnnotationHandler ah = new AnnotationHandler();
	private static KBProxy proxy = new KBProxyImpl();
	
	public static KBProxy getInstance(){
		return proxy;
	}
	
	public List<ISemanticAnnotation> getAnnotations(String processElementURI) {
		return getAnnotations(processElementURI, null);
	}
	
	public List<ISemanticAnnotation> getAnnotations(String processElementURI, IAnnotationType type) {
		List<ISemanticAnnotation> sas = new ArrayList<ISemanticAnnotation> ();
		String[] annotations = ah.retrieveAnnotationOf(processElementURI, getName (type));
		for (String an:annotations){
			ISemanticAnnotation sa = new SemanticAnnotationImpl();
			sa.setType(type);
			sa.setReferenceURI(an);
			sas.add (sa);
		}
		return sas;
	}

	private String getName(IAnnotationType type) {
		String name = "ALL";
		if (type == null){
			return name;
		}
		switch (type){
		case FUNCTIONAL_CLASSIFICATION:
			name = "FUNTIONAL_CLASSIFICATION";
			break;
		case REQUIREMENT:
			name = "REQUIREMENT";
			break;
		case PREFERENCE:
			name = "REFERENCE";
			break;
		case CONTEXTUAL_INFORMATION:
			name = "CONTEXTUAL_INFORMATION";
			break;
		case NON_FUNCTIONAL_PROPERTY:
			name = "NON_FUNCTIONAL_PROPERTY";
			break;
		case META_DATA:
			name = "METADATA";
			break;
		}
		return name;
	}

	@Override
	public List<ISemanticAnnotation> getInputAnnotations(String activityURI) {
		return getIOAnnotations(activityURI, "INPUT_METADATA");
	}

	@Override
	public List<ISemanticAnnotation> getOutputAnnotations(String activityURI) {
		return getIOAnnotations(activityURI, "OUTPUT_METADATA");
	}
	
	private List<ISemanticAnnotation> getIOAnnotations(String activityURI, String type) {
		List<ISemanticAnnotation> sas = new ArrayList<ISemanticAnnotation> ();
		String[] annotations = ah.retrieveAnnotationOf(activityURI, type);
		for (String an:annotations){
			ISemanticAnnotation sa = new SemanticAnnotationImpl();
			sa.setType(IAnnotationType.META_DATA);
			sa.setReferenceURI(an);
			sas.add (sa);
		}
		return sas;
	}

	@Override
	public void bindServices(String activityURI, List<IBinding> bindings) {
		removeExistingBindings (activityURI);
		for (IBinding binding: bindings){
			ah.addService("TASK", activityURI, binding.getServiceReference().toString(), binding.getOperation().toString());
		}
		
	}

	private void removeExistingBindings(String activityURI) {
		//Retrieving existing bindings
		List<Service> services = ah.retrieveServicesOf(activityURI);
		int index;
		for (Service s: services){
			ah.deleteService(activityURI, s.getURI(), s.getOperation());
		}
	}

	private void filterExistingBindings(String activityURI,
			List<IBinding> bindings) {
		//Retrieving existing bindings
		List<Service> services = ah.retrieveServicesOf(activityURI);
		int index;
		for (Service s: services){
			if ((index = duplicateBinding (s, bindings)) != -1){
				bindings.remove(index);
			}
		}
	}

	private int duplicateBinding(Service service, List<IBinding> bindings) {
		int index = -1;
		int i = 0;
		for (IBinding b: bindings){
			if (service.getURI().equalsIgnoreCase(b.getServiceReference().toString()) &&
				service.getOperation().equalsIgnoreCase(b.getOperation().toString())	
			){
				index = i;
				break;
			}
			i++;
		}
		return index;
	}

	@Override
	public void removeActivityAnnotations(String activityURI) {
		remoteAnnotation(activityURI, "FUNTIONAL_CLASSIFICATION");
		remoteAnnotation(activityURI, "NON_FUNCTIONAL_PROPERTY");
		remoteAnnotation(activityURI, "INPUT_METADATA");
		remoteAnnotation(activityURI, "OUTPUT_METADATA");
	}

	private void remoteAnnotation(String activityURI, String type) {
		String[] ans = ah.retrieveAnnotationOf(activityURI, type);
		for (String an: ans){
			ah.deleteAnnotation(activityURI, type, an);
		}
	}

	@Override
	public void addActivityAnnotations(String activityURI,
			List<ISemanticAnnotation> ans) {
		for (ISemanticAnnotation an:ans){
			IAnnotationType type = an.getType();
			switch (type){
			case FUNCTIONAL_CLASSIFICATION:
				ah.addFuntionalClassification(activityURI, an.getReferenceURI());
				break;
			case NON_FUNCTIONAL_PROPERTY:
				ah.addNonFunctionalProperty(activityURI, an.getReferenceURI());
				break;
			}
		}
	}
	
	public void addActivityInputAnnotations(String activityURI, List<ISemanticAnnotation> ans) {
		for (ISemanticAnnotation an:ans){
			ah.addInputMetadata(activityURI, an.getReferenceURI());
		}
	}
	
	public void addActivityOutputAnnotations(String activityURI, List<ISemanticAnnotation> ans) {
		for (ISemanticAnnotation an:ans){
			ah.addOutputMetadata(activityURI, an.getReferenceURI());
		}
	}
	
	public String query (String query){
		return ah.executeSPARQL(query).toString();
	}

}
