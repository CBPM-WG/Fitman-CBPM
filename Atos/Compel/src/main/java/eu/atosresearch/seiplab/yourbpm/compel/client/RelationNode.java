package eu.atosresearch.seiplab.yourbpm.compel.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RelationNode extends ModelNode implements Serializable {

    public RelationNode() { super(); }

    public RelationNode(String label, String URI) {
        super(label, URI);
    }

}

