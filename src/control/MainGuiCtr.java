package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import entity.AbsolutePath;
import entity.DBMS_AbsolutePath;
import boundary.DetailsGui;
import boundary.MainGui;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 22/set/2015 20:56:52
 */
public class MainGuiCtr {
	
	/**
	 * gui to manage
	 */
	private MainGui gui;
	
	private DBMS_AbsolutePath dbAbsolutePath;
	
	private AbsolutePath source;
	private AbsolutePath destination;
	private Config config;
	
	/**
	 * 
	 */
	public MainGuiCtr(MainGui gui) {
		this.gui = gui;
		this.config = new Config();
		
		//TODO da sistemare, fare in modo  che legga 
		//qst info dal file ini anzichè dal databse
		this.dbAbsolutePath = new DBMS_AbsolutePath();
		this.source = dbAbsolutePath.getRootSource();
		
		if(config.isFtp()){
			this.destination = new AbsolutePath();
			this.destination.setPath("lbackup\\");
			
		}else{
			this.destination = dbAbsolutePath.getRootBackupDir();
			
		}
		
		
		setTextInLabels();
		actionListeners();
	}

	/**
	 * set the labels int the gui
	 */
	private void setTextInLabels() {
		if(source != null){
			gui.setTextSource(source);
		}
		
		if(destination != null){
			gui.setTextDestination(destination.getPath());
		}
		
		gui.setTextLastAnalysisBackup(config.getDateAnalysis(), 
				config.getDataBackup());

	}

	/**
	 * 
	 */
	private void actionListeners() {
		gui.getButtSfogliaSource().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui.getFileChooser().setDialogTitle("Seleziona cartella sorgente per il backup");
				
				if(gui.getFileChooser().showOpenDialog(gui.getFileChooser()) == JFileChooser.APPROVE_OPTION){
					
					AbsolutePath temp = new AbsolutePath();
					temp.setPath(gui.getFileChooser().getSelectedFile().getAbsolutePath() + "\\");
					temp.setType(AbsolutePath.SOURCE);
					
					if(source == null){
						//il database non è settato, quindi si presuppone
						//che è il primo utilizzo del programmma
						dbAbsolutePath.insert(temp);
						
					}else{
						//già il database è settato quindi 
						
						
					}
					
					/*if(sources == null){
						sources = new ArrayList<AbsolutePath>();
						
						boolean c = dbAbsolutePath.insert(temp);
						System.out.println("insert source: " + c);
						sources.add(temp);
						
					}else{
						temp.setId(sources.get(0).getId());
						boolean c = dbAbsolutePath.updateRootSource(temp);
						System.out.println("update root destination: " + c);
						sources.set(0, temp);
					}*/
					
					gui.setTextSource(temp);
					config.setSourcePath(temp.getPath());
						
				}
				
			}
		});
		
		gui.getButtSfogliaDestination().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui.getFileChooser().setDialogTitle("Seleziona cartella destinazione Backup");
				
				if(gui.getFileChooser().showOpenDialog(gui.getFileChooser()) == JFileChooser.APPROVE_OPTION){
					
					AbsolutePath temp = new AbsolutePath();
					temp.setPath(gui.getFileChooser().getSelectedFile().getAbsolutePath() + "\\");
					temp.setType(AbsolutePath.ROOT_DESTINATION);
					
					//controllare se la cartella è vuota, oppure se contiene
					//dati, e controllare se contiene il database backuppato
					//per capire se è una cartella già organizzata per il lavoro
					
					if(destination == null){
						boolean c = dbAbsolutePath.insert(temp);
						System.out.println("insert root destination: " + c);
						
					}else{
						temp.setId(destination.getId());
						
						boolean c = dbAbsolutePath.updateRootDestination(temp);
						System.out.println("update root destination: " + c);
						
					}
					
					gui.setTextDestination(temp.getPath());
					config.setDestinazionePath(temp.getPath());
					destination = temp;					
					
				}
				
			}
		});
		
		gui.getButtStart().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String err = new String();
				
				if(source == null){
					err += "-Source path not setted\n";
					
				}else if(!new File(source.getPath()).exists()){
					err += "-Source path not exists\n";
					
				}else if(!new File(source.getPath()).isDirectory()){
					err += "-Source paht is not a directory\n";
					
				}
				
				if(destination == null){
					err += "-Destination path not setted\n";
					
				}else if(!new File(destination.getPath()).exists()){
					//err += "-Destination path not exists\n";
					//TODO sistemare qst cosa nel caso in cui sei in FTP
					
				}else if(!new File(destination.getPath()).isDirectory() ){
					err += "-Destination path is not a directory\n";
					
				}
				
				if(err.equals("")){
					config.setDateAnalysis();
					new DetailsGui(source, destination);
					
					gui.setTextLastAnalysisBackup(config.getDateAnalysis(), 
							config.getDataBackup());
				}else{
					JOptionPane.showMessageDialog(null, "There are some errors:\n"
							+ err, "warning", JOptionPane.WARNING_MESSAGE);
					
				}
								
			}
		});
		
	}
	

}
