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
	public List<Thread> thList;
	
	
	public MultiSearchMonitor(String source_path)
	{
		this.source_path=source_path;
		this.thList=new ArrayList<Thread>();
		
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
						
						MultiThreadSearch temp =new MultiThreadSearch(files[i].getAbsolutePath(), this);
						temp.start();
						//thList.add(temp);
						
						
					}else{
						
						this.filesFound.add(files[i]);
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
	
	public List<File> getList() {
		
		return this.filesFound;
	}
}
