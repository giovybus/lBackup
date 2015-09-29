package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	
	private List<AbsolutePath> sources;
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
		this.sources = dbAbsolutePath.getAllRootSources();
		this.destination = dbAbsolutePath.getRootBackupDir();
		
		setTextInLabels();
		actionListeners();
	}

	/**
	 * set the labels int the gui
	 */
	private void setTextInLabels() {
		if(sources != null && sources.get(0) != null){
			gui.setTextSource(sources.get(0).getPath());
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
				gui.getFileChooser().setDialogTitle("Seleziona cartella per il database");
				
				if(gui.getFileChooser().showOpenDialog(gui.getFileChooser()) == JFileChooser.APPROVE_OPTION){
					
					AbsolutePath temp = new AbsolutePath();
					temp.setPath(gui.getFileChooser().getSelectedFile().getAbsolutePath() + "\\");
					temp.setType(AbsolutePath.SOURCE);
					
					if(sources == null){
						sources = new ArrayList<AbsolutePath>();
						
						boolean c = dbAbsolutePath.insert(temp);
						System.out.println("insert source: " + c);
						
					}else{
						temp.setId(sources.get(0).getId());
						boolean c = dbAbsolutePath.updateRootSource(temp);
						System.out.println("update root destination: " + c);
					}
					
					gui.setTextSource(temp.getPath());
					config.setSourcePath(temp.getPath());
					sources.set(0, temp);	
				}
				
			}
		});
		
		gui.getButtSfogliaDestination().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui.getFileChooser().setDialogTitle("Seleziona cartella per il database");
				
				if(gui.getFileChooser().showOpenDialog(gui.getFileChooser()) == JFileChooser.APPROVE_OPTION){
					
					AbsolutePath temp = new AbsolutePath();
					temp.setPath(gui.getFileChooser().getSelectedFile().getAbsolutePath() + "\\");
					temp.setType(AbsolutePath.ROOT_DESTINATION);
					
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
				
				if(sources == null || sources.get(0) == null){
					err += "-Source path not setted\n";
					
				}else if(!new File(sources.get(0).getPath()).exists()){
					err += "-Source path not exists\n";
					
				}else if(!new File(sources.get(0).getPath()).isDirectory()){
					err += "-Source paht is not a directory\n";
					
				}
				
				if(destination == null){
					err += "-Destination path not setted\n";
					
				}else if(!new File(destination.getPath()).exists()){
					err += "-Destination path not exists\n";
					
				}else if(!new File(destination.getPath()).isDirectory()){
					err += "-Destination path is not a directory\n";
					
				}
				
				if(err.equals("")){
					config.setDateAnalysis();
					new DetailsGui(sources, destination);
					
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
