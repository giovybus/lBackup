package control;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import main.lBackupMain;

/**
 * @author Giovanni Buscarino (giovybus) (c) 2015
 *
 * created on 29/mar/2015
 */
public class Config {
	
	/**
	 * indica il percorso della cartella sorgente 
	 */
	private static final String K_PATH_SOURCE = "path_source";
	
	/**
	 * indica il percorso della cartella di destinazione
	 */
	private static final String K_PATH_DESTINATION = "path_destination";
	
	/**
	 * indica la data e l'ora dell'ultimo backup
	 */
	private static final String K_DATA_BACKUP = "last_backup";
	
	/**
	 * indica la data e l'ora dell'ultima analisi effettuata
	 * che solitamente può coincidere con il backup
	 */
	private static final String K_DATA_ANALYSIS = "last_analysis";
	
	/**
	 * boolean value
	 */
	private static final String K_AUTOMATIC_BACKUP = "automatic_backup";
	
	/**
	 * questo campo indica se il caricamento lo effettuo via ftp
	 * oppure in un'altra cartella locale al pc
	 */
	private static final String K_FTP = "ftp";
	
	/**
	 * indica il nome del database h2
	 */
	private static final String K_DATABASE_NAME = "db_name";
	
	/**
	 * the properties
	 */
	private Properties prop;
	
	/**
	 * simple date format per impostare 
	 * il formato della data di ultimo backup / analisi
	 * in dd/MM/yyyy HH:mm:ss
	 */
	private SimpleDateFormat dateFormat;
	
	/**
	 * costruttore senza parametri che 
	 * legge il file e inizializza l'oggetto
	 * prop, se non esiste nessun file lo crea
	 * automaticamente 
	 */
	public Config() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		prop = new Properties();
		leggiFileConfig();
	}
	
	public String getSourcePath(){
		String str = prop.getProperty(K_PATH_SOURCE);
		if(str == null){
			prop.setProperty(K_PATH_SOURCE, "");
			scriviFileINI("add path source");
			return "";
			
		}else{
			return str;
		}
	}
	
	public void setSourcePath(String src){
		prop.setProperty(K_PATH_SOURCE, src);
		scriviFileINI("aggiornamento");
		
	}
	
	public String getDestinationPath(){
		String str = prop.getProperty(K_PATH_DESTINATION);
		if(str == null){
			prop.setProperty(K_PATH_DESTINATION, "");
			scriviFileINI("add path destination");
			return "";
			
		}else{
			return str;
		}
	}
	
	public void setDestinazionePath(String des){
		prop.setProperty(K_PATH_DESTINATION, des);
		scriviFileINI("aggiornamento");
	}
	
	public String getDataBackup(){
		String str = prop.getProperty(K_DATA_BACKUP);
		if(str == null){
			prop.setProperty(K_DATA_BACKUP, "-");
			scriviFileINI("add data backup");
			return "-";
			
		}else{
			return str;
		}
	}
	
	public void setDataAttualeBackup(){
		prop.setProperty(K_DATA_BACKUP, dateFormat.format(new Date()));
		scriviFileINI("aggiornamento");
		
	}
	
	public String getDateAnalysis(){
		String str = prop.getProperty(K_DATA_ANALYSIS);
		if(str == null){
			prop.setProperty(K_DATA_ANALYSIS, "-");
			scriviFileINI("add data backup");
			return "-";
			
		}else{
			return str;
		}
	}
	
	public void setDateAnalysis(){
		prop.setProperty(K_DATA_ANALYSIS, dateFormat.format(new Date()));
		scriviFileINI("aggiornamento");
	}
	
	public boolean isAutomaticBakcup(){
		String str = prop.getProperty(K_AUTOMATIC_BACKUP);
		if(str == null){
			prop.setProperty(K_AUTOMATIC_BACKUP, "1");
			scriviFileINI("add automatic backup");
			return true;
			
		}else{
			return str.equals("1") ? true : false;
		}
	}
	
	public void setAutomaticBakcup(boolean aut){
		prop.setProperty(K_AUTOMATIC_BACKUP, aut ? "1" : "0");
		scriviFileINI("aggiornamento");
	}
	
	public boolean isFtp(){
		String str = prop.getProperty(K_FTP);
		if(str == null){
			prop.setProperty(K_FTP, "0");
			scriviFileINI("add ftp");
			return true;
			
		}else{
			return str.equals("1") ? true : false;
		}
	}
	
	public void setFtp(boolean ftp){
		prop.setProperty(K_FTP, ftp ? "1" : "0");
		scriviFileINI("aggiornamento");
	}
	
	/**
	 * legge il file di configurazione
	 * se esiste altrimenti, lo crea 
	 * con le impostazioni di default
	 */
	private void leggiFileConfig(){
		if(new File(lBackupMain.PATH_CONFIG).exists()){
			try{
				prop.load(new FileInputStream(lBackupMain.PATH_CONFIG));
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			prop.setProperty(K_PATH_SOURCE, "-");
			prop.setProperty(K_PATH_DESTINATION, "-");
			prop.setProperty(K_DATA_BACKUP, "-");
			prop.setProperty(K_DATA_ANALYSIS, "-");
			prop.setProperty(K_AUTOMATIC_BACKUP, "1");
			prop.setProperty(K_FTP, "0");
			scriviFileINI("first-launch");
			
		}
	}
	
	/**
	 * scrive il file prop
	 * @param commento
	 * serve per inserire un commento
	 * nel file
	 */
	private void scriviFileINI(String commento){
		try{
			OutputStream o = new FileOutputStream(new File(lBackupMain.PATH_CONFIG));
			prop.store(o, commento);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
