/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.soa4all.lpml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.soa4all.lpml.TestUtil.deepCopy;

import org.junit.Test;
import org.soa4all.lpml.Activity;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;



public class ProcessImplTest {


	final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		ProcessImpl p1 = new ProcessImpl();
		ProcessImpl p2 = new ProcessImpl();

		// then
		assertCommonProperties(p1);
		assertFalse(p1.getID().equals(p2.getID()));
	}

	
	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		ProcessImpl p = new ProcessImpl(ID);
		
		// then
		assertCommonProperties(p);
		assertSame(ID, p.getID());
	}
	
	@Test
	public void testHashCode(){
		
		// given
		ProcessImpl p = new ProcessImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), p.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		ProcessImpl p = new ProcessImpl(ID);
		
		// then
		assertEquals(p,p);
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		ProcessImpl p = new ProcessImpl(ID);
		
		// then
		assertFalse(p.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		ProcessImpl p1 = new ProcessImpl(ID);
		ProcessImpl p2 = new ProcessImpl(ID);
		
		// then
		assertEquals(p1,p2);
		assertEquals(p2,p1);
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		ProcessImpl p1 = new ProcessImpl(ID);
		ProcessImpl p2 = new ProcessImpl(ID + "sdf");
		
		// then
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
	}
	
	@Test
	public void testEqualsWithSameIdsAndDifferentAnnotations(){
		
		// given
		ProcessImpl p1 = new ProcessImpl(ID);
		ProcessImpl p2 = new ProcessImpl(ID);
		
		p1.getSemanticAnnotations().add(new SemanticAnnotationImpl());
		
		
		// then
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
	}
	
	@Test
	public void testEqualsWithSameIdsAndSameAnnotations(){
		
		// given
		ProcessImpl p1 = new ProcessImpl(ID);
		ProcessImpl p2 = new ProcessImpl(ID);
		
		ISemanticAnnotation a = new SemanticAnnotationImpl();
		p1.getSemanticAnnotations().add(a);
		p2.getSemanticAnnotations().add(a);
		
		// then
		assertEquals(p1,p2);
		assertEquals(p2,p1);
	}
	
	
	@Test
	public void testEqualsWithSameIdsAndDifferentFlows(){
		
		// given
		ProcessImpl p1 = new ProcessImpl(ID);
		ProcessImpl p2 = new ProcessImpl(ID);
		
		p1.getFlows().add(new FlowImpl());
		
		
		// then
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
	}
	
	
	@Test
	public void testEqualsWithSameIdsAndDifferentElements(){
		
		// given
		ProcessImpl p1 = new ProcessImpl(ID);
		ProcessImpl p2 = new ProcessImpl(ID);
		
		p1.getProcessElements().add(new ActivityImpl());
		
		
		// then
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
	}
	
	@Test
	public void testSerialization(){
		
		// given
		ProcessImpl e = new ProcessImpl(ID);
		
		// when
		ProcessImpl deserialized = deepCopy(e);
		
		// then
		assertEquals(e, deserialized);
	}
	

	@Test
	public void testGetStartElementFromEmptyProcess(){
		
		//given
		ProcessImpl process = new ProcessImpl(ID);
		
		//then
		assertNull(process.getStartElement());
		
	}
	
	@Test
	public void testGetExistingStartElement(){
		
		//given
		ProcessImpl process = new ProcessImpl(ID);
		ActivityImpl startElement = new ActivityImpl();
		startElement.setStartElement(true);
		
		process.getProcessElements().add(startElement);
		
		//when
		Activity returned = (Activity) process.getStartElement();
		
		//then
		assertSame(startElement, returned);
		
	}
	
	@Test
	public void testGetStartElementWhenMoreThanOneArePresent(){
		
		//given
		ProcessImpl process = new ProcessImpl(ID);
		
		ActivityImpl startElement1 = new ActivityImpl();
		startElement1.setStartElement(true);
		process.getProcessElements().add(startElement1);
		
		ActivityImpl startElement2 = new ActivityImpl();
		startElement2.setStartElement(true);
		process.getProcessElements().add(startElement2);
		
		//when
		Activity returned = (Activity) process.getStartElement();
		
		//then
		assertTrue(returned == startElement1 || returned == startElement2);
		
	}
	
	@Test
	public void testGetEndElementFromEmptyProcess(){
		
		//given
		ProcessImpl process = new ProcessImpl(ID);
		
		//then
		assertNull(process.getEndElement());
		
	}
	
	@Test
	public void testGetExistingEndElement(){
		
		//given
		ProcessImpl process = new ProcessImpl(ID);
		ActivityImpl element = new ActivityImpl();
		element.setEndElement(true);
		
		process.getProcessElements().add(element);
		
		//when
		Activity returned = (Activity) process.getEndElement();
		
		//then
		assertSame(element, returned);
		
	}
	
	@Test
	public void testGetEndElementWhenMoreThanOneArePresent(){
		
		//given
		ProcessImpl process = new ProcessImpl(ID);
		
		ActivityImpl element1 = new ActivityImpl();
		element1.setEndElement(true);
		process.getProcessElements().add(element1);
		
		ActivityImpl element2 = new ActivityImpl();
		element2.setEndElement(true);
		process.getProcessElements().add(element2);
		
		//when
		Activity returned = (Activity) process.getEndElement();
		
		//then
		assertTrue(returned == element1 || returned == element2);
		
	}
	
	private void assertCommonProperties(ProcessImpl p1) {
		
		assertNotNull(p1.getID());
		
		assertEquals(0, p1.getSemanticAnnotations().size());
		assertEquals(0, p1.getProcessElements().size());
		assertEquals(0, p1.getFlows().size());
		
	}
	
}
