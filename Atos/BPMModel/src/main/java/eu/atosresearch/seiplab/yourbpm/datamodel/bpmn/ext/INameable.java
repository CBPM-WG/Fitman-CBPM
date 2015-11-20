package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext;

/**
 * Represents something that has a name. Usually names have meaning
 * for humans. For example "Check credit history" could be a name of an
 * activity that is displayed in tools, like process editors and let
 *  the user refer verbally to the activity 
 */
public interface INameable {

	String getName();

	void setName(String value);
	
}
