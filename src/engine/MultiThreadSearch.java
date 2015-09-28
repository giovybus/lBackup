package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco Alaimo Pizylon8 Copiright (C) 2015 <br>
 * <b>Email:</b> alaim.marco@gmail.com<br>
 *
 * created on 26/set/2015 12:28:16
 *
 */
public class MultiThreadSearch extends Thread
{
	
	private String source_folder=null;
	private List<File> file_list=null;
	private MultiSearchMonitor monitor=null;
	
	public MultiThreadSearch(String source_folder,MultiSearchMonitor monitor)
	{
		this.source_folder=source_folder;
		this.file_list=new ArrayList<File>();
		this.monitor=monitor;
		
	}
	
	
	public void run() 
	{
			try{
					synchronized (monitor) 
					{
						monitor.thList.add(this);
					}
					
					
					File files[]=new File(this.source_folder).listFiles();
					if(files==null)
						{
							synchronized (monitor) 
							{
								monitor.thList.remove(this);
								return;
							}
						}
					
					for(int i=0;i<files.length;i++)
						{
						
							//System.out.println(files[i].getAbsolutePath());
							if(files[i].isDirectory())
								{
									if(monitor.isAllowDirectory(files[i]))
										{
											MultiThreadSearch temp=new MultiThreadSearch(files[i].getAbsolutePath(),this.monitor);
											temp.start();
										}
									
								}else{
									
									if(monitor.isAllowExtension(files[i]))
									{
										monitor.addFileList(files[i]);
									}
									
									//System.out.println(files[i].getAbsolutePath());
								}
						}
					
					
					synchronized (monitor) {
						
						
						System.out.println(this.source_folder);
						monitor.thList.remove(this);
						
						monitor.notify();
						
					}
						
					
				
			}catch(Exception e){
				e.printStackTrace();
			}
	}
}
