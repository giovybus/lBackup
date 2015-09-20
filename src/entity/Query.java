package entity;
import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.lBackupMain;
import boundary.BackupGui;


/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 04/apr/2015 22.19 (sabato di pasqua)
 */
public class Query {
	
	/**
	 * serve per la tabella percorsi_assoluti
	 */
	public static final int DIR_SORGENTE = 1;
	
	/**
	 * serve per la tabella percorsi_assoluti
	 */
	public static final int DIR_DESTINAZIONE = 2;
	
	/**
	 * serve per la tabella percorsi_assoluti
	 */
	public static final int DIR_SUBDESTINAZIONE = 0;
	
	
	/**
	 * serve per la tabella da_backuppare
	 */
	public static final int STATO_DA_COMPLETARE = 0;
	
	/**
	 * serve per la tabella da_backuppare
	 */
	public static final int STATO_COMPLETATO = 1;
	
	private static final String TABLE_PERCORSI_ASSOLUTI = 
			"create table if not exists percorsi_assoluti ("
					+ "id int primary key auto_increment not null," 
					+ "path varchar(500) unique,"
					+ "is_source int(2) default 0"
					+ ")";
	
	private static final String TABLE_FILES = 
			"create table if not exists files("
					+ "id int primary key auto_increment not null,"
					+ "id_percorso_assoluto int,"
					+ "percorso_relativo varchar(500),"
					+ "md5 varchar(32),"
					+"foreign key (id_percorso_assoluto) references percorsi_assoluti(id) on delete cascade,"
					+ "unique(percorso_relativo, id_percorso_assoluto)"
					+ ")";
	
	private static final String TABLE_VIRTUAL_LINK = 
			"create table if not exists virtual_link ("
				+ "id_files int," 
				+ "id_percorso_assoluto int," 
	
				+ "foreign key (id_files) references files(id) on delete cascade,"
				+ "foreign key (id_percorso_assoluto) references percorsi_assoluti(id) on delete cascade"	
				+ ")";
	
	public static final String TABLE_DA_BACKUPPARE = 
			"create table if not exists da_backuppare ("
				+ "id int primary key auto_increment not null," 
				+ "path_sorgente varchar(500),"
				+ "path_destinazione varchar(500),"
				+ "stato int(2)"
				+ ")";
	
	private Connection conn;
	private ResultSet res;
	private Statement sta;
	
	//private static final String urlH2 = "jdbc:h2:C:\\Users\\giovybus\\AppData\\Roaming\\lBackup\\db1;auto_server=true";
	private static final String urlH2 = "jdbc:h2:" + lBackupMain.PATH_HOME_DIR + "db1;auto_server=true";
	private static final String urlMysql = "jdbc:mysql://127.0.0.1:3306/lBackup";
	private static String url;
	private static final String user = "root";
	private static final String psw = "";
	
	/**
	 * costruttore, instanzia la connessione
	 */
	public Query() {
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			url = urlH2;
			break;

		case lBackupMain.SERVER_MYSQL:
			url = urlMysql;
			break;
			
		default:
			url = urlH2;
			break;
		}
		
