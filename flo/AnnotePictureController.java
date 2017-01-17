package fr.uga.miashs.album.control;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.service.PictureAnnotationService;

@Named
@ManagedBean(value="annote")
@SessionScoped
public class AnnotePictureController {

	public String picUri;
	public String type;
	
    @Inject 
    public static PictureAnnotationService pictureAnnotationService;
    
    private final static String baseURI = "http://www.semanticweb.org/Projet/AlbumPhoto.owl#";
    private final static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    
    public void annotePictureToPhoto() throws URISyntaxException{
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		URI uriPhoto = new URI ( params.get("pictureUri") );
		String pictureId = params.get("pictureId");
		
		System.out.println("annoter comme photo from Picture Controller");
		
		URI uriClassPhoto = new URI(baseURI+"Photo");
		PictureAnnotationService.insertManualTriplet(uriPhoto, rdf+"type", uriClassPhoto);
		System.out.println("fin annotation");
	}
	
    public void annotePictureByForm() throws URISyntaxException{
    	if(picUri != null && type != null){
    		URI photo = new URI(picUri);
        	annotePictureType(photo,type);
    	}
    	else{
    		System.out.println("erreur arguments nulles");
    	}
    }
    
	public void annotePictureToPhoto(URI photo) throws URISyntaxException{
		System.out.println("debut enregistrement");
		annotePicture(photo, rdf+"type", baseURI+"Photo");
		System.out.println("fin enregistrement");
	}
	
	public void annotePictureType(URI photo, String type) throws URISyntaxException{
		switch(type){
		case "photo" 			: annotePictureToPhoto(photo); 			break;
		case "natureMorte" 		: annotePictureToNatureMorte(photo); 	break;
		case "paysage" 			: annotePictureToPaysage(photo); 		break;
		case "panorama" 		: annotePictureToPanorama(photo); 		break;
		case "photoAbstraite" 	: annotePictureToPhotoAbstraite(photo);	break;
		case "photoArt" 		: annotePictureToPhotoArt(photo); 		break;
		case "photoSport" 		: annotePictureToPhotoSport(photo); 	break;
		case "portrait" 		: annotePictureToPortrait(photo); 		break;
		case "selfie" 			: annotePictureToSelfie(photo); 		break;
		}
	}
	
