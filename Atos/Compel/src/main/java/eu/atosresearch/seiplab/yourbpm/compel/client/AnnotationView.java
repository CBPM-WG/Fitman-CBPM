package eu.atosresearch.seiplab.yourbpm.compel.client;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyService;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyServiceAsync;

/********
 * AnnotationViewClass class, shows the annotations
 * @author A135586
 *
 */
public class AnnotationView extends ScrollPanel{ //SimplePanel { 
	 
	private static FlexTable pT = new FlexTable();  //Main table
	private static FlexTable ftFC = new FlexTable(); //Functional Classification
	private static FlexTable ftNFP = new FlexTable(); //Non Functional Property
	private static FlexTable ftIn = new FlexTable(); //INPUT_METADATA
	private static FlexTable ftPref = new FlexTable(); //Preference
	private static FlexTable ftReq = new FlexTable(); //Requirement
	private static FlexTable ftCtxt = new FlexTable(); //Contextual Information
	private static FlexTable ftOut = new FlexTable(); //OUTPUT_METADATA
	public Anchor remove = new Anchor(constants.removeB());
	public Anchor search = new Anchor(constants.searchB());
	private static Receiver rec = null;
	
	
	static String id="";
	
	//Constants
	private static CompelConstants constants = GWT.create(CompelConstants.class);
	
	//Logging
	private static Logger logger = Logger.getLogger("");
	
	public AnnotationView(Receiver receiver) {
		
		setWidth("100%");
		setHeight("570px");

		rec=receiver;
		
		//Create the view
		createView();	
    }    
		
    /**************************
     * 
     */
    void createView(){
    	
		//No spaces
		ftFC.setCellSpacing(0);
		ftNFP.setCellSpacing(0);
		ftIn.setCellSpacing(0);
		ftPref.setCellSpacing(0);
		ftReq.setCellSpacing(0);
		ftCtxt.setCellSpacing(0);
		ftOut.setCellSpacing(0);		
		pT.setCellSpacing(0);
		
		ftFC.setCellPadding(5);
		ftNFP.setCellPadding(5);
		ftIn.setCellPadding(5);
		ftPref.setCellPadding(5);
		ftReq.setCellPadding(5);
		ftCtxt.setCellPadding(5);
		ftOut.setCellPadding(5);
		pT.setCellPadding(5);		
		
		//Create Annotation Browser View
		ftFC.setText(0, 0, constants.functionalCass());
		ftNFP.setText(0, 0, constants.nonFunctionalPro());				
		ftIn.setText(0, 0, constants.metadataI());
		ftPref.setText(0, 0, constants.preference());	
		ftReq.setText(0, 0, constants.requirement());
		ftCtxt.setText(0, 0, constants.contextualInf());
		ftOut.setText(0, 0, constants.metadataO());
				
		//Style		
		ftFC.setStyleName("table");
		ftNFP.setStyleName("table");
		ftIn.setStyleName("table");
		ftPref.setStyleName("table");
		ftReq.setStyleName("table");
		ftCtxt.setStyleName("table");
		ftOut.setStyleName("table");
		pT.setStyleName("table");

		search.setStyleName("clickable");
		remove.setStyleName("clickable");
				
		//Listeners
		createListeners();		
		
		pT.setWidget(1, 0, ftFC);
		pT.getFlexCellFormatter().setColSpan(1, 0, 2);
		ftFC.setSize("100%", "100%");
		
		pT.setWidget(2, 0, ftNFP);		
		pT.getFlexCellFormatter().setColSpan(2, 0, 2);
		ftNFP.setSize("100%", "100%");
		
		pT.setWidget(3, 0, ftIn);		
		pT.getFlexCellFormatter().setColSpan(3, 0, 2);
		ftIn.setSize("100%", "100%");

		pT.setWidget(4, 0, ftPref);		
		pT.getFlexCellFormatter().setColSpan(4, 0, 2);
		ftPref.setSize("100%", "100%");
		
		pT.setWidget(5, 0, ftReq);	
		pT.getFlexCellFormatter().setColSpan(5, 0, 2);
		ftReq.setSize("100%", "100%");
		
		pT.setWidget(6, 0, ftCtxt);
		pT.getFlexCellFormatter().setColSpan(6, 0, 2);
		ftCtxt.setSize("100%", "100%");
		
		pT.setWidget(7, 0, ftOut);	
		pT.getFlexCellFormatter().setColSpan(7, 0, 2);
		ftOut.setSize("100%", "100%");

		pT.setWidget(8, 0, search);
		pT.setWidget(8, 1, remove);		
		
		//STYLES
		ftFC.getRowFormatter().addStyleName(0, "header");	
		ftNFP.getRowFormatter().addStyleName(0, "header");	
		ftIn.getRowFormatter().addStyleName(0, "header");
		ftPref.getRowFormatter().addStyleName(0, "header");
		ftReq.getRowFormatter().addStyleName(0, "header");
		ftCtxt.getRowFormatter().addStyleName(0, "header");
		ftOut.getRowFormatter().addStyleName(0, "header");
		
		add(pT);

		pT.setSize("100%", "100%");	
    }
    
