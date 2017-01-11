package fr.uga.miashs.album.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.Picture;

@ApplicationScoped
public class PictureService extends JpaService<Long,Picture> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void create(Picture pic) throws ServiceException {
        pic.setAlbum(getEm().merge( pic.getAlbum()));
        super.create(pic);
    }


    @SuppressWarnings("unchecked")
    public List<Picture> listPictureByAlbum(Album a) throws ServiceException {
        Query query = getEm().createNamedQuery("Picture.findByAlbum");
        query.setParameter("album", a);
        return query.getResultList();
    }

    public Picture getById(Integer id) {
        Query query = getEm().createNamedQuery("Picture.findById");
        query.setParameter("id", id);
        return (Picture) query.getSingleResult();
    }
}