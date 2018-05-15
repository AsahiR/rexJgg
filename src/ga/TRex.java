package ga;
import java.util.Random;
import java.io.*;

public class TRex {
	public enum ProbabilityDistribution{
		NORMAL,
		UNIFORM
	}
	
	private ProbabilityDistribution fProbabilityDistribution;
	
	private double fExpansionRatio;
	
	/** center vector of parents **/
	private TVector fXg;
	private TVector fWork;
	private int fNoOfParents;
	private Random fRandom;
	
	public TRex(int dimension,
			int noOfParents,
			ProbabilityDistribution pd,
			Random random
			) {
		fNoOfParents=noOfParents;
		fProbabilityDistribution=pd;
		fRandom=random;
		switch(fProbabilityDistribution) {
			case NORMAL:
				fExpansionRatio = Math.sqrt(1.0 / (double)fNoOfParents);
				break;
			case UNIFORM:
				fExpansionRatio = Math.sqrt(3.0 / (double)fNoOfParents);
				break;
		}
		fXg=new TVector();
		fXg.setDimension(dimension);
		fWork=fXg.clone();
	}
	
	public TRex(
			int dimension,
			ProbabilityDistribution pd,
			Random random
			) {
		this(dimension,dimension+1,pd,random);
	}
	
	public int getNoOfParents() {
		return fNoOfParents;
	}
	
	public void makeOffspring(TPopulation parents,
			int noOfKids,TPopulation kids) {
		assert parents.getPopulationSize() == fNoOfParents;
		calcXg(parents);
		/* clear individual(fEvalutionValue,fVector)*/

		for(int i=0;i<kids.getPopulationSize();i++) {
			TVector kVec=kids.getIndividual(i).getVector();
			kVec.copyFrom(fXg);
			for(int j=0;j<parents.getPopulationSize();j++) {
				fWork.copyFrom(parents.getIndividual(j).getVector());
				fWork.sub(fXg);
                double r=0;
                switch(fProbabilityDistribution) {
                case UNIFORM:
                  r=fRandom.nextDouble()*(2*fExpansionRatio)-fExpansionRatio;
                  break;
                case NORMAL:
                  r=fRandom.nextGaussian();
                  r*=Math.sqrt(fExpansionRatio);
                  break;
                }
                fWork.scalarProduct(r);
                kVec.add(fWork);
			}
		}
	}
	
	private void calcXg(TPopulation parents) {
		/* setDimensions?*/
		fXg.scalarProduct(0.0);
		for(int i=0;i<fXg.getDimension();i++) {
			fXg.add(parents.getIndividual(i).getVector());
		}
		fXg.scalarProduct(1.0/(double)parents.getPopulationSize());
	}
}


	/*
	private int fDimension;
	private TPopulation fPopulation;
	private int fPopulationSize;
	private TPopulation fParents;
	private TPopulation fKids;
	private int fNoOfKids;


	private static void initializePopulation(TPopulation population,int dimensionSize,double min,double max,Random rand) {
		for(int i=0;i<population.getPopulationSize();i++) {
			TVector vector=population.getIndividual(i).getVector();
			vector.setDimension(dimensionSize);
			for(int j=0;j<dimensionSize;j++) {
				double randomValue=rand.nextDouble()*(max-min)+min;
				vector.setElement(j,randomValue);
			}
		}
	}
	
	private static void evaluate(TPopulation population) {
		for(int i=0;i<population.getPopulationSize();i++) {
			TVector vector=population.getIndividual(i).getVector();
			double eval=Ktablet.calc(vector);
			vector.setEvaluationValue(eval);
		}
	}
	
	public static void main(String[] args) {
		int dimensionSize=20;
		int populationSize=12*dimensionSize;
		int noOfKids=6*dimensionSize;
		double min=-5.0;
		double max=5.0;
		long maxEvals=(long)(dimensionSize*1e5);
		
		Random rand=new Random();
		TPopulation population=ga.initialize();

	}
	*/
