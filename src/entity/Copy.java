package entity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * @author Giovanni Buscarino (giovybus) (c) 2015
 *
 * created on 05/apr/2015
 */
public class Copy {


	/**
	 * questo metodo è stato copiato da: http://examples.javacodegeeks.com/core-java/io/file/4-ways-to-copy-file-in-java/
	 * secondo la guida è il più veloce
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
	
	/**
	 * questo metodo è stato copiato da: http://examples.javacodegeeks.com/core-java/io/file/4-ways-to-copy-file-in-java/
	 * secondo la guida è il secondo metodo più veloce
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFileUsingJava7Files(File source, File dest)
			throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}
	
	
	public static String getMd5(String path){
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			String md5 = DigestUtils.md5Hex(fis);
			fis.close();
			
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getMd5(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			String md5 = DigestUtils.md5Hex(fis);
			fis.close();
			
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
