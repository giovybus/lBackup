package control;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.lBackupMain;
import engine.MultiSearchMonitor;
import entity.AbsolutePath;
import entity.Copy;
import entity.DBMS_FileSource;
import entity.DBMS_FileToBackup;
import entity.FileToBackup;
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
	 * log file
	 */
	private LogCtr log;
	
	private DBMS_FileToBackup dbFilesToBackup;
	
	private Config config;
	
	/**
	 * black list with absolute path like:
	 * C:\Windows\temp
	 */
	private List<String>blacklistByPath;
	
	/**
	 * black list that contains only the 
	 * name of directory.
	 * 
	 * .svn | bin | .git | 
	 */
	private List<String>blacklistByName;
	
	/**
	 * black list that contains the files
	 * to exclued like:
	 * .class | .db | .temp
	 */
	private List<String>blacklistByExtension;
	
	/**
	 * 
	 */
	public DetailsCtr(DetailsGui gui) {
		this.gui = gui;
		this.nFilesToCopy = new int[3];
		this.decimalFormat = new DecimalFormat("###.##");
		this.log = new LogCtr();
		this.dbFilesToBackup = new DBMS_FileToBackup();
		this.config = new Config();
		
		if(this.gui.getSources() != null){
			this.source = new File(gui.getSources().get(0).getPath());
		}
		
		this.destination = new File(gui.getDestination().getPath());
		this.dbFileSource = new DBMS_FileSource();
		
		Thread td = new Thread(){
			public void run(){
				long start = System.currentTimeMillis();
				//stepsNoMultiThread();
				stepsMultiThread();
				long finish = System.currentTimeMillis();
				
				String msgToLog = "Backup completed in: " + (finish-start) + "ms, "
						+ "file scanned: " + (nFilesToCopy[0] + nFilesToCopy[1] + nFilesToCopy[2]) 
						+ ", program version: " + lBackupMain.version;
				
				log.scriviLog(msgToLog);
				System.out.println(msgToLog);
			}
		};
		
		td.start();
	}

	/**
	 * 
	 */
	private void stepsMultiThread() {
		MultiSearchMonitor mm = new MultiSearchMonitor(source.getAbsolutePath());
		mm.run();
		
		List<File> fList = mm.getList();
		System.out.println("list size " + fList.size());
	}

	/**
	 * search new files
	 * make a decision
	 */
	private void stepsNoMultiThread() {
		
		List<String>path = new ArrayList<>();
		path.add(source.getAbsolutePath());
		navigaDirectory(path, "", source.list().length, gui.getSources().get(0));
		
		gui.changeColorOfLabels();
		
		int tot = nFilesToCopy[I_NEW] + nFilesToCopy[I_MODIFY];
		String totalSize = getTotalSize();
		gui.setNumOfFilesToCopy(tot, totalSize);
		
		if(config.isAutomaticBakcup()){
			copyAllFiles();
		}
				
	}
	
	/**
	 * copy all files 
	 */
	private void copyAllFiles() {
		List<FileToBackup>files = dbFilesToBackup.getAllFilesToBackup();
		
		if(files != null){
			gui.getProgressBar().setMinimum(0);
			gui.getProgressBar().setMaximum(files.size());
			
			for(int i=0; i<files.size(); i++){
				gui.getProgressBar().setValue(i);
				gui.setTextToLabFileToCopy(files.get(i).getPathSource());
				
				try{
					Copy.copyFileUsingFileChannels(files.get(i).getFileSource(), 
							files.get(i).getFileDestination());	
					dbFilesToBackup.delete(files.get(i));
					
				}catch(Exception e){
					e.printStackTrace();
					System.err.println(files.get(i).getFileSource().getAbsolutePath());
				}
			}	
		}
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
						 * create the dir of destination if not exists
						 */
						File dir = new File(destination.getAbsoluteFile() + "\\" + rel);
						if(!dir.exists()){
							dir.mkdir();
						}
						
					}else{
//						System.out.println("Dimensione del file: " + list[j].length());		
						String rel = source.toURI().relativize(list[j].toURI()).getPath();
						//System.out.println(rel + " ultima mod:" + list[j].lastModified() + " dim:" + list[j].length() + "B"); 
						
						dimSorgente += list[j].length();
						
						FilesSource fs = new FilesSource();
						fs.setAbsolutePath(absolutePath);
						fs.setMd5(Copy.getMd5(list[j]));
						fs.setRelativePath(rel);
						
						//long in = System.currentTimeMillis();
						int status = dbFileSource.newInsert(fs);
						//long fi = System.currentTimeMillis();
						//System.out.println(fi-in);
						
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
							newRevisionFileToCopy(rel, fs.getRevision());
							break;
							
						case FilesSource.STATUS_NEW:
							System.out.println("NUOVO) " + rel);
							nFilesToCopy[I_NEW]++;
							dimBackup += list[j].length();
							gui.setNumOfNewFiles(nFilesToCopy[I_NEW]);
							
							newFileToCopy(rel);
							break;
							
						case FilesSource.STATUS_NOT_MODIFY:
							//System.out.println("NON MODIFICCATO) " + rel);
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
	
	/**
	 * @param rel
	 * @param revision
	 */
	private void newRevisionFileToCopy(String rel, int revision) {
		FileToBackup temp = new FileToBackup();
		temp.setPathSource(source.getAbsolutePath() + "\\" + rel);
		temp.setPathDestination(destination.getAbsolutePath() + "\\" + rel + ".rv" + revision);
		temp.setStatus(0);
		
		dbFilesToBackup.insert(temp);
	}

	/**
	 * 
	 * @param rel
	 */
	private void newFileToCopy(String rel){
		FileToBackup temp = new FileToBackup();
		temp.setPathSource(source.getAbsolutePath() + "\\" + rel);
		temp.setPathDestination(destination.getAbsolutePath() + "\\" + rel);
		temp.setStatus(0);
		
		dbFilesToBackup.insert(temp);
	}
}
