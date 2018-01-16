import java.io.*;
import java.util.ArrayList;

/*
 * AllTags serializes all tags and implements singleton design pattern
 */
public class AllTags implements Serializable{
	//the instance of AllTags
	public static AllTags allTags = new AllTags(new File("CONFIG\\AllTags.txt"));
	public ArrayList<String> tags;
	public String path;

	/*
	 * AllTags constructor takes serialization file.
	 * 
	 * @param	f	File for serializing allTags
	 */
	private AllTags(File f){
		try {
			f.createNewFile();
		} catch (IOException e) {

		}
		tags = new ArrayList<String>();
		path = f.getAbsolutePath();
	}

	/*
	 * returns the allTags singleton object.
	 * 
	 * @return	AllTags	 all tags singleton object
	 */
	public static AllTags getInstance() {
		return allTags;
	}

	/*
	 * Adds a tag to the tags arraylist and serializes AllTags object.
	 * 
	 * @param	tag		the tag to add to tags 
	 */
	public void addTag(String tag) throws IOException {
		if (!tags.contains(tag)) {
			tags.add(tag);
		}
		AllTags.saveToFile();
	}

	/*
	 * Returns the tags ArrayList.
	 * 
	 * @return	returns the tags ArrayList	
	 */
	public ArrayList<String> getTags() {
		return tags;
	}

	/*
	 * Deserializes AllTags object from the file path.
	 */
	public static void readFromFile () throws IOException, ClassNotFoundException, EOFException {
		ObjectInputStream o = null;
		try {
			FileInputStream f = new FileInputStream(allTags.path);
			o = new ObjectInputStream(f);
			AllTags at = (AllTags) o.readObject();
			allTags = at;
		} catch (EOFException e) {
		} finally {
			if (o != null) {
				o.close();
			}
		}
	}

	/*
	 * Serializes AllTags object to the file Path.
	 */
	public static void saveToFile () throws IOException {
		OutputStream file = new FileOutputStream(allTags.path);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		output.writeObject(allTags);
		output.close();
	}
}
