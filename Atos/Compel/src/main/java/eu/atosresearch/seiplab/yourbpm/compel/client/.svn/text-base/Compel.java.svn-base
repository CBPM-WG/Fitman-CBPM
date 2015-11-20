package eu.atosresearch.seiplab.yourbpm.compel.client;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

import eu.atosresearch.seiplab.yourbpm.compel.client.conf.CompelClientConfiguration;
import eu.atosresearch.seiplab.yourbpm.compel.shared.ConfigurationModel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class Compel implements EntryPoint {	  
	  private DockLayoutPanel sp1 = new DockLayoutPanel(Unit.PX) ;		
	  private LayoutPanel p1 = new LayoutPanel();

	  //Constants
	  private CompelConstants constants = GWT.create(CompelConstants.class);
	  
	  private NamedFrame frameOryx = new NamedFrame(constants.oryx()); //Oryx
	  public String id = "";
	  
	  String urlRep = "http://localhost:8088/backend/poem/repository";
		
	  /**
	   * Entry point method.
	   */
	  public void onModuleLoad() {
		  //Loading Compel configuration
		  CompelClientConfiguration.loadConfiguration(new AsyncCallback<ConfigurationModel> (){

				@Override
				public void onFailure(Throwable arg0) {
					MessageBox.alert("Error", "Compel configuration could not be loaded, please, contact site administrator", null);
				}

				@Override
				public void onSuccess(ConfigurationModel model) {
					CompelClientConfiguration.setConfiguration(model);
					
					//Initializing compel
					initializeCompel();
				}});
	  }

	private void initializeCompel() {
		String idEditor = Window.Location.getParameter("id");
		
		frameOryx.setUrl(CompelClientConfiguration.getConfiguration().getOryxURL()+"?id=" + idEditor);
		  
		  p1.add(frameOryx);
		  p1.setSize("100%", "100%"); 
		  
		  //frameOryx.setSize("800px", "700px");
		  frameOryx.setSize("100%", "100%");
		  		  
		  sp1.addWest(p1, 1000);

		  Receiver receiver = new Receiver();

		  OntologyBrowserView oBV = new OntologyBrowserView(receiver); 
		  AnnotationView aV = new AnnotationView(receiver); 
		  ServiceOperationsView sOV = new ServiceOperationsView(receiver); 
		  
		  final TabPanel tp = new TabPanel();
		  tp.setVisible(true);
		  		  
		  sp1.add(tp);		  
		  
          final SectionStack sectionStack = new SectionStack();  
          sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);		           
          sectionStack.setHeight(""+(int)(Window.getClientHeight()*0.96)+"px");		           
          sectionStack.setWidth(""+(int)(Window.getClientWidth()*0.286)+"px");
          sectionStack.setCanResizeSections(true);
          sectionStack.setHeaderHeight(15);
		           
          SectionStackSection section1 = new SectionStackSection("Ontology Browser");		           
          section1.setExpanded(true);  
          section1.setResizeable(true);
		  section1.setCanCollapse(true);
		  Canvas c = new Canvas();
		  c.setOverflow(Overflow.AUTO);	
		  c.setShowCustomScrollbars(false);
		  c.addChild(oBV);		           
		  section1.addItem(c);  
		  sectionStack.addSection(section1);  
		     
		  SectionStackSection section2 = new SectionStackSection("Task/Process Annotations");  
		  section2.setExpanded(true);  
		  section2.setCanCollapse(true);
		  section2.setResizeable(true);
		  Canvas c1 = new Canvas();
		  c1.setOverflow(Overflow.AUTO);
		  c1.addChild(aV);
		  section2.addItem(c1); 		           
		  sectionStack.addSection(section2);
		           
		  SectionStackSection section3 = new SectionStackSection("Service Operations");  
		  section3.setExpanded(true);  
		  section3.setCanCollapse(true);
		  section3.setResizeable(true);
		  Canvas c3 = new Canvas();		           
		  c3.setOverflow(Overflow.AUTO);       
		  c3.addChild(sOV);
		  section3.addItem(c3);		           
		  sectionStack.addSection(section3);  
		     
		  HLayout layout = new HLayout();  
		  layout.setMembersMargin(20);  
		  layout.addMember(sectionStack);
		  layout.draw();

		  tp.add(layout, "Light-weighted Semantic Composition");
		  tp.selectTab(0);
		  
		  final Button bRep = new Button("Repository");
		  bRep.addClickHandler(new ClickHandler(){
		        	   
			  public void onClick(ClickEvent event){
				  if(frameOryx.getUrl().equals(CompelClientConfiguration.getConfiguration().getOryxURL())){
					  frameOryx.setUrl(urlRep);
					  bRep.setText("Editor");
				  }else if(frameOryx.getUrl().equals(urlRep)){
					  		frameOryx.setUrl(CompelClientConfiguration.getConfiguration().getOryxURL());
					  		bRep.setText("Repository");
				  }
			  }
		  });
		  
//          tp.add(bRep, "Check I/O Compatibility");
//          tp.add(new HTML("Baz"), "Resolve Process");
		  
          tp.setWidth("100%");
          tp.setHeight(Window.getClientHeight()+"px");
          Window.addResizeHandler(new ResizeHandler() {

        	  public void onResize(ResizeEvent event) {
				      int height = event.getHeight();
				
				      //tp.setHeight(height + "px");
				      tp.setHeight(""+(int)(Window.getClientHeight()*0.96) + "px");
					  //sectionStack.setHeight(height);					       
				      sectionStack.setHeight(""+(int)(Window.getClientHeight()*0.96)+"px");
			  }
		  });			           
		           
          RootLayoutPanel.get().add(sp1);
	}
}