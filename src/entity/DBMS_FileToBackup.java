package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 27/set/2015 19:10:08
 */
public class DBMS_FileToBackup extends DBMS{
	
	public boolean insert(FileToBackup fb){
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("INSERT INTO file_to_backup (absolute_path_source, "
					+ "absolute_path_destination, status) VALUES ("
					+ "'" + fb.getPathSource() + "',"
					+ "'" + fb.getPathDestination() + "',"
					+ "" + fb.getStatus()
					+ ")");
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean modify(FileToBackup fb){
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("UPDATE file_to_backup SET "
					+ "absolute_path_source='" + fb.getPathSource() + "',"
					+ "absolute_path_destination='" + fb.getPathDestination() + "',"
					+ "status=" + fb.getStatus() + " WHERE id=" + fb.getId());
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<FileToBackup>getAllFilesToBackup(){
		List<FileToBackup>files = null;
		checkConnessione();
		try{
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM file_to_backup WHERE 1");
			while(res.next()){
				if(files==null)files = new ArrayList<>();
				files.add(new FileToBackup(res.getInt("id"), 
						res.getString("absolute_path_source"), 
						res.getString("absolute_path_destination"), 
						res.getInt("status")));
			}
			
			sta.close();
			res.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return files;
	}

	/**
	 * @param fileToBackup
	 */
	public boolean delete(FileToBackup fb) {
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("DELETE FROM file_to_backup WHERE id=" + fb.getId());
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
