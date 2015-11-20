package eu.atosresearch.seiplab.yourbpm.compel.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import eu.atosresearch.seiplab.yourbpm.compel.client.conf.CompelClientConfiguration;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyService;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyServiceAsync;

/******
 * OntologyBrowserView class, shows the Ontology tree view
 * @author A135586
 *
 */
public class OntologyBrowserView extends SimplePanel { 
	 
	private Grid t1 = new Grid(3, 3);
	private Grid t2 = new Grid(1, 3); 
	private Grid pT = new Grid(3, 1);
	private ListBox ontol = new ListBox();
	private static ListBox types = new ListBox();
	private TextBox newURL = new TextBox();
	private Button bName = new Button();
	private Button bClose = new Button();
	private String url = ""; // Selected Ontology
	private String type = ""; //Selected Ontology type
	public OntologyTree tree = new OntologyTree();
	private HashMap<String, String> ontolURL = new HashMap<String, String>();
	
	private Grid tName = new Grid(3,2);
	private final TextBox tBName = new TextBox();
	private PopupPanel pop = new PopupPanel(false);
	
	private Receiver rec = null;
	
	//Constants
	private static CompelConstants constants = GWT.create(CompelConstants.class);
	private Anchor addBt2 = new Anchor(constants.add());
	private Anchor addBt = new Anchor(constants.add());
	private Anchor load = new Anchor(constants.load());
	
	//Logging
	Logger logger = Logger.getLogger("OntologyBrowserView");
	
	public OntologyBrowserView(Receiver receiver) {
		
		rec=receiver;
		
		ontolURL.put("wine", CompelClientConfiguration.getConfiguration().getOntologyWine()); 
		ontolURL.put("pizza", CompelClientConfiguration.getConfiguration().getOntologyPizza());
		ontolURL.put("food", CompelClientConfiguration.getConfiguration().getOntologyFood());
		ontolURL.put("class_funcionales", CompelClientConfiguration.getConfiguration().getOntologySOA4AllWP7FC());
		ontolURL.put("I/O", CompelClientConfiguration.getConfiguration().getOntologySOA4AllWP7IO());
		ontolURL.put("Tourism", CompelClientConfiguration.getConfiguration().getOntologyTourism());
		ontolURL.put("Logistic", CompelClientConfiguration.getConfiguration().getOntologyLogistic());
		ontolURL.put("Logistic-v2", CompelClientConfiguration.getConfiguration().getOntologyLogisticClassif());
		
		//Create the view
		createView();	
    }    
    
	public static ListBox getType(){
		return types;
	}
	
    private void createView(){
    	
    	ontol.addItem("");
    	Iterator iterator = ontolURL.keySet().iterator();
    	while( iterator.hasNext() ){
    		ontol.addItem((String)iterator.next());    		
    	} 
    			
    	types.addItem("");
		types.addItem(constants.functionalCass());   //Functional Classification
		types.addItem(constants.metadataI()); 		 //Metadata Input
		types.addItem(constants.metadataO()); 		 //Metadata Output
		types.addItem(constants.requirement()); 	 //Requirement
		types.addItem(constants.nonFunctionalPro()); //Non Functional Property
		types.addItem(constants.contextualInf());    //Contextual Information
		types.addItem(constants.preference()); 		 //Preference
				
		//No spaces
		t1.setCellSpacing(5);
		t1.setCellPadding(0);
		
		//Create Ontology Browser View
		t1.setText(0, 0, constants.addOntology());
		t1.setWidget(0, 1, newURL);
		t1.setWidget(0, 2, addBt);
		
		t1.setText(1, 0, constants.selectOntology());
		t1.setWidget(1, 1, ontol);	
		t1.setWidget(1, 2, load);
		
		t1.setText(2, 0, constants.ontology());
		
		t2.setText(0, 0, constants.type());
		t2.setWidget(0, 1, types);		
		t2.setWidget(0, 2, addBt2);

		pT.setWidget(0, 0, t1);			
		pT.setWidget(1, 0, tree);  //ONTOLOGY TREE
		pT.setWidget(2, 0, t2);
		
		newURL.setValue(constants.introURL());
		
		//Style
		t1.setStyleName("table");
		t2.setStyleName("table");		
		load.setStyleName("clickable");
		addBt.setStyleName("clickable");
		addBt2.setStyleName("clickable");
		ontol.setStyleName("list");
		types.setStyleName("list");
		newURL.setStyleName("list");
		pop.setStyleName("gwt-PopupPanel2");
				
		//Listeners
		createListeners();		
		
		t1.setSize("100%", "100%");
		t2.setSize("100%", "100%");
		pT.setSize("100%", "100%");
		
		add(pT);
		
    }
    
