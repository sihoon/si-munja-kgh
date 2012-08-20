package com.common.util;

import java.io.File;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class UnixtimeFileRenamePolicy implements FileRenamePolicy {
	
	String addString = null;
	public void setAddFileName(String addStr ) {
		this.addString = addStr;
	}

	public File rename(File f) {
		
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
