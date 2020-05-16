/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
import java.io.File;
import java.io.IOException;
/**
 * FileManager
 * A collection of file management functions
 */
public class FileManager {
	
	//reads a file and returns a list of strings by line of the file
	public List<String> readByLine(String path){
		List<String> out=new ArrayList<String>();
		try {
			File f= new File(path);
			Scanner scan=new Scanner(f);
			while(scan.hasNextLine()) {
				out.add(scan.nextLine());
			}
			scan.close();
		}
		catch(IOException e) {
			System.out.println(e);
		}
		return out;
	}
	
	//reads a .csv(must be a perfect rectangle) and returns two dimensional array by columns and then rows - [columnNumber,rowNumber]
	public List<List<Double>> readRectangleCSV(String path){
		List<String> rawOutput=this.readByLine(path);
		//list of each line casted to floats separated by commas 
		List<List<Double>> sep=new ArrayList<List<Double>>();
		for(int i=0;i<rawOutput.size();i++) {
			List<Double> tad=new ArrayList<Double>();
			String[] toCast=rawOutput.get(i).split(",");
			for(String j:toCast) {
				tad.add(Double.parseDouble(j));
			}
			sep.add(tad);
		}
		//now sep needs to be reformatted as a list of columns in place of floats
		List<List<Double>> out=new ArrayList<List<Double>>();
		//for columns
		for(int i=0;i<sep.get(0).size();i++) {
			out.add(new ArrayList<Double>());
			//for rows
			for(int j=0;j<sep.size();j++) {
				out.get(i).add(sep.get(j).get(i));

			}
		}
		return out;
	}
}
