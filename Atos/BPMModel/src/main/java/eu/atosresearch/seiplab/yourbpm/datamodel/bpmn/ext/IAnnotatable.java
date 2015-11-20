package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext;

import java.util.Set;

/**
 * An annotatable object has a set of semantic annotations
 * 
 *
 */
public interface IAnnotatable {

	Set<ISemanticAnnotation> getSemanticAnnotations();
	
}
