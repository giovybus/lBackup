package control;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import main.lBackupMain;

/**
 * @author Giovanni Buscarino (giovybus) (c) 2015
 *
 * created on 29/mar/2015
 */
public class Config {
	
	private static final String K_PATH_SOURCE = "pth_src";
	private static final String K_PATH_DESTINATION = "pth_des";
	private static final String K_DATA_BACKUP = "last_bck";
	
	private Properties prop;
	
	/**
	 * costruttore senza parametri che 
	 * legge il file e inizializza l'oggetto
	 * prop, se non esiste nessun file lo crea
	 * automaticamente 
	 */
	public Config() {
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
			prop.setProperty(K_DATA_BACKUP, "");
			scriviFileINI("add data backup");
			return "";
		}else{
			return str;
		}
	}
	
	public void setDataAttualeBackup(){
		prop.setProperty(K_DATA_BACKUP, new Date().toString());
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
