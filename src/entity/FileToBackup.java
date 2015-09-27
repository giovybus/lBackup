package entity;

import java.io.File;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 27/set/2015 19:12:20
 */
public class FileToBackup {
	private int id;
	private String pathSource;
	private String pathDestination;
	private int status;
	
	/**
	 * 
	 */
	public FileToBackup() {
		// TODO Auto-generated constructor stub
	}
	
	public FileToBackup(int id, String pathSource, String pathDestination,
			int status) {
		this.id = id;
		this.pathSource = pathSource;
		this.pathDestination = pathDestination;
		this.status = status;
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
	 * @return the pathSource
	 */
	public String getPathSource() {
		return pathSource;
	}

	/**
	 * @param pathSource the pathSource to set
	 */
	public void setPathSource(String pathSource) {
		this.pathSource = pathSource;
	}

	/**
	 * @return the pathDestination
	 */
	public String getPathDestination() {
		return pathDestination;
	}

	/**
	 * @param pathDestination the pathDestination to set
	 */
	public void setPathDestination(String pathDestination) {
		this.pathDestination = pathDestination;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	public File getFileSource(){
		return new File(pathSource); 		
	}
	
	public File getFileDestination(){
		return new File(pathDestination);
	}
}
