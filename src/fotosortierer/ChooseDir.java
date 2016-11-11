package fotosortierer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;

public class ChooseDir {
	
	private JFileChooser chooser;
	private String[] fileTypes;
	private ArrayList<String> allPicturePaths = new ArrayList<String>();
	private String mainPath;
	private HashMap<String, Integer> fileNameCounter = new HashMap<String, Integer>();
	
	public ChooseDir(String[] fileTypes) {
		this.fileTypes = fileTypes;
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
	    		int counter;
	    		//System.out.println("Parent name: "+file.getParentFile().getName()); //DEBUG
	    		if(fileNameCounter.containsKey(file.getParentFile().getName())) {
	    			counter = fileNameCounter.get(file.getParentFile().getName());
	    		} else {
	    			counter = 0;
	    		}
	    		fileNameCounter.put(file.getParentFile().getName(), counter+1);
	    		String newPath = mainPath+"/"+file.getParentFile().getName()+counter+"."+getFileExtension(file);
	    		//System.out.println("New FilePath: "+newPath); //DEBUG
	    		file.renameTo(new File(newPath));
	    	}
	    } else {
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