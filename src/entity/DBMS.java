package entity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
	private String DATABASE_NAME="db1";
	private String urlH2 = "jdbc:h2:" + lBackupMain.PATH_HOME_DIR + DATABASE_NAME + ";AUTO_SERVER=TRUE;Mode=Mysql;";
	
	@SuppressWarnings("unused")
	private static final String urlMySql = "jdbc:mysql://127.0.0.1:3306/db1";
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
	
	/**
	 * check the connection
	 */
	protected void checkConnessione() {
		try{
			if(conn.isClosed()){
				createConn();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * this method cerate the database with all tables
	 * @return
	 */
	public boolean createDatabase(){
		String query = new String();
		try{
			if(new File("database/database").exists()){
				FileReader file = new FileReader("database/database");
				BufferedReader buff = new BufferedReader(file);
				
					while(buff.ready()){
//						split(";")[0]
						query += buff.readLine();
					}
				
				buff.close();
				System.out.println(query);
				
				checkConnessione();
				
				sta = conn.createStatement();
				sta.execute(query);
				
				sta.close();
				conn.close();
				return true;
			}else{
				System.out.println("file not exists.");
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
