package model;
import java.nio.file.Path;
import java.util.*;

public class Album {
 
	private String nom;
	
	private List<Path> pictures;
	
	public Album() {
		pictures=new ArrayList<Path>();
	}

	public List<Path> getPictures() {
		return pictures;
	}

	public void addPicture(Path picture) {
		this.pictures.add(picture);
	}

	public Album(String n) {
		nom=n;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
