package org.soa4all.lpml.util;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IBinding;

import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.soa4all.lpml.*;
import org.soa4all.lpml.Process;
import org.soa4all.lpml.impl.ActivityImpl;
import org.soa4all.lpml.impl.BindingImpl;
import org.soa4all.lpml.impl.FlowImpl;
import org.soa4all.lpml.impl.ParameterImpl;
import org.soa4all.lpml.impl.ProcessImpl;
import org.soa4all.lpml.impl.SemanticAnnotationImpl;
import org.soa4all.lpml.util.LPMLValidationException;

public class LPMLValidatorTest {

	
	@Test
	public void testEmptyProcess() throws Exception {
		new LPMLValidator().validate((Process)new ProcessImpl());
	}
	
	
	@Test(expected=LPMLValidationException.class)
	public void testMoreThanOneStartElement() throws Exception{
		
		//given
		Process p = (Process) (Process) new ProcessImpl();
		
		Activity start1 = (Activity) (Activity) new ActivityImpl(); 
		start1.setStartElement(true);
		p.getProcessElements().add((ProcessElement) start1);
		
		Activity start2 = (Activity) (Activity) new ActivityImpl(); 
		start2.setStartElement(true);
		p.getProcessElements().add((ProcessElement) start2);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	@Test(expected=LPMLValidationException.class)
	public void testMoreThanOneEndElement() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity start1 = (Activity) (Activity) new ActivityImpl(); 
		start1.setEndElement(true);
		p.getProcessElements().add((ProcessElement) start1);
		
		Activity start2 = (Activity) (Activity) new ActivityImpl(); 
		start2.setEndElement(true);
		p.getProcessElements().add((ProcessElement) start2);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	
	@Test(expected=LPMLValidationException.class)
	public void testOrphanedFlow() throws Exception {
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Flow flow1 = (Flow) new FlowImpl();
		p.getFlows().add(flow1);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	
	@Test(expected=LPMLValidationException.class)
	public void testNoDefaultFlow() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
		

		Activity a3 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a3);
		
		Flow flow1 = createFlow(a1, a2);
		flow1.setSequenceNumber(1);
		p.getFlows().add(flow1);
		
		Flow flow2 = createFlow(a1, a3);
		flow2.setSequenceNumber(2);
		p.getFlows().add(flow2);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	
	@Test(expected=LPMLValidationException.class)
	public void testMoreThanOneDefaultFlow() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
		

		Activity a3 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a3);
		
		Flow flow1 = createFlow(a1, a2);
		p.getFlows().add(flow1);
		
		Flow flow2 = createFlow(a1, a3);
		p.getFlows().add(flow2);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	
	@Test(expected=LPMLValidationException.class)
	public void testDefaultFlowWithCondition() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
				
		Flow flow1 = createFlow(a1, a2);
		flow1.setCondition("sparql condition");
		p.getFlows().add(flow1);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception	
	}
	
	@Test(expected=LPMLValidationException.class)
	public void testDefaultFlowWithEmptyStringCondition() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
				
		Flow flow1 = createFlow(a1, a2);
		flow1.setCondition("");
		p.getFlows().add(flow1);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception	
	}
	
	public void testDefaultFlowWithNullCondition() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
				
		Flow flow1 = createFlow(a1, a2);
		flow1.setCondition(null);
		p.getFlows().add(flow1);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then ok	
	}
	
	@Test(expected=LPMLValidationException.class)
	public void testDefaultFlowWithAnnotations() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
				
		Flow flow1 = createFlow(a1, a2);
		flow1.getSemanticAnnotations().add(new SemanticAnnotationImpl());
		p.getFlows().add(flow1);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	@Test
	public void testMoreThanFlowGoesOutOfStartElement() throws Exception{
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl(); 
		a1.setStartElement(true);
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
		

		Activity a3 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a3);
		
		Flow flow1 = createFlow(a1, a2); // the default flow
		p.getFlows().add(flow1);
		
		Flow flow2 = createFlow(a1, a3);
		flow2.setSequenceNumber(1);
		p.getFlows().add(flow2);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then no exception
	}
	
	
	@Test(expected=LPMLValidationException.class)
	public void testActivitiesWithDuplicateIDs() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) (Activity) new ActivityImpl("id1");
		a1.setStartElement(true);
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl("id1");
		p.getProcessElements().add((ProcessElement) a2);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	


	@Test(expected=LPMLValidationException.class)
	public void testActivitiesAndFlowWithDuplicateIDs() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity a1 = (Activity) new ActivityImpl("id1");
		p.getProcessElements().add((ProcessElement) a1);
		
		Activity a2 = (Activity) (Activity) new ActivityImpl(); 
		p.getProcessElements().add((ProcessElement) a2);
				
		Flow flow1 = createFlow("id1",(ProcessElement)a1,(ProcessElement) a2);
		p.getFlows().add(flow1);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	@Test(expected=LPMLValidationException.class)
	public void testStartElementWithInputParameters() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity startElement = (Activity) new ActivityImpl("id1");
		startElement.setStartElement(true);
		Parameter param = (Parameter) new ParameterImpl();
		startElement.getInputParameters().add(param);
		
		p.getProcessElements().add((ProcessElement) startElement);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	
	@Test(expected=LPMLValidationException.class)
	public void testEndElementWithOutputParameters() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity endElement = (Activity) new ActivityImpl("id1");
		endElement.setEndElement(true);
		Parameter param = (Parameter) new ParameterImpl();
		endElement.getOutputParameters().add(param);
		
		p.getProcessElements().add((ProcessElement) endElement);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	@Test(expected=LPMLValidationException.class)
	public void testBindingWithNullServiceURI() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity activity = (Activity) new ActivityImpl("id1");
		IBinding b = new BindingImpl();
		b.setServiceReference(null);
		b.setOperation(new URI("http://host/operation1"));
		activity.getBindings().add(b);
		
		p.getProcessElements().add((ProcessElement) activity);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	@Test(expected=LPMLValidationException.class)
	public void testBindingWithNullOperationURI() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity activity = (Activity) (Activity) new ActivityImpl("id1");
		IBinding b = new BindingImpl();
		b.setServiceReference(new URL("http://host/service1"));
		b.setOperation(null);
		activity.getBindings().add(b);
		
		p.getProcessElements().add((ProcessElement) activity);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception
	}
	
	@Test
	public void testBindingWithNonNullServiceAndOperation() throws Exception{
		
		//given
		Process p = (Process) new ProcessImpl();
		
		Activity activity = (Activity) new ActivityImpl("id1");
		IBinding b = new BindingImpl();
		b.setServiceReference(new URL("http://host/service1"));
		b.setOperation(new URI("http://host/operation1"));
		activity.getBindings().add(b);
		
		p.getProcessElements().add((ProcessElement) activity);
		
		//when
		new LPMLValidator().validate(p);
		
		
		//then exception shall NOT be thrown
	}
	
	
	private Flow createFlow(String id, ProcessElement source, ProcessElement dest){
		Flow flow = (Flow) new FlowImpl(id);
		flow.setSource(source);
		flow.setDestination(dest);
		return flow;
	}
	
	private Flow createFlow(Activity a1, Activity a2){
		Flow flow = (Flow) new FlowImpl();
		flow.setSource(a1);
		flow.setDestination(a2);
		return flow;
	}
	
	
}
