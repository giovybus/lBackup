package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

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
	
	/**
	 * 
	 */
	public MainGuiCtr(MainGui gui) {
		this.gui = gui;
		
		dbAbsolutePath = new DBMS_AbsolutePath();
		sources = dbAbsolutePath.getAllRootSources();
		destination = dbAbsolutePath.getRootBackupDir();
		
		actionListeners();
	}

	/**
	 * 
	 */
	private void actionListeners() {
		gui.getButtSfogliaSource().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui.getFileChooserSource().setDialogTitle("Seleziona cartella per il database");
				
				if(gui.getFileChooserSource().showOpenDialog(gui.getFileChooserSource()) == JFileChooser.APPROVE_OPTION){
					
					AbsolutePath temp = new AbsolutePath();
					temp.setPath(gui.getFileChooserSource().getSelectedFile().getAbsolutePath() + "\\");
					temp.setType(AbsolutePath.SOURCE);
					
					if(sources == null){
						sources = new ArrayList<AbsolutePath>();
						
						boolean c = dbAbsolutePath.insert(temp);
						System.out.println("insert source: " + c);
						
					}else{
						boolean c = dbAbsolutePath.updateRootSource(temp);
						System.out.println("update root destination: " + c);
					}
					
					sources.set(0, temp);
						
				}
				
			}
		});
		
		gui.getButtSfogliaDestination().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gui.getFileChooserSource().setDialogTitle("Seleziona cartella per il database");
				
				if(gui.getFileChooserSource().showOpenDialog(gui.getFileChooserSource()) == JFileChooser.APPROVE_OPTION){
					
					AbsolutePath temp = new AbsolutePath();
					temp.setPath(gui.getFileChooserSource().getSelectedFile().getAbsolutePath() + "\\");
					temp.setType(AbsolutePath.ROOT_DESTINATION);
					
					if(destination == null){
						boolean c = dbAbsolutePath.insert(temp);
						System.out.println("insert root destination: " + c);
						
					}else{
						boolean c = dbAbsolutePath.updateRootDestination(temp);
						System.out.println("update root destination: " + c);
						
					}
					
					destination = temp;
					
				}
				
			}
		});
		
		gui.getButtStart().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new DetailsGui();				
			}
		});
		
	}
	

}
