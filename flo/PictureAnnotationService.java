package fr.uga.miashs.album.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;
import org.apache.openejb.jee.oejb2.EjbRelationshipRoleType.RelationshipRoleSource;

public class PictureAnnotationService {

	public void insertTriplet(URI sujet, String predicat, URI objet ){
		// SPARQL Update
		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
													"INSERT DATA {"+ sujet.toString()+" "+predicat+" "+objet.toString()+". ");
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	
	public void insertTriplet(URI sujet, String predicat, String objet ){
		// SPARQL Update
		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
													"INSERT DATA {"+ sujet.toString()+" "+predicat+" "+objet+". ");
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	
	//TODO fini query
	public void query(){
		// SPARQL Query
		Query query = QueryFactory.create("SELECT ?s  WHERE {?s a <http://imss.univ-grenoble-alpes.fr/ns/album#Picture>.}");
		  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode x = soln.get("s") ;       // Get a result variable by name.
		      Resource r = soln.getResource("p") ; // Get a result variable - must be a resource
		      Literal l = soln.getLiteral("o") ;   // Get a result variable - must be a literal
		      System.out.println(x+" "+r+" "+l);
		    }
		  }
	}
	
	/*Custom methode*/
	public static void insertManualTriplet(URI sujet, String predicat, URI objet ){
		System.out.println("creation model");
		Model m = ModelFactory.createDefaultModel();
		System.out.println("preparation du triplet");
		Resource r = m.createResource(sujet.toString());
		Property p = m.createProperty(predicat);
		Resource o = m.createResource(objet.toString());
		r.addProperty(p, o);
		System.out.println("triplet achevé");
		//File f = new File(predicat);
		//m.write(System.out,"RDF/XML");
		System.out.println("connection à la db jena");
		String serviceURI = "http://localhost:3030/ALBUM/";
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		//accessor.putModel(m);
		accessor.add(m);
		System.out.println("triplet ajouté");
	}
	
	
	
	public static void uploadRDF(File rdf)
			throws IOException {

		String serviceURI = "http://localhost:3030/ALBUM";
		// parse the file
		Model m = ModelFactory.createDefaultModel();
		try (FileInputStream in = new FileInputStream(rdf)) {
			m.read(in, null, "RDF/XML");
		}

		// upload the resulting model
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		accessor.putModel(m);
	}

	public static void execSelectAndPrint(String query) {
		String serviceURI = "http://localhost:3030/ALBUM";
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}

	public static void execSelectAndProcess(String query) {
		String serviceURI = "http://localhost:3030/ALBUM";
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			// assumes that you have an "?x" in your query
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}

		
}