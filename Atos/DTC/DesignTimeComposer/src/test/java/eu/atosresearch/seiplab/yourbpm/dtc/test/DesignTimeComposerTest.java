package eu.atosresearch.seiplab.yourbpm.dtc.test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.soa4all.lpml.impl.SemanticAnnotationImpl;

import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.IAnnotationType;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.exception.DTComposerException;
import eu.atosresearch.seiplab.yourbpm.dtc.kb.KBProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.kb.KBProxyImpl;
import eu.atosresearch.seiplab.yourbpm.dtc.run.DesignComposerDemonstration;

public class DesignTimeComposerTest {
	DesignComposerDemonstration demo = null;
	
	@Before
	public void setUp() throws URISyntaxException, DTComposerException{
		demo = new DesignComposerDemonstration(
		"dtcomposer-service-configuration-debug.xml");		
	}
	
	@Test
	@Ignore
	public void testSearchForActivityBindings() throws Exception {		
		// Bind task
		String processURI = "oryx-canvas123";
		String activityURI = "oryx_660B94A6-F9B2-450C-8C24-AFE47917B196";
	
		Assert.assertTrue(demo.getComposer().searchForActivityBindings(processURI, activityURI));
	}
	
	@Test
	@Ignore
	public void testCreditCardBusinessRegistrationProcess() throws Exception {		
		// Bind task
		String fcns = "http://www.soa4all.eu/ontology/eGovernment/fc#";
		String ions = "http://www.soa4all.eu/ontology/eGovernment/iotypes#";
		String processURI = "c557d961-0124-42b2-9608-51d34b3fdaa7";
		String activityURI = "13011e2b-1f19-4a47-bf79-bfd7e8ed6fbc";
		KBProxy kbProxy = KBProxyImpl.getInstance();
		kbProxy.removeActivityAnnotations (activityURI);
		List<ISemanticAnnotation> ans = new ArrayList<ISemanticAnnotation>();
		ISemanticAnnotation an1 = new SemanticAnnotationImpl();
		an1.setType(IAnnotationType.FUNCTIONAL_CLASSIFICATION);
		an1.setReferenceURI(fcns + "CreateSalesOrder");
		ans.add(an1);
		kbProxy.addActivityAnnotations (activityURI, ans);
		List<ISemanticAnnotation> ians = new ArrayList<ISemanticAnnotation>();
		ISemanticAnnotation an2 = new SemanticAnnotationImpl();
		an2.setType(IAnnotationType.META_DATA);
		an2.setReferenceURI(ions + "CRMEGovDocument");
		ians.add(an2);
		kbProxy.addActivityInputAnnotations (activityURI, ians);
		List<ISemanticAnnotation> oans = new ArrayList<ISemanticAnnotation>();
		ISemanticAnnotation an3 = new SemanticAnnotationImpl();
		an3.setType(IAnnotationType.META_DATA);
		an3.setReferenceURI(ions + "Invoice");
		oans.add(an3);
		kbProxy.addActivityOutputAnnotations (activityURI, oans);
		Assert.assertTrue(demo.getComposer().searchForActivityBindings(processURI, activityURI));
	}
	
	@Test
	@Ignore
	public void testBindFlightTask() throws Exception {		
		// Bind task
		String processURI = "oryx_2FEE9E47-9C3E-4709-98FC-C8D5BE4DBC03";
		String activityURI = "oryx_F181088D-D763-44FE-A9E7-58EC3DE3267A";
	
		Assert.assertTrue(demo.getComposer().searchForActivityBindings(processURI, activityURI));
	}
}
