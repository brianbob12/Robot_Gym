/**
 * 
 * @author cyrus singer
 * Robot_Gym version 0.1
 *
 */
import java.util.*;
/**
 * Matrix
 * A simple double matrix with multiplication. For use in evaluation neural nets
 */
public class Matrix {
	public int nRows;
	public int nCols;
	private List<List<Double>> data;//data as a list of columns
	
	public Matrix(int nCols,int nRows) {
		this.nRows=nRows;
		this.nCols=nCols;
	}
	public void setData(List<List<Double>> x) {
		this.data=x;
	}
	public List<List<Double>> getData(){
		return this.data;
	}
	public List<Double> getCol(int index){
		if(this.data==null) {
			return null;
		}
		//do not return pointer to this.data
		//for safety make new object
		List<Double> out=new ArrayList<Double>();
		for(int i=0;i<this.nRows;i++) {
			out.add(this.data.get(index).get(i));
		}
		return out;
	}
	public List<Double> getRow(int index){
		if(this.data==null) {
			return null;
		}
		List<Double> out=new ArrayList<Double>();
		for(int i=0;i<this.data.size();i++) {
			out.add(this.data.get(i).get(index));
		}
		return out;
	}
	
	//returns the dot product of two vectors/lists
	public double dotProduct(List<Double>x,List<Double> y) {
		double out =0;
		for (int i=0;i<x.size();i++) {
			out+=x.get(i)*y.get(i);
		}
		return out;
	}
	
	//multiplies this by the given argument matrix. 
	public Matrix multiply(Matrix transformation) {
		
		Matrix product=new Matrix(transformation.nCols,this.nRows);
		List<List<Double>> newData=new ArrayList<List<Double>>();
		List<List<Double>> rows=new ArrayList<List<Double>>();//store the rows of this matrix to avoid repetition
		for(int i=0;i<this.nRows;i++) {
			rows.add(this.getRow(i));
		}
		
		for(int i=0;i<transformation.nCols;i++) {
			List<Double> column=transformation.getCol(i);
			newData.add(new ArrayList<Double>());
			for(int j=0;j<this.nRows;j++) {
				newData.get(i).add(this.dotProduct(rows.get(j), column));//avoiding repetition here
			}
		}
		product.setData(newData);
		return product;
	}

	//adds two matrices together using Entryswise sum
	//this implies that the matrices have to be the same shape
	public Matrix add(Matrix m) {
		Matrix product=new Matrix(this.nCols,this.nRows);
		List<List<Double>> newData=new ArrayList<List<Double>>();
		List<List<Double>> tad=m.getData();
		
		for(int i=0;i<this.nCols;i++) {
			newData.add(new ArrayList<Double>());
			for(int j=0;j<this.nRows;j++) {
				newData.get(i).add(this.data.get(i).get(j)+tad.get(i).get(j));
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
