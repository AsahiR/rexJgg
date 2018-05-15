package ga;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author kannra
 * @versoin 1.0
 * @since 1.0
 */
public class TVector {
	/**
	 * @param double[] fData element of vector
	 * @param double EPS  error
	 */
	private double[] fData;
	private static double EPS=1e-10;

	/* constructor */
	public TVector() {
		fData=new double[0];
	}

	/*copy constructor*/
	public TVector(TVector src) {
		fData=new double[src.fData.length];
		for(int i=0; i<fData.length;i++) {
			fData[i]=src.fData[i];
		}
	}
	
	public TVector copyFrom(TVector src) {
		if(fData.length != src.fData.length) {
			fData=new double[src.fData.length];
		}
		for(int i=0;i<fData.length;i++) {
			fData[i]=src.fData[i];
		}
		return this;
	}
	
	@Override
	public TVector clone() {
		return new TVector(this);
		/* call copy constructor*/
	}
	
	/**
	 * @param PrintWriter pw
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(fData.length);
		for(int i=0;i<fData.length;i++) {
			pw.print(fData[i]+" ");
		}
		pw.println("");
	}
	
	/**
	 * @throws java.io.IOException
	 * @param BufferedReader br
	 */
	public void readFrom(BufferedReader br) throws IOException{
		int dimension=Integer.parseInt(br.readLine());
		setDimension(dimension);
		String[] tokens=br.readLine().split(" ");
		for(int i=0;i<dimension;i++) {
			fData[i]=Double.parseDouble(tokens[i]);
		}
	}
	
	/* not have field dimension*/
	/**
	 * @param int dimension
	 */
	public void setDimension(int dimension) {
		if(fData.length != dimension) {
			fData=new double[dimension];
		}
	}
	
	/**
	 * @return int fData.length
	 */
	public int getDimension() {
		return fData.length;
	}
	
	/**
	 * @param int index
	 * @return double fData[index]
	 */
	public double getElement(int index) {
		return fData[index];
	}

	/**
	 * @param int index
	 * @param double e element
	 */
	public void setElement(int index,double e) {
		fData[index]=e;
	}
	
	/**
	 * @return String
	 */
	@Override
	public String toString() {
		String str="";
		for(int i=0;i<fData.length;i++) {
			str+=fData[i]+" ";
		}
		return str;
	}
	
	/**
	 * @param Object o
	 * @return boolean True if this equals to param o
	 */
	@Override
	public boolean equals(Object o) {
		TVector v=(TVector)o;
		for(int i=0;i<fData.length;i++) {
			if(Math.abs(fData[i]-v.fData[i]) > EPS) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param TVector v
	 * @return Tvector this+v
	 */
	public TVector add(TVector v) {
		assert fData.length == v.fData.length;
		for(int i=0;i<fData.length;i++) {
			fData[i]+=v.fData[i];
		}
		return this;
	}

	/**
	 * @param TVector v
	 * @return Tvector this-v
	 */
	public TVector sub(TVector v) {
		assert fData.length == v.fData.length;
		for(int i=0;i<fData.length;i++) {
			fData[i]-=v.fData[i];
		}
		return this;
	}

	/**
	 * @return double L2Norm of this.
	 */
	public double calculateL2Norm() {
		double temp=0;
		for(int i=0;i<fData.length;i++) {
			temp+=fData[i]*fData[i];
		}
		return Math.sqrt(temp);
	}
	
	/**
	 * @param double a
	 * @return Tvector a*Tvector(broadcast)
	 */
	public TVector scalarProduct(double a) {
		for(int i=0; i<fData.length;i++) {
			fData[i]*=a;
		}
		return this;
	}

	/**
	 * @return Tvector size of this == 1
	 */
	public TVector normalize() {
		double weight=calculateL2Norm();
		if (weight==0) {
			return this;
			/*divide by zero*/
		}
		return scalarProduct(1/weight);
	}
	
	/**
	 * @param TVector x
	 * @return double this dot x
	 */
	public double innerProduct(TVector x) {
		double result=0;
		if (fData.length != x.fData.length){
			System.err.println("lenght differs");
			System.exit(1);
			/*exception*/
			
		}
		for(int i=0;i<fData.length;i++) {
			result+=fData[i]*x.fData[i];
		}
		return result;
	}
	
	/**
	 * @param TVector x
	 * @return Tvextor this*x(broadcast)
	 */
	public TVector elementwiseProduct(TVector x) {
		/*differ length ,exception*/
		for(int i=0; i<fData.length;i++) {
			fData[i]*=x.fData[i];
		}
		return this;
	}
}
