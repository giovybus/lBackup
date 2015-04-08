package control;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import boundary.BackupGui;
import entity.Copy;
import entity.Query;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 07/apr/2015 22:09:25
 */
public class BackupCtr {

	/**
	 * file che contiene la cartella 
	 * relativa all'ultimo backup effettuato
	 */
	private File ultimoBackup = null;
	
	/**
	 * file che contiene la cartella
	 * relativa al path sorgente da 
	 * cui effettuare il backup
	 */
	private File sorgente = null;
	
	/**
	 * questo sarà la
	 * cartella che conterrà il backup
	 * e il nome sarà il tempo in ms
	 */
	private File destinazione;
	
	private NumberFormat nf;
	
	private Query query;
	
	/**
	 * 
	 */
	public BackupCtr() {
		query = new Query();
	}

	/**
	 * @param percorso assoluto: C:\\users\\..\\dest\\
	 * inserisce il percorso nel database
	 */
	public void inserisciPathDestinazione(String text) {
		query.inserisciPercorsoAssoluto(text, Query.DIR_DESTINAZIONE);
		
	}
	
	/**
	 * @param percorso assoluto: C:\\users\\..\\source\\
	 * inserisce il percorso nel database
	 */
	public void inserisciPathSorgente(String text) {
		query.inserisciPercorsoAssoluto(text, Query.DIR_SORGENTE);
		
	}

	/**
	 * @return il percorso assoluto della directory di destinazione C:\\users\\..\\dest\\
	 */
	public List<String> getPathDestinazione() {
		List<String>s = query.getPercorsoAssoluto(Query.DIR_DESTINAZIONE);
		return s;
	}

	/**
	 * @return il percorso assoluto della directory di destinazione C:\\users\\..\\source\\
	 */
	public List<String> getPathSorgente() {
		return query.getPercorsoAssoluto(Query.DIR_SORGENTE);
	}

	/**
	 * setta i files sorgenti e destinazione
	 */
	public void startBackup(File sorgente, File destinazione) {
		this.sorgente = sorgente;
		this.destinazione = destinazione; 
		
		if(!destinazione.exists()){
			BackupGui.addStringAreaLog("creo la cartella destinazione: " + destinazione.mkdir());
		}else{
			BackupGui.addStringAreaLog("la cartella di destinazione esiste già");
		}
		
		engine();
		
	}
	
