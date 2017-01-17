package fr.uga.miashs.album.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;

@Named
@ApplicationScoped
public class AppUserService extends JpaService<Long,AppUser> {

	@Override
	public void create(AppUser v) throws ServiceException {
		try {
			super.create(v);
		}
		catch (RollbackException e) {
			if (e.getCause() instanceof EntityExistsException) {
				throw new ServiceException("Un utilisateur avec l'email "+v.getEmail()+" existe déjà ",e);
			}
			else {
				new ServiceException(e);
			}
		}
	}

	public AppUser login(String email, String password) throws ServiceException {
		Query query = getEm().createNamedQuery("AppUser.login");
		query.setParameter("email", email);
		query.setParameter("password", password);
		try {
			return (AppUser) query.getSingleResult();
		}
		catch (NoResultException e) {
			throw new ServiceException("Utilisateur Inconnu",e);
		}
	}
	
	public List<AppUser> listUsers() {
		 Query query = getEm().createNamedQuery("AppUser.findAll");
		 return query.getResultList();
	}
	
	public AppUser getUserById(long id) {
        Query query = getEm().createNamedQuery("AppUser.findById");
        query.setParameter("id", id);
        return (AppUser) query.getSingleResult();
    }
}
