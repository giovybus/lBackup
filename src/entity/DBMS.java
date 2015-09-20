package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import main.lBackupMain;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 21:47:57
 */
public class DBMS {
	private static final String urlH2 = "jdbc:h2:" + lBackupMain.PATH_HOME_DIR + "db1;auto_server=true";
	private static final String user = "root";
	private static final String psw = "";
	protected Connection conn;
	protected Statement sta;
	protected ResultSet res;
	
	/**
	 * constructor
	 */
	public DBMS() {
		createConn();
	}
	
	/**
	 * create the connection with the database
	 */
	private void createConn(){
		try{
			conn = DriverManager.getConnection(urlH2, user, psw);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

}
