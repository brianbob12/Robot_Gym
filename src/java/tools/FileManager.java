package tools;
/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/**
 * FileManager
 * A collection of file management functions
 */
public abstract class FileManager {
	
	//reads a file and returns a list of strings by line of the file
	//propagates exceptions
	public static List<String> readByLine(String path) throws IOException{
		List<String> out=new ArrayList<String>();
		File f= new File(path);
		Scanner scan=new Scanner(f);
		while(scan.hasNextLine()) {
			out.add(scan.nextLine());
		}
		scan.close();
		return out;
	}
	
	//reads a .csv(must be a perfect rectangle) and returns two dimensional array by columns and then rows - [columnNumber,rowNumber]
	//propagates exceptions
	public static List<List<Double>> readRectangleCSV(String path) throws IOException{
		List<String> rawOutput=readByLine(path);
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
	
	//writes lines to a text file 
	public static void writeToTxt(String path,List<String> lines) throws IOException{
		//object setup
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
		//iterate over lines bar the final line
		for (int i=0;i<lines.size()-1;i++) {
			bufferedWriter.write(lines.get(i));
			bufferedWriter.newLine();
		}
		//write final line without newLine
		bufferedWriter.write(lines.get(lines.size()-1));
		//close
		bufferedWriter.close();
	}
}
