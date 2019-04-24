package com.example.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
public class TerminateUtil {
	 private static final String TASKLIST = "tasklist";
	 private static final String KILL = "taskkill /F /IM ";
	 private static InputStreamReader INPUTSTREAMREADER = null;
	 private static BufferedReader BUFFEREDREADER = null;
	 /**
	  * Delete process run by cmdUtil
	  * @param processName - the process you want to kill that you create to windows like RetrieveFace_ByCam.exe
	  */
	 public static void cmdProcessTerminate(String processName){
	  // Detect the process run by cmd 
	  if (isProcessRunning(processName)) {
	   // Kill the process run by windows
	   killProcess(processName);
	  }  
	  try {
	   if(BUFFEREDREADER!=null) {
	    BUFFEREDREADER.close();    
	   }
	   if(INPUTSTREAMREADER!=null) {
	    INPUTSTREAMREADER.close();    
	   }
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	 }
	 
	 /**
	  * Detect the process run by cmd 
	  * @param serviceName 
	  * @return boolean
	  */
	 protected static boolean isProcessRunning(String serviceName) {
	  //Start the Process
	  Process process = null;
	  try {
	   process = Runtime.getRuntime().exec(TASKLIST);
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  
	  //Read and list the process run by windows
	  final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	  String line;
	  try {
	   while ((line = reader.readLine()) != null) {
//	    System.out.println(line);
	    if (line.contains(serviceName)) {
	     return true;
	    }
	   }
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  return false;
	 }

	 /**
	  * Kill the process run by windows
	  * @param serviceName 
	  */
	 protected static void killProcess(String serviceName) {
	  try {
	   Runtime.getRuntime().exec(KILL + serviceName);
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	 }
}