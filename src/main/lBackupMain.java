package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import control.Config;
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
	
	public static final Color WET_ASPHALT = new Color(52, 73, 94);
	public static final Color MIDNIGHT_BLUE = new Color(44, 62, 80);
	public static final Color ESMERALD = new Color(46, 204, 113);
	public static final Color CLOUDS = new Color(236, 240, 241);
	public static final Color SILVER = new Color(189, 195, 199);
	public static final Font h1 = new Font("Roboto", Font.PLAIN, 25);
	public static final Font h3 = new Font("Roboto", Font.PLAIN, 15);
	
	/**
	 * @param args
	 * 	all parameters are deprecated 
	 * -nogui il programma parte senza l'interfaccia grafica
	 * -start il programma parte direttamente a fare il backup
	 * -server:h2 (come default usa h2)
	 * -server:mysql
	 */
	public static void main(String[] args) {
		
		if(args.length > 1) System.out.println("all parameters are deprecated.");
		
		/*if(!new File(PATH_HOME_DIR).exists()){
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
		
		if(!new File(PATH_HOME_DIR).exists()){
			new File(PATH_HOME_DIR).mkdir();
			new Config();
			//create the database
		}
		
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
