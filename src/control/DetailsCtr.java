package control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import entity.AbsolutePath;
import entity.Copy;
import entity.DBMS_FileSource;
import entity.FilesSource;
import boundary.DetailsGui;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 22/set/2015 22:23:26
 */
public class DetailsCtr {
	private DetailsGui gui;
	
	private File source;
	private File destination;
	private DBMS_FileSource dbFileSource;
	
	
	/**
	 * 
	 */
	public DetailsCtr(DetailsGui gui) {
		this.gui = gui;
		if(gui.getSources() != null){
			this.source = new File(gui.getSources().get(0).getPath());
		}
		this.destination = new File(gui.getDestination().getPath());
		this.dbFileSource = new DBMS_FileSource();
		
		Thread td = new Thread(){
			public void run(){
				steps();
			}
		};
		
		td.start();
	}

	/**
	 * search new files
	 * make a decision
	 */
	private void steps() {
		
		List<String>path = new ArrayList<>();
		path.add(source.getAbsolutePath());
		navigaDirectory(path, "", source.list().length, gui.getSources().get(0));
		
		//controllo file con l'ultimo backup (tabella files_source last = 1)
		
		//prendo le decisioni in merito al risultato del controllo
				
				
		/*if(dbFileSource.removeAllLastTrue()){
			System.out.println(dbFileSource.setAllLastTrue());
		}else{
			System.out.println("non sono riuscito a cancellare gli elementi files_source where last=1");
		}*/
		
	}
	
	/**
	 * src - primoPak
	 *  |		|- prova.java
	 * 	|		|- altroPak
	 * 	|				|-settings.java
	 * 	|
	 *  |		|- secondoPak
	 *  |				|-frame.java
	 *  |				|-gui.java
	 *  |
	 *  |
	 *  |
	 * 	|- main.java
	 * 
	 * 
	 * @param path
	 * @param estensione
	 * @param numDir
	 */
	private void navigaDirectory(List <String> path, String estensione, int numDir, AbsolutePath absolutePath){
		//for(int i=0; i<path.size(); i++)System.out.println(path.get(i));
		
		List <String> temp = new ArrayList<String>();
		int contaDirectory = 0;
		String est = estensione;
		
		if(numDir == 0) contaDirectory = -1;
		
		for(int i=0; i<path.size(); i++){
			
			File f = new File(path.get(i));
				
				File []list = f.listFiles();
				
				for(int j=0; j<list.length; j++){
					if(list[j].isDirectory()){
						temp.add(list[j].getAbsolutePath());
						contaDirectory++;
						String rel = source.toURI().relativize(list[j].toURI()).getPath();
						new File(destination.getAbsoluteFile() + "\\" + rel).mkdir();
						
					}else{
//						System.out.println("Dimensione del file: " + list[j].length());		
						String rel = source.toURI().relativize(list[j].toURI()).getPath();
						System.out.println("\t" + rel);
						//System.out.println(rel + " ultima mod:" + list[j].lastModified() + " dim:" + list[j].length() + "B"); 
						
						//query.inserisciFile(idPercorsoAssoluto, rel, Copy.getMd5(list[j]));
						FilesSource fs = new FilesSource();
						fs.setAbsolutePath(absolutePath);
						fs.setLast(false);
						fs.setMd5(Copy.getMd5(list[j]));
						fs.setRelativePath(rel);
						
						System.out.println(dbFileSource.newInsert(fs));
					}
				}
		}
		
		if(numDir != -1) navigaDirectory(temp, est, contaDirectory, absolutePath);
	}
}
