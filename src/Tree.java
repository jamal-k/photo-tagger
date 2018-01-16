import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Tree for all files including the selected root File.
 */
public class Tree {
	public File rootFile;
	public FileNode root;
	public static final String[] ext = {".jpg", ".png"};
	public static ArrayList<ImageFile> imageFiles = new ArrayList<ImageFile>();

	/*
	 * Instantiates a Tree object.
	 * 
	 * @param	rootFile	the selected root File
	 */
	public Tree(File rootFile) throws ClassNotFoundException, IOException {
		this.rootFile = rootFile;
		this.root = new FileNode(rootFile, null, FileType.DIRECTORY);
		ImageManagerSingleton.readFromFile();
		AllTags.readFromFile();
		buildTree(rootFile, root);
	}

	/*
	 * Populates the children of root with FileNode objects. Also populates imageFiles with any images encountered.
	 * 
	 * @param	rootFile	the root File 
	 * @param	root		the root FileNode Object
	 */
	public static void buildTree(File rootFile, FileNode root) throws ClassNotFoundException, IOException {
		ImageManagerSingleton.readFromFile();

		for (File f: rootFile.listFiles()) {
			if (f.isDirectory()) {

				FileNode fNode = new FileNode(f, root, FileType.DIRECTORY);
				root.addChild(fNode.getName(), fNode);
				buildTree(f, fNode);

			} else {
				for (String e: ext) {
					//check that file is an image
					if (f.getName().contains(e)) {

						FileNode fNode = new FileNode(f, root, FileType.IMAGEFILE);
						root.addChild(fNode.getName(), fNode);
						System.out.println(ImageManagerSingleton.getImageFiles());

						//if saved copy of image not found, create new ImageFile
						if (ImageManagerSingleton.getImageFiles() != null && !ImageManagerSingleton.getImageFiles().containsKey(fNode.getOGName())) {

							System.out.println("if");
							ImageFile i = new ImageFile(fNode);
							ImageManagerSingleton.add(i);
							imageFiles.add(ImageManagerSingleton.getImageFiles().get(fNode.getOGName()));

						} else {
							// retrieve saved copy of ImageFile
							if (ImageManagerSingleton.getImageFiles() != null) {
								System.out.println("else");
								imageFiles.add(ImageManagerSingleton.getImageFiles().get(fNode.getOGName()));
							}

						}
					} else {
						FileNode fNode = new FileNode(f, root, FileType.FILE);
						root.addChild(fNode.getName(), fNode);
					}
				}
			}
		}
	}

	/*
	 * Returns the ArrayList of ImageFiles.
	 * 
	 * @return	the ArrayList of ImageFiles
	 */
	public static ArrayList<ImageFile> getImageFiles() {
		return imageFiles;
	}

}