
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetPicture
 */
@WebServlet("/pictures/*")
public class GetPicture extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetPicture() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pictureFile = request.getPathInfo();
		Path rootDir = Paths.get(getServletContext().getInitParameter("directory"));

		Path picturePath = rootDir.resolve(pictureFile.substring(1));

		if (!Files.exists(picturePath)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType(getServletContext().getMimeType(picturePath.toString().toLowerCase()));

		InputStream is = Files.newInputStream(picturePath);

		String largeur = request.getParameter("largeur");
		if (largeur != null) {
			try {
				int width = Integer.parseInt(largeur);
				BufferedImage bim = ImageIO.read(is);
				int height = (int) (bim.getHeight() * (((double) width) / bim.getWidth()));
				Image resizedImg = bim.getScaledInstance(width, height, Image.SCALE_FAST);
				BufferedImage rBimg = new BufferedImage(width, height, bim.getType());
				// Create Graphics object
				Graphics2D g = rBimg.createGraphics();

				// Draw the resizedImg from 0,0 with no ImageObserver
				g.drawImage(resizedImg, 0, 0, null);

				// Dispose the Graphics object, we no longer need it
				g.dispose();

				ImageIO.write(rBimg, "png", response.getOutputStream());

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {

			OutputStream os = response.getOutputStream();

			byte[] buf = new byte[4 * 1024];
			int read = 0;
			while ((read = is.read(buf)) != -1) {
				os.write(buf, 0, read);
			}
			is.close();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
