package eu.atosresearch.seiplab.yourbpm.compel.shared;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class ConfigurationModel extends BaseModel implements IsSerializable {

    public ConfigurationModel() {}
   

    public String getOryxURL(){
		return get("oryx_url");
	}
	public String getOntologyWine(){
		return get("ontologies_wine");
	}
	public String getOntologyFood(){
		return get("ontologies_food");
	}
	public String getOntologyPizza(){
		return get("ontologies_pizza");
	}
	public String getOntologySOA4AllWP7FC(){
		return get("ontologies_soa4all_wp7_fc");
	}
	public String getOntologySOA4AllWP7IO(){
		return get("ontologies_soa4all_wp7_io");
	}
	public String getOntologyTourism(){
		return get("ontologies_tourism");
	}
	public String getOntologyLogistic(){
		return get("ontologies_logistic");
	}
	public String getOntologyLogisticClassif(){
		return get("ontologies_logistic_Classification");
	}
	public String getDTCEndpoint(){
		return get("dtc_endpoint");
	}
}

