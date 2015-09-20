package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 21:51:58
 */
public class VirtualLink {
	private FilesBackup realFile;
	private AbsolutePath backup;
	
	/**
	 * dafault constructor 
	 */
	public VirtualLink() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the realFile
	 */
	public FilesBackup getRealFile() {
		return realFile;
	}

	/**
	 * @param realFile the realFile to set
	 */
	public void setRealFile(FilesBackup realFile) {
		this.realFile = realFile;
	}

	/**
	 * @return the backup
	 */
	public AbsolutePath getBackup() {
		return backup;
	}

	/**
	 * @param backup the backup to set
	 */
	public void setBackup(AbsolutePath backup) {
		this.backup = backup;
	}

}