	public void annotePictureToNatureMorte(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"#natureMorte");
	}
	
	public void annotePictureToPaysage(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"paysage");
	}
	
	public void annotePictureToPanorama(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"panorama");
	}
	
	public void annotePictureToPhotoAbstraite(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"photoAbstraite");
	}
	
	public void annotePictureToPhotoArt(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"photoArt");
	}
	
	public void annotePictureToPhotoSport(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"photoSport");
	}
	
	public void annotePictureToPortrait(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"portrait");
	}
	
	public void annotePictureToSelfie(URI photo) throws URISyntaxException{
		annotePicture(photo, rdf+"type", baseURI+"selfie");
	}
	
	public void annotePictureContient(URI photo,URI thing) throws URISyntaxException{
		annotePicture(photo,baseURI+"contient",thing);
	}
	
	public void annotePictureAEteCreerPar(URI photo,URI author) throws URISyntaxException{
		annotePicture(photo,baseURI+"aEteCreerPar",author);
	}
	
	public void annotePictureCreerLe(URI photo, String date) throws URISyntaxException{
		annotePicture(photo,baseURI+"creerLe",date);		
	}
	
	public void annotePicture(String uri_sujet, String predicat, String uri_objet) throws URISyntaxException{
		URI sujet = new URI(uri_sujet);
		URI objet = new URI(uri_objet);
		PictureAnnotationService.insertManualTriplet(sujet, predicat, objet);
	}
	
	public void annotePicture(URI uri_sujet, String predicat, String uri_objet) throws URISyntaxException{
		URI objet = new URI(uri_objet);
		PictureAnnotationService.insertManualTriplet(uri_sujet, predicat, objet);
	}
	
	public void annotePicture(URI uri_sujet, String predicat, URI uri_objet) throws URISyntaxException{
		PictureAnnotationService.insertManualTriplet(uri_sujet, predicat, uri_objet);
	}
	
	public static ArrayList<String> getPictureByQuery(String query,String selector){
		return PictureAnnotationService.execSelectAndReturnUris(query,selector);
	}
	
	public static ArrayList<String> getAllPictureByPersonURI(String PersonURI){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" SELECT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:contient "+PersonURI+"."
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureByPersonName(String firstName,String lastName){
		String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#> "
						+" SELECT ?image "
						+" WHERE { "
						+" ?image albumPhoto:aEteCreerPar ?person . "
						+" ?person foaf:firstName "+'"'+firstName+'"'+". "  
						+" ?person foaf:familyName "+'"'+lastName+'"'+". "
						+" }";
		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureCreateByPersonURI(String PersonURI){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" SELECT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:aEteCreerPar "+PersonURI+"."
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureCreateByPersonName(String firstName,String lastName){
		String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#> "
						+" SELECT ?image "
						+" WHERE { "
						+" ?image albumPhoto:aEteCreerPar ?person . "
						+" ?person foaf:firstName "+'"'+firstName+'"'+". "  
						+" ?person foaf:familyName "+'"'+lastName+'"'+". "
						+" }";
		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureContainTwoPersonByURI(String URIPerson1,String URIPerson2){
		String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#> "
						+" SELECT ?image "
						+" WHERE { "
						+" ?image albumPhoto:contient "+URIPerson1+" . "
						+" ?image albumPhoto:contient "+URIPerson2+" . "
						+" }";
		return getPictureByQuery(query,"image");
	} 
	
	public static ArrayList<String> getAllPictureContainTwoPersonByNames(String firstName1,String lastName1,String firstName2, String lastName2){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" SELECT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:contient ?personX . "
						+" ?personX foaf:firstName "+'"'+firstName1+'"'+" . "  
						+" ?personX foaf:familyName "+'"'+lastName1+'"'+" . "
						+"  ?image albumPhoto:contient ?personY. "
						+" ?personY foaf:firstName "+'"'+firstName2+'"'+" . "  
						+" ?personY foaf:familyName "+'"'+lastName2+'"'+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureContainSomePerson(){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX contact: <http://www.w3.org/2000/10/swap/pim/contact#> "
						+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:contient ?x. "
						+"  ?x rdf:type contact:Person. "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureNotContainPerson(){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX contact: <http://www.w3.org/2000/10/swap/pim/contact#> "
						+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:contient ?x. "
						+"   MINUS { "
						+"  SELECT ?image "
						+"  Where{ "
						+"	 	?image albumPhoto:contient ?x. "
						+"  	?x rdf:type contact:Person. "
						+"    } "
						+" } "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllFriendPictureByName(String firstName, String familyName){
		String query = " PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#> "
						+" PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:contient ?personne. "
						+"  ?personne albumPhoto:amiDe ?ami . "
						+"  ?ami foaf:firstName "+'"'+firstName+'"'+" ; "
						+"      foaf:familyName "+'"'+familyName+'"'+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureTakeOnEvent(){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:estLieeA ?x. "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureTakeOnEventDepictingAPersonByName(String firstName,String familyName){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:estLieeA ?x. "
						+"  ?image albumPhoto:contient ?personne . "
						+"  ?personne foaf:firstName "+'"'+firstName+'"'+" ; "
						+"      foaf:familyName "+'"'+familyName+'"'+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureByPlaceName(String Name){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:estLocalise ?lieu. "
						+"  ?lieu foaf:name "+'"'+Name+'"'+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureByRegionName(String Name){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX dbo: <http://dbpedia.org/ontology/> "
						+" PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:estLocalise ?lieu. "
						+"  ?lieu dbo:region ?region . "
						+"  ?region foaf:name "+'"'+Name+'"'+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureByObjectName(String Name){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
						+" PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:contient ?chose. "
						+"  ?objet rdfs:subClassOf* albumPhoto:Objet . "
						+"  ?chose rdf:type ?objet . "
						+"  ?chose foaf:name "+'"'+Name+'"'+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureByAnimalName(String Name){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
						+" PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
						+ " PREFIX dbo: <http://dbpedia.org/ontology/> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image albumPhoto:contient ?chose. "
						+"  ?animal rdfs:subClassOf* dbo:Eukaryote . "
						+"  ?chose rdf:type ?animal . "
						+"  ?chose foaf:name "+'"'+Name+'"'+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	public static ArrayList<String> getAllPictureType(String type){
		String query = "PREFIX albumPhoto: <http://www.semanticweb.org/Projet/AlbumPhoto.owl#>"
						+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+" SELECT DISTINCT ?image "
						+" WHERE { "
						+"  ?image rdf:type albumPhoto:"+type+" . "
						+" } ";

		return getPictureByQuery(query,"image");
	}
	
	
	public static void main(String[] args){
		System.out.println("test");
		//ArrayList<String> res = getAllPictureByPersonURI("<http://www.semanticweb.org/Projet/AlbumPhoto.owl#ManuelAtencia>");
		//ArrayList<String> res = getAllPictureByPersonName("Manuel","Atencia");
		//ArrayList<String> res = getAllPictureCreateByPersonURI("<http://www.semanticweb.org/Projet/AlbumPhoto.owl#FlorianPizzala>");
		//ArrayList<String> res = getAllPictureByPersonName("Florian","Pizzala");
		//ArrayList<String> res = getAllPictureContainTwoPersonByURI("<http://www.semanticweb.org/Projet/AlbumPhoto.owl#ManuelAtencia>","<http://www.semanticweb.org/Projet/AlbumPhoto.owl#FlorianPizzala>");
		//ArrayList<String> res = getAllPictureContainTwoPersonByNames("Manuel","Atencia","Florian","Pizzala");
		//ArrayList<String> res = getAllPictureContainSomePerson();
		//ArrayList<String> res = getAllPictureNotContainPerson();
		//ArrayList<String> res = getAllFriendPictureByUri("<http://www.semanticweb.org/Projet/AlbumPhoto.owl#GaetanRomagna>");
		//ArrayList<String> res = getAllFriendPictureByName("Gaetan","Romagna");
		//ArrayList<String> res = getAllPictureTakeOnEvent();
		//ArrayList<String> res = getAllPictureTakeOnEventDepictingAPersonByName("Gaetan","Romagna");
		//ArrayList<String> res = getAllPictureByPlaceName("Grenoble");
		//ArrayList<String> res = getAllPictureByRegionName("Isère");
		//ArrayList<String> res = getAllPictureByObjectName("stylo");
		//ArrayList<String> res = getAllPictureByAnimalName("chien");
		ArrayList<String> res = getAllPictureType("selfie");
		for(String s : res){
			System.out.println(s);
		}
	}
	
}
