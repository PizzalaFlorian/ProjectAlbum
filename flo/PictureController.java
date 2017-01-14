package fr.uga.miashs.album.control;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureAnnotationService;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;

@Named
@RequestScoped
public class PictureController {
	
    @Inject
    private PictureService pictureService;
    
    @Inject PictureAnnotationService pictureAnnotationService;
    
    public PictureController(){
    	pictureService = new PictureService();
    }
    
    public Picture getPictureById(long id){
    	System.out.println("refresh");
    	return pictureService.getPictureById(id);
    }

    public void createPicture(Path path, String filename, Album album) {
    	System.out.println("debut create picture");
		System.out.println(path);
		System.out.println(filename);
		System.out.println(album);
    	Picture picture = new Picture();
		picture.setAlbum(album);
		picture.setTitle(filename);
		picture.setLocalfile(path.toString());
		String id = "http://www.semanticweb.org/picture/"+album.getId()+"/"+filename;
		try {
			URI uri = new URI(id);
			picture.setUri(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("debut service picture");
			System.out.println(picture);
			pictureService.create(picture);
			System.out.println("fin service picture");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public List<Picture> getListPictureByCurrentAlbumId(long albumId) {
		System.out.println("get picture appelé");
		try {
			return pictureService.listPictureByAlbum(albumId);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void deletePictureById(long pictureId){
		System.out.println("delete picture");
		pictureService.deleteById(pictureId);
	}
	
	public void clickMe(){
		System.out.println("click");
	}
	
	public void annotePictureToPhoto() throws URISyntaxException{
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		URI uriPhoto = new URI ( params.get("pictureUri") );
		
		System.out.println("annoter comme photo from Picture Controller");
		
		URI uriClassPhoto = new URI("http://www.semanticweb.org/Projet/AlbumPhoto.owl#Photo");
		//pictureAnnotationService.insertTriplet(uriPhoto, "rdf:Class", uriClassPhoto);
		pictureAnnotationService.insertManualTriplet(uriPhoto, "rdf:Class", uriClassPhoto);
		System.out.println("fin annotation");
		
	}
	
	/*
	public void validerTitre(FacesContext ctx, UIComponent comp, Object value) {
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Path rootDir = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("directory"));
		Path albPath = rootDir.resolve((String) value);
		if (Files.exists(albPath)) {
			msgs.add(new FacesMessage("Un album avec le même nom existe déjà "));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}

	public void validerZip(FacesContext ctx, UIComponent comp, Object value) {
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		if (file.getSize() > 1024 * 1024 * 100) {
			msgs.add(new FacesMessage("file too big"));
		}
		if (!"application/zip".equals(file.getContentType())) {
			msgs.add(new FacesMessage("not a Zip file"));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}
	*/
	    
}
