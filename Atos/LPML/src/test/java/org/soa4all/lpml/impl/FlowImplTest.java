package org.soa4all.lpml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.soa4all.lpml.TestUtil.deepCopy;

import org.junit.Test;
import org.soa4all.lpml.Flow;

public class FlowImplTest {
	
final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		Flow e1 = (Flow) new FlowImpl();
		Flow e2 = (Flow) new FlowImpl();

		// then
		assertCommonProperties(e1);

		assertFalse(e1.getID().equals(e2.getID()));
	}
	

	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		Flow e = (Flow) new FlowImpl(ID);
		
		// then
		assertSame(ID, e.getID());
		assertCommonProperties(e);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		Flow e1 = (Flow) new FlowImpl(null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		Flow e = (Flow) new FlowImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		Flow e = (Flow) new FlowImpl(ID);
		
		// then
		assertEquals(e,e);
		assertEquals(e.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		Flow e = (Flow) new FlowImpl(ID);
		
		// then
		assertFalse(e.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		Flow e1 = (Flow) new FlowImpl(ID);
		Flow e2 = (Flow) new FlowImpl(ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
		assertEquals(e1.hashCode(), e2.hashCode());
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		Flow e1 = (Flow) new FlowImpl(ID);
		Flow e2 = (Flow) new FlowImpl(ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentName(){
		
		// given
		Flow e1 = (Flow) new FlowImpl(ID);
		Flow e2 = (Flow) new FlowImpl(ID);
		
		e1.setName("sdfasdfa");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	
	@Test
	public void testEqualsWithDifferentCondition(){
		
		// given
		Flow e1 = (Flow) new FlowImpl(ID);
		Flow e2 = (Flow) new FlowImpl(ID);
		
		e1.setCondition("");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentSource(){
		
		// given
		Flow e1 = (Flow) new FlowImpl(ID);
		Flow e2 = (Flow) new FlowImpl(ID);
		
		e1.setSource(new ActivityImpl());
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentDestination(){
		
		// given
		Flow e1 = (Flow) new FlowImpl(ID);
		Flow e2 = (Flow) new FlowImpl(ID);
		
		e1.setDestination(new ActivityImpl());
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentSequenceNumber(){
		
		// given
		Flow e1 = (Flow) new FlowImpl(ID);
		Flow e2 = (Flow) new FlowImpl(ID);
		
		e1.setSequenceNumber(4);
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testSerialization(){
		
		// given
		Flow e = (Flow) new FlowImpl(ID);
		
		// when
		Flow deserialized = deepCopy(e);
		
		// then
		assertEquals(e, deserialized);
	}
	
	private void assertCommonProperties(Flow flow) {
		
		assertNotNull(flow.getID());
		assertNull(flow.getCondition());
		assertNull(flow.getSource());
		assertNull(flow.getDestination());
		assertNull(flow.getName());
		assertEquals(Integer.MAX_VALUE, flow.getSequenceNumber());
	}

}
