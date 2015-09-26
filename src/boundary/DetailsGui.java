package boundary;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import control.DetailsCtr;
import entity.AbsolutePath;
import main.lBackupMain;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 22/set/2015 21:22:54
 */
public class DetailsGui {
	private JDialog dlg;
	
	private JPanel panCenter;
	
	private JLabel labFirstStep;
	private final String phraseFirstStep = "New files: ";
	
	private JLabel labSecondStep;
	private final String phraseSecondStep = "Modify files: ";
	
	private JLabel labThirdStep;
	private final String phraseThirdStep = "Copy Files: ";
	
	private JButton buttAllow;
	
	private JPanel panSouth;
	private JProgressBar progressBar;
	
	private List<AbsolutePath>sources;
	private AbsolutePath destination;
	
	/**
	 * default constructor
	 */
	public DetailsGui(List<AbsolutePath>sources, AbsolutePath destination) {
		this.sources = sources;
		this.destination = destination;
		
		initPanSouth();
		initPanelCenter();
		initDlg();
		
		new DetailsCtr(this);
		
		dlg.setModal(true);
		dlg.setVisible(true);
		
	}

	/**
	 * 
	 */
	private void initPanSouth() {
		panSouth = new JPanel();
		panSouth.setBackground(lBackupMain.WET_ASPHALT);
	
	}

	/**
	 * 
	 */
	private void initPanelCenter() {
		panCenter = new JPanel();
		panCenter.setBackground(lBackupMain.WET_ASPHALT);
		
		BoxLayout box = new BoxLayout(panCenter, BoxLayout.PAGE_AXIS);
		panCenter.setLayout(box);
		
		labFirstStep = new JLabel(phraseFirstStep);
		labFirstStep.setForeground(lBackupMain.CLOUDS);
		labFirstStep.setFont(lBackupMain.h1);
		panCenter.add(labFirstStep);
		
		labSecondStep = new JLabel(phraseSecondStep);
		labSecondStep.setForeground(lBackupMain.CLOUDS);
		labSecondStep.setFont(lBackupMain.h1);
		panCenter.add(labSecondStep);
		
		labThirdStep = new JLabel(phraseThirdStep);
		labThirdStep.setForeground(lBackupMain.SILVER);
		labThirdStep.setFont(lBackupMain.h1);
		panCenter.add(labThirdStep);
		
	}

	/**
	 * init the dialog and add allo
	 * items
	 */
	private void initDlg() {
		dlg = new JDialog();
		dlg.setTitle("final step");
		dlg.setSize(530, 300);
		dlg.setIconImage(lBackupMain.getLogo());
		dlg.setLocationRelativeTo(null);
		dlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		dlg.add(panCenter, BorderLayout.CENTER);
		dlg.add(panSouth, BorderLayout.SOUTH);
		
	}

	/**
	 * @return the progressBar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @param progressBar the progressBar to set
	 */
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	/**
	 * @return the sources
	 */
	public List<AbsolutePath> getSources() {
		return sources;
	}

	/**
	 * @param sources the sources to set
	 */
	public void setSources(List<AbsolutePath> sources) {
		this.sources = sources;
	}

	/**
	 * @return the destination
	 */
	public AbsolutePath getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(AbsolutePath destination) {
		this.destination = destination;
	}

	/**
	 * @return the labFirstStep
	 */
	public JLabel getLabFirstStep() {
		return labFirstStep;
	}

	/**
	 * @return the labSecondStep
	 */
	public JLabel getLabSecondStep() {
		return labSecondStep;
	}

	/**
	 * @return the labThirdStep
	 */
	public JLabel getLabThirdStep() {
		return labThirdStep;
	}

	/**
	 * @return the buttAllow
	 */
	public JButton getButtAllow() {
		return buttAllow;
	}
	
	public void setNumOfNewFiles(int num){
		this.labFirstStep.setText(phraseFirstStep + num);
	}
	
	public void setNumOfModifiedFiles(int num){
		this.labSecondStep.setText(phraseSecondStep + num);
	}
	
	public void changeColorOfLabels(){
		this.labFirstStep.setForeground(lBackupMain.SILVER);
		this.labSecondStep.setForeground(lBackupMain.SILVER);
		this.labThirdStep.setForeground(lBackupMain.CLOUDS);
	}
	
	/**
	 * 
	 * @param num
	 * 	number of files to copy
	 * 
	 * @param totalSize
	 * 	string contains the size in this format: 53 MB or 47 GB
	 */
	public void setNumOfFilesToCopy(int num, String totalSize){
		this.labThirdStep.setText(phraseThirdStep 
				+ num + " [" + totalSize + "]");
	}
}
