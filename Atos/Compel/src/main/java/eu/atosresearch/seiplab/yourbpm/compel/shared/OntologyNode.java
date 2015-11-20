package eu.atosresearch.seiplab.yourbpm.compel.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import eu.atosresearch.seiplab.yourbpm.compel.client.ModelNode;


@SuppressWarnings("serial")
public class OntologyNode extends ModelNode implements IsSerializable {

    public OntologyNode() {}
    
    public OntologyNode(String label, String URI) {
        super(label, URI);
    }
    
    public void setSourceURL(String url) {
        set("src", url);
    }
    public String getSource() {
        return get("src");
    }
}

