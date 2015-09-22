package boundary;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

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
	private JLabel labSecondStep;
	private JLabel labThirdStep;
	private JButton buttAllow;
	
	private JPanel panSouth;
	private JProgressBar progressBar;
	
	/**
	 * default constructor
	 */
	public DetailsGui() {
		initPanelCenter();
		initDlg();
	}

	/**
	 * 
	 */
	private void initPanelCenter() {
		panCenter = new JPanel();
		panCenter.setBackground(lBackupMain.WET_ASPHALT);
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
		//dlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		dlg.add(panCenter);
		
		dlg.setModal(true);
		dlg.setVisible(true);
		
	}
}
