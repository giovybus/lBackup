package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import main.lBackupMain;
import control.BackupCtr;
import control.Config;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 29/mar/2015
 */
public class BackupGui {
	private static JFrame frm;
	
	private JPanel panNorth;
	private JLabel labPathTo;
	private JFileChooser fileChooserTo;
	private JButton buttSfogliaTo;
	
	private JLabel labPathFrom;
	private JFileChooser fileChooserFrom;
	private JButton buttSfogliaFrom;
	
	private JPanel panCenter;
	private static JTextArea areaLog;
	private static String msgWelcome;
	private JScrollPane scrollAreaLog;
		
	private JPanel panSouth;
	private JButton buttStart;
	public static JProgressBar bar;
	
	private BackupCtr ctr;
	
	private Config config;
	
	private static final String vl[] = {"\\", "|", "/", "-"};
	
	/**
	 * constructor
	 */
	public BackupGui() {
		ctr = new BackupCtr();
		config = new Config();
		
		msgWelcome = "Welcome to lBackup " + lBackupMain.version 
		+ "\nlatest backup is: " + config.getDataBackup() + "\ncredits:\n\tGiovanni Buscarino\n\tMarco Alaimo\n";
		
		initPanCenter();
		initPanNorth();
		initPanSouth();
		initFrame();
	}
	
	/**
	 * inizializza il pannello centrale
	 * e gli inserisce la text area
	 */
	private void initPanCenter() {
		panCenter = new JPanel(new GridLayout(1, 1));
		panCenter.setBorder(BorderFactory.createTitledBorder("log"));
		
		initAreaLog();
		panCenter.add(scrollAreaLog);
	}

	/**
	 * inizializza la textarea
	 */
	private void initAreaLog() {
		areaLog = new JTextArea(msgWelcome);
			
		loadingHold();
		//areaLog.setPreferredSize(new Dimension(800, 100));
		areaLog.setEditable(false);
		/*areaLog.setLineWrap(true);
		areaLog.setWrapStyleWord(true);*/
		
		areaLog.setForeground(new Color(0, 255, 0));
		areaLog.setBackground(Color.BLACK);
		
		scrollAreaLog = new JScrollPane(areaLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
	}

	/**
	 * inizializza il pannello nord
	 * e gli inserisce i fields
	 */
	private void initPanNorth() {
		panNorth = new JPanel();
		
		BoxLayout box = new BoxLayout(panNorth, BoxLayout.PAGE_AXIS);
		panNorth.setLayout(box);
		panNorth.setPreferredSize(new Dimension(400, 80));
		
		panNorth.add(getPanFrom());
		panNorth.add(getPanTo());
		
	}

	/**
	 * istanzia un pannello con all'interno 
	 * il label e il bottone e tutte le azioni
	 * @return
	 */
	private Component getPanTo() {
		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createTitledBorder("percorso di arrivo"));
		pan.setLayout(null);
		
		initLabelTo();
		pan.add(labPathTo);
		labPathTo.setBounds(10, 10, 580, 20);
		
		initButtSfogliaTo();
		pan.add(buttSfogliaTo);
		buttSfogliaTo.setBounds(600, 10, 80, 25);
		
		return pan;
	}