	/**
	 * impossibile commentare qst metodo, 
	 * già il nome dice tutto
	 */
	protected void engine() {
		System.out.println("sorgente: " + sorgente.getAbsoluteFile());
		BackupGui.addStringAreaLog("cartella sorgente: " + sorgente.getAbsoluteFile());
		
		query.cancellaTuttiFilesSorgenti(query.getIdPercorsoAssoluto(sorgente.getAbsolutePath() + "\\"));
		/*for(String s : listSorgente){
			System.out.println(s);
		}*/
		
		List<String>path = new ArrayList<>();
		path.add(sorgente.getAbsolutePath());
		int idSorgente = query.getIdPercorsoAssoluto(sorgente.getAbsolutePath()+"\\");
		
		BackupGui.addStringAreaLog("espoloro tutte le cartelle ...");
		navigaDirectory(path, "", sorgente.list().length, idSorgente);
		
		
		String []ultimoBk = query.getPathDirUltimoBackup();
		if(ultimoBk != null){
			BackupGui.addStringAreaLog("ultimo backup effettuato: " + ultimoBk[0]);
			ultimoBackup = new File(ultimoBk[0]);
		}
		
		if(ultimoBackup != null){
			System.out.println("ultmio backup: " + ultimoBackup.getAbsolutePath());
			
			//int id = query.inserisciPercorsoAssoluto(destinazione.getAbsolutePath()+"\\", Query.DIR_SUBDESTINAZIONE);
			System.out.println("file nuovi trovati:");
			BackupGui.addStringAreaLog("elenco file nuovi trovati:");
			query.scovaFileNuovi(sorgente, idSorgente, destinazione, Integer.parseInt(ultimoBk[1]));
			System.out.println("----");
			BackupGui.addStringAreaLog("---");
			
			System.out.println("file diversi trovati:");
			BackupGui.addStringAreaLog("elenco file esistenti ma che sono cambiati rispetto all'ultimo backup:");
			query.scovaFileDiversi(sorgente, idSorgente, destinazione, Integer.parseInt(ultimoBk[1]));
			System.out.println("-----");
			BackupGui.addStringAreaLog("---");
				
		}else{
			System.out.println("questo è il primo backup, quindi copio ttt i file presenti nella cartella source");
			BackupGui.addStringAreaLog("Nessun backup trovato, quindi copio tutti i file cosi come sono");
			List<String>files = query.getFilesSorgente(query.getIdPercorsoAssoluto(sorgente.getAbsolutePath()+"\\"));
			if(files!= null){
				BackupGui.addStringAreaLog("file che saranno copiati:");
				for(String f : files){
					System.out.println("sorgente: " + sorgente.getAbsolutePath() + "\\" + f + ", destinazione:" + destinazione.getAbsolutePath() + "\\" + f);
					BackupGui.addStringAreaLog(sorgente.getAbsolutePath() + "\\" + f + ", destinazione:" + destinazione.getAbsolutePath() + "\\" + f);
					query.scriviPathInDaBackuppare(sorgente.getAbsolutePath() + "\\" + f, 
							destinazione.getAbsolutePath() + "\\" + f, Query.STATO_DA_COMPLETARE);
					
				}
			}
		}
		
		int choose = JOptionPane.showConfirmDialog(null, "vuoi effettuare il backup di questi dati?", "", JOptionPane.INFORMATION_MESSAGE);
		if(choose == JOptionPane.YES_OPTION){
			int idPathAssoluto = query.inserisciPercorsoAssoluto(destinazione.getAbsolutePath() + "\\", Query.DIR_SUBDESTINAZIONE);
			query.effettuaBackup(idPathAssoluto, destinazione.toURI());
			
			if(ultimoBackup != null){
				BackupGui.addStringAreaLog("file che non sono cambiati rispetto all'ultimo backup:");
				System.out.println("file che non sono cambiati:");
				query.scovaFileNonCambiati(sorgente, idSorgente, destinazione, Integer.parseInt(ultimoBk[1]), idPathAssoluto);
				System.out.println("-----");
			}
			
			BackupGui.addStringAreaLog("Backup completato");
			stampaDimensioni();
		
			
			
			/*areaLog.append("Dimensione Totale cartella sorgente: 1250124 B\n");
			areaLog.append("Dimensione totale cartella backup: 120 B\n");
			areaLog.append("Dimensione totale risparmiata: 12458 B\n");*/
			
			
		}else{
			try {
				FileUtils.deleteDirectory(destinazione);
				System.out.println("directory cancellata correttamente");
				BackupGui.addStringAreaLog("cancello la directory che avevo creato per il backup: " + destinazione.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 
	 */
	private void stampaDimensioni() {
		long dimSorgente = query.getDimensioneCartellaSorgente(sorgente.getAbsoluteFile()+"\\");
		double dimSorgenteMB = dimSorgente/(1024*1024);
		double dimSorgenteGB = dimSorgenteMB/(1024);
		
		String dimSorgenteStr = "";
		if(dimSorgenteMB > 0 && dimSorgenteGB > 0){
			dimSorgenteStr = "Dimensione totale cartella sorgente: " + dimSorgenteGB + " GB, " + dimSorgenteMB + " MB, " + dimSorgente + " B";
		}else if(dimSorgenteMB > 0){
			dimSorgenteStr = "Dimensione totale cartella sorgente: " + dimSorgenteMB + " MB, " + dimSorgente + " B";
		}else{
			dimSorgenteStr = "Dimensione totale cartella sorgente: " + dimSorgente + " B";
		}
		
		BackupGui.addStringAreaLog(dimSorgenteStr);
		
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
	private void navigaDirectory(List <String> path, String estensione, int numDir, int idPercorsoAssoluto){
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
						
					}else if(controllaEstensione(list[j], estensione)){
//						System.out.println("Dimensione del file: " + list[j].length());		
						String rel = sorgente.toURI().relativize(list[j].toURI()).getPath();
						//System.out.println(rel + " ultima mod:" + list[j].lastModified() + " dim:" + list[j].length() + "B"); 
						query.inserisciFile(idPercorsoAssoluto, rel, Copy.getMd5(list[j]));
					}
				}
		}
		
		if(numDir != -1) navigaDirectory(temp, est, contaDirectory, idPercorsoAssoluto);
	}
	
	private boolean controllaEstensione(File file, String estensione){
		return true;
		/*String []estensioni = estensione.split(",");
		boolean controllo = false;		
		String ext = file.getAbsolutePath();
				
		String prova = "";		
		StringTokenizer str = new StringTokenizer(ext, ".");
		while(str.hasMoreTokens()){
			prova = str.nextToken();
		}
		
		
		for(int i=0; i<(estensioni.length-1); i++){			
			if(prova.toLowerCase().equals(estensioni[i].toLowerCase().trim())){
				//System.out.println(prova + " " + estensioni[i].toLowerCase().trim());
				controllo = true;
				break;
			}
		}
		//System.out.println(controllo + " " + estensione + " " + file.getAbsolutePath());
		return controllo;*/
	}

}
