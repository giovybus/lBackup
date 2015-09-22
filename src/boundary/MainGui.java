package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import com.sun.org.apache.bcel.internal.generic.LMUL;

import control.MainGuiCtr;
import main.lBackupMain;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 21:30:50
 */
public class MainGui {
	
	/**
	 * main panel that contains all panels
	 */
	private JFrame frm;
	
	/**
	 * center panel that contains all buttons
	 * and label 
	 */
	private JPanel panCenter;
	
	/**
	 * filechooser that allow to choose
	 * the source dir
	 */
	private JFileChooser fileChooserSource;
	
	/**
	 * button for the fileChooserSource
	 */
	private JButton buttSfogliaSource;
	
	/**
	 * filechooser that allow to choose
	 * the destination dir
	 */
	private JFileChooser fileChooserDestination;
	
	/**
	 * button for the fileChooserDestination
	 */
	private JButton buttSfogliaDestination;
	
	/**
	 * button that start the process for 
	 * the backup
	 */
	private JButton buttStart;
	
	/**
	 * desktop dir
	 */
	private File desktop;
	
	/**
	 * default construct
	 */
	public MainGui() {
		desktop = new File(System.getProperty("user.home") + "\\Desktop");
		initCenterPanel();
		initFrame();
		
		new MainGuiCtr(this);
	}

	/**
	 * init the center panel
	 * and add all items
	 */
	private void initCenterPanel() {
		panCenter = new JPanel();
		panCenter.setBackground(lBackupMain.MIDNIGHT_BLUE);
		
		BoxLayout box = new BoxLayout(panCenter, BoxLayout.PAGE_AXIS);
		panCenter.setLayout(box);
		
		initButtFileSource();
		panCenter.add(getPanelChooseSource());
		
		initButtFileDestination();
		panCenter.add(getPanelChooseDestination());
		
		initButtStart();
		panCenter.add(getPanelStart());
	}

	/**
	 * @return
	 */
	private Component getPanelStart() {
		JPanel pan = new JPanel();
		pan.setBackground(lBackupMain.MIDNIGHT_BLUE);
		BoxLayout box = new BoxLayout(pan, BoxLayout.LINE_AXIS);
		pan.setLayout(box);
		
		pan.add(buttStart);
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setSize(new Dimension(140, 80));
		panel.setBackground(lBackupMain.MIDNIGHT_BLUE);
		JLabel l = new JLabel("start");
		l.setForeground(lBackupMain.CLOUDS);
		panel.add(l);
		
		pan.add(panel);
		
		return pan;
	}

	/**
	 * @return
	 */
	private Component getPanelChooseDestination() {
		JPanel pan = new JPanel();
		pan.setBackground(lBackupMain.MIDNIGHT_BLUE);
		BoxLayout box = new BoxLayout(pan, BoxLayout.LINE_AXIS);
		pan.setLayout(box);
		
		pan.add(buttSfogliaDestination);
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setSize(new Dimension(140, 80));
		panel.setBackground(lBackupMain.MIDNIGHT_BLUE);
		JLabel l = new JLabel("Destination Dirctory");
		l.setForeground(lBackupMain.CLOUDS);
		panel.add(l);
		
		pan.add(panel);
		
		return pan;
	}

	/**
	 * @return
	 */
	private Component getPanelChooseSource() {
		JPanel pan = new JPanel();
		pan.setBackground(lBackupMain.MIDNIGHT_BLUE);
		BoxLayout box = new BoxLayout(pan, BoxLayout.LINE_AXIS);
		pan.setLayout(box);
		
		pan.add(buttSfogliaSource);
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setSize(new Dimension(140, 80));
		panel.setBackground(lBackupMain.MIDNIGHT_BLUE);
		JLabel l = new JLabel("Source Dirctory");
		l.setForeground(lBackupMain.CLOUDS);
		panel.add(l);
		
		pan.add(panel);
		
		return pan;
	}

	/**
	 * button start
	 */
	private void initButtStart() {
		URL url = MainGui.class.getResource("img/destination.png");
		
		ImageIcon img;
		if(url != null)img = new ImageIcon(url);
		else img = new ImageIcon();
		
		buttStart = new JButton();
		buttStart.setIcon(img);
		buttStart.setToolTipText("start");
		buttStart.setContentAreaFilled(false);
		buttStart.setFocusPainted(false);
	}

	/**
	 * init destination directoy
	 */
	private void initButtFileDestination() {
		URL url = MainGui.class.getResource("img/source.png");
		
		ImageIcon img;
		if(url != null)img = new ImageIcon(url);
		else img = new ImageIcon();
		
		buttSfogliaDestination = new JButton();
		buttSfogliaDestination.setIcon(img);
		buttSfogliaDestination.setToolTipText("Open destination directory");
		buttSfogliaDestination.setContentAreaFilled(false);
		buttSfogliaDestination.setFocusPainted(false);
		
		fileChooserDestination = new JFileChooser(desktop);
		fileChooserDestination.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
	}

	/**
	 * init the button
	 */
	private void initButtFileSource() {
		URL url = MainGui.class.getResource("img/source.png");
		
		ImageIcon img;
		if(url != null)img = new ImageIcon(url);
		else img = new ImageIcon();
		
		buttSfogliaSource = new JButton();
		buttSfogliaSource.setIcon(img);
		buttSfogliaSource.setToolTipText("Open source directory");
		buttSfogliaSource.setContentAreaFilled(false);
		buttSfogliaSource.setFocusPainted(false);
		
		fileChooserSource = new JFileChooser(desktop);
		fileChooserSource.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	/**
	 * init the main frame and
	 * add all items
	 */
	private void initFrame() {
		frm = new JFrame("lBackup (" + lBackupMain.version + ")" );
		frm.setSize(800, 530);
		frm.setIconImage(lBackupMain.getLogo());
		frm.setLocationRelativeTo(null);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frm.setBackground(lBackupMain.WET_ASPHALT);
		//frm.setResizable(false);
		
		frm.add(panCenter, BorderLayout.CENTER);
		
		frm.setVisible(true);
	}

	/**
	 * @return the fileChooserSource
	 */
	public JFileChooser getFileChooserSource() {
		return fileChooserSource;
	}

	/**
	 * @return the buttSfogliaSource
	 */
	public JButton getButtSfogliaSource() {
		return buttSfogliaSource;
	}

	/**
	 * @return the fileChooserDestination
	 */
	public JFileChooser getFileChooserDestination() {
		return fileChooserDestination;
	}

	/**
	 * @return the buttSfogliaDestination
	 */
	public JButton getButtSfogliaDestination() {
		return buttSfogliaDestination;
	}

	/**
	 * @return the buttStart
	 */
	public JButton getButtStart() {
		return buttStart;
	}
	
	
}
