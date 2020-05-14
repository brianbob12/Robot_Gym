/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
/**
 * Matrix
 * A simple float matrix with multiplication. For use in evaluation neural nets
 */
public class Matrix {
	public int nRows;
	public int nCols;
	private List<List<Float>> data;//data as a list of columns top left entry would be data[0][0]
	
	public Matrix(int nRows,int nCols) {
		this.nRows=nRows;
		this.nCols=nCols;
	}
	public void setData(List<List<Float>> x) {
		this.data=x;
	}
	public List<List<Float>> getData(){
		return this.data;
	}
	public List<Float> getCol(int index){
		if(this.data==null) {
			return null;
		}
		//do not return pointer to this.data
		//for safety make new object
		List<Float> out=new ArrayList<Float>();
		for(int i=0;i<this.nRows;i++) {
			out.add(this.data.get(index).get(i));
		}
		return out;
	}
	public List<Float> getRow(int index){
		if(this.data==null) {
			return null;
		}
		List<Float> out=new ArrayList<Float>();
		for(int i=0;i<this.data.size();i++) {
			out.add(this.data.get(i).get(index));
		}
		return out;
	}
	
	//returns the dot product of two vectors/lists
	public float dotProduct(List<Float>x,List<Float> y) {
		float out =0;
		for (int i=0;i<x.size();i++) {
			out+=x.get(i)*y.get(i);
		}
		return out;
	}
	
	//multiplies this by the given argument matrix. 
	public Matrix multiply(Matrix transformation) {
		Matrix product=new Matrix(this.nRows,transformation.nCols);
		List<List<Float>> newData=new ArrayList<List<Float>>();
		List<List<Float>> rows=new ArrayList<List<Float>>();//store the rows of this matrix to avoid repetition
		for(int i=0;i<this.nRows;i++) {
			rows.add(this.getRow(i));
		}
		
		for(int i=0;i<transformation.nCols;i++) {
			List<Float> column=transformation.getCol(i);
			newData.add(new ArrayList<Float>());
			for(int j=0;j<this.nRows;j++) {
				newData.get(i).add(this.dotProduct(rows.get(j), column));//avoiding repetition here
			}
		}
		product.setData(newData);
		return product;
	}
	
	//Compatibility test for matrix multiplication
	public boolean compatableMult(Matrix transformation) {
		return(this.nCols==transformation.nRows);
	}
	
	//for debugging
	public void print() {
		for(int i=0;i<this.nRows;i++) {
			for(int j=0;j<this.nCols;j++) {
				System.out.print(this.data.get(j).get(i));
				System.out.print(",");
			}
			System.out.println();
		}
	}
}
