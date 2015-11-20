package eu.atosresearch.seiplab.yourbpm.compel.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;

import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyService;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyServiceAsync;
import eu.atosresearch.seiplab.yourbpm.compel.shared.Service;


/********************************
 * ServiceOperationsView class, shows the services
 * @author A135586
 *
 */
public class ServiceOperationsView  extends ScrollPanel{ //extends SimplePanel {

	private FlexTable pT = new FlexTable();
	private static FlexTable t1 = new FlexTable(); 
	private static Receiver rec = null;
	private static String selectedServiceURI;
	
	//Constants
	private static CompelConstants constants = GWT.create(CompelConstants.class);
	public static Anchor bind = new Anchor(constants.bindB());
	public static Anchor resolve = new Anchor(constants.resolveB());
	
	 //Logging
	static Logger logger = Logger.getLogger("ServiceOperationsView");
	
	public ServiceOperationsView(Receiver receiver) {
		setWidth("100%");
		setHeight("570px");		
		
		Window.addResizeHandler(new ResizeHandler() {

		     public void onResize(ResizeEvent event) {
		       int height = event.getHeight();
		       setHeight(height + "px");
		     }
		});
		
		rec=receiver;
		
		//Create the view
		createView();	
		
    }   
	
    void createView(){
		//No spaces
		t1.setCellSpacing(0);
		pT.setCellSpacing(0);

		t1.setCellPadding(5);
		pT.setCellPadding(5);
		
		prepareServiceTable();

		//Style		
		t1.setStyleName("table");
		pT.setStyleName("table");

		bind.setStyleName("clickable");
		resolve.setStyleName("clickable");
		
		resolve.setVisible(false);
		
		//Listeners
		createListeners();		

		pT.setWidget(1, 0, t1);
		pT.getFlexCellFormatter().setColSpan(1, 0, 4);
		
		pT.setWidget(2, 0, bind);
		pT.setWidget(2, 1, resolve);  
		t1.setSize("100%", "100%");
		
		add(pT);

		pT.setSize("100%", "100%");	
    }

	private static void prepareServiceTable() {
		t1.setText(1, 1, constants.serviceName());
		t1.setText(1, 3, constants.operationName()); 
		t1.getFlexCellFormatter().setWidth(1, 0, "5px");
		t1.getFlexCellFormatter().setWidth(1, 1, "30px");
		t1.getFlexCellFormatter().setWidth(1, 3, "30px");
		t1.getFlexCellFormatter().setWidth(1, 4, "5px");
		t1.getFlexCellFormatter().setColSpan(1, 1, 2);
		t1.getFlexCellFormatter().setColSpan(1, 3, 2);
		
		t1.getRowFormatter().addStyleName(1, "header");
	}
    
    void createListeners(){
    	
		bind.addClickHandler(new ClickHandler(){
			
			public void onClick(ClickEvent event){
				bind.setEnabled(false);
				String id = rec.getTaskId();
							
				if(id != null && !id.equals("") && selectedServiceURI!=null && !selectedServiceURI.equals("")){ 
					selectService(id, selectedServiceURI);
				}else{
					bind.setEnabled(true);
					MessageBox.alert(constants.warning(), "Please, select a service to bind", null);
				}
				bind.setEnabled(true);
			}
		});
		
		resolve.addClickHandler(new ClickHandler(){
			
			public void onClick(ClickEvent event){
				resolve.setEnabled(false);
				
				String id = rec.getTaskId();
				String modelS = rec.getProcessId();

				if(id != null && !id.equals("")){
					if(modelS != null && !modelS.equals("")){
						receiveJson(modelS);
						resolve.setEnabled(true);
					}else{
						resolve.setEnabled(true);
//						MessageBox.alert(constants.warning(), constants.needModel(), null);
					}					
				}else{
					resolve.setEnabled(true);
//					MessageBox.alert(constants.warning(), constants.needTS(), null);
				}
				resolve.setEnabled(true);
			}
		});
    }  

    /************
     * Retrieves the services
     */
    static void refresh(){
    	String id = rec.getTaskId();
    	
    	cleanServicesTable();
    	
		if(id != null && !id.equals("")){
			retrieveServices(id);
		}

    }
    
    /**********************
     * Clean services table
     */
    static void cleanServicesTable(){
		if(t1.getRowCount()>0)
			t1.removeAllRows();	
		
		prepareServiceTable();
	}
    
