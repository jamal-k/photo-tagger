import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.*;

/*
 * Represents an image and its characteristics.
 */
public class ImageFile implements Serializable {
	
	public FileNode fileNode;
	public ArrayList<String> tags;
	public static Logger nameLogger = Logger.getLogger(ImageFile.class.getName());
	public static Handler fh;
	public ArrayList<String> nameLog;
	
	/*
	 * Initialises an ImageFile Object.
	 * 
	 * @param	fileNode	the FileNode to initialise
	 */
	public ImageFile (FileNode fileNode) throws IOException {
		this.fileNode = fileNode;
		tags = new ArrayList<String>();
		nameLog = new ArrayList<String>();
		
		for (String tag : tags) {
			AllTags.getInstance().addTag(tag);
		}
		
		//getting the logger for this fileNode from Serialized file and assigning to this ImageFile
		if (ImageManagerSingleton.getInstance().images.containsKey(fileNode.getOGName())) {
			fh = ImageManagerSingleton.getInstance().images.get(getNode().getOGName()).fh;
			nameLogger = ImageManagerSingleton.getInstance().images.get(getNode().getOGName()).nameLogger;
		}
		
		//else creating a new logger
		else {
		try {
			fh = new FileHandler("CONFIG\\LOG.log", true);
			nameLogger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (IOException e) {
			
		}
		}
		
		nameLogger.log(Level.INFO, fileNode.getOGName() + " to " + fileNode.getPath());
		updateLog("Initialization", fileNode.getPath());
	}
	
	/*
	 * Adds the array of tags to tags.
	 * 
	 * @param	tagstoAdd	the array of tags to add
	 */
	public void addTags (String[] tagsToAdd) throws IOException {
		for (String t: tagsToAdd) {
			addTag(t);
		}
	}
	
	/*
	 * Adds the tag to the tags and changes the name of the image file to the new name.
	 * 
	 * @param	tag		the String to add to tags
	 */
	public void addTag(String tag) throws IOException {
		if (!tags.contains(tag)) {
			//removing this ImageFile from the Serialized file
			ImageManagerSingleton.remove(fileNode.getOGName());
			tags.add(tag);
			AllTags.getInstance().addTag(tag);
			
			//Renaming the File associated with this ImageFile object.
			String currPath = fileNode.getPath();
			String result = currPath.substring(0, currPath.lastIndexOf("."));
			result += " @" + tag;
			result += currPath.substring(currPath.lastIndexOf("."));

			fileNode.setPath(result);
			
			//Serializes this ImageFile
			ImageManagerSingleton.add(this);
			
			//Updates Logger
			nameLogger.log(Level.INFO, fileNode.getOGName() + " to " + result);
			updateLog(currPath, result);
		}
		System.out.println(AllTags.getInstance().getTags());
	}
	
	/*
	 * Removes the tags from tags.
	 * 
	 * @param	tagsToRemove	the array of tags to remove
	 */
	public void removeTags(String[] tagsToRemove) throws IOException {
		for (String t: tagsToRemove) {
			removeTag(t);
		}
	}
	
	/*
	 * Removes the tag from tags and changes the name of the image file to the new name.
	 * 
	 * @param	tag		the tag to remove
	 */
	public void removeTag(String tag) throws IOException {
		if (tags.contains(tag)) {
			ImageManagerSingleton.remove(fileNode.getOGName());
			tags.remove(tag);
			
			String currPath = fileNode.getPath();
			String result = currPath.replaceAll(" @" + tag, "");
			
			fileNode.setPath(result);
			ImageManagerSingleton.add(this);
			nameLogger.log(Level.INFO, fileNode.getOGName() + " to " + result);
			updateLog(currPath, result);
		}
	}
	
	/*
	 * Returns the FileNode of the ImageFile.
	 * 
	 * @return		the FileNode of the ImageFile
	 */
	public FileNode getNode() {
		return fileNode;
	}
	
	/*
	 * Reverts the name to a previous name.
	 * 
	 * @param	path	the path to revert to
	 */
	public void revertName(String path) throws IOException {
		ImageManagerSingleton.remove(fileNode.getOGName());
		
		String currPath = fileNode.getPath();
		
		File f = new File (path);
		
		//Retrieves tags from file name and removes tags that no longer exists from the tags ArrayList
		for (int i=0; i<fileNode.getName().length()-1; i++) {
			if (fileNode.getName().charAt(i) == '@') {
				for (int j=i+1; j<fileNode.getName().length(); j++) {
					if (fileNode.getName().charAt(j) == '@' && !f.getName().contains(fileNode.getName().substring(i+1, j-1))) {
						tags.remove(fileNode.getName().substring(i+1, j-1));
					}
					else if (fileNode.getName().charAt(j) == '.' && !f.getName().contains(fileNode.getName().substring(i+1, j))) {
						tags.remove(fileNode.getName().substring(i+1, j));
					}
				}
			}
		}
		
		fileNode.setPath(path);
		ImageManagerSingleton.add(this);
		
		nameLogger.log(Level.INFO, fileNode.getOGName() + " reverted to " + fileNode.getPath());
		updateLog(currPath, path);
	}
	
	/*
	 * Updates nameLog ArrayList with new of File.
	 * 
	 * @param	oldName		the name before modification
	 * @param	newName		the name after modification
	 */
	public void updateLog(String oldName, String newName) {
		nameLog.add(Calendar.getInstance().getTime() + ": " + oldName + " to " + newName);
	}
	
	/*
	 * Returns the filtered list of ImageFile Objects, by using an array of tags to search for
	 * 
	 * @param	search	the array of tags to search for
	 */
	public boolean tagCheck (String[] tagsToCheck) {		
		for (String tag : tagsToCheck) {
			if (!tags.contains(tag)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		return fileNode.getName();
	}
}
