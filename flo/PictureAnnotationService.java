package fr.uga.miashs.album.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;
import org.apache.openejb.jee.oejb2.EjbRelationshipRoleType.RelationshipRoleSource;

public class PictureAnnotationService {
	
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
		
		System.out.println("connection à la db jena");
		String serviceURI = "http://localhost:3030/ALBUM/";
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		System.out.println("upload du triplet");
		accessor.add(m);
		System.out.println("triplet ajouté");
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
	
	public static ArrayList<String> execSelectAndReturnUris(String query,String selector){
		ArrayList<String> list = new ArrayList<String>();
		String serviceURI = "http://localhost:3030/ALBUM";
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		//ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get(selector);
			list.add(x.toString());
		}
		return list;
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