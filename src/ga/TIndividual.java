package ga;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author kannra
 * @version 1.0
 * @since 1.0
 * class for Indivdual.
 */
public class TIndividual implements Comparable<TIndividual>{
	/** evaluationValue */
	private double fEvaluationValue;
	/** vector */
	private TVector fVector;

	/** error point for equals */
	private static final double EPS = 1e-10;

	/**
	 * default constructor.
	 * fEvaluationValue=NaN,fVector=default
	 */
	public TIndividual() {
		fEvaluationValue=Double.NaN;
		fVector=new TVector();
	}
	
	/**
	 * copy constructor
	 * @param TIndividual src
	 */
	public TIndividual(TIndividual src) {
		fEvaluationValue=src.fEvaluationValue;
		fVector=new TVector(src.fVector);
	}
	
	/**
	 * use constructor
	 * @return TIndividual
	 */
	@Override
	public TIndividual clone() {
		return new TIndividual(this);
	}
	
	/**
	 * efficient copy
	 * @param src
	 * @return this
	 */
	public TIndividual copyFrom(TIndividual src) {
		fEvaluationValue=src.fEvaluationValue;
		fVector.copyFrom(src.fVector);
		return this;
	}
	
	/**
	 * @param pw PrintWriter
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(fEvaluationValue);
		fVector.writeTo(pw);
	}
	
	/**
	 * @param BufferedReader br
	 */
	public void readFrom(BufferedReader br) throws IOException{
		fEvaluationValue=Double.parseDouble(br.readLine());
		fVector.readFrom(br);
	}
	
	/**
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder str=new StringBuilder();
		str.append(fEvaluationValue);
		str.append("\n");
		str.append(fVector.toString());
		return str.toString();
	}
	
	/**
	 * @param TVector
	 */
	public void setfVector(TVector src) {
		fVector=new TVector(src);
	}
	
	/**
	 * @return Tvector
	 */
	public TVector getVector() {
		return fVector;
	}
	
	/**
	 * @return double
	 */
	public double getEvaluationValue() {
		return fEvaluationValue;
	}

	/**
	 * @param eval double
	 */
	public void setEvaluationValue(double eval) {
		fEvaluationValue = eval;
	}
	/**
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o) {
		TIndividual indv = (TIndividual)o;

		if (Math.abs(fEvaluationValue - indv.fEvaluationValue) > EPS) {
			return false;
		}

		if (fVector.equals(indv.fVector)) {
			return true;
		}

		return false;
	}
	
	/**
	 * @param indv TIndividual
	 */
	@Override
	public int compareTo(TIndividual indv) {

		double res = fEvaluationValue - indv.fEvaluationValue;

		if (res > 0.0) {
			return 1;
		}
		else if (res < 0.0){
			return -1;
		}
		else {
			return 0;
		}
	}
	
}