package eu.atosresearch.seiplab.yourbpm.compel.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface OntologyServiceAsync{
	
	public void buildModelFromOntology(String restURL, AsyncCallback<OntologyNode> callBack) throws Exception;
	
	public void addRequirement(String id, String annotation, AsyncCallback callBack) throws Exception;
	
	public void addReference(String id, String annotation, AsyncCallback callBack) throws Exception;
	
	public void addContextualInformation(String id, String annotation, AsyncCallback callBack) throws Exception;
	
	public void addFuntionalClassification(String id, String annotation, AsyncCallback callBack) throws Exception;
	
	public void addNonFunctionalProperty(String id, String annotation, AsyncCallback callBack) throws Exception;
	
	public void addInputMetadata(String id, String annotation, AsyncCallback callBack) throws Exception;

	public void addOutputMetadata(String id, String annotation, AsyncCallback callBack) throws Exception;

	public void retrieveAnnotationOf(String id, String type, AsyncCallback<List<String>> asyncCallback) throws Exception;
	
	public void retrieveServicesOf(String id, AsyncCallback<List<Service>> asyncCallback) throws Exception;
	
	public void deleteAnnotation(String id, String type, String annotation, AsyncCallback callBack) throws Exception;
	
	public void invokeDTC(String processId, String taskId, AsyncCallback<Boolean> callBack);

	public void selectService(String idBPMN, String serviceUri, AsyncCallback callBack) throws Exception;
	
	public void receiveJson(String modelS, AsyncCallback callBack);

	public void getSelectedService(String id, AsyncCallback<Service> asyncCallback);
}
