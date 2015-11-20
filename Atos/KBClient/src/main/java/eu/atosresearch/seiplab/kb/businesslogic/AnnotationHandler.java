package eu.atosresearch.seiplab.kb.businesslogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.BindingSet;

import eu.atosresearch.seiplab.kb.sesame.ISesame;
import eu.atosresearch.seiplab.kb.sesame.SesameRepository;
import eu.atosresearch.seiplab.kb.tools.AeSimpleMD5;
import eu.atosresearch.seiplab.kb.types.Service;

public class AnnotationHandler {
	private ISesame sesame;
	private static final String NAMESPACE_DATA="http://webn1.atosresearch.eu/data#";
	private static final String NAMESPACE_BPMN_ELEMENT="http://dkm.fbk.eu/index.php/BPMN_Ontology#BPMN_element";
	private static final String NAMESPACE_HAS_RELATION="http://bridge-webn1.atosresearch.eu/bridge#hasAnnotation";
	private static final String NAMESPACE_MODEL_REFERENCE="http://www.w3.org/ns/sawsdl#modelReference";
	private static final String NAMESPACE_OWL="http://www.w3.org/2002/07/owl#";
	private static final String NAMESPACE_RDF="http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String NAMESPACE_RDFS="http://www.w3.org/2000/01/rdf-schema#";


	private static final String NAMESPACE_PROCESS="http://dkm.fbk.eu/index.php/BPMN_Ontology#process";
	private static final String NAMESPACE_BPROCESS="http://bridge-webn1.atosresearch.eu/bridge#Process";
	private static final String NAMESPACE_TASK="http://dkm.fbk.eu/index.php/BPMN_Ontology#task";
	private static final String NAMESPACE_IO="http://bridge-webn1.atosresearch.eu/bridge#IOElement";

	private static final String NAMESPACE_SERVICE="http://www.wsmo.org/ns/posm/0.2#Service";
	private static final String NAMESPACE_OPERATION="http://www.wsmo.org/ns/posm/0.2#Operation";
	private static final String NAMESPACE_HAS_SERVICE="http://bridge-webn1.atosresearch.eu/bridge#hasService";

	private static final String NAMESPACE_HAS_SELECTED_SERVICE="http://bridge-webn1.atosresearch.eu/bridge#hasSelectedService";
	private static final String NAMESPACE_HAS_OPERATION="http://www.wsmo.org/ns/posm/0.2#hasOperation";
	private static final String NAMESPACE_MESSAGE="http://www.wsmo.org/ns/posm/0.2#Message";
	private static final String NAMESPACE_HAS_INPUT_MESSAGE="http://www.wsmo.org/ns/posm/0.2#hasInput";
	private static final String NAMESPACE_HAS_OUTPUT_MESSAGE="http://www.wsmo.org/ns/posm/0.2#hasOutput";

	private static final String NAMESPACE_HAS_REQUIREMENT="http://bridge-webn1.atosresearch.eu/bridge#hasRequirement";
	private static final String NAMESPACE_HAS_REFERENCE="http://bridge-webn1.atosresearch.eu/bridge#hasReference";
	private static final String NAMESPACE_HAS_CONTEXTUAL_INFORMATION="http://bridge-webn1.atosresearch.eu/bridge#hasContextualInformation";
	private static final String NAMESPACE_HAS_FUNTIONAL_CLASSIFICATION="http://bridge-webn1.atosresearch.eu/bridge#hasFunctionalClassification";
	private static final String NAMESPACE_HAS_NON_FUNCTIONAL_PROPERTY="http://bridge-webn1.atosresearch.eu/bridge#hasNonFunctionalProperty";
	private static final String NAMESPACE_HAS_METADATA="http://bridge-webn1.atosresearch.eu/bridge#hasMetadata";
	private static final String NAMESPACE_HAS_INPUT_METADATA="http://bridge-webn1.atosresearch.eu/bridge#hasInputMetadata";
	private static final String NAMESPACE_HAS_OUTPUT_METADATA="http://bridge-webn1.atosresearch.eu/bridge#hasOutputMetadata";
	private static final String NAMESPACE_HAS_BPMN="http://bridge-webn1.atosresearch.eu/bridge#hasBPMN";

