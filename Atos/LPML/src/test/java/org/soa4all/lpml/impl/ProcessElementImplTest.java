/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.soa4all.lpml.ProcessElement;

public class ProcessElementImplTest {

	final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		ProcessElement e1 = (ProcessElement) new ActivityImpl();
		ProcessElement e2 = (ProcessElement) new ActivityImpl();

		// then
		assertNotNull(e1.getID());
		assertNotNull(e2.getID());
		assertFalse(e1.getID().equals(e2.getID()));
		assertEquals(0, e1.getSemanticAnnotations().size());
		assertEquals(0, e2.getSemanticAnnotations().size());
	}
	

	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		ProcessElement e = (ProcessElement) new ActivityImpl(ID);
		
		// then
		assertSame(ID, e.getID());
		assertEquals(0, e.getSemanticAnnotations().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		ProcessElement e = (ProcessElement) new ActivityImpl(null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		ProcessElement e = (ProcessElement) new ActivityImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		ProcessElement e = (ProcessElement) new ActivityImpl(ID);
		
		// then
		assertEquals(e,e);
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		ProcessElement e = (ProcessElement) new ActivityImpl(ID);
		
		// then
		assertFalse(e.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		ProcessElement e1 = (ProcessElement) new ActivityImpl(ID);
		ProcessElement e2 = (ProcessElement) new ActivityImpl(ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		ProcessElement e1 = (ProcessElement) new ActivityImpl(ID);
		ProcessElement e2 = (ProcessElement) new ActivityImpl(ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithSameIdsAndDifferentAnnotations(){
		
		// given
		ProcessElement e1 = (ProcessElement) new ActivityImpl(ID);
		ProcessElement e2 = (ProcessElement) new ActivityImpl(ID);
		
		e1.getSemanticAnnotations().add(new SemanticAnnotationImpl());
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithSameIdsAndSameAnnotations(){
		
		// given
		ProcessElement e1 = (ProcessElement) new ActivityImpl(ID);
		ProcessElement e2 = (ProcessElement) new ActivityImpl(ID);
		
		ISemanticAnnotation a = new SemanticAnnotationImpl();
		e1.getSemanticAnnotations().add(a);
		e2.getSemanticAnnotations().add(a);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
	}
	
} 
