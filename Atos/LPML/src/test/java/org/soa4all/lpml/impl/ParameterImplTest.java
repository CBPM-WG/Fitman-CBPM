package org.soa4all.lpml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.soa4all.lpml.TestUtil.deepCopy;

import org.junit.Test;
import org.soa4all.lpml.Parameter;

public class ParameterImplTest {
	
	final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		Parameter e1 = (Parameter) new ParameterImpl();
		Parameter e2 = (Parameter) new ParameterImpl();

		// then
		assertCommonProperties(e1);
		assertFalse(e1.getID().equals(e2.getID()));
	}
	
	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		Parameter e = (Parameter) new ParameterImpl(ID);
		
		// then
		assertCommonProperties(e);
		assertSame(ID, e.getID());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		Parameter e = (Parameter) new ParameterImpl(null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		Parameter e = (Parameter) new ParameterImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		Parameter e = (Parameter) new ParameterImpl();
		
		// then
		assertEquals(e,e);
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		Parameter e = (Parameter) new ParameterImpl(ID);
		
		// then
		assertFalse(e.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		Parameter e1 = (Parameter) new ParameterImpl(ID);
		Parameter e2 = (Parameter) new ParameterImpl(ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		Parameter e1 = (Parameter) new ParameterImpl(ID);
		Parameter e2 = (Parameter) new ParameterImpl(ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentName() throws Exception{
		
		// given
		Parameter e1 = (Parameter) new ParameterImpl(ID);
		Parameter e2 = (Parameter) new ParameterImpl(ID);
		
		e1.setName("some name");
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentAnnotations() throws Exception{
		
		// given
		Parameter e1 = (Parameter) new ParameterImpl(ID);
		Parameter e2 = (Parameter) new ParameterImpl(ID);
		
		e1.getSemanticAnnotations().add(new SemanticAnnotationImpl());
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentValue() throws Exception{
		
		// given
		Parameter e1 = (Parameter) new ParameterImpl(ID);
		Parameter e2 = (Parameter) new ParameterImpl(ID);
		
		e1.setValue("some value");
		
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
		assertEquals(e1.hashCode(), e2.hashCode());
		assertEquals(e2.hashCode(), e1.hashCode());
	}
	
	@Test
	public void testValue() throws Exception{
		
		// given
		Parameter e1 = (Parameter) new ParameterImpl(ID);
		final String VAL = "sdslkdfjal;ds";
		
		//when
		e1.setValue(VAL);
		
		// then
		assertEquals(VAL,e1.getValue());
	}
	
	@Test
	public void testSerialization(){
		
		// given
		Parameter e = (Parameter) new ParameterImpl(ID);
		
		// when
		Parameter deserialized = deepCopy(e);
		
		// then
		assertEquals(e, deserialized);
	}
	
	private void assertCommonProperties(Parameter e1) {
		
		assertNotNull(e1.getID());
		
		assertNull(e1.getName());
		assertEquals(0,e1.getSemanticAnnotations().size());
		assertNull(e1.getValue());
	}

}
