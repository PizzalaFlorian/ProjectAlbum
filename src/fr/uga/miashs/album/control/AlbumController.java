package fr.uga.miashs.album.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.hsqldb.lib.HashSet;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@Named
@RequestScoped
public class AlbumController {

	@Inject
	private AppUserSession appUserSession;
	
	@Inject
	private AlbumService albumService;

	
	private Album album;
	private Picture pictures;
	
	public Picture getPictures() {
		return pictures;
	}



	public void setPictures(Picture pictures) {
		this.pictures = pictures;
	}



	public Album getAlbum() {
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		return album;
	}
		

	
	public String createAlbum() {
		Album album = getAlbum();
		try {
			albumService.create(album);
			savePictures(album);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.list_album;
	}
	
	private void savePictures(Album album) {
		Set<Picture> res=null;
		res.add(pictures);
		album.setPictures(res);
		
	}



	public List<Album> getListAlbumOwnedByCurrentUser() {
		try {
			return albumService.listAlbumOwnedBy(appUserSession.getConnectedUser());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String delete (Album a){
		albumService.delete(a);
		return Pages.list_album;
	}
	
	
// *******************************************	Fonction persos **********************************************
	public String getPictures(Album a){
		Set<Picture> set = a.getPictures();
		return Pages.list_album;
	}
	
	public String setPictures(Album a,Set<Picture> set){
		a.setPictures(set);
		return Pages.list_album;
	}
	
	public int getNumberPictures(Album a){
		return a.getNumberPictures();
	}
}
