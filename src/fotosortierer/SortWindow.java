package fotosortierer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class SortWindow {
	
	private JFrame frame1 = new JFrame("Fotosortierer");
	private JButton chooseDirButton = new JButton("Verzeichnis wählen");
	private ChooseDir directWindow;
	private String[] fileTypes = {"jpg", "png"};//Ersetzen durch Auslesen des Eingabefeldes
	
	public SortWindow() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(300,200)); //ANPASSEN
		frame1.setMinimumSize(new Dimension(300,200));
		frame1.setMaximumSize(new Dimension(450,300));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridBagLayout());
		
		chooseDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				directWindow = new ChooseDir(fileTypes);
				directWindow.chooseDirect();
			}
		});
		cp.add(chooseDirButton);
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
}