/**
 * 
 */
package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author franv
 *
 */
public class FileSystem {
	File folder;
	
	public FileSystem(String folderName) {
	    //String PATH = "/remote/dir/server/";
	    String directoryName = folderName; //PATH.concat(this.getClassName());
	    //String fileName = id + getTimeStamp() + ".txt";

	    folder = new File(directoryName);
	    if (! folder.exists()){
	        folder.mkdir();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }
	}
	
	public String getDocumentList()
	{
		File[] listOfFiles = folder.listFiles();
		String fileList = "";
		
		for (int i = 0; i < listOfFiles.length; i++) 
		{
		  if (listOfFiles[i].isFile()) 
		  {
		    fileList += listOfFiles[i].getName() + "\n";
		  }
		}
		
		return fileList;
	}

	public String getDocumentListInLine()
	{
		File[] listOfFiles = folder.listFiles();
		String fileList = "";
		
		for (int i = 0; i < listOfFiles.length; i++) 
		{
		  if (listOfFiles[i].isFile()) 
		  {
		    fileList += listOfFiles[i].getName() + "||";
		  }
		}
		
		return fileList;
	}
	
	public boolean createFile(String fileName, String contents)
	{
		
	
		File file = new File(folder + "/" + fileName );
		if( file.exists() )
			return false;

		try{
	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(contents);
	        bw.close();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        //System.exit(-1);
	        return false;
	    }
	    return true;
	}
	
	public String getFile(String fileName) throws IOException
	{
		File file = new File(folder.getPath() + "/" + fileName );

		if( !file.exists() )
			throw new IOException("File does not exsist.");
		
        return Files.readString(file.toPath());
	}
	
	public void writeToFile(String fileName, String contents) throws IOException
	{
		File file = new File(folder + "/" + fileName );

		if(! file.exists() )
			throw new IOException("File does not exsist.");
	
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(contents);
        bw.close();        
	}
	
	public boolean deleteFile(String fileName) throws IOException
	{
		File file = new File(folder + "/" + fileName );
		try {
			file.delete();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	
	
	


}
