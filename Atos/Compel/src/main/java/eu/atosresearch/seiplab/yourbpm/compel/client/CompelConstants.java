package eu.atosresearch.seiplab.yourbpm.compel.client;

import com.google.gwt.i18n.client.Constants;

/*************************
 * Messages and constants
 * @author A135586
 *
 *************************/
public interface CompelConstants extends Constants {
	  @DefaultStringValue("Ontology Browser")
	  String ontologyBrowser();

	  @DefaultStringValue("Select Ontology:")
	  String selectOntology();

	  @DefaultStringValue("Add Ontology:")
	  String addOntology();

	  @DefaultStringValue("Ontology:")
	  String ontology();

	  @DefaultStringValue("Annotation Type:")
	  String type();

	  @DefaultStringValue("Introduce URL...")
	  String introURL();

	  @DefaultStringValue("Add")
	  String add();

	  @DefaultStringValue("Close")
	  String close();

	  @DefaultStringValue("Load")
	  String load();
	  
	  @DefaultStringValue("Services  /")
	  String services();

	  @DefaultStringValue("Templates")
	  String templates();
	  
	  @DefaultStringValue("Binding")
	  String binding();

	  @DefaultStringValue("Task/Activity:")
	  String taskActivity();
	  
	  @DefaultStringValue("Search S/T:")
	  String search();

	  @DefaultStringValue("Annotation:")
	  String annotation();
	  
	  @DefaultStringValue("List of available")
	  String list();
	  
	  @DefaultStringValue("Services/Templates:")
	  String servicesTem();

	  @DefaultStringValue("Requirement")
	  String requirement();
	  
	  @DefaultStringValue("Preference")
	  String preference();
	  
	  @DefaultStringValue("Contextual Information")
	  String contextualInf();
	  
	  @DefaultStringValue("Functional Classification")
	  String functionalCass();
	  
	  @DefaultStringValue("Non Functional Property")
	  String nonFunctionalPro();
	  
	  @DefaultStringValue("Input")
	  String metadataI();
	  
	  @DefaultStringValue("Output")
	  String metadataO();

	  @DefaultStringValue("Need to select a concept and type")
	  String needCT();
	  
	  @DefaultStringValue("WARNING")
	  String warning();

	  @DefaultStringValue("Need the task id in order to save")
	  String needT();
	  
	  @DefaultStringValue("Need the task id in order to search")
	  String needTS();

	  @DefaultStringValue("Need the type id in order to search")
	  String needTyS();

	  @DefaultStringValue("Need a url to load the ontology!")
	  String needURL();

	  @DefaultStringValue("You must introduce a new URL")
	  String needNewURL();

	  @DefaultStringValue("You must introduce a name")
	  String needNewName();

	  @DefaultStringValue("This ontology is already on the list")
	  String notNewName();

	  @DefaultStringValue("Error onFailure:")
	  String errorF();

	  @DefaultStringValue("Error Exception:")
	  String errorE();

	  @DefaultStringValue("Search")
	  String searchB();

	  @DefaultStringValue("Remove")
	  String removeB();

	  @DefaultStringValue("Bind")
	  String bindB();

	  @DefaultStringValue("Resolve")
	  String resolveB();

	  @DefaultStringValue("Service Name")
	  String serviceName();

	  @DefaultStringValue("isProcess")
	  String p();

	  @DefaultStringValue("Operation")
	  String operationName();
	  
	  @DefaultStringValue("Add Ontology")
	  String addOntoName();
	  
	  @DefaultStringValue("Name:")
	  String name();
	  
	  @DefaultStringValue("No services found")
	  String noServices();
	  
	  @DefaultStringValue("Service binded")
	  String binded();
	  
	  @DefaultStringValue("Need the model in order to resolve")
	  String needModel();
	  
	  @DefaultStringValue("Service(s) deleted")
	  String servicesDeleted();


	  @DefaultStringValue("Oryx")
	  String oryx();

	  @DefaultStringValue("Please, select a composition task before annotating it")
	  String selectProcessElement();
}
