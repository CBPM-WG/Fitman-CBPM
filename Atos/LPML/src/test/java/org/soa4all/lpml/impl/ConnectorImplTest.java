package org.soa4all.lpml.impl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IConnector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.soa4all.lpml.TestUtil.deepCopy;

import org.junit.Test;

public class ConnectorImplTest {
	
final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		IConnector e1 = new ConnectorImpl();
		IConnector e2 = new ConnectorImpl();

		// then
		assertCommonProperties(e1);

		assertFalse(e1.getID().equals(e2.getID()));
	}
	

	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		IConnector e1 = new ConnectorImpl(ID);
		
		// then
		assertCommonProperties(e1);		
		assertSame(ID, e1.getID());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		IConnector e1 = new ConnectorImpl(null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		IConnector e = new ConnectorImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		IConnector e = new ConnectorImpl(ID);
		
		// then
		assertEquals(e,e);
		assertEquals(e.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		IConnector e = new ConnectorImpl(ID);
		
		// then
		assertFalse(e.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		IConnector e1 = new ConnectorImpl(ID);
		IConnector e2 = new ConnectorImpl(ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
		assertEquals(e1.hashCode(), e2.hashCode());
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		IConnector e1 = new ConnectorImpl(ID);
		IConnector e2 = new ConnectorImpl(ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentOutputParam(){
		
		// given
		IConnector e1 = new ConnectorImpl(ID);
		IConnector e2 = new ConnectorImpl(ID);
		
		e1.setOutputParameter(new ParameterImpl());
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentSemanticMapping(){
		
		// given
		IConnector e1 = new ConnectorImpl(ID);
		IConnector e2 = new ConnectorImpl(ID);
		
		e1.setSemanticMapping("sdfs");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	
	@Test
	public void testSerialization(){
		
		// given
		IConnector e = new ConnectorImpl(ID);
		
		// when
		IConnector deserialized = deepCopy(e);
		
		// then
		assertEquals(e, deserialized);
	}
	
	
	private void assertCommonProperties(IConnector connector) {
		
		assertNotNull(connector.getID());
		assertNull(connector.getOutputParameter());
		assertNull(connector.getSemanticMapping());
		
		assertEquals(0, connector.getInputParameters().size());
	}

}
