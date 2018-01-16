import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.List;

public class PhotoRenamer {

	private JFrame frame;
	private final JButton addTags, search, chooseDir, addNewTag, removeTag, revertName; // , refreshSummary;
	private JList<ImageFile> images;
	private JList<String> allTags, currTags, log;
	private JTextArea currDir, newTag, summary;
	private JLabel picLabel = new JLabel();


	/*
	 * Creates JFrame with all GUI Components
	 */
	private PhotoRenamer() {
		frame = new JFrame("Photo Renamer");
		frame.setPreferredSize(new Dimension(777, 444));

		allTags = new JList<String>();
		DefaultListModel<String> allTagsModel = new DefaultListModel<String>();
		System.out.println(AllTags.getInstance().getTags());
		for (String tag : AllTags.getInstance().getTags()) {
			System.out.println(new JCheckBox(tag).getText());
			allTagsModel.addElement(tag);
		}
		allTags.setModel(allTagsModel);
		allTags.setBackground(new Color(0, 100, 200));
		allTags.setForeground(Color.WHITE);
		JScrollPane allTagsScroll = new JScrollPane(allTags, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		addTags = new JButton("Add Tags");
		addTags.setForeground(Color.WHITE);
		addTags.setBackground(new Color(0, 100, 200));

		search = new JButton("Search for tags");
		search.setForeground(Color.WHITE);
		search.setBackground(new Color(0, 100, 200));

		currDir = new JTextArea();
		currDir.setEditable(false);
		currDir.setText("Select a directory");
		currDir.setBackground(Color.CYAN);
		JScrollPane currDirScroll = new JScrollPane(currDir, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		chooseDir = new JButton("Choose Directory");
		chooseDir.setBackground(Color.RED);

		images = new JList<ImageFile>();
		JScrollPane imagesScroll = new JScrollPane(images, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		images.setBackground(Color.BLACK);
		images.setForeground(Color.WHITE);

		newTag = new JTextArea();
		newTag.setEditable(true);
		newTag.setBackground(Color.LIGHT_GRAY);
		newTag.setText("Type new tag here");
		JScrollPane newTagScroll = new JScrollPane(newTag, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		addNewTag = new JButton("Add new tag");
		addNewTag.setBackground(Color.LIGHT_GRAY);

		currTags = new JList<String>();
		currTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		currTags.setBackground(Color.MAGENTA);
		JScrollPane currTagsScroll = new JScrollPane(currTags, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		removeTag = new JButton("Remove Tag");
		removeTag.setBackground(Color.MAGENTA);

		log = new JList<String>();
		log.setBackground(Color.ORANGE);
		JScrollPane logScroll = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		revertName = new JButton("Revert Name");
		revertName.setBackground(Color.ORANGE);

		// refreshSummary = new JButton("Refresh Summary");
		// refreshSummary.setBackground(Color.GREEN);

		/*
		summary = new JTextArea();
		summary.setEditable(false);
		summary.setBackground(Color.GREEN);
		summary.setText("Summary goes here");
		JScrollPane summaryScroll = new JScrollPane(summary, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		*/

		// JPanel for all JButton Objects
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1, 7));
		p1.add(chooseDir);
		p1.add(addNewTag);
		p1.add(removeTag);
		p1.add(revertName);
		p1.add(addTags);
		p1.add(search);
		// p1.add(refreshSummary);

		//JPanel for JList of images, current tags, and new tag text area
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(3, 1));
		p2.add(imagesScroll);
		p2.add(currTagsScroll);
		p2.add(newTagScroll);

		//JPanel for image preview, and log
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(2, 1));
		p3.add(picLabel);
		p3.add(logScroll);

		//JPanel for all tags and summary
		JPanel p4 = new JPanel();
		p4.setLayout(new GridLayout(1, 1));
		p4.add(allTagsScroll);
		// p4.add(summaryScroll);

		//JPanel for purpose of organizing p2, p3, and p4 in GridLayout
		JPanel p5 = new JPanel();
		p5.setLayout(new GridLayout(1, 3));
		p5.add(p2, 0);
		p5.add(p3, 1);
		p5.add(p4, 2);

		frame.getContentPane().setLayout(new BorderLayout());
		Container content = frame.getContentPane();

		//Add GUI components to frame in BorderLayout
		content.add(p1, BorderLayout.PAGE_END);
		content.add(p5, BorderLayout.CENTER);
		content.add(currDirScroll, BorderLayout.PAGE_START);

		addTags.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (allTags.getSelectedValuesList() != null) {
						
						ImageFile x = images.getSelectedValue();

						List<String> selectedTags = allTags.getSelectedValuesList();
						String[] tagsToAdd = new String[selectedTags.size()];

						for (int i=0; i < selectedTags.size(); i++) {
							tagsToAdd[i] = selectedTags.get(i);
						}

						images.getSelectedValue().addTags(tagsToAdd);
						
						DefaultListModel<String> currTagsModel = new DefaultListModel<String>();

						for (String tag : x.tags) {
							currTagsModel.addElement(tag);
						}

						currTags.setModel(currTagsModel);
					}

					images.updateUI();

				} catch (IOException e1) {

				}
			}
		});

		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!allTags.getSelectedValuesList().isEmpty()) {

					List<String> selectedTags = allTags.getSelectedValuesList();
					String[] tagsToSearch = new String[selectedTags.size()];

					for (int i=0; i < selectedTags.size(); i++) {
						tagsToSearch[i] = selectedTags.get(i);
					}

					DefaultListModel<ImageFile> imagesModel = new DefaultListModel<ImageFile>();

					// Only images containing every tag being searched for in updated JList of images
					for (ImageFile i : Tree.imageFiles) {
						if (i.tagCheck(tagsToSearch)) {
							imagesModel.addElement(i);
						}
					}

					images.setModel(imagesModel);

				}
			}
		});


		chooseDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				chooseDir.setBackground(Color.CYAN);
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				DefaultListModel<ImageFile> imagesModel = new DefaultListModel<ImageFile>();
				imagesModel.clear();

				int returnVal = fileChooser.showOpenDialog(frame.getContentPane());

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (file.exists()) {
						currDir.setText("Selected directory : " + file.getAbsolutePath());
						try {
							Tree t = new Tree(file);
							int count = 0;

							for (ImageFile i: Tree.getImageFiles()) {
								imagesModel.add(count, i);
								count += 1;
							}

							images.setModel(imagesModel);

							DefaultListModel<String> allTagsModel = new DefaultListModel<String>();

							for (String tag : AllTags.getInstance().getTags()) {
								allTagsModel.addElement(tag);
							}

							allTags.setModel(allTagsModel);

						} catch (ClassNotFoundException e1) {

						} catch (IOException e1) {

						}
					}
				} else {
					currDir.setText("No Path Selected");
				}
			}
		});


		images.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					final ImageFile x = images.getSelectedValue();

					BufferedImage img = null;

					try {
						img = ImageIO.read(x.getNode().getFile());
						ImageIcon icon = new ImageIcon(img);
						Image img1 = icon.getImage();
						Image img2 = img1.getScaledInstance(169, 169, Image.SCALE_SMOOTH);
						icon = new ImageIcon(img2);

						picLabel = new JLabel(icon);

						p5.remove(p3);

						p3.removeAll();

						p3.add(picLabel);
						p3.add(logScroll);

						p5.add(p3, 1);

						DefaultListModel<String> currTagsModel = new DefaultListModel<String>();

						for (String tag : x.tags) {
							currTagsModel.addElement(tag);
						}

						currTags.setModel(currTagsModel);

						DefaultListModel<String> logModel = new DefaultListModel<String>();

						for (String log: x.nameLog) {
							logModel.addElement(log);
						}

						log.setModel(logModel);

						p3.updateUI();

					} catch (IOException e1){

					} catch (NullPointerException e1) {

					}
				}
			}
		});


		addNewTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (newTag.getText() != "" && !newTag.getText().contains("@")) {
					try {
						ImageFile x = images.getSelectedValue();

						images.getSelectedValue().addTag(newTag.getText());

						DefaultListModel<String> allTagsModel = new DefaultListModel<String>();

						for (String tag : AllTags.getInstance().getTags()) {
							allTagsModel.addElement(tag);
						}

						allTags.setModel(allTagsModel);

						DefaultListModel<String> currTagsModel = new DefaultListModel<String>();

						for (String tag : x.tags) {
							currTagsModel.addElement(tag);
						}

						currTags.setModel(currTagsModel);

						DefaultListModel<String> logModel = new DefaultListModel<String>();

						for (String log: x.nameLog) {
							logModel.addElement(log);
						}

						log.setModel(logModel);

					} catch (IOException e1) {

					}
				}
				else if (newTag.getText().contains("@")) {
					System.out.println("Cannot add tag containing '@'!");
				}

				frame.repaint();

			}
		});


		removeTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currTags.getSelectedValue() != null) {

					ImageFile x = images.getSelectedValue();

					try {
						images.getSelectedValue().removeTag(currTags.getSelectedValue());
					} catch (IOException e1) {

					}

					images.updateUI();

					DefaultListModel<String> currTagsModel = new DefaultListModel<String>();

					for (String tag : x.tags) {
						currTagsModel.addElement(tag);
					}

					currTags.setModel(currTagsModel);
				}
			}
		});

		revertName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (log.getSelectedValue() != null) {
					try {
						ImageFile x = images.getSelectedValue();

						images.getSelectedValue().revertName(log.getSelectedValue().substring(log.getSelectedValue().indexOf(" to ") + 4));

						DefaultListModel<String> currTagsModel = new DefaultListModel<String>();

						for (String tag : x.tags) {
							currTagsModel.addElement(tag);
						}

						currTags.setModel(currTagsModel);

					} catch (IOException e1) {

					}

					images.updateUI();
					currTags.updateUI();
				}
			}
		});
	}

	private void createAndShowGui() {
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main (String[] args) throws ClassNotFoundException, EOFException, IOException {
		AllTags.readFromFile();
		ImageManagerSingleton.readFromFile();
		PhotoRenamer gg = new PhotoRenamer();
		gg.createAndShowGui();
	}

}
