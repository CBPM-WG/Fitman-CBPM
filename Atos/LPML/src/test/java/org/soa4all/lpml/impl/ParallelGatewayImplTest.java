package org.soa4all.lpml.impl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.IParallelGateway;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.soa4all.lpml.TestUtil.deepCopy;

import org.junit.Test;
import org.soa4all.lpml.ParallelGateway;

public class ParallelGatewayImplTest {
	
	final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		ParallelGatewayImpl e1 = new ParallelGatewayImpl(true);
		ParallelGatewayImpl e2 = new ParallelGatewayImpl(true);

		// then
		assertCommonProperties(e1);

		assertFalse(e1.getID().equals(e2.getID()));
		assertTrue(e1.isSplit());
		assertTrue(e2.isSplit());
		
	}
	

	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		ParallelGatewayImpl e = new ParallelGatewayImpl(true, ID);
		
		// then
		assertSame(ID, e.getID());
		assertCommonProperties(e);
		assertTrue(e.isSplit());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		ParallelGatewayImpl e = new ParallelGatewayImpl(true, null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		ParallelGatewayImpl e = new ParallelGatewayImpl(true, ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		ParallelGatewayImpl e = new ParallelGatewayImpl(true, ID);
		
		// then
		assertEquals(e,e);
		assertEquals(e.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		ParallelGatewayImpl e = new ParallelGatewayImpl(true, ID);
		
		// then
		assertFalse(e.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		ParallelGatewayImpl e1 = new ParallelGatewayImpl(true, ID);
		ParallelGatewayImpl e2 = new ParallelGatewayImpl(true, ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
		assertEquals(e1.hashCode(), e2.hashCode());
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		ParallelGatewayImpl e1 = new ParallelGatewayImpl(true, ID);
		ParallelGatewayImpl e2 = new ParallelGatewayImpl(true, ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	
	
	@Test
	public void testEqualsWithDifferentSplitProperty(){
		
		// given
		ParallelGatewayImpl e1 = new ParallelGatewayImpl(true, ID);
		ParallelGatewayImpl e2 = new ParallelGatewayImpl(false, ID);
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentAnnotations(){
		
		// given
		ParallelGatewayImpl e1 = new ParallelGatewayImpl(true, ID);
		ParallelGatewayImpl e2 = new ParallelGatewayImpl(true, ID);
		
		e1.getSemanticAnnotations().add(new SemanticAnnotationImpl());
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	
	@Test
	public void testEqualsWithDifferentPosition(){
		
		// given
		ParallelGatewayImpl e1 = new ParallelGatewayImpl(true, ID);
		ParallelGatewayImpl e2 = new ParallelGatewayImpl(true, ID);
		
		e1.setX(1);
		e1.setY(342);
		e1.setZ(56);
		
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
	}

	@Test
	public void testPositionableProperties(){
		
		// given
		ParallelGatewayImpl g = new ParallelGatewayImpl(true, ID);
		
		// when
		g.setX(1);
		g.setY(342);
		g.setZ(56);
		
		// then
		assertEquals(1, g.getX(), 0);
		assertEquals(342, g.getY(), 0);
		assertEquals(56, g.getZ(), 0);
	}
	
	@Test
	public void testSerialization(){
		
		// given
		ParallelGateway e = (ParallelGateway) new ParallelGatewayImpl(true, ID);
		
		// when
		ParallelGateway  deserialized = deepCopy(e);
		
		// then
		assertEquals(e, deserialized);
	}
	
	
	
	private void assertCommonProperties(IParallelGateway gateway) {
		
		assertNotNull(gateway.getID());
		assertNull(gateway.getName());
		assertEquals(0, gateway.getSemanticAnnotations().size());
		
		assertEquals(Float.NaN, gateway.getX(),0);
		assertEquals(Float.NaN, gateway.getY(),0);
		assertEquals(Float.NaN, gateway.getZ(),0);
	}

}
