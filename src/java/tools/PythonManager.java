package tools;


import java.io.File;

/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.io.IOException;

/**
 * Python Manager
 * 
 * This manages the python code from java.
 *
 */
public abstract class PythonManager {
	//runs the trainAgent python file and returns the process
	public static Process trainAgent(String inputFile, String outputFile, String dataDir, int loops) throws IOException {
		Runtime rt = Runtime.getRuntime();
		String command="cmd /c trainNewAgent.py";
		command+=" -i "+inputFile;
		command+=" -o "+outputFile;
		command+=" -e "+dataDir;
		command+=" -l "+Integer.toString(loops);
		String[] envp= {};
		System.out.println(command);
		Process process = rt.exec(command,envp,new File("src/python"));
		return process;
	}
	
	//returns true if the process finished successfully, otherwise returns false
	public static boolean finishedSuccessfully(Process process) {
		try {
			if(process.exitValue()==2) {
				return true;//thread finished
			}
			else {
				return false;//thread failed
			}
		}
		catch(IllegalThreadStateException e){
			return false;//thread not finished
		}
	}

}
