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

/**
 * Diese Klasse erstellt das Hauptfenster des Projekts, in dem Einstellungen getroffen und der Sortiervorgang gestartet werden koennen.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class SortWindow {
	
	/**Hauptfenster des Programms*/
	private JFrame frame1 = new JFrame("Fotosortierer");
	/**Objekt des Ordnerwaehlfensters*/
	private ChooseDir directWindow;
	
	/**Checkbox fuer die Erkennung doppelter Elemente*/
	private JCheckBox checkBoxDuplicates = new JCheckBox("Doppelte Fotos löschen");
	/**Eingabefeld fuer zu pruefende Dateitypen*/
	private JTextField textFieldFileTypes = new JTextField("jpg, png");
	/**Radiobox ob Dateien kopiert werden sollen*/
	private JRadioButton radioCopy = new JRadioButton("Kopieren");
	/**Radiobox ob Dateien verschoben werden sollen*/
	private JRadioButton radioMove = new JRadioButton("Verschieben");
	/**Button zum Oeffnen der Ordnerauswahl*/
	private JButton chooseDirButton = new JButton("Verzeichnis wählen");
	/**Gruppe von Radiobuttons*/
    private ButtonGroup bg = new ButtonGroup();

    /**
     * Konstruktor der SortWindowKlasse, welcher das gesamte Design aufgrund von GridBagLayouts aufbaut.
     */
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
	
	/**
	 * Diese Methode extrahiert die eingegebenen Dateiendungen aus der Eingabebox in einen String-Array.
	 * @return Gibt String-Array eingegebener Dateiendungen zurueck
	 */
	private String[] extractFileTypes() {
		String str = textFieldFileTypes.getText();
		
		String[] fileTypes = str.split(",");
		
		String[] defaultTypes = {"jpg", "png"};
		return (fileTypes.length == 0 ? defaultTypes : fileTypes);
	}
}