    void createListeners(){
    	
		search.addClickHandler(new ClickHandler(){
			
			public void onClick(ClickEvent event){
				search.setEnabled(false);
				
				invokeDTC(rec.getProcessId(), id);
			} 
		});
		
		remove.addClickHandler(new ClickHandler(){		
			
			public void onClick(ClickEvent event){
				String id = rec.getTaskId();
				if(id != null && !id.equals("")){
					int tam = ftFC.getRowCount();
					for(int i=1; i<tam;i++){					
						CheckBox cB = (CheckBox)ftFC.getWidget(i, 0);
						if(cB.getValue()){ 
							ftFC.remove(cB);
							ftFC.removeRow(i);
							deleteAnnotation(id,"FUNTIONAL_CLASSIFICATION", cB.getText());
							tam--;
							i=0; 
						}
					}
					
					if(ftFC.getRowCount() == 1){
						ftFC.removeAllRows();
						pT.remove(ftFC);
					}
					
					tam = ftNFP.getRowCount();
					for(int i=1; i<tam;i++){					
						CheckBox cB = (CheckBox)ftNFP.getWidget(i, 0);
						if(cB.getValue()){ 
							ftNFP.remove(cB);
							ftNFP.removeRow(i);
							deleteAnnotation(id,"NON_FUNCTIONAL_PROPERTY", cB.getText());
							tam--;
							i=0; 
						}
					}
					
					if(ftNFP.getRowCount() == 1){
						ftNFP.removeAllRows();
						pT.remove(ftNFP);
					}

					tam = ftIn.getRowCount();
					for(int i=1; i<tam;i++){					
						CheckBox cB = (CheckBox)ftIn.getWidget(i, 0);
						if(cB.getValue()){ 
							ftIn.remove(cB);
							ftIn.removeRow(i);
							deleteAnnotation(id,"INPUT_METADATA", cB.getText());
							tam--;
							i=0; 
						}
					}
					
					if(ftIn.getRowCount() == 1){
						ftIn.removeAllRows();
						pT.remove(ftIn);
					}

					tam = ftPref.getRowCount();
					for(int i=1; i<tam;i++){					
						CheckBox cB = (CheckBox)ftPref.getWidget(i, 0);
						if(cB.getValue()){ 
							ftPref.remove(cB);
							ftPref.removeRow(i);
							deleteAnnotation(id,"REFERENCE", cB.getText());
							tam--;
							i=0; 
						}
					}
					
					if(ftPref.getRowCount() == 1){
						ftPref.removeAllRows();
						pT.remove(ftPref);
					}

					tam = ftReq.getRowCount();
					for(int i=1; i<tam;i++){					
						CheckBox cB = (CheckBox)ftReq.getWidget(i, 0);
						if(cB.getValue()){ 
							ftReq.remove(cB);
							ftReq.removeRow(i);
							deleteAnnotation(id,"REQUIREMENT", cB.getText());
							tam--;
							i=0; 
						}
					}
					
					
					if(ftReq.getRowCount() == 1){
						ftReq.removeAllRows();
						pT.remove(ftReq);
					}

					tam = ftCtxt.getRowCount();
					for(int i=1; i<tam;i++){					
						CheckBox cB = (CheckBox)ftCtxt.getWidget(i, 0);
						if(cB.getValue()){ 
							ftCtxt.remove(cB);
							ftCtxt.removeRow(i);
							deleteAnnotation(id,"CONTEXTUAL_INFORMATION", cB.getText());
							tam--;
							i=0; 
						}
					}

					
					if(ftCtxt.getRowCount() == 1){
						ftCtxt.removeAllRows();
						pT.remove(ftCtxt);
					}

					tam = ftOut.getRowCount();
					for(int i=1; i<tam;i++){					
						CheckBox cB = (CheckBox)ftOut.getWidget(i, 0);
						if(cB.getValue()){ 
							ftOut.remove(cB);
							ftOut.removeRow(i);
							deleteAnnotation(id,"OUTPUT_METADATA", cB.getText());
							tam--;
							i=0; 
						}
					}
					
					if(ftOut.getRowCount() == 1){
						ftOut.removeAllRows();
						pT.remove(ftOut);
					}
					
									
					if((ftFC.getRowCount()==0)&&(ftNFP.getRowCount()==0)&&(ftIn.getRowCount()==0)&&
					   (ftPref.getRowCount()==0)&&(ftReq.getRowCount()==0)&&(ftCtxt.getRowCount()==0)&&
					   (ftOut.getRowCount()==0)){
						  ftFC.setText(0, 0, constants.functionalCass());						
						  ftFC.getRowFormatter().addStyleName(0, "header");
						  pT.setWidget(1, 0, ftFC);
						  pT.getFlexCellFormatter().setColSpan(1, 0, 2);
						  ftFC.setSize("100%", "100%");
							
						  ftNFP.setText(0, 0, constants.nonFunctionalPro());						
						  ftNFP.getRowFormatter().addStyleName(0, "header");
						  pT.setWidget(2, 0, ftNFP);		
  						  pT.getFlexCellFormatter().setColSpan(2, 0, 2);
						  ftNFP.setSize("100%", "100%");
							
						  ftIn.setText(0, 0, constants.metadataI());						
						  ftIn.getRowFormatter().addStyleName(0, "header");
						  pT.setWidget(3, 0, ftIn);		
						  pT.getFlexCellFormatter().setColSpan(3, 0, 2);
						  ftIn.setSize("100%", "100%");

						  ftPref.setText(0, 0, constants.preference());						
						  ftPref.getRowFormatter().addStyleName(0, "header");
						  pT.setWidget(4, 0, ftPref);		
						  pT.getFlexCellFormatter().setColSpan(4, 0, 2);
						  ftPref.setSize("100%", "100%");
							
						  ftReq.setText(0, 0, constants.requirement());						
						  ftReq.getRowFormatter().addStyleName(0, "header");
						  pT.setWidget(5, 0, ftReq);	
						  pT.getFlexCellFormatter().setColSpan(5, 0, 2);
						  ftReq.setSize("100%", "100%");
							
			    		  ftCtxt.setText(0, 0, constants.contextualInf());						
			    		  ftCtxt.getRowFormatter().addStyleName(0, "header");
						  pT.setWidget(6, 0, ftCtxt);
						  pT.getFlexCellFormatter().setColSpan(6, 0, 2);
						  ftCtxt.setSize("100%", "100%");
							
			    		  ftOut.setText(0, 0, constants.metadataO());						
			    		  ftOut.getRowFormatter().addStyleName(0, "header");
						  pT.setWidget(7, 0, ftOut);	
						  pT.getFlexCellFormatter().setColSpan(7, 0, 2);
						  ftOut.setSize("100%", "100%");
					}

				}else{
//					MessageBox.alert(constants.warning(), constants.needTS(), null);
				}
			}
		});
    }
    
