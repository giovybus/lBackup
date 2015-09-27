package control;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import main.lBackupMain;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 27/set/2015 17:57:42
 */
public class LogCtr {
	
	/**
	 * indica il percorso del file di log
	 * dove memorizzo i dati del backup
	 */
	private static final String PATH_LOG = lBackupMain.PATH_HOME_DIR + "backup.log";
	
	private Logger logger;
	private FileHandler fh;
	
	/**
	 * 
	 */
	public LogCtr() {
		logger = Logger.getLogger("lbackup");  
	       
		try{
			fh = new FileHandler(PATH_LOG, true);  
	        logger.addHandler(fh);
	        //SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(new Formatter() {
				
				@Override
				public String format(LogRecord r) {
					Date date = new Date();
					date.setTime(r.getMillis());
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					
					
					return "[" + dateFormat.format(date) + "] " + r.getMessage() + "\r\n";
				}
			});
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	     	
	}
	
	public void scriviLog(String log){
		logger.info(log);
		fh.close();
	}
}
