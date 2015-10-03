package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 03/ott/2015 16:15:49
 */
public class DBMS_FileToBeChecked extends DBMS{

	/**
	 * costruttore che mi instaura la connessione
	 * cn il database
	 */
	public DBMS_FileToBeChecked() {
		checkConnessione();
	}
	
	/**
	 * inserisce i files, ma non chiude la connessione
	 * @param f
	 * @return
	 */
	public boolean insert(FileToBeChecked f){
		//checkConnessione();
		try{
			sta = conn.createStatement();
			sta.execute("INSERT INTO file_to_be_checked "
					+ "(full_path, md5) VALUES ("
					+ "'" + f.getFullPathClear() + "',"
					+ "'" + f.getMd5() + "'"
					+ ")");
			
			/*sta.close();
			conn.close();*/
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