		createConn();
	}
	
	private void createConn(){
		try{
			conn = DriverManager.getConnection(url, user, psw);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * crea tutte le tabelle del database se non
	 * esistono
	 * @return
	 */
	public boolean creaDatabase() {
		try {
			System.out.println("creo il database");
			sta = conn.createStatement();
			sta.execute(TABLE_PERCORSI_ASSOLUTI + ";" 
					+ TABLE_FILES + ";" 
					+ TABLE_VIRTUAL_LINK + ";" 
					+ TABLE_DA_BACKUPPARE);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * questo metodo inserisce un path assoluto
	 * e ritorna il suo id
	 * 
	 * @param path
	 * @param isSource
	 * @return
	 * 
	 * questo metodo ritorna l'id generato dall'inserimento
	 */
	public int inserisciPercorsoAssoluto(String path, int isSource){
		String queryMysql = "INSERT INTO percorsi_assoluti (path, is_source) VALUES ('" + path.replace("\\", "\\\\") + "', " + isSource + ")";
		String queryH2 = "INSERT INTO percorsi_assoluti (path, is_source) VALUES ('" + path + "', " + isSource + ")";
		
		String query = new String();
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			query = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			query = queryMysql;
			break;

		default:
			query = queryH2;
			break;
		}
		
		System.out.println(query);
		try{
			sta = conn.createStatement();
			sta.execute(query, Statement.RETURN_GENERATED_KEYS);
			res = sta.getGeneratedKeys();
			if(res.next()){
				return res.getInt(1);
			}else{
				return 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * metodo da utilizzare solo per il sorgente 
	 * e la destinazione che per ora sono solo una cartella
	 * @return
	 */
	public List<String> getPercorsoAssoluto(int tipologia) {
		List<String>p = null;
		String query = new String();
		String queryH2 = "SELECT * FROM PERCORSI_ASSOLUTI where IS_SOURCE = " + tipologia + " limit 1";
		String queryMySql = "SELECT * FROM PERCORSI_ASSOLUTI where IS_SOURCE = " + tipologia + " limit 1";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			query = queryH2;
			break;

		case lBackupMain.SERVER_MYSQL:
			query = queryMySql;
			break;
			
		default:
			query = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(query);
			
			if(res.next()){
				if(p == null)p = new ArrayList<>();
				p.add(new String(res.getString("PATH")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	/**
	 * @param idPercorsoAssoluto
	 */
	public List<String> getFilesSorgente(int idPercorsoAssoluto) {
		List<String>p = null;
		
		String query = new String();
		String queryH2 = "SELECT PERCORSO_RELATIVO  FROM FILES where ID_PERCORSO_ASSOLUTO  = " + idPercorsoAssoluto;
		String queryMysql = "SELECT PERCORSO_RELATIVO  FROM FILES where ID_PERCORSO_ASSOLUTO  = " + idPercorsoAssoluto;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			query = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			query = queryMysql;
			break;

		default:
			query = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(query);
			
			while(res.next()){
				if(p == null)p = new ArrayList<>();
				p.add(new String(res.getString(1)));
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;		
	}
	
	public boolean inserisciFile(int idPercorsoAss, String percorsoRelativo, String md5){
		String query = new String();
		String queryH2 = "INSERT INTO FILES ( ID_PERCORSO_ASSOLUTO , "
				+ "PERCORSO_RELATIVO , md5 ) VALUES ("
				+ idPercorsoAss + ","
				+ "'" + percorsoRelativo.replace("'", "''") + "',"
				+ "'" + md5 + "'"
				+ ")";
		
		String queryMysql = "INSERT INTO FILES ( ID_PERCORSO_ASSOLUTO , "
				+ "PERCORSO_RELATIVO , md5 ) VALUES ("
				+ idPercorsoAss + ","
				+ "'" + percorsoRelativo.replace("'", "''").replace("\\", "\\\\") + "',"
				+ "'" + md5 + "'"
				+ ")";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			query = queryH2;
			break;

		case lBackupMain.SERVER_MYSQL:
			query = queryMysql;
			break;
			
		default:
			query = queryH2;
			break;
		}
		
		System.out.println(query);
		try {
			sta = conn.createStatement();
			sta.execute(query);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void inserisciVirtualLink(int idFile, int idPercorsoAssoluto){

		String q = new String();
		String queryH2 = "INSERT INTO VIRTUAL_LINK  (ID_FILES, ID_PERCORSO_ASSOLUTO ) VALUES ("
				+ idFile + ","
				+  idPercorsoAssoluto
				+ ")";
		String queryMysql = "INSERT INTO VIRTUAL_LINK  (ID_FILES, ID_PERCORSO_ASSOLUTO ) VALUES ("
				+ idFile + ","
				+  idPercorsoAssoluto
				+ ")";

		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		
		System.out.println(q);
		
		try {
			sta = conn.createStatement();
			sta.execute(q);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param sorgente
	 * @return
	 */
	public int getIdPercorsoAssoluto(String sorgente) {
		int id = 0;
		String q = new String();
		
		String queryH2 = "SELECT id FROM PERCORSI_ASSOLUTI where path = '" + sorgente + "' limit 1";
		String queryMysql = "SELECT id FROM PERCORSI_ASSOLUTI where path = '" + sorgente.replace("\\", "\\\\") + "' limit 1";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		
		System.out.println(q);
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			
			if(res.next()){
				id = res.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * mi da sia il path [0] che l'id [1]
	 * @return
	 */
	public String []getPathDirUltimoBackup(){
		String query = new String();
		String queryH2 = "SELECT path, id FROM PERCORSI_ASSOLUTI WHERE is_source=" + DIR_SUBDESTINAZIONE + " ORDER BY id DESC limit 1";
		String queryMysql = "SELECT path, id FROM PERCORSI_ASSOLUTI WHERE is_source=" + DIR_SUBDESTINAZIONE + " ORDER BY id DESC limit 1";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			query = queryH2;
			break;

		case lBackupMain.SERVER_MYSQL:
			query = queryMysql;
			break;
			
		default:
			query = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(query);
			
			if(res.next()){
				String s[] = new String[2];
				s[0] = res.getString(1);
				s[1] = Integer.toString(res.getInt(2));
				
				return s;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * questo metodo serve per tenere aggiornata la lista
	 * dei file nel path sorgente cosi da avere le info
	 * aggiornate al momento di un nuvo backup
	 */
	public void cancellaTuttiFilesSorgenti(int idPercorsoAssoluto){
		String query = new String();
		String queryH2 = "delete from FILES where ID_PERCORSO_ASSOLUTO = " + idPercorsoAssoluto;
		String queryMysql = "delete from FILES where ID_PERCORSO_ASSOLUTO = " + idPercorsoAssoluto;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			query = queryH2;
			break;

		case lBackupMain.SERVER_MYSQL:
			query = queryMysql;
			break;
			
		default:
			query = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			sta.execute(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * questo metodo mi trova tutti i file che sono nuovi rispetto 
	 * all'ultimo backup effettuato nella cartella source
	 * @return
	 * mi ritorna il numero di file nuovi trovati
	 */
	public int scovaFileNuovi(File sorgente, int idSorgente, File destinazione, int idUltimoBackup) {
		int n=0;
		String q = new String();
		//String queryH2 = "(select PERCORSO_RELATIVO   from files where ID_PERCORSO_ASSOLUTO =" + idSorgente + ") except (select PERCORSO_RELATIVO from files where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") except select PERCORSO_RELATIVO from files where id in (select ID_FILES  from VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")";
		String queryH2 = "select PERCORSO_RELATIVO   from files where ID_PERCORSO_ASSOLUTO =" + idSorgente + " minus (( select PERCORSO_RELATIVO from files where ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + " ) union all(select PERCORSO_RELATIVO from (select * from VIRTUAL_LINK where id_percorso_assoluto =" + idUltimoBackup + ")v left join  files f on v.id_files =f.id))";
		String queryMysql = "(select PERCORSO_RELATIVO   from files where ID_PERCORSO_ASSOLUTO =" + idSorgente + ") except (select PERCORSO_RELATIVO from files where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") except select PERCORSO_RELATIVO from files where id in (select ID_FILES  from VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		
		System.out.println(q);
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			while(res.next()){
				System.out.println("file da copiare: " + res.getString("PERCORSO_RELATIVO"));
				BackupGui.addStringAreaLog(res.getString("PERCORSO_RELATIVO"));
				scriviPathInDaBackuppare(sorgente.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"),
						destinazione.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"), STATO_DA_COMPLETARE);
				n++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return n;
	}

	/**
	 * questo metodo effettua la query per intercettare 
	 * i file che rispetto all'ultimo backup effettuato
	 * sono cambiati
	 */
	public int scovaFileDiversi(File sorgente, int idSorgente, File destinazione, int idUltimoBackup) {
		int num=0;
		
		String q = new String();
		String queryH2 = "select f.* from (select * from FILES   where id_percorso_assoluto =" + idSorgente + ")f "
		+ "left join" 
		+"("
		+ "select percorso_relativo  ,md5 ,id from files where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup 
		+ " union all"
		+ "(select percorso_relativo, md5 ,id from (select * from VIRTUAL_LINK where id_percorso_assoluto =" + idUltimoBackup + ")v left join  files f on v.id_files =f.id))t "
		+"on f.md5=t.md5 "
		+ "where t.md5 is null";
		
//		String queryH2 = "select * from (SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")h except( select d.* from (SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")d join (select * from files where ID_PERCORSO_ASSOLUTO = " + idSorgente + ")f on f.percorso_relativo =d.percorso_relativo and f.md5=d.md5)";
		String queryMysql = "select * from (SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")h except( select d.* from (SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")d join (select * from files where ID_PERCORSO_ASSOLUTO = " + idSorgente + ")f on f.percorso_relativo =d.percorso_relativo and f.md5=d.md5)";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		
		System.out.println(q);
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			while(res.next()){
				System.out.println("file da copiare: " + res.getInt("id") + " " + res.getString("PERCORSO_RELATIVO"));
				BackupGui.addStringAreaLog(res.getString("PERCORSO_RELATIVO"));
				scriviPathInDaBackuppare(sorgente.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"),
						destinazione.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"), STATO_DA_COMPLETARE);
				num++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return num;
	}
	
	/**
	 * mi effettua la query per selezionare i
	 * file che non sono cambiati rispetto
	 * all'ultimo backup effettuato
	 */
	public int scovaFileNonCambiati(File sorgente, int idSorgente, File destinazione, int idUltimoBackup, int idPercorsoAssolutoBackup) {
		int n=0;
		String q = new String();
		String queryH2 = "select d.* from(SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")d join  (select * from files where ID_PERCORSO_ASSOLUTO = " + idSorgente + " )f on f.percorso_relativo =d.percorso_relativo and f.md5=d.md5";;
		String queryMysql = "select d.* from(SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")d join  (select * from files where ID_PERCORSO_ASSOLUTO = " + idSorgente + " )f on f.percorso_relativo =d.percorso_relativo and f.md5=d.md5";;

		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		
		System.out.println(q);
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			
			while(res.next()){
				System.out.println("file non modificati: " + res.getInt("id") + " " + res.getString("PERCORSO_RELATIVO"));
				BackupGui.addStringAreaLog(res.getString("PERCORSO_RELATIVO"));
				
				inserisciVirtualLink(res.getInt("id"), idPercorsoAssolutoBackup);
				n++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return n;
	}
	
	/**
	 * questo metodo mi scrive nella tabella da_backuppare
	 * i file che dovrò copiare successivamente
	 * @param sorg
	 * @param dest
	 * @param stato
	 */
	public boolean scriviPathInDaBackuppare(String sorg, String dest, int stato){
		String q = new String();
		
		String queryH2 = "insert into DA_BACKUPPARE (PATH_SORGENTE ,PATH_DESTINAZIONE ,STATO ) "
				+ "values("
				+ "'" + sorg.replace("'", "''") + "',"
				+ "'" + dest.replace("'", "''") + "',"
				+ stato 
				+ ")";
		
		String queryMysql = "insert into DA_BACKUPPARE (PATH_SORGENTE ,PATH_DESTINAZIONE ,STATO ) "
				+ "values("
				+ "'" + sorg.replace("'", "''") + "',"
				+ "'" + dest.replace("'", "''") + "',"
				+ stato 
				+ ")";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;

		default:
			q = queryH2;
			break;
		}
		try {
			sta = conn.createStatement();
			sta.execute(q);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void cancellaFileDaBackuppare(int id){
		String q = new String();
		String queryH2 = "delete from DA_BACKUPPARE where ID = " + id;
		String queryMysql = "delete from DA_BACKUPPARE where ID = " + id;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			sta.execute(q);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * mi cancella tutti i dati nella tabella da 
	 * backuppare, dove lo stato è in attesa
	 */
	public void cancellaTuttiFileDaBackuppare() {
		String q = new String();
		String queryH2 = "delete from DA_BACKUPPARE where stato = " + STATO_DA_COMPLETARE;
		String queryMysql = "delete from DA_BACKUPPARE where stato = " + STATO_DA_COMPLETARE;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;

		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;
			
		default:
			q = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			sta.execute(q);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param int1
	 * @param statoCompletato
	 */
	private void modificaStatoFileDaBackuppare(int id, int stato) {
		String q = new String();
		String queryH2 = "update DA_BACKUPPARE set stato = " + stato + " where ID = " + id;
		String queryMysql = "update DA_BACKUPPARE set stato = " + stato + " where ID = " + id;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		try {
			sta = conn.createStatement();
			sta.execute(q);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param idPathAssoluto
	 * id del path appena creato
	 * @param pathRelativo
	 * il path della cartella appena creata backup/14014520150/
	 */
	public void effettuaBackup(int idPathAssoluto, URI pathRelativo){
		int numFileStimati = getNumfileDaCopiare();
		
		String q = new String();
		String queryH2 = "SELECT PATH_SORGENTE ,PATH_DESTINAZIONE, id FROM DA_BACKUPPARE WHERE STATO = " + STATO_DA_COMPLETARE;
		String queryMysql = "SELECT PATH_SORGENTE ,PATH_DESTINAZIONE, id FROM DA_BACKUPPARE WHERE STATO = " + STATO_DA_COMPLETARE;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2; 
			break;
		}
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
		
			BackupGui.setMinMaxBar(0, numFileStimati);
			
			while(res.next()){
				try {
					Copy.copyFileUsingFileChannels(new File(res.getString(1)), new File(res.getString(2)));
					
					File copiato = new File(res.getString(2));
					String rel = pathRelativo.relativize(copiato.toURI()).getPath();
					inserisciFile(idPathAssoluto, rel, Copy.getMd5(copiato));
					
					//cancellaFileDaBackuppare(res.getInt(3));
					modificaStatoFileDaBackuppare(res.getInt(3), STATO_COMPLETATO);
					System.out.println("FILE COPIATO");
					BackupGui.addStringAreaLog("FILE COPIATO CORRETTAMENTE: " + res.getString(1));
				} catch (Exception e) {
					System.err.println("FILE NON COPIATO: " + res.getString(1));
					BackupGui.addStringAreaLog("ERRORE FILE NON COPIATO: " + res.getString(1));
					e.printStackTrace();
				}
				
				BackupGui.increaseBar();
				
			}
			
			//effettua il backup solo se è selezionato il server h2
			if(lBackupMain.PAR_SERVER == lBackupMain.SERVER_H2){
				backupH2(getPercorsoAssoluto(DIR_DESTINAZIONE).get(0));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @return
	 */
	private int getNumfileDaCopiare() {
		int i = 0;
		String q = new String();
		String queryH2 = "SELECT count(id) FROM DA_BACKUPPARE WHERE STATO = " + STATO_DA_COMPLETARE;
		String queryMysql = "SELECT count(id) FROM DA_BACKUPPARE WHERE STATO = " + STATO_DA_COMPLETARE;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			
			if(res.next()){
				i=res.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return i;
	}

	/**
	 * Mi da la dimensione dei file contenuti
	 * nella cartella sorgente
	 * @return
	 */
	public long getDimensioneCartellaSorgente(String s){
		long dim = 0;
		String q = new String();
		String queryH2 = "SELECT PERCORSO_RELATIVO FROM FILES WHERE ID_PERCORSO_ASSOLUTO  = (SELECT ID FROM PERCORSI_ASSOLUTI WHERE PATH = '" + s + "')";
		String queryMysql = "SELECT PERCORSO_RELATIVO FROM FILES WHERE ID_PERCORSO_ASSOLUTO  = (SELECT ID FROM PERCORSI_ASSOLUTI WHERE PATH = '" + s + "')";
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;

		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;
			
		default:
			q = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			
			while(res.next()){
				dim += new File(s + res.getString(1)).length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dim;
	}

	/**
	 * mi da la dimensione dei file 
	 * appena copiati nella cartella
	 * di backup 
	 * @return
	 */
	public long getDimensioneCartellaBackup() {
		long dim = 0;
		
		String q = new String();
		String queryH2 = "SELECT PATH_DESTINAZIONE, ID FROM DA_BACKUPPARE WHERE STATO = " + STATO_COMPLETATO;
		String queryMysql = "SELECT PATH_DESTINAZIONE, ID FROM DA_BACKUPPARE WHERE STATO = " + STATO_COMPLETATO;
		
		switch (lBackupMain.PAR_SERVER) {
		case lBackupMain.SERVER_H2:
			q = queryH2;
			break;
			
		case lBackupMain.SERVER_MYSQL:
			q = queryMysql;
			break;

		default:
			q = queryH2;
			break;
		}
		
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			
			while(res.next()){
				dim += new File(res.getString(1)).length();
				cancellaFileDaBackuppare(res.getInt(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dim;
	}
	
	private void backupH2(String path){
		try{
			String[] args =
				{
				   "-url",
				   url,
				   "-user",
				   user,
				   "-script",
				   path + "lbackup.zip"
				};
				
				for(String s : args) System.out.print(s + " ");
			
				org.h2.tools.Script.main(args);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void restoreH2(){
		try{
			String[] args =
				{
				   "-url",
				   url,
				   "-user",
				   user,
				   "-script",
				    lBackupMain.PATH_HOME_DIR + "restore.zip"
				};
				org.h2.tools.RunScript.main(args);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void chiudiConnessione(){
		try {
			if(conn != null){
				System.out.println("chiudo la connessione.");
				conn.close();
			}
			
			if(sta != null){
				System.out.println("chiudo lo statement");
				sta.close();
			}
			
			if(res != null){
				System.out.println("chiudo il resultset");
				res.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
