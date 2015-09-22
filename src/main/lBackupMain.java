package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import sun.applet.Main;
import entity.AbsolutePath;
import entity.Copy;
import entity.DBMS;
import entity.DBMS_AbsolutePath;
import entity.DBMS_FileSource;
import entity.FilesSource;
import entity.Query;
import boundary.BackupGui;
import boundary.MainGui;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 29/mar/2015 21.07
 */
public class lBackupMain {
	
	/**
	 *indica la cartella ...\AppData\..\LegendaryBackup\ 
	 */
	public final static String PATH_HOME_DIR = System.getenv("AppData") + "\\lBackup\\";
	
	/**
	 * indica il percorso assoluto del file config.ini
	 */
	public final static String PATH_CONFIG = PATH_HOME_DIR + "config.ini";
	
	/**
	 * logo img
	 */
	private static ImageIcon icon;
	private static URL urlIcon;
	
	/**
	 * indica la versione del programma
	 */
	public static final String version = "1.2.0-alpha";
	
	/**
	 * se è true questa variabile
	 * mi fa partire automaticamente il
	 * backup appena avvio il programma
	 * 
	 * -start (parametro console)
	 */
	public static boolean PAR_START = false;  
	
	public static final int SERVER_H2 = 0;
	public static final int SERVER_MYSQL = 1;
	
	public static int PAR_SERVER = SERVER_H2;
	
	private static File sorgente;
	private static File destinazione;
	
	private static DBMS_FileSource dbFileSource;
	
	public static final Color WET_ASPHALT = new Color(52, 73, 94);
	public static final Color MIDNIGHT_BLUE = new Color(44, 62, 80);
	public static final Color ESMERALD = new Color(46, 204, 113);
	public static final Color CLOUDS = new Color(236, 240, 241);
	public static final Color SILVER = new Color(189, 195, 199);
	public static final Font h1 = new Font("Roboto", Font.PLAIN, 25);
	
	/**
	 * @param args
	 * -nogui il programma parte senza l'interfaccia grafica
	 * -start il programma parte direttamente a fare il backup
	 * -server:h2 (come default usa h2)
	 * -server:mysql
	 */
	public static void main(String[] args) {
		/*System.out.println("parametri: -start parte il programma direttamente a fare il backup senza chiedere conferme\n"
				+ "-server:h2 imposta il server h2 e usa le query ad-hoc\n"
				+ "-server:mysql imposta il server mysql e usa le query ad-hoc");*/
		
		/*if(args != null){
			for(String s : args){
				switch (s) {
				case "-start":
					PAR_START = true;
					System.out.println("paramatro -start SETTATO CORRETTAMENTE");
					break;
					
				case "-server:h2":
					PAR_SERVER = SERVER_H2;
					System.out.println("DATABASE settato: H2");
					break;
				
				case "-server:mysql":
					PAR_SERVER = SERVER_MYSQL;
					System.out.println("DATABASE settato: MYSQL");
					break;
					
				default:
					break;
				}
			}
		}
		
		setLookAndFeel();
		initImageIcon();
		boolean contr = true;
		
		if(!new File(PATH_HOME_DIR).exists()){
			new File(PATH_HOME_DIR).mkdir();
			
			Query q = new Query();
			contr = q.creaDatabase();
		}
		
		if(contr){
			new BackupGui();
		}else{
			JOptionPane.showMessageDialog(null, "non posso aprire il programma perchè non riesco a comunicare con il database");
		}*/
		
		setLookAndFeel();
		initImageIcon();
		new MainGui();
		
		/*inserisciRootSorgente();
		inserisciRootBackup();*/
		
		/*dbFileSource = new DBMS_FileSource();
		
		List<AbsolutePath>allRootSources = getAllRootSources();
		System.out.println("cartelle root sorgente: " + allRootSources.size());
		for(AbsolutePath a : allRootSources){
			System.out.println("\tid: " + a.getId() + ", path: " + a.getPath());
		}
		sorgente = new File(allRootSources.get(0).getPath());
		
		AbsolutePath rootBackup = getRootBackup();
		System.out.println("cartella root backup: " + rootBackup.getPath());
		
		destinazione = new File(rootBackup.getPath() + "\\" + Long.toString(System.currentTimeMillis()));
		System.out.println("cartella child backup: " + destinazione.getAbsolutePath());
		
		List<String>path = new ArrayList<>();
		path.add(sorgente.getAbsolutePath());
		navigaDirectory(path, "", sorgente.list().length, allRootSources.get(0));
		
		//controllo file con l'ultimo backup (tabella files_source last = 1)
		
		//prendo le decisioni in merito al risultato del controllo
		
		
		/*if(dbFileSource.removeAllLastTrue()){
			System.out.println(dbFileSource.setAllLastTrue());
		}else{
			System.out.println("non sono riuscito a cancellare gli elementi files_source where last=1");
		}*/
		
		
	}
	
