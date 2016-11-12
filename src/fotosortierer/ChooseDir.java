package fotosortierer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Diese Klasse erstellt das Auswahlfenster fuer den Hauptordner und fuehrt alle Dateiverarbeitungen aus.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class ChooseDir {
	
	/**FileChooseFensterElement*/
	private JFileChooser chooser;
	/**String-Array aus ueberpruefbaren Dateiendungen*/
	private String[] fileTypes;
	/**Pfade aller gefundenen Dateien*/
	private ArrayList<String> allPicturePaths = new ArrayList<String>();
	/**Pfad des gewaehlten Oberordners*/
	private String mainPath;
	/**Key-Value-Verzeichnis aus bisherigen Ordnernamen und Zaehler fuer die Menge von Dateien*/
	private HashMap<String, Integer> fileNameCounter = new HashMap<String, Integer>();
	/**Hashcodes bisheriger Dateien zur Ueberpruefung von Dopplungen*/
	private ArrayList<String> hashCodes = new ArrayList<String>();
	/**Dateierweiterungen fuer Fotodateien*/
	private String[] photoExtensions = {"jpg", "jpeg", "png"};
	
	/**Boolean ob Duplikate entfernt werden sollen*/
	private boolean findDuplicates;
	/**Boolean ob Dateien kopiert statt verschoben werden sollen*/
	private boolean copyInsteadMove;
	/**Zaehler wie viele Dateien kopiert bzw. verschoben wurden*/
	private int intMoved = 0;
	/**Zaehler wie viele Duplikate ignoriert wurden*/
	private int intDuplicates = 0;
	
	/**
	 * Konstruktor des Ordnerauswahlfensters zur Uebergabe wichtiger Parameter
	 * @param fileTypes String-Array aus ueberpruefbaren Dateiendungen
	 * @param findDuplicates Boolean ob Duplikate entfernt werden sollen
	 * @param copyInsteadMove Boolean ob Dateien kopiert statt verschoben werden sollen
	 */
	public ChooseDir(String[] fileTypes, boolean findDuplicates, boolean copyInsteadMove) {
		this.fileTypes = fileTypes;
		this.findDuplicates = findDuplicates;
		this.copyInsteadMove = copyInsteadMove;
		chooser = new JFileChooser();
	}
	
	/**
	 * Diese Methode laesst den Nutzer das Verzeichnis auswaehlen und ueberprueft etwaige Aenderungswuensche mit Bestaetigungsfenstern.
	 * Ferner wertet sie Nutzereinstellungen wie Dateiendungen oder Kopiervorgaenge aus.
	 * Anschliessend fuehrt sie das Programm aus und informiert bei Abschluss ueber alle getaetigten Vorgaenge.
	 */
	public void chooseDirect() {
		chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
	    chooser.setDialogTitle("Verzeichnis auswählen");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	String path = chooser.getSelectedFile().getAbsolutePath();
	    	listSubFolders(path);
	    	mainPath = path;
	    	
	    	//System.out.println("Main path:"+path); //DEBUG
	    	//System.out.println("Paths of pictures: "+allPicturePaths); //DEBUG
	    	//System.out.println("Number of pictures: "+allPicturePaths.size()); //DEBUG
	    	
	    	String lineSep = System.getProperty("line.separator");
	    	int resultQuest = JOptionPane.showConfirmDialog(null,
	                "Möchten Sie den Sortiervorgang für das folgende Verzeichnis starten?"+lineSep+mainPath+lineSep+"Der Vorgang kann einige Minuten dauern.",
	                "Vorgang beginnen?",
	                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    	if(resultQuest == JOptionPane.NO_OPTION) {
	    		return;
	    	}
	    	
	    	JOptionPane.showMessageDialog(null, "Bitte warten Sie auf die Bestätigungsnachricht.", "Vorgang startet", JOptionPane.INFORMATION_MESSAGE);
	    	
	    	for(String str : allPicturePaths) {
	    		File file = new File(str);
	    		
	    		boolean fileReady = true;
	    		if(findDuplicates && containsIgnoreCase(getFileExtension(file), photoExtensions)) {
	    			String hash = generateHash(file);
	    			
	    			if(hashCodes.contains(hash)) {
	    				//System.out.println("Duplicate: "+file.getName()); //DEBUG
	    				intDuplicates++;
	    				fileReady = false;
	    			} else {
	    				hashCodes.add(hash);
	    			}
	    		}
	    		
	    		if(fileReady) {
	    			int counter;
		    		//System.out.println("Parent name: "+file.getParentFile().getName()); //DEBUG
		    		if(fileNameCounter.containsKey(file.getParentFile().getName())) {
		    			counter = fileNameCounter.get(file.getParentFile().getName());
		    		} else {
		    			counter = 0;
		    		}
		    		fileNameCounter.put(file.getParentFile().getName(), counter+1);
		    		String newPath = mainPath+"/"+file.getParentFile().getName()+counter+"."+getFileExtension(file);
		    		
		    		if(copyInsteadMove) {
		    			//System.out.println("Copied to: "+newPath); //DEBUG
		    			try {
							Files.copy(file.toPath(), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		} else {
		    			//System.out.println("Moved to: "+newPath); //DEBUG
			    		file.renameTo(new File(newPath));
		    		}
		    		intMoved++;
	    		}
	    	}
	    	
	    	String word1 = copyInsteadMove ? "kopiert" : "bewegt";
	    	if(findDuplicates) {
	    		JOptionPane.showMessageDialog(null, "Es wurden "+intMoved+" Dateien "+word1+" und "+intDuplicates+" doppelte Dateien entfernt.", "Vorgang abgeschlossen", JOptionPane.INFORMATION_MESSAGE);
	    	} else {
	    		JOptionPane.showMessageDialog(null, "Es wurden "+intMoved+" Dateien "+word1+".", "Vorgang abgeschlossen", JOptionPane.INFORMATION_MESSAGE);
	    	}
	    } else {
	    	JOptionPane.showMessageDialog(null, "Es wurde kein Verzeichnis ausgewählt!", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
	    	//System.out.println("No directory selected"); //DEBUG
	    }
	}
	
	/**
	 * Diese Methode listet alle Subordner und -dateien aus einem Pfad auf, was die rekursive Erfassung aller Verzeichnisse eines Pfads erlaubt.
	 * @param path Pfad des Subordners
	 */
	private void listSubFolders(String path) {
		//System.out.println("Controll Folder: "+path); //DEBUG
		ArrayList<String> localPicturePaths = new ArrayList<String>();
		File[] files = new File(path).listFiles();
		
		if(files != null) {
			for(File file : files) {
			    if(file.isDirectory()) {
			    	//System.out.println("Folder: "+file.getName()); //DEBUG
			        listSubFolders(file.getAbsolutePath());
			    } else if(file.isFile()) {
			    	if(containsIgnoreCase(getFileExtension(file), fileTypes)) {
			    		//System.out.println("File: "+file.getName()+" in Folder "+file.getParent()); //DEBUG
				    	localPicturePaths.add(file.getAbsolutePath());
			    	} else {
			    		//System.out.println("Ignore File: "+file.getName()); //DEBUG
			    	}
			    }
			}
		}
		
		allPicturePaths.addAll(localPicturePaths);
	}
	
	/**
	 * Diese Methode gibt die Dateiendung einer Datei zurueck.
	 * 
	 * Source: http://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
	 * @param file Zu pruefende Datei
	 * @return Gibt Dateiendung zurueck
	 */
	private String getFileExtension(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}
	
	/**
	 * Diese Methode ueberprueft ob ein String, unabhaengig von Gross- und Kleinschreibung in einem String-Array vorkommt.
	 * 
	 * Source: http://stackoverflow.com/questions/8751455/arraylist-contains-case-sensitivity
	 * @param str Zu pruefender String
	 * @param list Array aus Strings
	 * @return Boolean ueber Vorhandensein des Strings in der Liste
	 */
	private boolean containsIgnoreCase(String str, String[] list){
	    for(String i : list){
	        if(i.equalsIgnoreCase(str))
	            return true;
	    }
	    return false;
	}
	
	/**
	 * Diese Methode wandelt eine Menge an Bytes in einen hexadezimalen String um.
	 * 
	 * Source: https://sites.google.com/site/matthewjoneswebsite/java/md5-hash-of-an-image
	 * @param inBytes Array aus Bytes
	 * @return Rueckgabe des Hex-Strings
	 */
	private static String returnHex(byte [] inBytes) {
		String hexString = null;
		for(int i=0;i<inBytes.length;i++) {
			hexString += Integer.toString((inBytes[i] & 0xff) + 0x100, 16).substring(1);
		}
		return hexString;
	}
	
	/**
	 * Diese Methode generiert den md5-Hashcode eines Bildes.
	 * 
	 * Source: https://sites.google.com/site/matthewjoneswebsite/java/md5-hash-of-an-image
	 * @param file Zu pruefendes Bild
	 * @return Ausgabe des md5-Hashes
	 */
	private String generateHash(File file) {
		try {
			BufferedImage buffImg = ImageIO.read(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(buffImg, getFileExtension(file), outputStream);
            byte[] data = outputStream.toByteArray();

            //System.out.println("Start MD5 Digest");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] hash = md.digest();
            return returnHex(hash);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}