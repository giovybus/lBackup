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
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("INSERT INTO files_source (id_absolute_path, "
					+ "relative_path, md5, last) VALUES ("
					+ f.getAbsolutePath().getId() + ","
					+ "'" + f.getRelativePathClear() + "',"
					+ "'" + f.getMd5() + "',"
					+ f.isLast()
					+ ")");
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * questo metodo nuovo mi permette di fare
	 * atutomaticamente tutti i controlli per 
	 * quanto riguarda la tipologia del file
	 * nel backup, se è un file che rispetto
	 * all'ultimo backup è nuovo o se già c'era
	 * ma è stato modificato
	 * @param f
	 * @return
	 * 	0: file esistetnte ma modificato<br>
	 * 	1: file nuovo<br>
	 * 	2: file uguale<br>
	 * 	3: errore
	 */
	public int newInsert(FilesSource f){
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM files_source WHERE "
					+ "relative_path='" + f.getRelativePathClear() + "'");
			
			if(res.next()){
				f.setId(res.getInt(1));
				
				if(f.getMd5().equals(res.getString("md5"))){
					//file uguale 
					return 2;
				}else{
					//file diverso
					sta.execute("UPDATE files_source SET md5='" + f.getMd5() + "' WHERE id=" + f.getId());
					return 0;
				}
				
			}else{
				//file nuovo
				sta.execute("INSERT INTO files_source (id_absolute_path, "
						+ "relative_path, md5, last) VALUES ("
						+ f.getAbsolutePath().getId() + ","
						+ "'" + f.getRelativePathClear() + "',"
						+ "'" + f.getMd5() + "',"
						+ f.isLast()
						+ ")");
				
				return 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return 3;
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
