/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext;


/**
 * TODO @Yosu - set the appropriate values for the displayable property
 * [Yosu] Displayable elements:
 * 	FC at activity
 *  I/O metadata at I/O Parameter of activities
 *  Requirements, preferences at activity
 *  Requirements, constraints, preferences at process
 * TODO @Yosu - define the meaning and possible use cases for the annotation types
 * given below
 */
public enum IAnnotationType  {

	FUNCTIONAL_CLASSIFICATION(0, "functionalClassification", "functionalClassification", true),
	
	NON_FUNCTIONAL_PROPERTY(1, "nonFunctionalProperty", "nonFunctionalProperty", false), //NFP are overloaded by requirements, constraints and preferences

	PRECONDITION(2, "precondition", "precondition", false), //Yosu: preconditions and effects are not used by WP6 tools

	EFFECT(3, "effect", "effect", false),

	META_DATA(4, "metaData", "metaData", true), //Yosu: metadata is used to describe I/O only

	REQUIREMENT(5, "requirement", "requirement", true), //Yosu: requirements are used at process and activity level

	CONSTRAINT(6, "constraint", "constraint", true), //Yosu: constraints are used at process level

	SELECTION_CRITERIA(7, "selectionCriteria", "selectionCriteria", false),

	REPLACEMENT_CONDITION(8, "replacementCondition", "replacementCondition", false),

	CONTEXTUAL_INFORMATION(9, "contextualAnnotation", "contextualAnnotation",false),
	
	PREFERENCE(10, "preference", "preference", true); //Yosu: preferences are used at process and activity level

	
	public static IAnnotationType getByLiteral(String literal) {
		
		for(IAnnotationType type:IAnnotationType.values()){
			if(type.getLiteral().equals(literal)){
				return type;
			}
		}
		return null;
	}

	
	public static IAnnotationType getByName(String name) {
		
		for(IAnnotationType type:IAnnotationType.values()){
			if(type.getName().equals(name)){
				return type;
			}
		}
		
		return null;
		
	}

	public static IAnnotationType getByValue(int value) {
		
		for(IAnnotationType type:IAnnotationType.values()){
			if(type.getValue() == value){
				return type;
			}
		}
		
		return null;
	}

	private final int value;
	private final String name;
	private final String literal;
	private boolean displayed;

	private IAnnotationType(int value, String name, String literal, boolean displayed) {
		this.value = value;
		this.name = name;
		this.literal = literal;
		this.displayed = displayed;
	}


	public int getValue() {
	  return value;
	}
	
	public String getName() {
	  return name;
	}

	public String getLiteral() {
	  return literal;
	}
	
	public boolean isDisplayed(){
		return this.displayed;
	}

	@Override
	public String toString() {
		return literal;
	}
}
