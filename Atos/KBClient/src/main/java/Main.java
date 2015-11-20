import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import eu.atosresearch.seiplab.kb.businesslogic.AnnotationHandler;
import eu.atosresearch.seiplab.kb.tools.AeSimpleMD5;


public class Main {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		AnnotationHandler ah=new AnnotationHandler();
		//"T1_Task oryx_79DBAEC9-F38D-4F69-936F-61F609446110"
		//ah.addFuntionalClassification("T1_Task oryx_79DBAEC9-F38D-4F69-936F-61F609446110", "mememe");

//		ah.addService("PROCESS", "1", "http://localhost:8080/servicio", "mio");
//		String[][] rs=ah.retrieveServicesOf("1");		
//	
//		for(int i=0;i<rs.length;i++){
//			System.out.println(rs[i][0]+" - "+rs[i][1]);
//		}
		
//		String ns = "http://www.webn1.org/ontology#";
		String BPMN_id = "oryx_B840EEC0-9CA9-40E1-8E55-60202CEEE5B8";
//		String fc = ns + "BusinessEntity";
//		String nfp = ns + "ForFree";
//		String in= ns + "DocumentForm";
//		String out= ns + "Report";
//		
//		//Add annotations
//		ah.addFuntionalClassification(BPMN_id, fc);
//		ah.addNonFunctionalProperty(BPMN_id, nfp);
//		ah.addInputMetadata(BPMN_id, in);
//		ah.addOutputMetadata(BPMN_id, out);
//		
//		//Read Annotations
		String[] result = ah.retrieveAnnotationOf(BPMN_id, "ALL");
		System.out.println ("Results:");
		for (String s: result){
			System.out.println ("Result: " + s);
		}
//		
//		//Remove Annotations
//		ah.deleteAnnotation(BPMN_id, "FUNTIONAL_CLASSIFICATION", fc);
//		ah.deleteAnnotation(BPMN_id, "NON_FUNCTIONAL_PROPERTY", nfp);
//		ah.deleteAnnotation(BPMN_id, "INPUT_METADATA", in);
//		ah.deleteAnnotation(BPMN_id, "OUTPUT_METADATA", out);
//		
//		//Read Annotations
//		String[] result = ah.retrieveAnnotationOf(BPMN_id, "ALL");
//		System.out.println ("Results:");
//		for (String s: result){
//			System.out.println ("Result: " + s);
//		}
//		
//		ah.addService("TASK", BPMN_id, "http://www.webn1.org/service/service1.wsdl", "operation1");
//		String[][] services = ah.retrieveServicesOf(BPMN_id);
//		int i=1;
//		for (String[] service:services){
//			System.out.println ("Service #" + i + ", URL: " + service[0] + " Operation: " + service[1]);
//		}
		
		//Load Service description
//		ah.loadTurtleFile("/home/yosu/projects/WebN1/Oryx/workspace/DTC/DesignTimeComposer_v2/src/main/resources/ontologies/production/services/sap_es_sales_order_create_request_confirmation_B.n3");
//		ah.loadTurtleFile("/home/yosu/projects/WebN1/Oryx/workspace/DTC/DesignTimeComposer_v2/src/main/resources/ontologies/production/NFPOntology.n3");

//		ah.loadRDFXMLFile("/home/yosu/projects/WebN1/Oryx/workspace/DTC/DesignTimeComposer_v2/src/main/resources/ontologies/production/wp7-businessregistration-fc.wsml.rdf.xml");
//		ah.loadRDFXMLFile("/home/yosu/projects/WebN1/Oryx/workspace/DTC/DesignTimeComposer_v2/src/main/resources/ontologies/production/wp7-io.wsml.rdf.xml");
		
		//Deploy a service description:
//		ah.loadTurtleFile("/opt/eclipse/eclipseGWT/Compel/src/main/resources/ServiceDescriptions/HotelService.posm");
//		ah.loadTurtleFile("/opt/eclipse/eclipseGWT/Compel/src/main/resources/ServiceDescriptions/HostelWorld.posm");
//		ah.loadTurtleFile("/opt/eclipse/eclipseGWT/Compel/src/main/resources/ServiceDescriptions/HotelFind.posm");
//		ah.loadTurtleFile("/opt/eclipse/eclipseGWT/Compel/src/main/resources/ServiceDescriptions/FirstHotel.posm");
//		ah.loadTurtleFile("/opt/eclipse/eclipseGWT/Compel/src/main/resources/ServiceDescriptions/AirCongo.posm");
//		ah.loadTurtleFile("/opt/eclipse/eclipseGWT/Compel/src/main/resources/ServiceDescriptions/BrusselAirlines.posm");		
	}
}
