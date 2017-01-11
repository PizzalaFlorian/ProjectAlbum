package fr.uga.miashs.album.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name="Album.findAllOwned",
                query="SELECT a FROM Album a WHERE a.owner=:owner"),
})
public class Album {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String title;
	
	private String description;
	
	@NotNull
	@ManyToOne
	private AppUser owner;
	
	@ManyToMany
	private Set<AppUser> sharedWith;
	
	@OneToMany(mappedBy="album")
	private Set<Picture> pictures = new HashSet<Picture>();;
	
	
	// private List<String> picturesPath;

	protected Album() {
	
	}
	
	public Album(AppUser owner) {
		this.owner=owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		Path rootDir = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("directory"));
		Path albumPath = rootDir.resolve(title);
		/*
		picturesPath = new ArrayList<String>();
		try {
			Files.list(albumPath).forEach(p -> picturesPath.add(rootDir.relativize(p).toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public Set<AppUser> getSharedWith() {
		return sharedWith;
	}

	public Set<Picture> getPictures() {
		return pictures;
	}
	
	public void addPicture(Picture picture){
		System.out.println(picture.getAlbum());
		System.out.println(picture.getTitle());
		System.out.println(picture.getLocalfile());
		System.out.println(picture.getUri());
		System.out.println(picture);
		System.out.println(this.pictures);
		this.pictures.add(picture);
		
	}
}
