package eu.atosresearch.seiplab.yourbpm.compel.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;



@RemoteServiceRelativePath("ontol")
public interface OntologyService extends RemoteService {
	
	public OntologyNode buildModelFromOntology(String restURL) throws Exception;

	public void addRequirement(String id, String annotation) throws Exception;
	
	public void addReference(String id, String annotation) throws Exception;
	
	public void addContextualInformation(String id, String annotation) throws Exception;
	
	public void addFuntionalClassification(String id, String annotation) throws Exception;
	
	public void addNonFunctionalProperty(String id, String annotation) throws Exception;
	
	public void addInputMetadata(String id, String annotation) throws Exception;
	
	public void addOutputMetadata(String id, String annotation) throws Exception;

	public List<String> retrieveAnnotationOf(String id, String type) throws Exception;	
	
	public List<Service> retrieveServicesOf(String id) throws Exception;
	
	public void deleteAnnotation(String id, String type, String annotation) throws Exception;
	
	public boolean invokeDTC(String processId, String taskId);

	public void selectService(String idBPMN, String serviceUri) throws Exception;
	
	public void receiveJson(String modelS);
	
	public Service getSelectedService(String id);

}