    /*******************
     * Clean the annotations tables
     */
    static void clean(){
   		if(ftFC.getRowCount()>0){
   			ftFC.removeAllRows();	
   		}	
   		
		ftFC.setText(0, 0, constants.functionalCass());						
		ftFC.getRowFormatter().addStyleName(0, "header");
		pT.setWidget(1, 0, ftFC);
		pT.getFlexCellFormatter().setColSpan(1, 0, 2);
		ftFC.setSize("100%", "100%");
   		
   		if(ftNFP.getRowCount()>0){
   			ftNFP.removeAllRows();	
   		}
   		
   		ftNFP.setText(0, 0, constants.nonFunctionalPro());						
   		ftNFP.getRowFormatter().addStyleName(0, "header");
		pT.setWidget(2, 0, ftNFP);		
		pT.getFlexCellFormatter().setColSpan(2, 0, 2);
		ftNFP.setSize("100%", "100%");
   		
   		if(ftIn.getRowCount()>0){
   			ftIn.removeAllRows();	
   		}
   		
		ftIn.setText(0, 0, constants.metadataI());						
		ftIn.getRowFormatter().addStyleName(0, "header");
		pT.setWidget(3, 0, ftIn);		
		pT.getFlexCellFormatter().setColSpan(3, 0, 2);
		ftIn.setSize("100%", "100%");
   		
   		if(ftPref.getRowCount()>0){
   			ftPref.removeAllRows();	
   		}
   		
   		ftPref.setText(0, 0, constants.preference());						
		ftPref.getRowFormatter().addStyleName(0, "header");
		pT.setWidget(4, 0, ftPref);		
		pT.getFlexCellFormatter().setColSpan(4, 0, 2);
		ftPref.setSize("100%", "100%");
   		
   		if(ftReq.getRowCount()>0){
   			ftReq.removeAllRows();	
   		}
   		
		ftReq.setText(0, 0, constants.requirement());						
		ftReq.getRowFormatter().addStyleName(0, "header");
		pT.setWidget(5, 0, ftReq);	
		pT.getFlexCellFormatter().setColSpan(5, 0, 2);
		ftReq.setSize("100%", "100%");
   		
   		if(ftCtxt.getRowCount()>0){
   			ftCtxt.removeAllRows();	
   		}
   		
  		ftCtxt.setText(0, 0, constants.contextualInf());						
		ftCtxt.getRowFormatter().addStyleName(0, "header");
		pT.setWidget(6, 0, ftCtxt);
		pT.getFlexCellFormatter().setColSpan(6, 0, 2);
		ftCtxt.setSize("100%", "100%");
   		
   		if(ftOut.getRowCount()>0){
   			ftOut.removeAllRows();	
   		}
   		
  		ftOut.setText(0, 0, constants.metadataO());						
		ftOut.getRowFormatter().addStyleName(0, "header");
		pT.setWidget(7, 0, ftOut);	
		pT.getFlexCellFormatter().setColSpan(7, 0, 2);
		ftOut.setSize("100%", "100%");
   		
   		ServiceOperationsView.cleanServicesTable();
    }
    
 
    /***********
     * Retrieve and shows annotations
     */
    static void refresh(){	
    	id = rec.getTaskId();
    	
    	logger.log(Level.INFO, "Retrieving and showing BPMN element annotations");

    	if(id != null && !id.equals("")){
			retrieve(id,"FUNTIONAL_CLASSIFICATION",ftFC);
			retrieve(id,"NON_FUNCTIONAL_PROPERTY",ftNFP);
			retrieve(id,"INPUT_METADATA",ftIn);
			retrieve(id,"REFERENCE",ftPref);
			retrieve(id,"REQUIREMENT",ftReq);
			retrieve(id,"CONTEXTUAL_INFORMATION",ftCtxt);
			retrieve(id,"OUTPUT_METADATA",ftOut);
    	}else{
//			MessageBox.alert(constants.warning(), constants.needT(), null);
    	}
    }
    