    void createListeners(){
    	
    	ontol.addChangeHandler(new ChangeHandler(){
    		
			public void onChange(ChangeEvent event){
				String on = ontol.getItemText(ontol.getSelectedIndex());								
				url= (String) ontolURL.get(on);				
			}
		});
		
		types.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				type = types.getItemText(types.getSelectedIndex());								
			}
		});
		
		load.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){			
				  				
				  if (url == null || url.length() == 0) {					
//			        MessageBox.alert(constants.warning(), constants.needURL(), null);			        
			      }else{  
			    	logger.log(Level.INFO, "Loading ontology from url " + url);
			    	
					pT.clearCell(1, 0);						
			    	
			    	tree= new OntologyTree();
			    	tree.loadOntology(url);			    	

			    	pT.setWidget(1, 0, tree);
			      }		
			}
		});
		
		newURL.addClickHandler(new ClickHandler(){
			
			public void onClick(ClickEvent event){				  				
				newURL.setText("");
			}
		});
		
		addBt.addClickHandler(new ClickHandler(){
			
			public void onClick(ClickEvent event){				  				
				final String url = newURL.getText();

				if((constants.introURL()).equals(url)|| url==null || "".equals(url)){
//					MessageBox.alert(constants.warning(), constants.needNewURL(), null);					
				}else{
					tBName.setStyleName("list");
					bName.setStyleName("button");
					bClose.setStyleName("button");
					bName.setText(constants.add());
					bClose.setText(constants.close());
					
					tName.setText(0, 0, constants.addOntoName());
					tName.setText(0, 1, " "+constants.name());
					tName.setText(1, 0, constants.name());
					tName.setWidget(1, 1, tBName);
					tName.setWidget(2, 0, bName);
					tName.setWidget(2, 1, bClose);
					
					tName.getRowFormatter().addStyleName(0, "header");
					tName.setCellPadding(0);
					tName.setCellSpacing(0);
					
					pop.add(tName);
					pop.setTitle("PopupPanel");
					pop.center();					
					pop.show();
				}
			}
		});
		
		bName.addClickHandler(new ClickHandler(){
			
			public void onClick(ClickEvent event){				  				
				String name = tBName.getText();

				if(name==null || "".equals(name)){
//					MessageBox.alert(constants.warning(), constants.needNewName(), null);
				}else{
					String valor = ontolURL.get(name);
					if(valor==null){
						ontolURL.put(name, url); 
						ontol.addItem(name);
						newURL.setText(""); 
					}else{
//						MessageBox.alert(constants.warning(), constants.notNewName(), null); // La url ya esta
					}
				}
				pop.hide();
			}
		});
		
		bClose.addClickHandler(new ClickHandler(){			
			public void onClick(ClickEvent event){	  				
				pop.hide();
			}
		});
		
		addBt2.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				
				String id = rec.getTaskId(); 
				String modelS = rec.getProcessId(); 
				
				//Process or task not selected
				if (id == null || id.isEmpty()){
					MessageBox.alert(constants.warning(), constants.selectProcessElement(), null);
				}
				addBt2.setEnabled(false);
				
				if((type==null||type.equals(""))||(tree.getSeleccionado()==null||tree.getSeleccionado().equals(""))){
					addBt2.setEnabled(true);
					MessageBox.alert(constants.warning(), constants.needCT(), null);
				}else{					
					String URI = tree.getSeleccionado();  
					
					if(id != null && !id.equals("")){
						if(type.equals(constants.requirement())){						
							addReq(id,URI); //addRequirement(id,annotation)
						}else if(type.equals(constants.preference())){
							addPref(id,URI);//addPreference(id,annotation)
						}else if(type.equals(constants.contextualInf())){
							addCI(id,URI);//addContextualInformation(id,annotation)						
						}else if(type.equals(constants.functionalCass())){
							addFC(id,URI);//addFuntionalClassification
						}else if(type.equals(constants.nonFunctionalPro())){
							addNFP(id,URI);//addNonFunctionalProperty
						}else if(type.equals(constants.metadataI())){
							addInputMetadata(id,URI);//addMetadata
						}else if(type.equals(constants.metadataO())){
							addOutputMetadata(id,URI);//addMetadata
						}
						
					}else{
						addBt2.setEnabled(true);
//						MessageBox.alert(constants.warning(), constants.needT(), null);
					}
				}
			}
		});	
    }
    
    /**
     * Call addRequirement 
     * @param id
     * @param annotation
     */    
    private void addReq(String id,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.addRequirement(id,annotation, new AsyncCallback() {
		                public void onFailure(Throwable caught) {
		                	addBt2.setEnabled(true);
//		                    MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Object result) {
							addBt2.setEnabled(true);
							AnnotationView.refresh();
							
							return;						
						}
		            });
		      }
		  catch(Exception err) {
			  addBt2.setEnabled(true);

//			  MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }

    /*******
     * Call addReference
     * @param id
     * @param annotation
     */
    private void addPref(String id,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.addReference(id,annotation, new AsyncCallback() {
		                public void onFailure(Throwable caught) {
		                	addBt2.setEnabled(true);

//		                    MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Object result) {
							addBt2.setEnabled(true);
							AnnotationView.refresh();

							return;						
						}
		            });
		      }
		  catch(Exception err) {
			  	addBt2.setEnabled(true);

//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }

    /****
     * Call addContextualInformation
     * @param id
     * @param annotation
     */
    private void addCI(String id,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.addContextualInformation(id,annotation, new AsyncCallback() {
		                public void onFailure(Throwable caught) {
		                	addBt2.setEnabled(true);

//		                    MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Object result) {
							addBt2.setEnabled(true);
							
							//Recuperar
							AnnotationView.refresh();

							return;						
						}
		            });
		      }
		  catch(Exception err) {
			  	addBt2.setEnabled(true);

//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }

    /*********
     * Call addFuntionalClassification
     * @param id
     * @param annotation
     */
    private void addFC(String id,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.addFuntionalClassification(id,annotation, new AsyncCallback<Object>() {
		                public void onFailure(Throwable caught) {
		                	addBt2.setEnabled(true);

//		                    MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Object result) {
							addBt2.setEnabled(true);
							
							//Recuperar
							AnnotationView.refresh();

							return;						
						}
		            });
		      }
		  catch(Exception err) {
			  	addBt2.setEnabled(true);

//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }

    /***********
     * Call addNonFunctionalProperty
     * @param id
     * @param annotation
     */
    private void addNFP(String id,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.addNonFunctionalProperty(id,annotation, new AsyncCallback() {
		                public void onFailure(Throwable caught) {
		                	addBt2.setEnabled(true);
		                	
//		                    MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Object result) {
							addBt2.setEnabled(true);
							
							//Recuperar
							AnnotationView.refresh();

							return;						
						}
		            });
		      }
		  catch(Exception err) {
			  	addBt2.setEnabled(true);

//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }
  
    /******************
     * Call addInputMetadata
     * @param id
     * @param annotation
     */
    private void addInputMetadata(String id,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.addInputMetadata(id,annotation, new AsyncCallback() {
		                public void onFailure(Throwable caught) {
		                	addBt2.setEnabled(true);

//		                	MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Object result) {
							addBt2.setEnabled(true);
							
							//Recuperar
							AnnotationView.refresh();
							
							return;						
						}
		            });
		      }
		  catch(Exception err) {
			  	addBt2.setEnabled(true);

//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    } 
    
    /*************************
     * Call addOutputMetadata
     * @param id
     * @param annotation
     */
    private void addOutputMetadata(String id,String annotation){
	      try {
		        OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
		        ServiceDefTarget target = (ServiceDefTarget)proxy; 
		        target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
		        proxy.addOutputMetadata(id,annotation, new AsyncCallback() {
		                public void onFailure(Throwable caught) {
		                	addBt2.setEnabled(true);

//		                	MessageBox.alert(constants.errorF(), caught.getMessage(), null);
		                }     
						public void onSuccess(Object result) {
							addBt2.setEnabled(true);
							
							AnnotationView.refresh();
							
							return;						
						}
		            });
		      }
		  catch(Exception err) {
			  	addBt2.setEnabled(true);

//		        MessageBox.alert(constants.errorE(), err.getMessage(), null);
		  }
    }

	public static void filterProcessAnnotationTypes() {
		types.clear();
		types.addItem("");
		types.addItem(constants.requirement()); 	 //Requirement
		types.addItem(constants.preference()); 		 //Preference
		types.addItem(constants.contextualInf());    //Contextual Information
	}

	public static void filterTaskAnnotationTypes() {
		types.clear();
		types.addItem("");
		types.addItem(constants.functionalCass());   //Functional Classification
		types.addItem(constants.metadataI()); 		 //Metadata Input
		types.addItem(constants.metadataO()); 		 //Metadata Output
		types.addItem(constants.nonFunctionalPro()); //Non Functional Property
	}

}