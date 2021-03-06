package fr.uga.miashs.album.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;


/*
 * L'annotation @Named permet de créer un bean CDI
 * Le bean CDI va remplacer les ManagedBean de JSF à partir de JSF 2.3
 * Leur intéret est qu'ils sont utilisables en dehors du contexte JSF.
 * On peut les utiliser aussi via l'annotation @Inject
 * Il faut faire attention que l'annotation @RequestScoped vienne bien du package 
 * javax.enterprise.context et non de l'ancien package javax.faces.bean
 */

@Named
@RequestScoped //implique que l'obj appUserController sera détruit en fin de requete
public class AppUserController implements Serializable {

	@Inject
	private AppUserService appUserService;
	
	private AppUser user;
	
	private AppUser selectedUser;
	
	public AppUserController() {
		System.out.println("coucou");
		user = new AppUser();
		selectedUser = new AppUser();
	}	
	
	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}	
	
	public AppUser getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(AppUser user) {
		this.selectedUser = user;
	}

	public String create() {
		try {
			appUserService.create(user);
		} catch (ServiceException e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(e.getLocalizedMessage());
			facesContext.addMessage("email", facesMessage);
			return null;
		}
		
		return Pages.list_user;
		
	}
	
	public String abord(){
		return Pages.login;
	}
	
	public boolean getRole(){
		boolean u = user.getRole();
		System.out.println(u);
		return u;
	}
	
	public String delete(long userId) {
		appUserService.deleteById(userId);
		
		return Pages.list_user;
	}
	
}
