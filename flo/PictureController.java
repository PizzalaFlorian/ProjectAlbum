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
import fr.uga.miashs.album.util.Pages;

@Named
public class PictureController {
	
    @Inject
    private PictureService pictureService;
    
    @Inject 
    public PictureAnnotationService pictureAnnotationService;
    
    private Picture selectedPicture;
    
    public PictureController(){
    	pictureService = new PictureService();
    }
    
    public Picture getSelectedPicture() {
        return selectedPicture;
    }
    
    public Picture getPictureById(long id){
    	System.out.println("refresh");
    	return pictureService.getPictureById(id);
    }

    public void createPicture(Path path, String filename, Album album) {
    	System.out.println("debut create picture");
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
		System.out.println(pictureId);
		//pictureService.deleteById(pictureId);
	}
		    
}
