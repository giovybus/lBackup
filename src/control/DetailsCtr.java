package control;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.lBackupMain;
import engine.MultiSearchMonitor;
import entity.AbsolutePath;
import entity.Blacklist;
import entity.Copy;
import entity.DBMS_Blacklist;
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
	 * ex: Thumbs.db
	 */
	private List<String>blacklistByFilesName;
	
	/**
	 * variabile che contiene il tempo totale del
	 * calcolo dell'md5 su tutti i files che sto 
	 * analizzando
	 */
	private long totalTimeMd5;
	
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
		
		initBlackList();
		
		if(this.gui.getSource() != null){
			this.source = new File(gui.getSource().getPath());
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
						+ ", program version: " + lBackupMain.version + ", total MD5: " + totalTimeMd5 + " ms";
				
				log.scriviLog(msgToLog);
				System.out.println(msgToLog);
			}
		};
		
		td.start();
	}

	/**
	 * 
	 */
	private void initBlackList() {
		DBMS_Blacklist dbBlacklist = new DBMS_Blacklist();
		
		this.blacklistByExtension = dbBlacklist.getAllBlacklistsBy(Blacklist.EXTENSION);
		this.blacklistByName = dbBlacklist.getAllBlacklistsBy(Blacklist.DIRECTORY_NAME);
		this.blacklistByPath = dbBlacklist.getAllBlacklistsBy(Blacklist.ABSOLUTE_PATH);
		this.blacklistByFilesName = dbBlacklist.getAllBlacklistsBy(Blacklist.FILE_NAME);
		
		String msg = "Blacklists: ";
		
		if(this.blacklistByExtension != null){
			msg += "extensions: " + blacklistByExtension.size() + "\n"; 
		}else{
			msg += "extensions: 0" + "\n";
		}
		
		if(this.blacklistByName != null){
			msg += "name: " + blacklistByName.size() + "\n"; 
		}else{
			msg += "name: 0\n"; 
		}
		
		if(this.blacklistByPath != null){
			msg += "absolute path: " + blacklistByPath.size() + "\n"; 
		}else{
			msg += "absolute path: 0\n";
		}
		
		if(this.blacklistByFilesName != null){
			msg += "file name: " + blacklistByFilesName.size() + "\n";
		}else{
			msg += "file name: 0\n";
		}
		
		System.out.println(msg);
		
	}

	/**
	 * 
	 */
	private void stepsMultiThread() {
		MultiSearchMonitor mm = new MultiSearchMonitor(source, destination, this.blacklistByPath,
				this.blacklistByName, this.blacklistByExtension, this.blacklistByFilesName);
		mm.run();
		
		List<File> fList = mm.getList();
		System.out.println("list size " + fList.size());
		
		for(File f : fList){
			//TODO multi thread
			String relative = source.toURI().relativize(f.toURI()).getPath();
			dimSorgente += f.length();
			
			FilesSource fs = new FilesSource();
			
			//non mi piace, da sistemare
			fs.setAbsolutePath(gui.getSource());
			fs.setRelativePath(relative);
			
			long in = System.currentTimeMillis();
			fs.setMd5(Copy.getMd5(f));
			long fi = System.currentTimeMillis();
			totalTimeMd5 += (fi-in);
			//System.out.println(fi-in);
			fs.setRelativePath(relative);
			
			
			int status = dbFileSource.newInsert(fs);
			switch (status) {
			case FilesSource.STATUS_DELETED:
				System.out.println("CANCELLATO) " + relative);
				break;

			case FilesSource.STATUS_ERROR:
				System.out.println("ERRORE) " + relative);
				break;
				
			case FilesSource.STATUS_MODIFY:
				System.out.println("MODIFICATO) " + relative);
				nFilesToCopy[I_MODIFY]++;
				dimBackup += f.length();
				gui.setNumOfModifiedFiles(nFilesToCopy[I_MODIFY]);
				newRevisionFileToCopy(relative, fs.getRevision());
				break;
				
			case FilesSource.STATUS_NEW:
				System.out.println("NUOVO) " + relative);
				nFilesToCopy[I_NEW]++;
				dimBackup += f.length();
				gui.setNumOfNewFiles(nFilesToCopy[I_NEW]);
				
				newFileToCopy(relative);
				break;
				
			case FilesSource.STATUS_NOT_MODIFY:
				//System.out.println("NON MODIFICCATO) " + rel);
				nFilesToCopy[I_NOT_MODIFY]++;
				gui.setNumOfNotModifyFiles(nFilesToCopy[I_NOT_MODIFY]);
				break;
				
			default:
				System.out.println("INDEFINITO) " + relative);
				break;
			}
		}
		
		gui.changeColorOfLabels();
		
		int tot = nFilesToCopy[I_NEW] + nFilesToCopy[I_MODIFY];
		String totalSize = getTotalSize();
		gui.setNumOfFilesToCopy(tot, totalSize);
		
		if(config.isAutomaticBakcup()){
			if(config.isFtp()){
				copyWithFtp();
			}else{
				copyAllFiles();
			}
			
			if(allFilesAreCopied()){
				dbFileSource.backupH2(destination.getAbsolutePath());
				
				JOptionPane.showMessageDialog(null, "All files are copied!!!", 
						"All done", JOptionPane.INFORMATION_MESSAGE);
			}else{
				int choose = JOptionPane.showConfirmDialog(null, 
						"Error, some files are not copied, do you want to see these files???",
						"keep calm :)", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				
				if(choose == JOptionPane.YES_OPTION){
					//mostra  i files che non sono stati copiati
					//e permette di agire direttamente sul probelma
					
					dbFileSource.backupH2(destination.getAbsolutePath());
				}else{
					dbFilesToBackup.deleteAll();
					dbFileSource.backupH2(destination.getAbsolutePath());
				}
			}
			
			config.setDataAttualeBackup();	
		}
	}
	
	/**
	 * this method check if all files are
	 * copied, in the backup directory
	 * @return
	 */
	private boolean allFilesAreCopied(){
		int c = dbFilesToBackup.getFilesToBackupInWait();
		
		if(c == 0)return true;
		else return false;
	}

	/**
	 * 
	 */
	private void copyWithFtp() {
		List<FileToBackup>files = dbFilesToBackup.getAllFilesToBackup();
		
		if(files != null){
			gui.getProgressBar().setMinimum(0);
			gui.getProgressBar().setMaximum(files.size());
			
			for(int i=0; i<files.size(); i++){
				gui.getProgressBar().setValue(i+1);
				gui.setTextToLabFileToCopy(files.get(i).getPathSource());
				
				try{
					/*Copy.copyFileUsingFileChannels(files.get(i).getFileSource(), 
							files.get(i).getFileDestination());*/
					//System.out.println(files.get(i).getFileSource() + " " + files.get(i).getFileDestination());
					 
					Copy.ftp(files.get(i));
					dbFilesToBackup.delete(files.get(i));
					
				}catch(Exception e){
					e.printStackTrace();
					System.err.println(files.get(i).getFileSource().getAbsolutePath());
				}
			}	
		}
		
	}

	/**
	 * search new files
	 * make a decision
	 */
	@SuppressWarnings("unused")
	private void stepsNoMultiThread() {
		
		List<String>path = new ArrayList<>();
		path.add(source.getAbsolutePath());
		navigaDirectory(path, "", source.list().length, gui.getSource());
		
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
				gui.getProgressBar().setValue(i+1);
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
	 * @deprecated
	 * viene usato qll di marco multithread
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
						//TODO analysis
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
