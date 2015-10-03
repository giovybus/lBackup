package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import boundary.MainGui;
import control.Config;
import entity.DBMS;

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
	public static final String version = "1.2.0-beta";
	
	/**
	 * se è true questa variabile
	 * mi fa partire automaticamente il
	 * backup appena avvio il programma
	 * 
	 * -start (parametro console)
	 */
	public static boolean PAR_START = false;  
	
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
	 */
	public static void main(String[] args) {
		
		if(args.length > 1) System.out.println("all parameters are deprecated.");
		
		setLookAndFeel();
		initImageIcon();
		
		boolean cheks = true;
		if(!new File(PATH_HOME_DIR).exists()){
			if(!new File(PATH_HOME_DIR).mkdir()){
				cheks = false;
			}
			
			//create the file config
			new Config();
			
			//create the database
			DBMS db = new DBMS();
			if(!db.createDatabase()){
				cheks = false;
			}
		}
		
		if(cheks){
			new MainGui();

		}else{
			JOptionPane.showMessageDialog(null, "An error occurred");
			
		}		
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
