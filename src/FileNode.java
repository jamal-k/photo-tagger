import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * FileNode for File.
 */
public class FileNode implements Serializable {
	
	public String name;
	public File file;
	public FileNode parent;
	public FileType type;
	public Map<String, FileNode> children;
	public final String ogName;
	
	/*
	 * Initiates a FileNode object.
	 * 
	 * @param	file	file the FileNode is created for
	 * @param	parent	parent of the FileNode
	 * @param	type	the FileType of the File of the FileNode
	 */
	public FileNode(File file, FileNode parent, FileType type) {
		this.name = file.getName();
		this.file = file;
		this.parent = parent;
		this.type = type;
		//Assigns original name of the FileNode
		if (type != FileType.DIRECTORY) {
			if (name.contains("@")) {
				ogName = name.substring(0, name.indexOf("@") - 1);
			}
			else {
				ogName = name.substring(0, name.lastIndexOf("."));
			}
		}
		else {
			ogName = "";
		}
		if (this.isDirectory()) {
			this.children = new HashMap<String, FileNode>();
		} else {
			this.children = null;
		}
	}
	
	/*
	 * Returns the String name.
	 * 
	 * @return	returns the String name
	 */
	public String getName() {
		return name;
	}
	
	public String getOGName() {
		return ogName;
	}
	
	/*
	 * Returns the String path.
	 * 
	 * @return	returns the String path
	 */
	public String getPath() {
		return file.getAbsolutePath();
	}
	
	/*
	 * Renames the file associated to this Node.
	 * 
	 * @param	path	the new path of the file to change to
	 */
	public void setPath(String path) {
		File f = new File(path);
		file.renameTo(f);
		FileNode fNode = new FileNode(f, parent, type);
		
		file = fNode.file;
		name = fNode.name;
	}
	
	/*
	 * Returns the File file.
	 * 
	 * @return	returns the File file
	 */
	public File getFile() {
		return this.file;
	}
	
	/*
	 * Returns the parent FileNode.
	 * 
	 * @return	returns the parent FileNode
	 */
	public FileNode getParent() {
		return this.parent;
	}
	
	/*
	 * Sets the parent FileNode of this FileNode
	 * 
	 * @param	p	the parent FileNode
	 */
	public void setParent(FileNode p) {
		this.parent = p;
	}
	
	/*
	 * Returns true if the FileNode type is a directory, false otherwise.
	 * 
	 * @return	true if the FileNode type is a directory, false otherwise.
	 */
	public boolean isDirectory() {
		return this.type == FileType.DIRECTORY;
	}
	
	/*
	 * Adds a child to the children of FileNode.
	 * 
	 * @param	name		name of child
	 * @param	childNode	the FileNode to add
	 */
	public void addChild(String name, FileNode childNode) {
		this.children.put(name, childNode);
	}
	
	/*
	 * Returns the Collection of children.
	 * 
	 * @return	returns the Collection of children
	 */
	public Collection<FileNode> getChildren() {
		return children.values();
	}
}
