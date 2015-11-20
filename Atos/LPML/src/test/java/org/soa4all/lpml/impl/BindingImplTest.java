package org.soa4all.lpml.impl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.soa4all.lpml.TestUtil.deepCopy;

import java.net.URI;
import java.net.URL;

import org.junit.Test;

public class BindingImplTest {
	
	final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		IBinding e1 = new BindingImpl();
		IBinding e2 = new BindingImpl();

		// then
		assertCommonProperties(e1);
		assertFalse(e1.getID().equals(e2.getID()));
	}
	
	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		IBinding e = new BindingImpl(ID);
		
		// then
		assertCommonProperties(e);
		assertSame(ID, e.getID());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		IBinding e = new BindingImpl(null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		IBinding e = new BindingImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		IBinding e = new BindingImpl(ID);
		
		// then
		assertEquals(e,e);
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		IBinding e = new BindingImpl(ID);
		
		// then
		assertFalse(e.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		IBinding e1 = new BindingImpl(ID);
		IBinding e2 = new BindingImpl(ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		IBinding e1 = new BindingImpl(ID);
		IBinding e2 = new BindingImpl(ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentServiceReference() throws Exception{
		
		// given
		IBinding e1 = new BindingImpl(ID);
		IBinding e2 = new BindingImpl(ID);
		
		e1.setServiceReference(new URL("http://www.sap.com"));
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentOperation() throws Exception{
		
		// given
		IBinding e1 = new BindingImpl(ID);
		IBinding e2 = new BindingImpl(ID);
		
		e1.setOperation(new URI("http://www.sap.com"));
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentName() throws Exception{
		
		// given
		IBinding e1 = new BindingImpl(ID);
		IBinding e2 = new BindingImpl(ID);
		
		e1.setName("some name");
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	

	@Test
	public void testSerialization(){
		
		// given
		IBinding e = new BindingImpl(ID);
		
		// when
		IBinding deserialized = deepCopy(e);
		
		// then
		assertEquals(e, deserialized);
	}
	
	
	private void assertCommonProperties(IBinding e1) {
		
		assertNotNull(e1.getID());
		assertNull(e1.getName());
		assertNull(e1.getServiceReference());
		assertNull(e1.getOperation());
		
	}

}
