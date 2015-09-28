package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 28/set/2015 19:19:45
 */
public class Blacklist {
	public static final int ABSOLUTE_PATH = 0;
	public static final int DIRECTORY_NAME = 1;
	public static final int EXTENSION = 2;
	
	private String path;
	private int kind;
	
	/**
	 *
	 */
	public Blacklist() {
		// TODO Auto-generated constructor stub
	}

	public Blacklist(String path, int kind) {
		this.path = path;
		this.kind = kind;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the kind
	 */
	public int getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind(int kind) {
		this.kind = kind;
	}

	/**
	 * @return
	 */
	public String getPathClear() {
		if(this.path == null)return "";
		else return this.path.replace("'", "''");
	}
	
}
