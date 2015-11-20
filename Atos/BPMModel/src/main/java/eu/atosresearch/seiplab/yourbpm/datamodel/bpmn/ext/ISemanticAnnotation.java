/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext;


/**
 * TODO @Yosu define the meaning of all the parameters below
 *
 */
public interface ISemanticAnnotation extends IIdentifiable {

	String getReferenceURI();
	void setReferenceURI(String value);

	String getExpression();
	void setExpression(String value);

	void setType(IAnnotationType type);
	IAnnotationType getType();

}