    /*********************
     * Call retrieveAnnotationOf
     * @param id
     * @param type
     */
    private static void retrieve(String id,final String type, final FlexTable t){
    	 try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.retrieveAnnotationOf(id,type, new AsyncCallback<List<String>>(){
		                public void onFailure(Throwable caught) {
//		                	MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(List<String> result) {
							if((result != null) && (result.size()>0)){					
								
								Iterator <String> it = result.iterator();																
								//while(it.hasNext()){
								if(type.equals("FUNTIONAL_CLASSIFICATION")){
					    		  ftFC.removeAllRows();
					    		  if(it.hasNext()){
					    			  ftFC.setText(0, 0, constants.functionalCass());						
					    			  ftFC.getRowFormatter().addStyleName(0, "header");
					    		  }
					    		  
								  for(int i=1; it.hasNext(); i++){
									int tam1=ftFC.getRowCount();
									final CheckBox cB = new CheckBox(it.next());
									ftFC.setWidget(tam1, 0,cB);
								  }
								  pT.remove(ftFC);
								  pT.setWidget(1, 0, ftFC);
								  
								}
								if(type.equals("NON_FUNCTIONAL_PROPERTY")){
				    			  ftNFP.removeAllRows();
				    			  if(it.hasNext()){
				    				  ftNFP.setText(0, 0, constants.nonFunctionalPro());						
				    				  ftNFP.getRowFormatter().addStyleName(0, "header");
				    			  }	  
	
								  for(int i=1; it.hasNext(); i++){
									int tam2=ftNFP.getRowCount();
									final CheckBox cB = new CheckBox(it.next());
									ftNFP.setWidget(tam2, 0,cB);
								  }
								 								  
								  pT.remove(ftNFP);
								  pT.setWidget(2, 0, ftNFP);

								}	
								if(type.equals("INPUT_METADATA")){
								  ftIn.removeAllRows();
								  if(it.hasNext()){
									  ftIn.setText(0, 0, constants.metadataI());						
									  ftIn.getRowFormatter().addStyleName(0, "header");
								  }
								  for(int i=1; it.hasNext(); i++){
									int tam3=ftIn.getRowCount();
									final CheckBox cB = new CheckBox(it.next());
									ftIn.setWidget(tam3, 0,cB);
								  }
								  						  
								  pT.remove(ftIn);
								  pT.setWidget(3, 0, ftIn);
								}
								if(type.equals("REFERENCE")){
								  ftPref.removeAllRows();
								  if(it.hasNext()){
									  ftPref.setText(0, 0, constants.preference());						
									  ftPref.getRowFormatter().addStyleName(0, "header");
								  }
								  for(int i=1; it.hasNext(); i++){
									int tam4=ftPref.getRowCount();
									final CheckBox cB = new CheckBox(it.next());
									ftPref.setWidget(tam4, 0,cB);
								  }
								  
								  pT.remove(ftPref);
								  pT.setWidget(4, 0, ftPref);
								}
								if(type.equals("REQUIREMENT")){
								  
								  ftReq.removeAllRows();
								  if(it.hasNext()){
									  ftReq.setText(0, 0, constants.requirement());						
									  ftReq.getRowFormatter().addStyleName(0, "header");
								  }
								  for(int i=1; it.hasNext(); i++){
									int tam5=ftReq.getRowCount();
									final CheckBox cB = new CheckBox(it.next());
									ftReq.setWidget(tam5, 0,cB);
								  }
								  
								  pT.remove(ftReq);
								  pT.setWidget(5, 0, ftReq);
								}
								if(type.equals("CONTEXTUAL_INFORMATION")){
								  
								  ftCtxt.removeAllRows();	
								  if(it.hasNext()){
									  ftCtxt.setText(0, 0, constants.contextualInf());						
									  ftCtxt.getRowFormatter().addStyleName(0, "header");
								  }
								  for(int i=1; it.hasNext(); i++){
									int tam6=ftCtxt.getRowCount();
									final CheckBox cB = new CheckBox(it.next());
									ftCtxt.setWidget(tam6, 0,cB);
								  }
								  
								  pT.remove(ftCtxt);
								  pT.setWidget(6, 0, ftCtxt);
								}
								if(type.equals("OUTPUT_METADATA")){
							
  								  ftOut.removeAllRows();
  								  if(it.hasNext()){
  									  ftOut.setText(0, 0, constants.metadataO());						
  									  ftOut.getRowFormatter().addStyleName(0, "header");
  								  }
								  for(int i=1; it.hasNext(); i++){
									int tam7=ftOut.getRowCount();
									final CheckBox cB = new CheckBox(it.next());
									ftOut.setWidget(tam7, 0,cB);
								  }
								  
								  pT.remove(ftOut);
								  pT.setWidget(7, 0, ftOut);
								}
							}else{
								t.removeAllRows();
							    t.getRowFormatter().addStyleName(0, "header");

								
								if(type.equals("FUNTIONAL_CLASSIFICATION")){
									  ftFC.setText(0, 0, constants.functionalCass());						
								}
								if(type.equals("NON_FUNCTIONAL_PROPERTY")){
									  ftNFP.setText(0, 0, constants.nonFunctionalPro());
								}
								if(type.equals("INPUT_METADATA")){
									  ftIn.setText(0, 0, constants.metadataI());						
								}
								if(type.equals("REFERENCE")){
									  ftPref.setText(0, 0, constants.preference());						
								}
								if(type.equals("REQUIREMENT")){
									  ftReq.setText(0, 0, constants.requirement());						
								}
								if(type.equals("CONTEXTUAL_INFORMATION")){
						    		  ftCtxt.setText(0, 0, constants.contextualInf());						
								}
								if(type.equals("OUTPUT_METADATA")){
						    		  ftOut.setText(0, 0, constants.metadataO());						
								}
							}
						}
		            });
		  }
		  catch(Exception err) {
//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
//		        MessageBox.alert(constants.errorE(), err.getCause()+"", null);
		  }
    }
    
    /*******************
     * Call deleteAnnotation
     * @param id
     * @param type
     * @param annotation
     */
    private void deleteAnnotation(String id,String type,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.deleteAnnotation(id,type,annotation, new AsyncCallback<List<String>>(){
		                public void onFailure(Throwable caught) {
//		                	MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(List<String> result) {
//							MessageBox.alert(constants.warning(), constants.servicesDeleted(), null);
						}
		            });
		  }
		  catch(Exception err) {
//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }
    
    
    /******************
     * Call invokeDTC
     * @param processId
     * @param taskId
     */
    private void invokeDTC(String processId,String taskId){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.invokeDTC(processId,taskId,new AsyncCallback<Boolean>(){
		                public void onFailure(Throwable caught) {
		                	search.setEnabled(true);
		                	MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Boolean result) {
							if(result){
								//Rendering found services
								ServiceOperationsView.refresh();
							}else{
								MessageBox.alert(constants.warning(), constants.noServices(), null);
							}
							search.setEnabled(true);
						}
		            });
		  }
		  catch(Exception err) {
			    search.setEnabled(true);
//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }

}