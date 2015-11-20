package org.soa4all.lpml;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import static junit.framework.Assert.*;

import org.junit.Test;


public class AnnotationTypeTest {
	
	@Test
	public void testNumberOfValues(){
		assertEquals(11, IAnnotationType.values().length);
	}
	
	@Test
	public void testValues(){
		
		for(IAnnotationType type:IAnnotationType.values()){
			assertNotNull(type.getLiteral());
			assertNotNull(type.getName());
		}
		
	}

}
