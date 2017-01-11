import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;


@ManagedBean
public class ListeAlbums {
	
	
	public List<String> getAlbums() {
		
		Path rootDir = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("directory"));
		List<String> albums = new ArrayList<String>();
		try {
			Files.list(rootDir).filter(p -> Files.isDirectory(p)==true).forEach(p -> albums.add(p.getFileName().toString()));
			return albums;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

}
