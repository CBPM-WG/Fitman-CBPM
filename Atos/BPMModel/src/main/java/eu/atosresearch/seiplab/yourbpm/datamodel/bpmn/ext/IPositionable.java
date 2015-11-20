package eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext;

/**
 * Objects of this type can be positioned in the 3D 
 * space. The coordinate system and unit of measurement
 * is not specified here. The most obvious system for 
 * process editing is a Cartesian coordinate system,
 * where the z index specifies which element is on top 
 * of another one.  
 *
 */
public interface IPositionable {

	float getX();
	void setX(float position);
	
	float getY();
	void setY(float position);
	
	float getZ();
	void setZ(float position);
}
