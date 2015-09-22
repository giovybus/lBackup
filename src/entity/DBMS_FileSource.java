package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 23:33:55
 */
public class DBMS_FileSource extends DBMS{

	/**
	 * 
	 */
	public DBMS_FileSource() {
		
	}
	
	public boolean insert(FilesSource f){
		
		//checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("INSERT INTO files_source (id_absolute_path, "
					+ "relative_path, md5, last) VALUES ("
					+ f.getAbsolutePath().getId() + ","
					+ "'" + f.getRelativePathClear() + "',"
					+ "'" + f.getMd5() + "',"
					+ f.isLast()
					+ ")");
			
			//sta.close();
			//conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * cancella tutti gli elementi vecchi della tabella 
	 * files_source
	 */
	public boolean removeAllLastTrue() {
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("DELETE FROM files_source WHERE last=1");
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * chiude la connessione, metodo da  usare solo
	 * qnd chiamo la insert, xk lascio la connessione
	 * aperta
	 */
	public void closeConnection(){
		try {
			sta.close();
			conn.close();
		} catch (Exception exc) {
			exc.printStackTrace();			
		}
	}
	
	/**
	 * imposta last=1 a tutti i valori della tabella
	 * @return
	 */
	public boolean setAllLastTrue(){
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("UPDATE files_source SET last=1 WHERE 1");
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
