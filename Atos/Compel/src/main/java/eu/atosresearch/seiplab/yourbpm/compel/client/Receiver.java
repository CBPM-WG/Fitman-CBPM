package eu.atosresearch.seiplab.yourbpm.compel.client;

import com.google.gwt.user.client.ui.SimplePanel;

/**************************
 * Receives info coming from Oryx
 * @author A135586
 *
 */
class Receiver extends SimplePanel { 
	 
	private String taskId;
	private String processId;
	private String bpmnModel;
	
	 
	public Receiver() {
		registerCompelReceiveModel(this); 	//Receives model from Oryx
		registerCompelReceiveTaskId(this); 		//Receives task id from Oryx
		registerCompelReceiveProcessId(this); 		//Receives process id from Oryx
    }    
	
	/********
	 * Receives the task id from Oryx
	 * @param receiver
	 */
    private native void registerCompelReceiveTaskId(Receiver receiver) /*-{
        $wnd.compelReceiveTaskId= function (myObject) {
            receiver.@eu.atosresearch.seiplab.yourbpm.compel.client.Receiver::display(Ljava/lang/String;)(myObject);                        
        };    	  
    }-*/;
    
    /********
	 * Receives the process id from Oryx
	 * @param receiver
	 */
    private native void registerCompelReceiveProcessId(Receiver receiver) /*-{
        $wnd.compelReceiveProcessId = function (myObject) {
            receiver.@eu.atosresearch.seiplab.yourbpm.compel.client.Receiver::setProcessId(Ljava/lang/String;)(myObject);                        
        };    	  
    }-*/;

    public void setProcessId (String processId){
    	this.processId = processId;
    }
    
    public String getProcessId (){
    	return this.processId;
    }
    
    public String getTaskId (){
    	return this.taskId;
    }
    
    /*********
     * Display annotations on AnnotationView, sending the task id
     * @param valor
     */
    public void display(String valor) {  
    	if (valor == null){
    		return;
    	}
		//this.taskId = valor.substring(valor.indexOf("oryx"));
		this.taskId = valor;
		
		AnnotationView.refresh();
		
		ServiceOperationsView.refresh();
		
		//Filter Annotation Types
		if (this.taskId.equals(this.processId)){
			//Selected process
			OntologyBrowserView.filterProcessAnnotationTypes();
		}else{
			OntologyBrowserView.filterTaskAnnotationTypes();
		}
    }
    
    
	/********
	 * Receives the model from Oryx as string
	 * @param receiver
	 */
    private native void registerCompelReceiveModel(Receiver receiver) /*-{
        $wnd.compelReceiveModel = function (myObject) {
            receiver.@eu.atosresearch.seiplab.yourbpm.compel.client.Receiver::displayModel(Ljava/lang/String;)(myObject);                        
        };    	  
    }-*/;

    /*********
     * Display model
     * @param valor
     */
    //TODO Receive BPMN 2,0 XML model, not the JSON
    void displayModel(String valor) {   
    	this.bpmnModel = valor;
    }
    
    /********
     * Returns the model
     * @return
     */
    String getModel(){
    	return this.bpmnModel;
    }

    /***********
     * Saves the model
     * @param valor
     */
    void setModel(String model){
    	this.bpmnModel = model;
    }

} 