package eu.atosresearch.seiplab.kb.sesame;

import java.io.File;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.query.BindingSet;

public interface ISesame {
	public void addRDF(String rdf);
	public void addTurtle(String turtle);
	public void addTurtleFromFile(String file);
	public void addRDFXMLFromFile(String file);
	public void clearRepository();
	public void addRDF(File file);
	public Set<Statement> query(String query);
	public Set<BindingSet> queryTuple(String query);
	public boolean ask(String query);
	public void addStatements(Set<Statement> triples);
	public void removeStatement(String uri);
	public void removeStatement(Statement st);
	public String queryToJSON(String query);
	public void addStatement(URI subject, URI predicate, String object);
}
