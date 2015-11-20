package org.soa4all.lpml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import static org.soa4all.lpml.TestUtil.*;

import org.junit.Test;
import org.soa4all.lpml.Activity;
import org.soa4all.lpml.Parameter;

public class ActivityImplTest {
	
	final String ID = "kjlsdfalkjaslkdjflsk";
	
	@Test
	public void testDefaultConstructor(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl();
		Activity e2 = (Activity) new ActivityImpl();

		// then
		assertCommonProperties(e1);

		assertFalse(e1.getID().equals(e2.getID()));
	}
	

	@Test
	public void testConstructorWithIDArgument(){
		
		// given
		Activity e = (Activity) new ActivityImpl(ID);
		
		// then
		assertSame(ID, e.getID());
		assertCommonProperties(e);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullID(){
		
		// given
		@SuppressWarnings("unused")
		Activity e = (Activity) new ActivityImpl(null);
	}
	
	@Test
	public void testHashCode(){
		
		// given
		Activity e = (Activity) new ActivityImpl(ID);
		
		// then
		assertEquals(ID.hashCode(), e.hashCode());
	}
	
	@Test
	public void testEqualsWithSameObject(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		
		// then
		assertEquals(e1,e1);
		assertEquals(e1.hashCode(), e1.hashCode());
	}
	
	@Test
	public void testEqualsWithNull(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		
		// then
		assertFalse(e1.equals(null));
	}
	
	@Test
	public void testEqualsWithSameIds(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		// then
		assertEquals(e1,e2);
		assertEquals(e2,e1);
		assertEquals(e1.hashCode(), e2.hashCode());
	}
	
	@Test
	public void testEqualsWithDifferentIds(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID + "sdf");
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentStartElement(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.setStartElement(true);
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentEndElement(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.setEndElement(true);
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentHumanTaskProperty(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.setHumanTask(true);
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentSynchronousProperty(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.setSynchronous(true);
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentAnnotations(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.getSemanticAnnotations().add(new SemanticAnnotationImpl());
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentBindings(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.getBindings().add(new BindingImpl());
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentInputParameters(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		Parameter p1 = (Parameter) new ParameterImpl();
                e1.getInputParameters().add(p1);
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentOutputParameters(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		Parameter p1 = (Parameter)  new ParameterImpl();
                e1.getOutputParameters().add(p1);
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentLoopParameter(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.setLoopParameter(new ParameterImpl());
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentConnectors(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
		e1.getConnectors().add(new ConnectorImpl());
		
		
		// then
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
	}
	
	@Test
	public void testEqualsWithDifferentPosition(){
		
		// given
		Activity e1 = (Activity) new ActivityImpl(ID);
		Activity e2 = (Activity) new ActivityImpl(ID);
		
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
		Activity a = (Activity) new ActivityImpl(ID);
		
		// when
		a.setX(1);
		a.setY(342);
		a.setZ(56);
		
		// then
		assertEquals(1, a.getX(), 0);
		assertEquals(342, a.getY(), 0);
		assertEquals(56, a.getZ(), 0);
	}
	
	@Test
	public void testSerialization(){
		
		// given
		Activity a = (Activity) new ActivityImpl(ID);
		
		// when
		Activity deserialized = deepCopy(a);
		
		// then
		assertEquals(a, deserialized);
	}
	
	private void assertCommonProperties(Activity activity) {
		
		assertNotNull(activity.getID());
		assertNull(activity.getName());
		assertFalse(activity.isStartElement());
		assertFalse(activity.isEndElement());
		assertFalse(activity.isHumanTask());
		assertFalse(activity.isSynchronous());
		assertNull(activity.getLoopParameter());
		assertEquals(0, activity.getSemanticAnnotations().size());
		assertEquals(0, activity.getBindings().size());
		assertEquals(0, activity.getInputParameters().size());
		assertEquals(0, activity.getOutputParameters().size());
		
		assertEquals(Float.NaN, activity.getX(),0);
		assertEquals(Float.NaN, activity.getY(),0);
		assertEquals(Float.NaN, activity.getZ(),0);
		
	}

}
