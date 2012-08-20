package com.m.common;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
  
import java.nio.ByteBuffer;  
import java.nio.channels.FileChannel;  
  
import java.util.ArrayList;  
import java.util.Date;
import java.util.List;  
  
public class FileUtils {  
  
  
   public void doUpload(byte[] bytes, String path, String fileName) throws Exception {
	   
       fileName = path + fileName;  
       File f = new File(fileName);  
       FileOutputStream fos = new FileOutputStream(f);  
       fos.write(bytes);
       f.setReadOnly();
       fos.close();
   }
   
   public String doUploadRename(byte[] bytes, String path, String fileName) throws Exception {
	   
       fileName = path + fileName;  
       File f = new File(fileName);
       f = this.rename(f);       
       FileOutputStream fos = new FileOutputStream(f);  
       fos.write(bytes);
       f.setReadOnly();
       fos.close();
       
       return f.getName();
   }
   
   
   public List<String> getDownloadList(String path) {
	   
       File dir = new File( path );  
       String[] children = dir.list();  
       List<String> dirList = new ArrayList<String>();  
       if (children == null) {  
              // Either directory does not exist or is not a directory  
          } else {  
              for (int i=0; i<children.length; i++) {  
                  // Get filename of file or directory  
                  dirList.add( children[i]);  
              }  
          }  
       return dirList;  
   }  
  
   public byte[] doDownload(String path, String fileName) {
	   
       FileInputStream fis;  
       byte[] data =null;  
       FileChannel fc;  
  
       try {  
           fis = new FileInputStream(path + fileName);  
           fc = fis.getChannel();  
           data = new byte[(int)(fc.size())];  
           ByteBuffer bb = ByteBuffer.wrap(data);  
           fc.read(bb);  
       } catch (FileNotFoundException e) {  
             
       } catch (IOException e) {  
             
       }  
       return data;  
   }
   
   private File rename(File f) {
		
	  //Get the parent directory path as in h:/home/user or /home/user
      String parentDir = f.getParent( );
    
      //Get filename without its path location, such as 'index.txt'
      String fname = f.getName( );
    
      //Get the extension if the file has one
      String fileExt = "";
      int i = -1;
      if(( i = fname.indexOf(".")) != -1){
    
          fileExt = fname.substring(i);
          fname = fname.substring(0,i);
      }
		
		try
		{
			Thread.sleep( 1 ); //millisecond
		}
		catch ( InterruptedException ie )
		{
			System.out.println(ie.toString());
		}
		

      //add the timestamp
      fname = ""+( new Date( ).getTime( ));
    
      //piece together the filename
      fname = parentDir + System.getProperty(
          "file.separator") + fname + fileExt;
    
      File temp = new File(fname);

       return temp;
   }
 
}  
