package fotosortierer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SortWindow {
	
	private JFrame frame1 = new JFrame("Fotosortierer");
	private ChooseDir directWindow;
	
	private JCheckBox checkBoxDuplicates = new JCheckBox("Doppelte Fotos löschen");
	private JTextField textFieldFileTypes = new JTextField("jpg, png");
	private JRadioButton radioCopy = new JRadioButton("Kopieren");
	private JRadioButton radioMove = new JRadioButton("Verschieben");
	private JButton chooseDirButton = new JButton("Verzeichnis wählen");
    private ButtonGroup bg = new ButtonGroup();

	public SortWindow() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(250,150));
		frame1.setMinimumSize(new Dimension(250,150));
		frame1.setMaximumSize(new Dimension(500,300));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridBagLayout());
		
		chooseDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				directWindow = new ChooseDir(extractFileTypes(), checkBoxDuplicates.isSelected(), radioCopy.isSelected() ? true : false);
				directWindow.chooseDirect();
			}
		});
		
		bg.add(radioCopy);
		bg.add(radioMove);
		radioMove.setSelected(true);
		checkBoxDuplicates.setSelected(true);
		
		cp.add(textFieldFileTypes, new GridBagCorners(0, 0, 2, 1, 1, 0.2));
		cp.add(checkBoxDuplicates, new GridBagCorners(0, 1, 2, 1, 1, 0.2));
		JPanel radioButtons = new JPanel();
		radioButtons.setLayout(new GridBagLayout());
		radioButtons.add(radioCopy, new GridBagCorners(0, 0, 1, 1, 0.5, 1));
		radioButtons.add(radioMove, new GridBagCorners(1, 0, 1, 1, 0.5, 1));
		cp.add(radioButtons, new GridBagCorners(0, 2, 1, 1, 1, 0.2));
		cp.add(chooseDirButton, new GridBagCorners(0, 3, 1, 2, 1, 0.2));
		chooseDirButton.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldFileTypes.setPreferredSize(new Dimension(0,0));
		checkBoxDuplicates.setPreferredSize(new Dimension(0,0));
		radioButtons.setPreferredSize(new Dimension(0,0));
		chooseDirButton.setPreferredSize(new Dimension(0,0));
		
		textFieldFileTypes.setToolTipText("Dateitypen zum Sortieren");
		checkBoxDuplicates.setToolTipText("Doppelte Dateien löschen?");
		radioCopy.setToolTipText("Dateien an neuen Ort kopieren");
		radioMove.setToolTipText("Dateien aus altem Speicherort löschen");
		chooseDirButton.setToolTipText("Oberverzeichnis zum Sortieren");
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	private String[] extractFileTypes() {
		String str = textFieldFileTypes.getText();
		
		String[] fileTypes = str.split(",");
		
		String[] defaultTypes = {"jpg", "png"};
		return (fileTypes.length == 0 ? defaultTypes : fileTypes);
	}
}