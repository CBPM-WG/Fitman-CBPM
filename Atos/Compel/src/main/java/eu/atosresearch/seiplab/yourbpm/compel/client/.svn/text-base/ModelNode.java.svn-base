package eu.atosresearch.seiplab.yourbpm.compel.client;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ModelNode extends BaseTreeModel implements IsSerializable {

    public ModelNode() {}
    
    public ModelNode(String label, String URI) {
        set("lab", label);
        set("uri", URI);
        set("fnd", label + " (" + URI + ")");
    }
    
    public String getURI() {
        return get("uri");
    }
    
    public String toString() {
        return get("lab");
    }
    
    public void setTooltip(String data) {
        set("tooltip", data);
    }
    public String getTooltip() {
        return get("tooltip");
    }
    
    public boolean equals(Object o) {
    	if (o == null) {
    		return false;
    	}
        if (false == o instanceof ModelNode) {
            return false;
        }
        if (o == this) {
        	return true;
        }
        return (getURI() != null) 
        	&& (getURI().equals(((ModelNode)o).getURI()));
    }
    
    public String getLabel() {
        return get("lab");
    }
}

