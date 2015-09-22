package entity;

import java.io.File;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 21:24:07
 */
public class Files {
	private int id;
	private AbsolutePath absolutePath;
	private String relativePath;
	private String md5;
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
	 * @return the absolutePath
	 */
	public AbsolutePath getAbsolutePath() {
		return absolutePath;
	}
	/**
	 * @param absolutePath the absolutePath to set
	 */
	public void setAbsolutePath(AbsolutePath absolutePath) {
		this.absolutePath = absolutePath;
	}
	/**
	 * @return the relativePath
	 */
	public String getRelativePath() {
		return relativePath;
	}
	
	/**
	 * @return
	 */
	public String getRelativePathClear() {
		if(this.relativePath == null) return "";
		else return this.relativePath.replace("\\", "\\\\");
	}
	
	/**
	 * @param relativePath the relativePath to set
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
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
	 * mi da il file reale
	 * @return
	 */
	public File getFile(){
		return new File(absolutePath.getPath() + "\\" + relativePath);
	}
}
