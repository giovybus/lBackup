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
					+ "relative_path, md5) VALUES ("
					+ f.getAbsolutePath().getId() + ","
					+ "'" + f.getRelativePath() + "',"
					+ "'" + f.getMd5() + "',"
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
	 * 
	 * @return
	 */
	public int newInsert(FilesSource f){
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM files_source WHERE "
					+ "relative_path='" + f.getRelativePath() + "'");
			
			if(res.next()){
				f.setId(res.getInt("id"));
				
				if(f.getMd5().equals(res.getString("md5"))){
					//file non modificato
					return FilesSource.STATUS_NOT_MODIFY;
					
				}else{
					//file modificato
					sta.execute("UPDATE files_source SET md5='" + f.getMd5() + "' WHERE id=" + f.getId());
					return FilesSource.STATUS_MODIFY;
				}
				
			}else{
				//file nuovo
				sta.execute("INSERT INTO files_source (id_absolute_path, "
						+ "relative_path, md5) VALUES ("
						+ f.getAbsolutePath().getId() + ","
						+ "'" + f.getRelativePath() + "',"
						+ "'" + f.getMd5() + "',"
						+ ")");
				
				return FilesSource.STATUS_NEW;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return FilesSource.STATUS_ERROR;
			
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
	
}
