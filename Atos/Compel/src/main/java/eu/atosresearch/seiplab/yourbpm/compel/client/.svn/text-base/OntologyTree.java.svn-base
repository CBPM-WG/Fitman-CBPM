package eu.atosresearch.seiplab.yourbpm.compel.client;

import java.util.List;

import com.extjs.gxt.ui.client.Style.*;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyNode;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyService;
import eu.atosresearch.seiplab.yourbpm.compel.shared.OntologyServiceAsync;

/****
 * Ontology tree
 * @author A135586
 *
 */

public class OntologyTree extends LayoutContainer{
	private PopupPanel progressPanel;
	Tree tree = new Tree();
	String uri = "";	

	@Override
	protected void onRender(Element parent, int pos) {	  
		super.onRender(parent, pos);		    

		setSize("350", "350");		    
		setBorders(true);	    
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTO);		    
		setStyleName("list");		    
		setMonitorWindowResize(true);		    
	}

	protected void onWindowResize(int pWidth, int pHeight) {
		super.onWindowResize(pWidth, pHeight);
	}

	public void loadOntology(final String url) {

		showProgressDialog("Loading, please wait.......");
		try {
			OntologyServiceAsync proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);	        	
			ServiceDefTarget target = (ServiceDefTarget)proxy; 
			target.setServiceEntryPoint(GWT.getModuleBaseURL()+"ontol");
			proxy.buildModelFromOntology(url, new AsyncCallback<OntologyNode>() {
				public void onFailure(Throwable caught) {
//					MessageBox.alert("Error", caught.getMessage(), null);
					clearProgressDialog();
				}	                
				public void onSuccess(OntologyNode result) {
					if (result == null) {
//						MessageBox.alert("Error", "Ontology not loaded!", null);
						clearProgressDialog();
						return;
					}	
					result.setSourceURL(url);

					showOntology(result);						
					clearProgressDialog();
				}
			});
		}
		catch(Exception err) {
			clearProgressDialog();
//			MessageBox.alert("Error", err.getMessage(), null);
		}
	}

	public void showOntology(OntologyNode ontoNode) {	  
		
		if(ontoNode.getChildCount() >= 2){			
			uri=((ModelNode)ontoNode.getChild(ontoNode.getChildCount()-2)).getURI();
			uri=uri.substring(0,uri.indexOf("#"));
		}else if((ontoNode.getChildCount() == 1)){
			uri=((ModelNode)ontoNode.getChild(ontoNode.getChildCount()-1)).getURI();
			uri=uri.substring(0,uri.indexOf("#"));
		}else{
			uri=ontoNode.getURI();
			uri=uri.substring(0,uri.indexOf("#"));
		}	
		tree.addItem(creaArbol(ontoNode));
		
		add(tree, new FlowData(10));
		doLayout();
	}
	
	public TreeItem creaArbol(ModelNode nodo)
	{
		String sNodo ="";
		TreeItem tI= null;
		
		if (nodo.isLeaf()){
			 sNodo = nodo.getLabel();
			 if(sNodo == null||"null".equals(sNodo)||"".equals(sNodo))
				 return new TreeItem(uri);
			 else
				 return new TreeItem(sNodo); 		//getURI());
		}
		
		List<ModelData> l = nodo.getChildren();
		sNodo = nodo.getLabel();
		if(sNodo == null||"null".equals(sNodo)||"".equals(sNodo))
			tI= new TreeItem(uri);		
		else
			tI= new TreeItem(sNodo);				//getURI());
		
	    for(int i=0; i<l.size();i++){
	       tI.addItem(creaArbol((ModelNode)l.get(i)));
	    }   
	    return tI;
	}
	
	
	public void showProgressDialog(String message) {	    	
		if (progressPanel == null) {				
			progressPanel = new PopupPanel(false, true);
			progressPanel.setSize("200", "300");
			Image.prefetch("Cargando2.gif");
			HorizontalPanel container = new HorizontalPanel();
			container.add(new Image("Cargando2.gif"));
			TableData td = new TableData();
			td.setVerticalAlign(VerticalAlignment.MIDDLE);
			container.add(new Label(message), td);
			progressPanel.setWidget(container);
			progressPanel.center();
			progressPanel.setStyleName("loadPopup");
		}
		else {
			((Label)((HorizontalPanel)progressPanel.getWidget()).getWidget(1)).setText(message);
		}

		progressPanel.show();						
	}

	public void clearProgressDialog() {
		if (progressPanel != null && progressPanel.isVisible()) {
			progressPanel.hide();
		}
	}

	public String getSeleccionado(){			   
		return uri+"#"+tree.getSelectedItem().getText();
	}
}
