package org.soa4all.lpml.impl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.soa4all.lpml.TestUtil.deepCopy;

import org.junit.Test;


public class SemanticAnnotationImplTest {
	
final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		ISemanticAnnotation e1 = new SemanticAnnotationImpl();
		ISemanticAnnotation e2 = new SemanticAnnotationImpl();

		// then
		assertCommonProperties(e1);
		assertFalse(e1.getID().equals(e2.getID()));
	}
	
	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		ISemanticAnnotation e = new SemanticAnnotationImpl(ID);
		
		// then
		assertCommonProperties(e);
		assertSame(ID, e.getID());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		ISemanticAnnotation e1 = new SemanticAnnotationImpl(null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		ISemanticAnnotation e = new SemanticAnnotationImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		ISemanticAnnotation e = new SemanticAnnotationImpl(ID);
		
		// then
		assertEquals(e,e);
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		ISemanticAnnotation e = new SemanticAnnotationImpl(ID);
		
		// then
		assertFalse(e.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		ISemanticAnnotation e1 = new SemanticAnnotationImpl(ID);
		ISemanticAnnotation e2 = new SemanticAnnotationImpl(ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		ISemanticAnnotation e1 = new SemanticAnnotationImpl(ID);
		ISemanticAnnotation e2 = new SemanticAnnotationImpl(ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentType() throws Exception{
		
		// given
		ISemanticAnnotation e1 = new SemanticAnnotationImpl(ID);
		ISemanticAnnotation e2 = new SemanticAnnotationImpl(ID);
		
		e1.setType(IAnnotationType.CONTEXTUAL_INFORMATION);
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
		
	@Test
	public void testEqualsWithDifferentReferenceURI() throws Exception{
		
		// given
		ISemanticAnnotation e1 = new SemanticAnnotationImpl(ID);
		ISemanticAnnotation e2 = new SemanticAnnotationImpl(ID);
		
		e1.setReferenceURI("http://www.genevski.com");
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	
	@Test
	public void testEqualsWithDifferentExpression() throws Exception{
		
		// given
		ISemanticAnnotation e1 = new SemanticAnnotationImpl(ID);
		ISemanticAnnotation e2 = new SemanticAnnotationImpl(ID);
		
		e1.setExpression("Genevski");
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testSerialization(){
		
		// given
		ISemanticAnnotation e = new SemanticAnnotationImpl(ID);
		
		// when
		ISemanticAnnotation deserialized = deepCopy(e);
		
		// then
		assertEquals(e, deserialized);
	}
	
	private void assertCommonProperties(ISemanticAnnotation e1) {
		
		assertNotNull(e1.getID());
		
		assertEquals(IAnnotationType.META_DATA, e1.getType());
		assertNull(e1.getReferenceURI());
		assertNull(e1.getExpression());
	}
}
