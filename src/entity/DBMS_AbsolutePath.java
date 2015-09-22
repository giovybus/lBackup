package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 22:01:21
 */
public class DBMS_AbsolutePath extends DBMS{

	/**
	 * default constructor 
	 */
	public DBMS_AbsolutePath() {
		
	}
	
	/**
	 * insert new absolute path in the database
	 * @param abs
	 * @return
	 */
	public boolean insert(AbsolutePath abs){
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("INSERT INTO absolute_path (path, type) VALUES ("
					+ "'" + abs.getPathClear() + "'," 
					+ abs.getType() 
					+ ")");
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public AbsolutePath getAbsolutePathById(int id){
		checkConnessione();
		
		AbsolutePath a = null;
		try{
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM absolute_path WHERE id=" + id);
			
			if(res.next()){
				a = new AbsolutePath(res.getInt("id"), 
						res.getString("path"), res.getInt("type"));
			}
			
			sta.close();
			res.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return a;
	}
	
	/**
	 * qst metodo mi da tutte le cartelle di root
	 * che sono contrassegnate come sorgenti di backup
	 * @return
	 */
	public List<AbsolutePath> getAllRootSources(){
		List<AbsolutePath>all = null;
		
		checkConnessione();
		try{
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM absolute_path WHERE type=0");
			
			while(res.next()){
				if(all == null)all = new ArrayList<AbsolutePath>();
				all.add(new AbsolutePath(res.getInt("id"), 
						res.getString("path"), res.getInt("type")));
			}
			
			sta.close();
			res.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return all;
	}

	/**
	 * @return
	 */
	public AbsolutePath getRootBackupDir() {
		AbsolutePath abs = null;
		
		checkConnessione();
		try{
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM absolute_path WHERE type=1 LIMIT 1");
			
			if(res.next()){
				abs = new AbsolutePath(res.getInt("id"), 
						res.getString("path"), res.getInt("type"));
			}
			
			sta.close();
			res.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return abs;
	}

	/**
	 * @param temp
	 */
	public boolean updateRootDestination(AbsolutePath temp) {
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("UPDATE absolute_path SET "
					+ "path='" + temp.getPathClear() + "' " 
					+ "WHERE type=" + AbsolutePath.ROOT_DESTINATION);
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param temp
	 * @return
	 */
	public boolean updateRootSource(AbsolutePath temp) {
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("UPDATE absolute_path SET "
					+ "path='" + temp.getPathClear() + "' " 
					+ "WHERE type=" + AbsolutePath.SOURCE);
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
