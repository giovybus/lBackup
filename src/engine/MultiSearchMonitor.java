package engine;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Alaimo Pizylon8 Copiright (C) 2015 <br>
 * <b>Email:</b> alaim.marco@gmail.com<br>
 *
 * created on 26/set/2015 13:10:59
 *
 */
public class MultiSearchMonitor extends Thread
{
	private String source_path;
	private List<File> filesFound;
	public 	List<Thread> thList;
	private List<String> blacklistByPath;
	private List<String>blacklistByName;
	private List<String>blacklistByExtension;
	
	public MultiSearchMonitor(String source_path,List<String> blacklistByPath, List<String>blacklistByName,List<String>blacklistByExtension)
	{
		this.source_path=source_path;
		this.thList=new ArrayList<Thread>();
		this.blacklistByPath=blacklistByPath;
		this.blacklistByName=blacklistByName;
		this.blacklistByExtension=blacklistByExtension;	
	}

	
	public synchronized void addFileList(File file)
	{
		this.filesFound.add(file);
	}
	
	

	
	
	public synchronized void run()
	{
		
		this.filesFound=new ArrayList<File>();
		File files[]=new File(this.source_path).listFiles();
		
		if(files.length>0)
			{
				thList.add(this);
			}
		
		for(int i=0;i<files.length;i++)
			{
				
				if(files[i].isDirectory())
					{
						
						if(isAllowDirectory(files[i]))
							{
								MultiThreadSearch temp =new MultiThreadSearch(files[i].getAbsolutePath(), this);
								temp.start();
							}
						//thList.add(temp);
						
						
					}else{
						
						if(isAllowExtension(files[i]))
							{
								this.filesFound.add(files[i]);
							}
					}
			}
		
		while(this.thList.size()>0)
			{
					try{
						
						System.out.println("sono in wait "+thList.size());
						this.wait();
						thList.remove(this);
						System.out.println("mi sveglio ");
						
					}catch(Exception e){
						e.printStackTrace();
					}
					
				
			}
		System.out.println("sono libero da thread");
		this.interrupt();
		
		
	}
	
	/**
	 * @param file
	 * @return
	 */
	public synchronized boolean isAllowExtension(File file)
	{
		boolean isAllow=true;
		if(this.blacklistByExtension!=null)
			{
				for(int i=0;i<blacklistByExtension.size();i++)
					{
						//System.out.println("name     "+file.getName().substring(file.getName().lastIndexOf(".")));
						
						if(file.getName().contains(".")&&file.getName().substring(file.getName().lastIndexOf(".")).equals(blacklistByExtension.get(i)))
							{
								//System.out.println("scarto estensione "+file.getName());
								return false;
							}
					}
			}
		
		
		
		return isAllow;
	}


	/**
	 * @param file
	 * @return
	 */
	public synchronized boolean isAllowDirectory(File file)
	{
		boolean is=true;
		String abPath=file.getAbsolutePath();
		if(this.blacklistByPath!=null)
			{
				for(int i=0;i<this.blacklistByPath.size();i++)
					{
						if(abPath.contains(this.blacklistByPath.get(i)))
							{
								
								return false;
							}
					}
					
			}
			
			String directoryName=file.getName();
			if(this.blacklistByName!=null)
				for(int i=0;i<this.blacklistByName.size();i++)
					{
						if(directoryName.equals(this.blacklistByName.get(i)))
							{
								//System.out.println("scartoooooooooooooooooooooooooooooooooooooo ");
								return false;
							}
					}

		
		
		return is;
	}


	public List<File> getList() {
		
		return this.filesFound;
	}
}
