package main;

import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

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
	public static final String version = "1.0.0-beta";
	
	/**
	 * @param args
	 * -nogui il programma parte senza l'interfaccia grafica
	 * -start il programma parte direttamente a a fare il backup
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		initImageIcon();
		if(!new File(PATH_HOME_DIR).exists()) new File(PATH_HOME_DIR).mkdir();
		new BackupGui();
		
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
