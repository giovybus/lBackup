package entity;

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
				a = new AbsolutePath();
				a.setId(res.getInt("id"));
				a.setPath(res.getString("path"));
				a.setType(res.getInt("type"));
			}
			
			sta.close();
			res.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return a;
	}

}
