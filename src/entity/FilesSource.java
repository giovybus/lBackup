package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 21:28:40
 */
public class FilesSource extends Files{
	private int revision;
	
	public static final int STATUS_NEW = 0;
	public static final int STATUS_MODIFY = 1;
	public static final int STATUS_NOT_MODIFY = 2;
	public static final int STATUS_DELETED = 3;
	public static final int STATUS_ERROR = -1;
	
	/**
	 * @return the revision
	 */
	public int getRevision() {
		return revision;
	}
	/**
	 * @param revision the revision to set
	 */
	public void setRevision(int revision) {
		this.revision = revision;
	}
	
}
