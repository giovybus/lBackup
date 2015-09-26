package entity;

/**
 * @author Giovanni Buscarino (giovybus) Copyright (c) 2015 <br>
 * <b>Email:</b> giovanni.buscarino[at]gmail.com<br>
 *
 * created on 20/set/2015 21:24:31
 */
public class AbsolutePath {
	public static final int SOURCE = 0;
	public static final int ROOT_DESTINATION = 1;
	public static final int CHILD_DESTINATION = 2;
	
	/**
	 * the AI id
	 */
	private int id;
	
	/**
	 * the relative path from X:\ ./
	 * to root directory source
	 */
	private String path;
	
	/**
	 * type of dir
	 * root backup
	 * source
	 * child backup
	 */
	private int type;
	
	/**
	 * void constructor
	 */
	public AbsolutePath() {
		
	}
	
	/**
	 * constructor param
	 * @param id
	 * @param path
	 * @param type
	 */
	public AbsolutePath(int id, String path, int type) {
		this.id = id;
		this.path = path;
		this.type = type;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * @return
	 * return a clear path for database
	 * @deprecated
	 */
	public String getPathClear() {
		if(this.path == null)return "";
		else return this.path.replace("\\", "\\\\");
	}
	
	
}
