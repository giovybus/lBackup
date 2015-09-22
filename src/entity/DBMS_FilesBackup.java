package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 22:08:33
 */
public class DBMS_FilesBackup extends DBMS{
	
	public boolean insert(FilesBackup f){
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("INSERT INTO files_backup (id_absolute_path, "
					+ "relative_path, md5, last) VALUES ("
					+ f.getAbsolutePath().getId() + ","
					//+ "'" + f.getRelativePathClear() + "',"
					+ ")");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
