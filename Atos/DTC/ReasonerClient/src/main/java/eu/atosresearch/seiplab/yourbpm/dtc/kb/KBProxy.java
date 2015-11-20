package eu.atosresearch.seiplab.yourbpm.dtc.kb;


import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;

import java.util.List;
import java.util.Set;

import org.openrdf.query.BindingSet;
//import org.soa4all.lpml.AnnotationType;
//import org.soa4all.lpml.Binding;
//import org.soa4all.lpml.ISemanticAnnotation;

public interface KBProxy {
    List<ISemanticAnnotation> getAnnotations (String processElementURI);
	List<ISemanticAnnotation> getInputAnnotations (String activityURI);
	List<ISemanticAnnotation> getOutputAnnotations (String activityURI);
	List<ISemanticAnnotation> getAnnotations(String processElementURI, IAnnotationType type);
	void bindServices (String activityURI, List<IBinding> bindings);
	void removeActivityAnnotations(String activityURI);
	void addActivityAnnotations(String activityURI, List<ISemanticAnnotation> ans);
	void addActivityInputAnnotations(String activityURI, List<ISemanticAnnotation> ans);
	void addActivityOutputAnnotations(String activityURI, List<ISemanticAnnotation> ans);
	public String query (String query);
}
