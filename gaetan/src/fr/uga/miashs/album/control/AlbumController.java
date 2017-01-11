package fr.uga.miashs.album.control;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import com.github.andrewoma.dexx.collection.HashSet;
import com.github.andrewoma.dexx.collection.Set;

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
	
	//private List<Album> albums;
	private Album selectedAlbum;
	
	
	public Album getAlbum() {
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		return album;
	}	
	
	public String createAlbum() throws IOException {
		Path rootDir = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("directory"));
		Path albPath = rootDir.resolve(appUserSession.getConnectedUser().toString()).resolve(album.getTitle());
		try {
			albumService.create(album);
			Files.createDirectories(albPath);
			saveFiles(new ZipInputStream(zip.getInputStream()), albPath, album);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.list_album;
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
	
	private Part zip;
	
	public Part getZip() {
		return zip;
	}

	public void setZip(Part zip) {
		this.zip = zip;
	}
	
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
	
	private void saveFiles(ZipInputStream zis, Path whereDir, Album album) {
		ZipEntry ent = null;
		try {
			while ((ent = zis.getNextEntry()) != null) {
				// si repertoire, on ne fait rien
				if (ent.getName().endsWith("/"))
					continue;
				int idx = ent.getName().lastIndexOf('/');
				String filename = ent.getName();
				if (idx > -1) {
					filename = ent.getName().substring(idx + 1);
				}
				// si fichier cachÃ©, on ne fait rien non plus
				if (filename.charAt(0) == '.')
					continue;
				try {

					Path path = whereDir.resolve(filename);
					linkPictures(path, filename, album);
					// Files.createFile(path);
					OutputStream bos = new BufferedOutputStream(Files.newOutputStream(path));
					byte[] b = new byte[4094];
					int read = -1;
					while ((read = zis.read(b)) != -1) {
						bos.write(b, 0, read);
					}
					bos.close();
					zis.closeEntry();
					// determin le type mime en fonction de l'extension du
					// fichier (en minuscule)
					String mime = FacesContext.getCurrentInstance().getExternalContext()
							.getMimeType(path.toString().toLowerCase());
					// si le type mime du fichier n'est pas une image, on la
					// supprime.
					if (mime == null || !mime.startsWith("image/"))
						Files.delete(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/*
	picturesPath = new ArrayList<String>();
	try {
		Files.list(albumPath).forEach(p -> pictures.add(rootDir.relativize(p).toString()));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
	
	private void linkPictures(Path path, String filename, Album album){
		Picture picture = new Picture();
		picture.setAlbum(album);
		picture.setTitle(filename);
		picture.setLocalfile(path);
		String id = "http://www.semanticweb.org/pitures/"+filename;
		try {
			URI uri = new URI(id);
			picture.setUri(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		album.addPicture(picture);
	}
	
	public Album getAlbumById(long albumId) {
        return albumService.getAlbumById(albumId);
    }

    public Album getSelectedAlbum() {
        return selectedAlbum;
    }
 
    public void setSelectedAlbum(Album selectedAlbum) {
        this.selectedAlbum = selectedAlbum;
    }
	
	private List<Path> photos;
	    
	public List<Path> getPhotos(HashSet<Picture> pictures) {
		System.out.println("start init");
		List<Picture> listPictures = (List<Picture>) pictures;
	    photos = new ArrayList<Path>();
	    listPictures.forEach(p -> photos.add(p.getLocalfile()));
	    return photos;
	}
	
	public List<Path> getPhotos() {
	    return photos;
	}
}
