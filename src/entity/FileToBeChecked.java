package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 03/ott/2015 16:13:40
 */
public class FileToBeChecked {
	private int id;
	private String fullPath;
	private String md5;
	
	public FileToBeChecked(int id, String fullPath, String md5) {
		this.id = id;
		this.fullPath = fullPath;
		this.md5 = md5;
	}
	
	public FileToBeChecked(String fullPath, String md5) {
		this.fullPath = fullPath;
		this.md5 = md5;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the fullPath
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	/**
	 * @return
	 */
	public String getFullPathClear() {
		if(this.fullPath == null)return "";
		else return this.fullPath.replace("'", "''");
	}
	
}
