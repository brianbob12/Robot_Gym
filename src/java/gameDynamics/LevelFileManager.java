package gameDynamics;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * LevelFileManager
 * 
 * This class handles the storage of levels. This includes three main features:
 * Save level to storage, Load level from storage, Get list of levels in storage
 */

public abstract class LevelFileManager {
	
	//loads level from path and returns level object
	//This will break whenever the code for Level class or any of its non-transient components are changed.
	//This includes: GameObject
	//The method handles ClassNotFoundException which is caused by source code changes. 
	//The method propagates IOExceptoins which are caused by directory issues
	public static Level loadLevel(String path) throws IOException{
		Level outLevel = null;
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			outLevel=(Level)ois.readObject();
			outLevel.postImportSetup();
		} catch (ClassNotFoundException e) {
			System.out.println("Source code changed for Level");
			e.printStackTrace();
			//this will cause a null to be returned
		}
		ois.close();

		return outLevel;
	}
	
	//saves level to the given path. Does not alter path at all
	public static void saveLevel(String path,Level level) throws IOException{
		FileOutputStream fos= new FileOutputStream(path);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(level);
		oos.flush();
	}

}
