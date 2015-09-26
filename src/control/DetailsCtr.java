package control;

import java.io.File;
import java.text.DecimalFormat;
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
	private DecimalFormat decimalFormat;
	
	private long dimSorgente;
	private long dimBackup;
	
	/**
	 * number of files to copy<br>
	 * [0] new files<br>
	 * [1] files modified<br>
	 * [2] files not modified<br>
	 */
	private int nFilesToCopy[];
	
	private final int I_NEW = 0;
	private final int I_MODIFY = 1;
	private final int I_NOT_MODIFY = 2;
	
	/**
	 * 
	 */
	public DetailsCtr(DetailsGui gui) {
		this.gui = gui;
		this.nFilesToCopy = new int[3];
		decimalFormat = new DecimalFormat("###.##");
		
		if(this.gui.getSources() != null){
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
		
		gui.changeColorOfLabels();
		
		int tot = nFilesToCopy[I_NEW] + nFilesToCopy[I_MODIFY];
		String totalSize = getTotalSize();
		gui.setNumOfFilesToCopy(tot, totalSize);
		
	}
	
	private String getTotalSize() {
		float dimSorgenteKB = (float)dimSorgente/(float)(1024);
		float dimSorgenteMB = (float)dimSorgente/(float)(1024*1024);
		float dimSorgenteGB = (float)dimSorgente/(float)(1024*1024*1024);
		
		float dimBackupKB = (float)dimBackup/(float)(1024);
		float dimBackupMB = (float)dimBackup/(float)(1024*1024);
		float dimBackupGB = (float)dimBackup/(float)(1024*1024*1024);
		
		System.out.println("sorgente: " + dimSorgente + ", backup: " + dimBackup);
		
		String dimSorgenteStr = "";
		if(dimSorgenteGB > 0.9){
			dimSorgenteStr = decimalFormat.format(dimSorgenteGB) + " GB ";
			//System.out.println(dimSorgenteGB);
			
		}else if(dimSorgenteMB > 0.9){
			dimSorgenteStr = decimalFormat.format(dimSorgenteMB) + " MB ";
			//System.out.println(dimSorgenteMB);
			
		}else if(dimSorgenteKB > 0.9){
			dimSorgenteStr = decimalFormat.format(dimSorgenteKB) + " KB ";
			//System.out.println(dimSorgenteKB);
			
		}else{
			dimSorgenteStr = decimalFormat.format(dimSorgente) + " B ";
			//System.out.println(dimSorgente);
		}
		
		String dimBackupStr = "";
		if(dimBackupGB > 0.9){
			dimBackupStr = decimalFormat.format(dimBackupGB) + " GB ";
			
		}else if(dimBackupMB > 0.9){
			dimBackupStr = decimalFormat.format(dimBackupMB) + " MB ";
			
		}else if(dimBackupKB > 0.9){
			dimBackupStr = decimalFormat.format(dimBackupKB) + " KB ";
			
		}else{
			dimBackupStr = decimalFormat.format(dimBackup) + " B ";
			
		}
		
		return dimBackupStr + "/" + dimSorgenteStr;
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
	 * 	unused
	 * @param estensione
	 * @param numDir
	 */
	private void navigaDirectory(List <String> path, String estensione, 
			int numDir, AbsolutePath absolutePath){
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
						
						/**
						 * create the dir of destination
						 */
						new File(destination.getAbsoluteFile() + "\\" + rel).mkdir();
						
					}else{
//						System.out.println("Dimensione del file: " + list[j].length());		
						String rel = source.toURI().relativize(list[j].toURI()).getPath();
						//System.out.println(rel + " ultima mod:" + list[j].lastModified() + " dim:" + list[j].length() + "B"); 
						
						dimSorgente += list[j].length();
						
						FilesSource fs = new FilesSource();
						fs.setAbsolutePath(absolutePath);
						fs.setMd5(Copy.getMd5(list[j]));
						fs.setRelativePath(rel);
						
						int status = dbFileSource.newInsert(fs);
						switch (status) {
						case FilesSource.STATUS_DELETED:
							System.out.println("CANCELLATO) " + rel);
							break;

						case FilesSource.STATUS_ERROR:
							System.out.println("ERRORE) " + rel);
							break;
							
						case FilesSource.STATUS_MODIFY:
							System.out.println("MODIFICATO) " + rel);
							nFilesToCopy[I_MODIFY]++;
							dimBackup += list[j].length();
							gui.setNumOfModifiedFiles(nFilesToCopy[I_MODIFY]);
							break;
							
						case FilesSource.STATUS_NEW:
							System.out.println("NUOVO) " + rel);
							nFilesToCopy[I_NEW]++;
							dimBackup += list[j].length();
							gui.setNumOfNewFiles(nFilesToCopy[I_NEW]);
							break;
							
						case FilesSource.STATUS_NOT_MODIFY:
							System.out.println("NON MODIFICCATO) " + rel);
							nFilesToCopy[I_NOT_MODIFY]++;
							gui.setNumOfNotModifyFiles(nFilesToCopy[I_NOT_MODIFY]);
							break;
							
						default:
							System.out.println("INDEFINITO) " + rel);
							break;
						}
									
					}
				}
		}
		
		if(numDir != -1) navigaDirectory(temp, est, contaDirectory, absolutePath);
	}
}