	/**
	 * istanzia il bottone sfoglia to
	 * e gli da l'azione
	 */
	private void initButtSfogliaTo() {
		File desktop = new File(System.getProperty("user.home") + "\\Desktop");
		fileChooserTo = new JFileChooser(desktop);
		fileChooserTo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		buttSfogliaTo = new JButton("sfoglia");
		buttSfogliaTo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooserTo.setDialogTitle("Seleziona cartella per il database");
				
				if(fileChooserTo.showOpenDialog(fileChooserTo) == JFileChooser.APPROVE_OPTION){
					labPathTo.setText(fileChooserTo.getSelectedFile().getAbsolutePath() + "\\");			
					config.setDestinazionePath(labPathTo.getText());
					ctr.inserisciPathDestinazione(labPathTo.getText());
				}
				
			}
		});
		
	}

	/**
	 * inizializza il label che conterrà il
	 * percorso di arrivo senza la directory finale
	 * che sarà generata,
	 * se esite il file config inserisce il 
	 * contenuto relativo
	 */
	private void initLabelTo() {
		List<String>s = ctr.getPathDestinazione();

		labPathTo = new JLabel();
		if(s == null){
			labPathTo.setText("not set");
		}else{
			labPathTo.setText(s.get(0));
		}
		
	}

	/**
	 * mi istanzia un pannello con all'interno
	 * il label e il bottone, e tutte le azioni
	 * @return
	 */
	private Component getPanFrom() {
		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createTitledBorder("percorso di partenza"));
		pan.setLayout(null);
		
		initLabelFrom();
		pan.add(labPathFrom);
		labPathFrom.setBounds(10, 10, 580, 20);
		
		initButtSfogliaFrom();
		pan.add(buttSfogliaFrom);
		buttSfogliaFrom.setBounds(600, 10, 80, 25);
		
		return pan;
	}

	/**
	 * inizializza il bottone per aprire il
	 * filechooser
	 */
	private void initButtSfogliaFrom() {
		File desktop = new File(System.getProperty("user.home") + "\\Desktop");
		fileChooserFrom = new JFileChooser(desktop);
		fileChooserFrom.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		buttSfogliaFrom = new JButton("sfoglia");
		buttSfogliaFrom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooserFrom.setDialogTitle("Seleziona cartella per il database");
				
				if(fileChooserFrom.showOpenDialog(fileChooserFrom) == JFileChooser.APPROVE_OPTION){
					labPathFrom.setText(fileChooserFrom.getSelectedFile().getAbsolutePath() + "\\");	
					config.setSourcePath(labPathFrom.getText());
					ctr.inserisciPathSorgente(labPathFrom.getText());
					
				}
			}
		});
		
	}

	/**
	 * inizializza il label che conterrà il
	 * percorso di partenza,
	 * se esite il file config inserisce il 
	 * contenuto relativo
	 */
	private void initLabelFrom() {
		List<String>s = ctr.getPathSorgente();
		
		labPathFrom = new JLabel();
		if(s == null){
			labPathFrom.setText("not set");
		}else{
			labPathFrom.setText(s.get(0));
		}
		
	}

	/**
	 * inizializza il pannello a sud e gli mette
	 * il bottone per generare il backup
	 */
	private void initPanSouth() {
		panSouth = new JPanel(/*new FlowLayout(FlowLayout.RIGHT)*/);
		panSouth.setBorder(BorderFactory.createTitledBorder("step finale"));
		
		BoxLayout box = new BoxLayout(panSouth, BoxLayout.X_AXIS);
		panSouth.setLayout(box);
		
		initProgressBar();
		panSouth.add(bar);
		
		panSouth.add(Box.createHorizontalGlue());
		
		initButtStart();
		panSouth.add(buttStart);
		
	}

	/**
	 * 
	 */
	private void initProgressBar() {
		bar = new JProgressBar();
		bar.setStringPainted(true);
		
		UIManager.put("ProgressBar.foreground", Color.blue);
		UIManager.put("ProgressBar.selectionBackground", Color.gray);
		UIManager.put("ProgressBar.selectionForeground", Color.black);
	}

	/**
	 * inizializza il bottone start
	 * e mette l'action listener
	 */
	private void initButtStart() {
		buttStart = new JButton("start");
		buttStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				config.setDataAttualeBackup();
				
				Thread t = new Thread(){
					public void run(){
						ctr.startBackup(new File(labPathFrom.getText()), 
								new File(labPathTo.getText() + Long.toString(System.currentTimeMillis())));
					}
				};
				
				bar.setValue(0);
				buttStart.setEnabled(false);
				t.start();
			}
		});
	}
	
	/**
	 * inizializza il frame e aggiunge
	 * i pannelli
	 */
	private void initFrame() {
		frm = new JFrame("lBackup (" + lBackupMain.version + ")" );
		frm.setSize(700, 400);
		frm.setIconImage(lBackupMain.getLogo());
		frm.setLocationRelativeTo(null);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.setResizable(false);
		
		
		frm.add(panNorth, BorderLayout.NORTH);
		frm.add(panCenter, BorderLayout.CENTER);
		frm.add(panSouth, BorderLayout.SOUTH);
		
		frm.setVisible(true);
		
		frm.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
			@Override
			public void windowIconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				ctr.chiudiConnessione();
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowActivated(WindowEvent arg0) {}
		});
		
	}
	
	/**
	 * automaticamente mette il carattere di new line
	 * @param txt
	 */
	public static void addStringAreaLog(String txt){
		areaLog.append(txt + "\n");
		areaLog.setCaretPosition(areaLog.getDocument().getLength());
		
		frm.repaint();
		frm.revalidate();
	}
	
	public static void increaseBar(){
		bar.setValue(bar.getValue() + 1);
	}
	
	public static void setMinMaxBar(int min, int max){
		bar.setMinimum(min);
		bar.setMaximum(max);
	}

	/**
	 * create a vintage bar loading!
	 */
	public static void loadingHold() {
		/*try{
			JOptionPane.showMessageDialog(null, areaLog.getText(areaLog.getText().length()-1, 1));
			String cha = areaLog.getText(areaLog.getText().length()-2, 1);
			
			for(int i=0; i<vl.length; i++){
				if(cha == vl[i] && i+1 < vl.length){
					
				}else if(i+1 >= vl.length){
					
				}else{
					
				}
			}
			
		}catch(Exception exc){
			exc.printStackTrace();
		}*/
		
	}

}
