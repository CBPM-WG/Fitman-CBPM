package eu.atosresearch.seiplab.kb.test;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.atosresearch.seiplab.kb.businesslogic.AnnotationHandler;
import eu.atosresearch.seiplab.kb.types.Service;

public class KBTest {
	private AnnotationHandler ah;
	
	@Before
	public void setUp() throws Exception {
		ah = new AnnotationHandler();
	}
	
	@Test
	@Ignore
	public void KBTest() {
		//Get Task Matching Services
		String id = "oryx_660B94A6-F9B2-450C-8C24-AFE47917B196";
		List<Service> services = ah.retrieveServicesOf (id);
		
		//Select Service
		ah.selectService(id, services.get(0).getURI());
		
		//Get Selected Service
		Service selectedService = ah.getSelectedService(id);
	}

}
