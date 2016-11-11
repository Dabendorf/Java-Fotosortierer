package fotosortierer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ChooseDir {
	
	private JFileChooser chooser;
	private String[] fileTypes;
	private ArrayList<String> allPicturePaths = new ArrayList<String>();
	private String mainPath;
	private HashMap<String, Integer> fileNameCounter = new HashMap<String, Integer>();
	private ArrayList<Integer> hashCodes = new ArrayList<Integer>();
	
	private boolean findDuplicates;
	private boolean copyInsteadMove;
	private int intMoved = 0;
	private int intDuplicates = 0;
	
	public ChooseDir(String[] fileTypes, boolean findDuplicates, boolean copyInsteadMove) {
		this.fileTypes = fileTypes;
		this.findDuplicates = findDuplicates;
		this.copyInsteadMove = copyInsteadMove;
		chooser = new JFileChooser();
	}
	
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
	    	
	    	for(String str : allPicturePaths) {
	    		File file = new File(str);
	    		
	    		if(hashCodes.contains(file.hashCode()) && findDuplicates) {
	    			System.out.println("DOUBLE: "+file.getName()); //DEBUG
	    			intDuplicates++;
	    		} else {
	    			System.out.println(file.hashCode());
	    			hashCodes.add(file.hashCode());
	    			int counter;
		    		//System.out.println("Parent name: "+file.getParentFile().getName()); //DEBUG
		    		if(fileNameCounter.containsKey(file.getParentFile().getName())) {
		    			counter = fileNameCounter.get(file.getParentFile().getName());
		    		} else {
		    			counter = 0;
		    		}
		    		fileNameCounter.put(file.getParentFile().getName(), counter+1);
		    		String newPath = mainPath+"/"+file.getParentFile().getName()+counter+"."+getFileExtension(file);
		    		System.out.println("New FilePath: "+newPath); //DEBUG
		    		file.renameTo(new File(newPath));
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
	
	//Source: http://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
	private String getFileExtension(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}
	
	//Source: http://stackoverflow.com/questions/8751455/arraylist-contains-case-sensitivity
	private boolean containsIgnoreCase(String str, String[] list){
	    for(String i : list){
	        if(i.equalsIgnoreCase(str))
	            return true;
	    }
	    return false;
	}
}