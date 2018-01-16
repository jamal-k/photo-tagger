import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
 * Singleton design pattern.
 * Manages the saving, loading, and storing of all images encountered.
 */
public class ImageManagerSingleton implements Serializable{
	public static ImageManagerSingleton im = new ImageManagerSingleton(new File("CONFIG\\Serialize.txt"));
	public Map<String, ImageFile> images;
	public String path;
	
	/*
	 * Instantiates the ImageManagerSingleton.
	 */
	private ImageManagerSingleton(File f){
		try {
			f.createNewFile();
		} catch (IOException e) {
			
		}
		images = new HashMap<String, ImageFile>();
		path = f.getAbsolutePath();
	}
	
	/*
	 * returns the ImageManagerSingleton object. 
	 * 
	 * @return 	im	ImageManagerSingleton object
	 */
	public static ImageManagerSingleton getInstance() {
		return im;
	}
	
	/*
	 * Sets the path for the file to serialize to.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/*
	 * Returns HashMap of ImageFiles stored in the ImageManager.
	 * 
	 * @returns 	HashMap of ImageFile Objects	
	 */
	public static HashMap<String, ImageFile> getImageFiles() {
		return (HashMap<String, ImageFile>) im.images;
	}
	
	/*
	 * Reads from the path in ImageManager.
	 */
	public static void readFromFile () throws IOException, ClassNotFoundException, EOFException {
		ObjectInputStream o = null;
		try {
			FileInputStream f = new FileInputStream(im.path);
			o = new ObjectInputStream(f);
			ImageManagerSingleton imSaved = (ImageManagerSingleton) o.readObject();
			im = imSaved;
		} catch (EOFException e) {
		} finally {
			if (o != null) {
				o.close();
			}
		}
	}
	
	/*
	 * Adds ImageFile to HashMap images.
	 * 
	 * @param	image	ImageFile to add
	 */
	public static void add (ImageFile image) throws IOException {
		im.images.put(image.getNode().getOGName(), image);
		ImageManagerSingleton.saveToFile();
	}
	
	/*
	 * Removes HashMap entry with key equal to fileNode.
	 * 
	 * @param	fileNode	the fileNode key with which to remove the entry
	 */
	public static void remove (String ogName) {
		im.images.remove(ogName);
	}
	
	/*
	 * Saves the HashMap images to the path in ImageManager.
	 */
	public static void saveToFile () throws IOException {
		OutputStream file = new FileOutputStream(im.path);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		output.writeObject(im);
		output.close();
	}
}