    /*****************
     * Calls retrieveServicesOf
     * @param id
     */
    private static void retrieveServices(String id){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.retrieveServicesOf(id, new AsyncCallback<List<Service>>(){
		                public void onFailure(Throwable caught) {
		                	logger.log(Level.SEVERE, "Error loading matched services from task binding");
		                }     
						public void onSuccess(List<Service> services) {							
							if(services != null ){								
								for (final Service service: services){
									int row = t1.getRowCount();
									String wsdl = service.getWSDLURL();
									String serviceName = wsdl.substring(wsdl.lastIndexOf("/")+1, wsdl.indexOf("?wsdl"));									
//									final RadioButton rB = new RadioButton(service.getURI(), splitInParts(serviceName,(int)(Window.getClientWidth()*0.017)).trim()); 	
									final RadioButton rB = new RadioButton("selectServiceRBGroup"); 
									if (service.isBound())
										rB.setChecked(true);
									rB.addClickHandler(new ClickHandler(){
										public void onClick(ClickEvent event){
											if(!service.isProcess()){ //FIXME Support returned processes as bindings
												if (service.isProcess())
													resolve.setVisible(true);
											}
											selectedServiceURI = service.getURI();
										}
									});
									
									//Radio Button
									t1.setWidget(row, 0, rB);
									t1.getFlexCellFormatter().setWidth(row, 0, "5px");
//									t1.getFlexCellFormatter().setColSpan(row, 0, 2);
									
									//Service Name
									t1.setText(row, 1, serviceName); 
									t1.getFlexCellFormatter().setWidth(row, 1, "30px"); 
//									t1.getFlexCellFormatter().setWordWrap(row, 2, true); 
									t1.getFlexCellFormatter().setColSpan(row, 1, 2); 

									//Operation
									t1.setText(row, 3, splitInParts(service.getOperation().substring(service.getOperation().indexOf("#OP") + 3), (int)(Window.getClientWidth()*0.019)).trim()); 
									t1.getFlexCellFormatter().setWidth(row, 3, "30px"); 
//									t1.getFlexCellFormatter().setWordWrap(row, 2, true); 
									t1.getFlexCellFormatter().setColSpan(row, 3, 2); 

									//WSDL
									Anchor wsdlAnchor = new Anchor("wsdl", true, service.getWSDLURL(),"_blank");									
									t1.setWidget(row, 4, wsdlAnchor);
									
									//FIXME Detecting matching processes. Currently not reported by KB
//									if(!aux.get(2).equals("false")){
//										Anchor process = new Anchor("process",true,aux.get(2),"_blank");
//										t1.setWidget(tam, 5,process);
//									}
								}	
							}
						}
		            });
		      }
		  catch(Exception err) {
		      logger.log(Level.SEVERE, "Error loading matched services from task binding", err);
		  }
  }
    
    /************************
     * Calls selectService
     * @param id
     * @param url
     * @param operation
     */
    private void selectService(String id, String serviceURI){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.selectService(id, serviceURI, new AsyncCallback<List<String>>(){
		                public void onFailure(Throwable caught) {
							bind.setEnabled(true);
							logger.log(Level.SEVERE, "Error registering service task binding in the KB");
//		                	MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(List<String> result) {
							bind.setEnabled(true);
//							MessageBox.alert(constants.warning(), constants.binded(), null);
						}
		            });
		  }
		  catch(Exception err) {
			    bind.setEnabled(true);
//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
  }
    

    /********************
     * Obtiene el modelo de Oryx en json => xml
     * @param modelS
     */
    private void receiveJson(String modelS){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.receiveJson(modelS, new AsyncCallback<List<String>>(){
		                public void onFailure(Throwable caught) {
							bind.setEnabled(true);
//		                	MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(List<String> result) {
							bind.setEnabled(true);
//							MessageBox.alert(constants.warning(), "Json received", null);
						}
		            });
		  }
		  catch(Exception err) {
			    bind.setEnabled(true);
//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }
    
    public static String splitInParts(String s, int partLength)
    {
        int len = s.length();

        // Number of parts
        int nparts = (len + partLength - 1) / partLength;
        String parts[] = new String[nparts];

        // Break into parts
        int offset= 0;
        int i = 0;
        String p="";
        while (i < nparts)
        {
            parts[i] = s.substring(offset, Math.min(offset + partLength, len));
            offset += partLength;
            p=(p+parts[i]).trim()+"\n";
            i++;            
        }
        
        return p;
        //return parts;
    }


}
