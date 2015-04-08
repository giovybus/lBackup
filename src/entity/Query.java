package entity;
import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	
	
	private Connection conn;
	private ResultSet res;
	private Statement sta;
	
	
	/**
	 * costruttore, instanzia la connessione
	 */
	public Query() {
		createConn();
	}
	
	private void createConn(){
		try{
			conn = DriverManager.getConnection("jdbc:h2:C:\\Users\\giovybus\\AppData\\Roaming\\Legendary\\db1;auto_server=true", "root", "");
		}catch(Exception e){
			e.printStackTrace();
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
		String query = "INSERT INTO percorsi_assoluti (path, is_source) VALUES ('" + path + "', " + isSource + ")";
		
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
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT * FROM PERCORSI_ASSOLUTI where IS_SOURCE = " + tipologia + " limit 1");
			
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
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT PERCORSO_RELATIVO  FROM FILES where ID_PERCORSO_ASSOLUTO  = " + idPercorsoAssoluto);
			
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

		try {
			sta = conn.createStatement();
			sta.execute("INSERT INTO FILES ( ID_PERCORSO_ASSOLUTO , "
					+ "PERCORSO_RELATIVO , md5 ) VALUES ("
					+ idPercorsoAss + ","
					+ "'" + percorsoRelativo + "',"
					+ "'" + md5 + "',"
					+ ")");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void inserisciVirtualLink(int idFile, int idPercorsoAssoluto){

		String q = "INSERT INTO VIRTUAL_LINK  (ID_FILES, ID_PERCORSO_ASSOLUTO ) VALUES ("
				+ idFile + ","
				+  idPercorsoAssoluto
				+ ")";
		
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
		String q = "SELECT id FROM PERCORSI_ASSOLUTI where path = '" + sorgente + "' limit 1";
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
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT path, id FROM PERCORSI_ASSOLUTI WHERE is_source=" + DIR_SUBDESTINAZIONE + " ORDER BY id DESC limit 1");
			
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
		try {
			sta = conn.createStatement();
			sta.execute("delete from FILES where ID_PERCORSO_ASSOLUTO = " + idPercorsoAssoluto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * questo metodo mi trova tutti i file che sono nuovi rispetto 
	 * all'ultimo backup effettuato nella cartella source
	 */
	public void scovaFileNuovi(File sorgente, int idSorgente, File destinazione, int idUltimoBackup) {
				
		//String q = "(select PERCORSO_RELATIVO  from files where ID_PERCORSO_ASSOLUTO =" + idSorgente + ") except (select PERCORSO_RELATIVO  from files where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ")";
		String q = "(select PERCORSO_RELATIVO   from files where ID_PERCORSO_ASSOLUTO =" + idSorgente + ") except (select PERCORSO_RELATIVO from files where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") except select PERCORSO_RELATIVO from files where id in (select ID_FILES  from VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")";
		System.out.println(q);
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			while(res.next()){
				System.out.println("file da copiare: " + res.getString("PERCORSO_RELATIVO"));
				BackupGui.addStringAreaLog(res.getString("PERCORSO_RELATIVO"));
				scriviPathInDaBackuppare(sorgente.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"),
						destinazione.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"), STATO_DA_COMPLETARE);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * questo metodo effettua la query per intercettare 
	 * i file che rispetto all'ultimo backup effettuato
	 * sono cambiati
	 */
	public void scovaFileDiversi(File sorgente, int idSorgente, File destinazione, int idUltimoBackup) {
		
		String q = "select * from (SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")h except( select d.* from (SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")d join (select * from files where ID_PERCORSO_ASSOLUTO = " + idSorgente + ")f on f.percorso_relativo =d.percorso_relativo and f.md5=d.md5)";
		//String q = "SELECT * FROM files WHERE id= (SELECT f.id FROM FILES f, FILES f1 where f.ID_PERCORSO_ASSOLUTO =" + idSorgente + " and f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + " and f.PERCORSO_RELATIVO = f1.PERCORSO_RELATIVO and  f.md5 <> f1.md5 and f.id > f1.id)";
		System.out.println(q);
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			while(res.next()){
				System.out.println("file da copiare: " + res.getInt("id") + " " + res.getString("PERCORSO_RELATIVO"));
				BackupGui.addStringAreaLog(res.getString("PERCORSO_RELATIVO"));
				scriviPathInDaBackuppare(sorgente.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"),
						destinazione.getAbsolutePath() + "\\" + res.getString("PERCORSO_RELATIVO"), STATO_DA_COMPLETARE);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * mi effettua la query per selezionare i
	 * file che non sono cambiati rispetto
	 * all'ultimo backup effettuato
	 */
	public void scovaFileNonCambiati(File sorgente, int idSorgente, File destinazione, int idUltimoBackup, int idPercorsoAssolutoBackup) {
				
		String q = "select d.* from(SELECT * FROM files f1 WHERE f1.id in (SELECT id_files FROM VIRTUAL_LINK where ID_PERCORSO_ASSOLUTO = " + idUltimoBackup + ") or f1.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + ")d join  (select * from files where ID_PERCORSO_ASSOLUTO = " + idSorgente + " )f on f.percorso_relativo =d.percorso_relativo and f.md5=d.md5";
		//String q = "SELECT * FROM files WHERE id in(SELECT f.id FROM FILES f, FILES f1 where f.ID_PERCORSO_ASSOLUTO =" + idUltimoBackup + " and f1.ID_PERCORSO_ASSOLUTO = " + idSorgente + " and f.PERCORSO_RELATIVO = f1.PERCORSO_RELATIVO  and (f.md5 = f1.md5))";
		System.out.println(q);
		try {
			sta = conn.createStatement();
			res = sta.executeQuery(q);
			
			while(res.next()){
				System.out.println("file non modificati: " + res.getInt("id") + " " + res.getString("PERCORSO_RELATIVO"));
				BackupGui.addStringAreaLog(res.getString("PERCORSO_RELATIVO"));
				
				inserisciVirtualLink(res.getInt("id"), idPercorsoAssolutoBackup);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * questo metodo mi scrive nella tabella da_backuppare
	 * i file che dovrò copiare successivamente
	 * @param sorg
	 * @param dest
	 * @param stato
	 */
	public boolean scriviPathInDaBackuppare(String sorg, String dest, int stato){
		String q = "insert into DA_BACKUPPARE (PATH_SORGENTE ,PATH_DESTINAZIONE ,STATO ) "
				+ "values("
				+ "'" + sorg + "',"
				+ "'" + dest + "',"
				+ stato 
				+ ")";
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
		try {
			sta = conn.createStatement();
			sta.execute("delete from DA_BACKUPPARE where ID = " + id);
			
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
		
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT PATH_SORGENTE ,PATH_DESTINAZIONE, id FROM DA_BACKUPPARE WHERE STATO = " + STATO_DA_COMPLETARE);
		
			BackupGui.setMinMaxBar(0, numFileStimati);
			
			while(res.next()){
				try {
					Copy.copyFileUsingFileChannels(new File(res.getString(1)), new File(res.getString(2)));
					
					File copiato = new File(res.getString(2));
					String rel = pathRelativo.relativize(copiato.toURI()).getPath();
					inserisciFile(idPathAssoluto, rel, Copy.getMd5(copiato));
					
					cancellaFileDaBackuppare(res.getInt(3));
					System.out.println("FILE COPIATO");
					BackupGui.addStringAreaLog("FILE COPIATO CORRETTAMENTE: " + res.getString(1));
				} catch (Exception e) {
					System.err.println("FILE NON COPIATO");
					BackupGui.addStringAreaLog("ERRORE FILE NON COPIATO: " + res.getString(1));
					e.printStackTrace();
				}
				
				BackupGui.increaseBar();
				
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
		
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT count(id) FROM DA_BACKUPPARE WHERE STATO = " + STATO_DA_COMPLETARE);
			
			if(res.next()){
				i=res.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return i;
	}

	/**
	 * @return
	 */
	public long getDimensioneCartellaSorgente(String s){
		long dim = 0;
		try {
			sta = conn.createStatement();
			res = sta.executeQuery("SELECT PERCORSO_RELATIVO FROM FILES WHERE ID_PERCORSO_ASSOLUTO  = (SELECT ID FROM PERCORSI_ASSOLUTI WHERE PATH = '" + s + "')");
			
			while(res.next()){
				dim += new File(s + res.getString(1)).length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dim;
	}

}
