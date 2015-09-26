package boundary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.File;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	private JFileChooser fileChooser;
	
	/**
	 * button for the fileChooserSource
	 */
	private JButton buttSfogliaSource;
	
	/**
	 * label for the filechooser source
	 */
	private JLabel labSourcePath;
	
	/**
	 * button for the fileChooserDestination
	 */
	private JButton buttSfogliaDestination;
	
	/**
	 * label that contains the path of destination
	 */
	private JLabel labDestinationPath;
	
	/**
	 * button that start the process for 
	 * the backup
	 */
	private JButton buttStart;
	
	/**
	 * jlabel
	 */
	private JLabel labStart;
	
	/**
	 * desktop dir
	 */
	private File desktop;
	
	/**
	 * default construct, init all items, and the principal frame
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
		
		fileChooser = new JFileChooser(desktop);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
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
		
		JPanel rect = getPanelRectangle();
		initLabStart();
		rect.add(labStart);
		
		pan.add(buttStart);
		pan.add(rect);
		
		return pan;
	}

	/**
	 * 
	 */
	private void initLabStart() {
		labStart = new JLabel();
		labStart.setFont(lBackupMain.h3);
		labStart.setForeground(lBackupMain.CLOUDS);
		
	}

	/**
	 * @return
	 */
	private Component getPanelChooseDestination() {
		JPanel pan = new JPanel();
		pan.setBackground(lBackupMain.MIDNIGHT_BLUE);
		BoxLayout box = new BoxLayout(pan, BoxLayout.LINE_AXIS);
		pan.setLayout(box);
		
		JPanel rect = getPanelRectangle();
		initLabDestinationPath();
		rect.add(labDestinationPath);
		
		pan.add(buttSfogliaDestination);
		pan.add(rect);
		
		return pan;
	}

	/**
	 * init the lab destination path
	 */
	private void initLabDestinationPath() {
		labDestinationPath = new JLabel();
		labDestinationPath.setFont(lBackupMain.h3);
		labDestinationPath.setForeground(lBackupMain.CLOUDS);
		
	}

	/**
	 * @return
	 */
	private Component getPanelChooseSource() {
		JPanel pan = new JPanel();
		pan.setBackground(lBackupMain.MIDNIGHT_BLUE);
		BoxLayout box = new BoxLayout(pan, BoxLayout.LINE_AXIS);
		pan.setLayout(box);
		
		JPanel rect = getPanelRectangle();
		initLabSourcePath();
		rect.add(labSourcePath);
		
		pan.add(buttSfogliaSource);
		pan.add(rect);
		
		return pan;
	}

	/**
	 * 
	 */
	private void initLabSourcePath() {
		labSourcePath = new JLabel();
		labSourcePath.setFont(lBackupMain.h3);
		labSourcePath.setForeground(lBackupMain.CLOUDS);
		
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
		
	}
	
	/**
	 * return the panel with image rectangle
	 * @return
	 */
	private JPanel getPanelRectangle(){
		URL url = MainGui.class.getResource("img/c.png");
		
		final ImageIcon img;
		if(url != null)img = new ImageIcon(url);
		else img = new ImageIcon();
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 47, 53)){
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {

			    super.paintComponent(g);
			        g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		panel.setBackground(lBackupMain.MIDNIGHT_BLUE);
		panel.setPreferredSize(new Dimension(500, 100));
		return panel;
	}

	/**
	 * init the button for the filechooser
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
		
	}

	/**
	 * init the main frame and
	 * add all items
	 */
	private void initFrame() {
		frm = new JFrame("lBackup (" + lBackupMain.version + ")" );
		frm.setSize(750, 530);
		frm.setIconImage(lBackupMain.getLogo());
		frm.setLocationRelativeTo(null);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.setResizable(false);
		
		frm.add(panCenter, BorderLayout.CENTER);
		
		frm.setVisible(true);
	}

	/**
	 * @return the fileChooserSource
	 */
	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	/**
	 * @return the buttSfogliaSource
	 */
	public JButton getButtSfogliaSource() {
		return buttSfogliaSource;
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
	
	/**
	 * @return the labDestinationPath
	 */
	public JLabel getLabDestinationPath() {
		return labDestinationPath;
	}
	
	/**
	 * @return the labStart
	 */
	public JLabel getLabStart() {
		return labStart;
	}
		
	/**
	 * @return the labSourcePath
	 */
	public JLabel getLabSourcePath() {
		return labSourcePath;
	}
}
