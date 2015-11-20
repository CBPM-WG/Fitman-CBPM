package eu.atosresearch.seiplab.kb.sesame;

import info.aduna.iteration.Iterations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

public class SesameRepository implements ISesame {

	Repository myRepository;
	RepositoryConnection con;
	private static final String CONTEXT = "http://webn1.atosresearch.eu/data";

	public SesameRepository() {
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream("/sesame.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		myRepository = new HTTPRepository(properties
				.getProperty("sesame.server"), properties
				.getProperty("sesame.repository"));
		try {
			myRepository.initialize();
		} catch (RepositoryException e) {
			throw new RuntimeException("Repository did not initialize ", e);
		}
	}

	public void addRDF(File file) {
		try {
			try {
				con = myRepository.getConnection();
				con.add(file, null, RDFFormat.RDFXML, new URIImpl(CONTEXT));
			} catch (RDFParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	public void addRDF(String rdf) {
		try {
			try {
				con = myRepository.getConnection();
				InputStream is = new ByteArrayInputStream(rdf.getBytes());
				con.add(is, null, RDFFormat.RDFXML, new URIImpl(CONTEXT));
			} catch (RDFParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addTurtleFromFile(String file) {
		try {
			try {
				con = myRepository.getConnection();
				FileInputStream fis=new FileInputStream(file);
				con.add(fis, null, RDFFormat.TURTLE, new URIImpl(CONTEXT));
			} catch (RDFParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	public void addRDFXMLFromFile(String file) {
		try {
			try {
				con = myRepository.getConnection();
				FileInputStream fis=new FileInputStream(file);
				con.add(fis, null, RDFFormat.RDFXML, new URIImpl(CONTEXT));
			} catch (RDFParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	

	public void addStatement(URI subject, URI predicate, String object) {
		ValueFactory f = myRepository.getValueFactory();
		try {
			try {
				con = myRepository.getConnection();
				Literal objl = f.createLiteral(object);
				try {
					new java.net.URL(object);
					URI obj = f.createURI(object);
					con.add(subject, predicate, obj, new URIImpl(CONTEXT));
				} catch (MalformedURLException e) {
					con.add(subject, predicate, objl, new URIImpl(CONTEXT));
				}
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
	}

	public void addStatements(Set<Statement> triples) {
		try {
			try {
				con = myRepository.getConnection();
				con.add(triples, new URIImpl(CONTEXT));
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	public void clearRepository() {
		try {
			try {
				con = myRepository.getConnection();
				con.clear(new URIImpl(CONTEXT));
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<BindingSet> queryTuple(String query){
		RepositoryConnection con;
		TupleQueryResult graphResult = null;
		Set<BindingSet> statements = new HashSet<BindingSet>();
		try {
			con = myRepository.getConnection();
			try {
				graphResult = con
				.prepareTupleQuery(QueryLanguage.SPARQL, query)
				.evaluate();

				while (graphResult.hasNext()) {
					statements.add(graphResult.next());
				}
			} catch (QueryEvaluationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedQueryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return statements;		
	}

	public Set<Statement> query(String query) {
		RepositoryConnection con;
		GraphQueryResult graphResult = null;
		Set<Statement> statements = new HashSet<Statement>();
		try {
			con = myRepository.getConnection();
			try {
				graphResult = con
				.prepareGraphQuery(QueryLanguage.SPARQL, query)
				.evaluate();

				while (graphResult.hasNext()) {
					statements.add(graphResult.next());
				}
			} catch (QueryEvaluationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedQueryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return statements;
	}
	
	public boolean ask(String query){
		RepositoryConnection con;
		boolean response=false;
		try {
			con = myRepository.getConnection();
			try {
				response = con
				.prepareBooleanQuery(QueryLanguage.SPARQL, query)
				.evaluate();

			} catch (QueryEvaluationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedQueryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return response;		
	}

	public String queryToJSON(String query) {
		RepositoryConnection con = null;
		String result = "";
		OutputStream out = new ByteArrayOutputStream();
		try {
			try {
				con = myRepository.getConnection();
				SPARQLResultsJSONWriter sparqlResultsJSONWriter = new SPARQLResultsJSONWriter(
						out);
				TupleQuery tupleQuery = con.prepareTupleQuery(
						QueryLanguage.SPARQL, query);
				tupleQuery.evaluate(sparqlResultsJSONWriter);
				result = out.toString();
			} catch (MalformedQueryException e) {
				e.printStackTrace();
			} catch (QueryEvaluationException e) {
				e.printStackTrace();
			} catch (TupleQueryResultHandlerException e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	 

	public Set<Statement> remove(String context, String query) {

		Set<Statement> statements = this.query(query);
		try {
			try {
				con = myRepository.getConnection();
				con.remove(statements, new URIImpl(context));
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return statements;
	}

	public void removeStatement(String uri) {
		RepositoryResult<Statement> statements = null;
		try {
			try {
				con = myRepository.getConnection();
				statements = con.getStatements(new URIImpl(uri), null, null,
						true);
				List<Statement> aboutURI = Iterations.addAll(statements,
						new ArrayList<Statement>());
				con.remove(aboutURI);
			} finally {
				statements.close();
				con.close();
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void removeStatement(Statement st) {
		// TODO Auto-generated method stub
		try {
			try {
				con = myRepository.getConnection();
				con.remove(st, new URIImpl(CONTEXT));
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}		
	}

	
	public void addTurtle(String turtle) {
		// TODO Auto-generated method stub
		try {
			try {
				con = myRepository.getConnection();
				InputStream is = new ByteArrayInputStream(turtle.getBytes());
				con.add(is, null, RDFFormat.TURTLE, new URIImpl(CONTEXT));
			} catch (RDFParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}		
		
	}
}