	/**
	 * src - primoPak
	 *  |		|- prova.java
	 * 	|		|- altroPak
	 * 	|				|-settings.java
	 * 	|
	 *  |		|- secondoPak
	 *  |				|-frame.java
	 *  |				|-gui.java
	 *  |
	 *  |
	 *  |
	 * 	|- main.java
	 * 
	 * 
	 * @param path
	 * @param estensione
	 * @param numDir
	 */
	private static void navigaDirectory(List <String> path, String estensione, int numDir, AbsolutePath absolutePath){
		//for(int i=0; i<path.size(); i++)System.out.println(path.get(i));
		
		List <String> temp = new ArrayList<String>();
		int contaDirectory = 0;
		String est = estensione;
		
		if(numDir == 0) contaDirectory = -1;
		
		for(int i=0; i<path.size(); i++){
			
			File f = new File(path.get(i));
				
				File []list = f.listFiles();
				
				for(int j=0; j<list.length; j++){
					if(list[j].isDirectory()){
						temp.add(list[j].getAbsolutePath());
						contaDirectory++;
						String rel = sorgente.toURI().relativize(list[j].toURI()).getPath();
						new File(destinazione.getAbsoluteFile() + "\\" + rel).mkdir();
						
					}else{
//						System.out.println("Dimensione del file: " + list[j].length());		
						String rel = sorgente.toURI().relativize(list[j].toURI()).getPath();
						System.out.println("\t" + rel);
						//System.out.println(rel + " ultima mod:" + list[j].lastModified() + " dim:" + list[j].length() + "B"); 
						
						//TODO inserire file nel database
						//query.inserisciFile(idPercorsoAssoluto, rel, Copy.getMd5(list[j]));
						FilesSource fs = new FilesSource();
						fs.setAbsolutePath(absolutePath);
						fs.setLast(false);
						fs.setMd5(Copy.getMd5(list[j]));
						fs.setRelativePath(rel);
						
						System.out.println(dbFileSource.insert(fs));
					}
				}
		}
		
		if(numDir != -1) navigaDirectory(temp, est, contaDirectory, absolutePath);
	}
	
	/**
	 * @return
	 */
	private static AbsolutePath getRootBackup() {
		DBMS_AbsolutePath dbAbs = new DBMS_AbsolutePath();
		return dbAbs.getRootBackupDir();
	}

	/**
	 * mi da tutte le cartelle memorizzate come root
	 * di sorgente
	 * @return
	 */
	private static List<AbsolutePath> getAllRootSources() {
		DBMS_AbsolutePath dbAbs = new DBMS_AbsolutePath();
		return dbAbs.getAllRootSources();
	}

	/**
	 * inserisce una cartella di backup nel database
	 */
	private static void inserisciRootBackup(){
		AbsolutePath abs = new AbsolutePath();
		abs.setPath("C:\\Users\\giovybus\\Desktop\\rep");
		abs.setType(1);
		DBMS_AbsolutePath dbAbs = new DBMS_AbsolutePath();
		System.out.println(dbAbs.insert(abs));
	}
	
	/**
	 * inserisce una cartella sorgente nel database
	 */
	private static void inserisciRootSorgente() {
		AbsolutePath abs = new AbsolutePath();
		abs.setPath("C:\\Users\\giovybus\\Desktop\\bc");
		abs.setType(0);
		DBMS_AbsolutePath dbAbs = new DBMS_AbsolutePath();
		System.out.println(dbAbs.insert(abs));
	}

	/**
	 * carica l'immagine del logo come risorsa,
	 * e inizializza la variabile icon
	 */
	private static void initImageIcon() {
		urlIcon = lBackupMain.class.getResource("images/lb.png");
		
		if(urlIcon != null) icon = new ImageIcon(urlIcon);
		else icon = new ImageIcon();
		
	}
	/**
	 * metodo pubblico che ritorna l'immagine
	 * da usare nei vari frames
	 * @return
	 */
	public static Image getLogo(){
		return icon.getImage();
	}

	/**
	 * setta il look and feel dei frames successivi
	 */
	private static void setLookAndFeel() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e){
			
		}
	}


}
