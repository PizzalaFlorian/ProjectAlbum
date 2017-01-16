package fr.uga.miashs.album.control;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.service.PictureAnnotationService;

@Named
public class AnnotePictureController {

    @Inject 
    public PictureAnnotationService pictureAnnotationService;
    
    public void annotePictureToPhoto() throws URISyntaxException{
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		URI uriPhoto = new URI ( params.get("pictureUri") );
		String pictureId = params.get("pictureId");
		
		System.out.println("annoter comme photo from Picture Controller");
		
		URI uriClassPhoto = new URI("http://www.semanticweb.org/Projet/AlbumPhoto.owl#Photo");
		PictureAnnotationService.insertManualTriplet(uriPhoto, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", uriClassPhoto);
		System.out.println("fin annotation");
	}
	
	public void annotePictureToPhoto(URI photo) throws URISyntaxException{
		System.out.println("debut enregistrement");
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#Photo");
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
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#natureMorte");
	}
	
	public void annotePictureToPaysage(URI photo) throws URISyntaxException{
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#paysage");
	}
	
	public void annotePictureToPanorama(URI photo) throws URISyntaxException{
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#panorama");
	}
	
	public void annotePictureToPhotoAbstraite(URI photo) throws URISyntaxException{
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#photoAbstraite");
	}
	
	public void annotePictureToPhotoArt(URI photo) throws URISyntaxException{
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#photoArt");
	}
	
	public void annotePictureToPhotoSport(URI photo) throws URISyntaxException{
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#photoSport");
	}
	
	public void annotePictureToPortrait(URI photo) throws URISyntaxException{
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#portrait");
	}
	
	public void annotePictureToSelfie(URI photo) throws URISyntaxException{
		annotePicture(photo, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://www.semanticweb.org/Projet/AlbumPhoto.owl#selfie");
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
	
}