	private static final String NAMESPACE_HAS_INCOMPATIBILITY_WITH="http://bridge-webn1.atosresearch.eu/bridge#hasIncompatibilityWith";

	public AnnotationHandler(){
		sesame=new SesameRepository();
	}
	
	public void addIncompatibility(String uri1, String uri2){
		sesame.addStatement(new URIImpl(uri1), new URIImpl(NAMESPACE_HAS_INCOMPATIBILITY_WITH),uri2);	
	}
	
	private void createElement(String id, String type){
		String query="ASK {<"+id+"> <"+NAMESPACE_RDF+"type> <"+type+">}";
		boolean existe=sesame.ask(query);
		if(!existe){
			query="ASK {<"+id+"> <"+NAMESPACE_RDF+"type> ?o}";
			existe=sesame.ask(query);
			if(existe)
				throw new RuntimeException("The BPMN element belongs to a incompatible type");
			sesame.addStatement(new URIImpl(id), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OWL+"NamedIndividual");
			sesame.addStatement(new URIImpl(id), new URIImpl(NAMESPACE_RDF+"type"), type);
		}		
	}
	
	public void loadTurtleFile(String file){
		try{
			sesame.addTurtleFromFile(file);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadRDFXMLFile(String file){
		try{
			sesame.addRDFXMLFromFile(file);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Set<BindingSet> executeSPARQL(String sparql){
		return sesame.queryTuple(sparql);
	}


	public void addRequirement(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_PROCESS);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_REQUIREMENT),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addReference(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_PROCESS);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_REFERENCE),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}

	public void addContextualInformation(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_PROCESS);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_CONTEXTUAL_INFORMATION),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addFuntionalClassification(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_TASK);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_FUNTIONAL_CLASSIFICATION),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addNonFunctionalProperty(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_TASK);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_NON_FUNCTIONAL_PROPERTY),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addMetadata(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_IO);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_METADATA),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addInputMetadata(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_TASK);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_INPUT_METADATA),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addOutputMetadata(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			createElement(nspmn, NAMESPACE_TASK);
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_OUTPUT_METADATA),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void addAnnotation(String BPMN_id, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			String query="ASK {<"+nspmn+"> <"+NAMESPACE_RDF+"type> <"+NAMESPACE_BPMN_ELEMENT+">}";
			boolean existe=sesame.ask(query);
			if(!existe){
				sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OWL+"NamedIndividual");
				sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_BPMN_ELEMENT);
			}
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_RELATION),annotation);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addService(String type,String BPMN_id, String url,String operation){
		try {
			String telement="";
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			if(type.equals("PROCESS")){
				telement=NAMESPACE_PROCESS;
			}else if(type.equals("TASK")){
				telement=NAMESPACE_TASK;
			}else if(type.equals("IOElement")){
				telement=NAMESPACE_IO;
			}

			createElement(nspmn, telement);

			String idser=AeSimpleMD5.MD5(url+operation);
			String nsser=NAMESPACE_DATA+"SRV"+idser;
			String query="ASK {<"+nsser+"> <"+NAMESPACE_RDF+"type> <"+NAMESPACE_SERVICE+">}";
			boolean existe=sesame.ask(query);
			if(!existe){
				query="ASK {<"+NAMESPACE_DATA+"OP"+operation+"> <"+NAMESPACE_RDF+"type> <"+NAMESPACE_OPERATION+">}";
				existe=sesame.ask(query);
				if(!existe){
					sesame.addStatement(new URIImpl(NAMESPACE_DATA+"OP"+operation), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OWL+"NamedIndividual");
					sesame.addStatement(new URIImpl(NAMESPACE_DATA+"OP"+operation), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OPERATION);					
				}
				sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OWL+"NamedIndividual");
				sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_SERVICE);
				sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_RDFS+"isDefinedBy"), url);
				sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_HAS_OPERATION),NAMESPACE_DATA+"OP"+operation);
			}
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_SERVICE),NAMESPACE_DATA+"SRV"+idser);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createService(String url,String operation){
		String idser=AeSimpleMD5.MD5(url+operation);
		String nsser=NAMESPACE_DATA+"SRV"+idser;
		String query="ASK {<"+nsser+"> <"+NAMESPACE_RDF+"type> <"+NAMESPACE_SERVICE+">}";
		boolean existe=sesame.ask(query);
		if(!existe){
			query="ASK {<"+NAMESPACE_DATA+"OP"+operation+"> <"+NAMESPACE_RDF+"type> <"+NAMESPACE_OPERATION+">}";
			existe=sesame.ask(query);
			if(!existe){
				sesame.addStatement(new URIImpl(NAMESPACE_DATA+"OP"+operation), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OWL+"NamedIndividual");
				sesame.addStatement(new URIImpl(NAMESPACE_DATA+"OP"+operation), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OPERATION);					
			}
			sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OWL+"NamedIndividual");
			sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_SERVICE);
			sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_RDFS+"isDefinedBy"), url);
			sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_HAS_OPERATION),NAMESPACE_DATA+"OP"+operation);
		}		
	}
	
	public void addMessage(String operation,String msg,String type){
		sesame.addStatement(new URIImpl(NAMESPACE_DATA+"MSG"+msg), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_OWL+"NamedIndividual");
		sesame.addStatement(new URIImpl(NAMESPACE_DATA+"MSG"+msg), new URIImpl(NAMESPACE_RDF+"type"), NAMESPACE_MESSAGE);
		if(type.equals("IN"))
			sesame.addStatement(new URIImpl(NAMESPACE_DATA+"OP"+operation), new URIImpl(NAMESPACE_HAS_INPUT_MESSAGE), NAMESPACE_DATA+"MSG"+msg);
		else if(type.equals("OUT"))
			sesame.addStatement(new URIImpl(NAMESPACE_DATA+"OP"+operation), new URIImpl(NAMESPACE_HAS_OUTPUT_MESSAGE), NAMESPACE_DATA+"MSG"+msg);
		else
			throw new RuntimeException("Invalid Message Type");
	}
	
	public void annotateService(String url,String operation,String annotation){
		String idser=AeSimpleMD5.MD5(url+operation);
		sesame.addStatement(new URIImpl(NAMESPACE_DATA+"SRV"+idser), new URIImpl(NAMESPACE_MODEL_REFERENCE), annotation);
	}
	
	public void addMsgAnnotation(String msg,String ann){
		sesame.addStatement(new URIImpl(NAMESPACE_DATA+"MSG"+msg), new URIImpl(NAMESPACE_MODEL_REFERENCE), ann);
	}
	

	public void deleteService(String BPMN_id, String url, String operation){
		try {		
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			String idser=AeSimpleMD5.MD5(url+operation);
			String nsser=NAMESPACE_DATA+"SRV"+idser;

			Statement stm=new StatementImpl(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_SERVICE),new URIImpl(nsser));
			sesame.removeStatement(stm);
			String query="ASK {?o <"+NAMESPACE_HAS_SERVICE+"> <"+nsser+">}";
			boolean existe=sesame.ask(query);
			if(!existe){
				stm=new StatementImpl(new URIImpl(nsser), new URIImpl(NAMESPACE_HAS_OPERATION),new URIImpl(NAMESPACE_DATA+"OP"+operation));
				sesame.removeStatement(stm);
				sesame.removeStatement(nsser);
				query="ASK {?o <"+NAMESPACE_HAS_OPERATION+"> <"+NAMESPACE_DATA+"OP"+operation+">}";
				existe=sesame.ask(query);
				if(!existe){
					sesame.removeStatement(NAMESPACE_DATA+"OP"+operation);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}

	public List<Service> retrieveServicesOf(String BPMN_id){
		List<Service> results = new ArrayList<Service>();
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			String query="SELECT DISTINCT ?s ?u ?o WHERE {<"+nspmn+"> <"+NAMESPACE_HAS_SERVICE+"> ?s.?s <"+NAMESPACE_HAS_OPERATION+"> ?o.?s <"+NAMESPACE_RDFS+"isDefinedBy> ?u}";
			Set<BindingSet> rs=sesame.queryTuple(query);
			Iterator<BindingSet> ite=rs.iterator();

			int i=0;
			while(ite.hasNext()){
				BindingSet st=ite.next();
				Service service = new Service();
				service.setURI(st.getValue("s").stringValue());
				service.setWSDLURL(st.getValue("u").stringValue());
				service.setOperation(st.getValue("o").stringValue());
				service.setBound(false);
				results.add (service);
				i++;
			}
			
			Service selectedService = getSelectedService(BPMN_id);
			if (selectedService != null)
				for (Service s: results){
					if (s.getURI().equals(selectedService.getURI())){
						s.setBound(true);
						break;
					}
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}

	public void deleteAnnotation(String BPMN_id, String type, String annotation){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			String relation="";

			if(type.equals("REQUIREMENT")){
				relation=NAMESPACE_HAS_REQUIREMENT;
			}else if(type.equals("REFERENCE")){
				relation=NAMESPACE_HAS_REFERENCE;
			}else if(type.equals("CONTEXTUAL_INFORMATION")){
				relation=NAMESPACE_HAS_CONTEXTUAL_INFORMATION;
			}else if(type.equals("FUNTIONAL_CLASSIFICATION")){
				relation=NAMESPACE_HAS_FUNTIONAL_CLASSIFICATION;
			}else if(type.equals("NON_FUNCTIONAL_PROPERTY")){
				relation=NAMESPACE_HAS_NON_FUNCTIONAL_PROPERTY;
			}else if(type.equals("METADATA")){
				relation=NAMESPACE_HAS_METADATA;
			}else if(type.equals("INPUT_METADATA")){
				relation=NAMESPACE_HAS_INPUT_METADATA;
			}else if(type.equals("OUTPUT_METADATA")){
				relation=NAMESPACE_HAS_OUTPUT_METADATA;
			}

			Statement stm=new StatementImpl(new URIImpl(nspmn), new URIImpl(relation),new URIImpl(annotation));
			sesame.removeStatement(stm);
			boolean existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_REQUIREMENT+"> ?o}");
			if(!existe){
				existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_REFERENCE+"> ?o}");
				if(!existe){
					existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_CONTEXTUAL_INFORMATION+"> ?o}");
					if(!existe){
						existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_FUNTIONAL_CLASSIFICATION+"> ?o}");
						if(!existe){
							existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_NON_FUNCTIONAL_PROPERTY+"> ?o}");
							if(!existe){
								existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_METADATA+"> ?o}");
								if(!existe){
									existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_INPUT_METADATA+"> ?o}");
									if(!existe){
										existe=sesame.ask("ASK {<"+nspmn+"> <"+NAMESPACE_HAS_OUTPUT_METADATA+"> ?o}");
										if(!existe){
											sesame.removeStatement(nspmn);
										}
									}
								}																
							}									
						}									
					}				
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean equals(String uri1,String uri2){
		if(uri1.indexOf("http://")>-1 && uri1.indexOf("<")<0 && uri1.indexOf("_:")<0)
			uri1="<"+uri1+">";
		else
			uri1="_:"+uri1;
		
		if(uri2.indexOf("http://")>-1 && uri2.indexOf("<")<0 && uri2.indexOf("_:")<0)
			uri2="<"+uri2+">";
		else
			uri2="_:"+uri2;
		
		if(!uri1.equals(uri2)){
			return false;
		}else{
			return sesame.ask("ASK {"+uri1+" <"+NAMESPACE_RDF+"type> <"+NAMESPACE_OWL+"Class>}");
		}
	}
	
	public boolean subClassOf(String node,String antecesor){
		if(node.indexOf("http://")>-1 && node.indexOf("<")<0 && node.indexOf("_:")<0)
			node="<"+node+">";
		else
			node="_:"+node;
				
		String query="SELECT ?f where {"+node+" <"+NAMESPACE_RDFS+"subClassOf> ?f}";
		Set<BindingSet> rs=sesame.queryTuple(query);
		if(rs.size()==0)
			return false;
		
		Iterator<BindingSet> ite=rs.iterator();
		while(ite.hasNext()){
			BindingSet bs=ite.next();
			if(bs.getValue("f").stringValue().equals(antecesor))
				return true;
		}
		ite=rs.iterator();
		while(ite.hasNext()){
			BindingSet bs=ite.next();
			if(!bs.getValue("f").stringValue().equals(node.replaceAll("_:", "")))
				return subClassOf(bs.getValue("f").stringValue(), antecesor);
			
		}
		return false;
	}
	
	public boolean superClassOf(String node,String sucesor){
		if(node.indexOf("http://")>-1 && node.indexOf("<")<0 && node.indexOf("_:")<0)
			node="<"+node+">";
		else
			node="_:"+node;

		
		String query="SELECT ?f where {?f <"+NAMESPACE_RDFS+"subClassOf> "+node+"}";
		Set<BindingSet> rs=sesame.queryTuple(query);
		if(rs.size()==0)
			return false;
		
		Iterator<BindingSet> ite=rs.iterator();
		while(ite.hasNext()){
			BindingSet bs=ite.next();
			if(bs.getValue("f").stringValue().equals(sucesor))
				return true;
		}
		ite=rs.iterator();
		while(ite.hasNext()){
			BindingSet bs=ite.next();
			return superClassOf(bs.getValue("f").stringValue(), sucesor);
		}
		return false;
	}	

	public String[] retrieveAnnotationOf(String BPMN_id, String type){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			String relation="";
			String query="";

			if(!type.equals("ALL")){
				if(type.equals("REQUIREMENT")){
					relation=NAMESPACE_HAS_REQUIREMENT;
				}else if(type.equals("REFERENCE")){
					relation=NAMESPACE_HAS_REFERENCE;
				}else if(type.equals("CONTEXTUAL_INFORMATION")){
					relation=NAMESPACE_HAS_CONTEXTUAL_INFORMATION;
				}else if(type.equals("FUNTIONAL_CLASSIFICATION")){
					relation=NAMESPACE_HAS_FUNTIONAL_CLASSIFICATION;
				}else if(type.equals("NON_FUNCTIONAL_PROPERTY")){
					relation=NAMESPACE_HAS_NON_FUNCTIONAL_PROPERTY;
				}else if(type.equals("METADATA")){
					relation=NAMESPACE_HAS_METADATA;
				}else if(type.equals("INPUT_METADATA")){
					relation=NAMESPACE_HAS_INPUT_METADATA;
				}else if(type.equals("OUTPUT_METADATA")){
					relation=NAMESPACE_HAS_OUTPUT_METADATA;
				}
				query="SELECT ?o WHERE {<"+nspmn+"> <"+relation+"> ?o}";
			}else{
				query="SELECT ?o WHERE {{<"+nspmn+"> <"+NAMESPACE_HAS_REQUIREMENT+"> ?o} UNION " +
				"{<"+nspmn+"> <"+NAMESPACE_HAS_REFERENCE+"> ?o} UNION " +
				"{<"+nspmn+"> <"+NAMESPACE_HAS_CONTEXTUAL_INFORMATION+"> ?o} UNION " +
				"{<"+nspmn+"> <"+NAMESPACE_HAS_FUNTIONAL_CLASSIFICATION+"> ?o} UNION " +
				"{<"+nspmn+"> <"+NAMESPACE_HAS_NON_FUNCTIONAL_PROPERTY+"> ?o} UNION " +
				"{<"+nspmn+"> <"+NAMESPACE_HAS_INPUT_METADATA+"> ?o} UNION " +
				"{<"+nspmn+"> <"+NAMESPACE_HAS_OUTPUT_METADATA+"> ?o} UNION " +
				"{<"+nspmn+"> <"+NAMESPACE_HAS_METADATA+"> ?o}}";
			}
			Set<BindingSet> rs=sesame.queryTuple(query);
			String[] result=new String[rs.size()];
			Iterator<BindingSet> ite=rs.iterator();
			int i=0;
			while(ite.hasNext()){
				BindingSet st=ite.next();
				result[i]=st.getValue("o").stringValue();
				i++;
			}

			return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean removeSelectedService(String BPMN_id){
		String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
		String query="SELECT DISTINCT ?s WHERE {<"+nspmn+"> <"+NAMESPACE_HAS_SELECTED_SERVICE+"> ?s}";
		Set<BindingSet> rs=sesame.queryTuple(query);

		try{

			if(rs.size()>0){
				Iterator<BindingSet> ite=rs.iterator();
				while(ite.hasNext()){
					BindingSet st=ite.next();
					sesame.removeStatement(new StatementImpl(new URIImpl(nspmn),new URIImpl(NAMESPACE_HAS_SELECTED_SERVICE),new URIImpl(st.getValue("s").stringValue())));
				}
			}
		}catch(Exception e){
			return false;
		}

		return true;
	}
	
	public void selectService(String BPMN_id, String nsser){
//		String nsser=NAMESPACE_DATA+"SRV"+AeSimpleMD5.MD5(url+operation);
		String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);

		String query="SELECT DISTINCT ?s WHERE {<"+nspmn+"> <"+NAMESPACE_HAS_SELECTED_SERVICE+"> ?s}";
		Set<BindingSet> rs=sesame.queryTuple(query);
		boolean noadd=false;

		if(rs.size()>0){
			Iterator<BindingSet> ite=rs.iterator();
			while(ite.hasNext()){
				BindingSet st=ite.next();
				if(!st.getValue("s").stringValue().trim().equals(nsser))
					sesame.removeStatement(new StatementImpl(new URIImpl(nspmn),new URIImpl(NAMESPACE_HAS_SELECTED_SERVICE),new URIImpl(st.getValue("s").stringValue())));
				else
					noadd=true;

			}
		}
		if(!noadd)
			sesame.addStatement(new URIImpl(nspmn), new URIImpl(NAMESPACE_HAS_SELECTED_SERVICE), nsser);
	} 

	public Service getSelectedService (String BPMN_id){
		try{
			String nspmn=NAMESPACE_DATA+"ID"+AeSimpleMD5.MD5(BPMN_id);
			String query="SELECT DISTINCT ?s ?u ?o WHERE {<"+nspmn+"> <"+NAMESPACE_HAS_SELECTED_SERVICE+"> ?s.?s <"+NAMESPACE_HAS_OPERATION+"> ?o.?s <"+NAMESPACE_RDFS+"isDefinedBy> ?u}";
			Set<BindingSet> rs=sesame.queryTuple(query);

			Iterator<BindingSet> ite=rs.iterator();
			if(ite.hasNext()){

				String[] result=new String[2];

				BindingSet st=ite.next();
				Service service = new Service();
				service.setURI(st.getValue("s").stringValue());
				service.setWSDLURL(st.getValue("u").stringValue());
				service.setOperation(st.getValue("o").stringValue());
				service.setBound(true);
				
				return service;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}
	
	
	/*
	public String[][] retrieveElementsAnnotatedWith(String uri_annotation){
		String query="SELECT DISTINCT ?o ?p WHERE {?o ?p <"+uri_annotation+">}";
		Set<BindingSet> rs=sesame.queryTuple(query);
		Vector<String[]> result=new Vector<String[]>();
		Iterator<BindingSet> ite=rs.iterator();
		while(ite.hasNext()){
			BindingSet st=ite.next();
			if(st.getValue("p").stringValue().equals(NAMESPACE_HAS_REQUIREMENT) ||
					st.getValue("p").stringValue().equals(NAMESPACE_HAS_REFERENCE) ||
					st.getValue("p").stringValue().equals(NAMESPACE_HAS_CONTEXTUAL_INFORMATION) ||
					st.getValue("p").stringValue().equals(NAMESPACE_HAS_FUNTIONAL_CLASSIFICATION) ||
					st.getValue("p").stringValue().equals(NAMESPACE_HAS_NON_FUNCTIONAL_PROPERTY) ||
					st.getValue("p").stringValue().equals(NAMESPACE_HAS_METADATA)){
				String o=st.getValue("o").stringValue();
				o=o.replace(NAMESPACE_DATA+"ID", "");
				String[] r=new String[]{o,st.getValue("p").stringValue()};
				result.add(r);
			}
		}
		String[][] data=new String[result.size()][2];
		for(int j=0;j<result.size();j++){
			data[j][0]=result.get(j)[0];
			data[j][1]=result.get(j)[1];
		}

		return data;
	}
	 */
}
