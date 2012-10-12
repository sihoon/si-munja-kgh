package com.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import com.common.VbyP;


public class Thumbnail2 {
    
    /**
     * @param loadFile   - 원본 파일명
     * @param saveFile   - 썸네일 파일명
     * @param maxWidth   - 썸네일 파일 가로 크기
     * @param maxHeight  - 썸네일 파일 세로 크기 
     * @throws InterruptedException 
     */
    public boolean createThumbnail(String loadFile, String saveFile, int autoScale) throws IOException{
     
		int maxWidth = SLibrary.intValue( VbyP.getValue("mms_width") );
		int maxHeight = SLibrary.intValue( VbyP.getValue("mms_height") );
    	
     String imageMagicDir = "/usr/local/ImageMagick/bin";//SLibrary.IfNull(SystemProperties.getProperty("images.magic.contextBase"));
     
     String autoSize = "!";
     //if(!autoScale.equals("Y")) autoSize = "!";
     System.out.println();
     File orgImgFile = new File(loadFile);
     File thumImgFile = new File(saveFile);
     
     ArrayList<String> command = new ArrayList<String>(10);
     
     command.add(imageMagicDir+"/convert");
     command.add("-geometry");
     command.add(maxWidth + "x" + maxHeight);
     command.add("-quality");
     command.add(""+VbyP.getValue("mms_quality"));
     command.add(orgImgFile.getAbsolutePath());
     command.add(thumImgFile.getAbsolutePath());
    
     System.out.println(command);
//     command.add("-resize");
//     command.add(maxWidth + "x" + maxHeight + autoSize);
//     command.add(orgImgFile.getAbsolutePath());
//     command.add(thumImgFile.getAbsolutePath());
     
     //System.out.println("thumbnail command String " + command);
     
     return exec((String[])command.toArray(new String[1]));
    }
    
    public boolean exec(String[] command) throws IOException{
     
     Process proc;
     
     try{
      //command 명령어 실행
      proc = Runtime.getRuntime().exec(command);
      
     }catch(IOException e){
      System.out.println("IOException while trying to execute " +SLibrary.inQuery(command));
      return false;
     }
     
     int exitStatus;
     
     while(true){
      try {
       exitStatus = proc.waitFor();
       break;
      } catch (java.lang.InterruptedException e) {
       System.out.println("Interrupted: Ignoring and waiting");
      }
      
     }
     
     if(exitStatus != 0){
      System.out.println("Error executing command: " +exitStatus);
     }
     
     return (exitStatus == 0);
    }

}