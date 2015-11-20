package eu.atosresearch.seiplab.kb.test;

import org.junit.Before;
import org.junit.Test;

import eu.atosresearch.seiplab.kb.businesslogic.AnnotationHandler;

public class DeployServiceDescriptionsTest {
	private AnnotationHandler ah;
	
	@Before
	public void setUp() throws Exception {
		ah = new AnnotationHandler();
	}
	
	@Test
	public void DeployServiceDescriptions() {
		//Deploy a service description:
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/FlightService.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/HotelService.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/HostelWorld.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/HotelFind.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/FirstHotel.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/AirCongo.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/BrusselAirlines.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/AddressServiceService.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/Stock.posm");
		ah.loadTurtleFile("./src/test/resources/ServiceDescriptions/manufacturing.posm");
		
	}

}
