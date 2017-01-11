package fr.uga.miashs.album.control;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import com.github.andrewoma.dexx.collection.HashSet;
import com.github.andrewoma.dexx.collection.Set;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.AlbumService;

public class PictureController {
	
	@Inject
	private AppUserSession appUserSession;
	
	@Inject
	private AlbumService albumService;
	
	
	private Album album;
	
	//private List<Album> albums;
	private Album selectedAlbum;
	
	
	public Album getAlbum() {
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		return album;
	}
	
	private List<Picture> pictures;
	
	
	private List<Path> photos;
	    
	@PostConstruct
	public void init() {
		System.out.println("start init");
		pictures = (List<Picture>) album.getPictures();
		System.out.println();
	    photos = new ArrayList<Path>();
	    //pictures.forEach(p -> photos.add(p.getLocalfile()));
	}
	
	public List<Path> getPhotos() {
	    return photos;
	}
	
	public Album getAlbumById(long albumId) {
        return albumService.getAlbumById(albumId);
    }
}
