import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
public class Album {

	private String nomAlbum;
	
	private List<String> pictures;

	public String getNomAlbum() {
		return nomAlbum;
	}
	
	

	public void setNomAlbum(String nomAlbum) {
		this.nomAlbum = nomAlbum;
		Path rootDir = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("directory"));
		Path albumPath = rootDir.resolve(nomAlbum);
		/*if (!Files.exists(albumPath)) {
			//FacesContext.getCurrentInstance().
			//response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return "";
		}*/
		pictures = new ArrayList<String>();
		try {
			Files.list(albumPath).forEach(p -> pictures.add(rootDir.relativize(p).toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public List<String> getPictures() {
		return pictures;
	}
	
	
}
