package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 28/set/2015 19:24:07
 */
public class DBMS_Blacklist extends DBMS{
	
	public boolean insert(Blacklist blacklist){
		checkConnessione();
		try {
			sta = conn.createStatement();
			sta.execute("INSERT INTO blacklist (path, kind) VALUES ("
					+ "'" + blacklist.getPathClear() + "'," 
					+ blacklist.getKind() 
					+ ")");
			
			sta.close();
			conn.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Blacklist>getAllBlacklists(){
		List<Blacklist>bl = null;
		checkConnessione();
		try{
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM blacklist WHERE 1");
			
			while(res.next()){
				if(bl == null)bl = new ArrayList<>();
				bl.add(new Blacklist(res.getString("path"), res.getInt("kind")));
			}
			
			sta.close();
			conn.close();
			res.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bl;
	}
	
	public List<String>getAllBlacklistsBy(int kind){
		List<String>bl = null;
		checkConnessione();
		try{
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM blacklist WHERE kind=" + kind);
			
			while(res.next()){
				if(bl == null)bl = new ArrayList<>();
				bl.add(res.getString("path"));
			}
			
			sta.close();
			conn.close();
			res.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bl;
	}
}
