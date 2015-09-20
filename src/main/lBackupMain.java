package main;

import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import entity.Query;
import boundary.BackupGui;

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
