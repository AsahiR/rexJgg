package ga;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TJgg {
	/** dimensional size of vector*/
	private int fDimension;

	/** set of individual*/
	private TPopulation fPopulation;

	/** size of fPopulation*/
	private int fPopulationSize;

	/** parents set*/
	private TPopulation fParents;

	/** size of fParents*/
	private int fParentsSize;

	/** size of fKids*/
	private TPopulation fKids;

	/** size of fKids*/
	private int fKidsSize;
	
	/** crossover object for Rex*/
	private TRex fRex;

	/** rng for Rex*/
	private Random fRexRandom;

	/** rng for parents selection*/
	private Random fParentRandom;
	
	/**
	 * @param dimension 
	 * @param populationSize
	 * @param kidsSize
	 * @param rexRandom
	 * @param parentRandom
	 */
	public TJgg(
			int dimension,int populationSize,
			int kidsSize,int parentsSize,
			Random rexRandom,Random parentRandom
			) {
		fDimension = dimension;
		fPopulationSize = populationSize;
		fParentsSize = parentsSize;
		fRexRandom = rexRandom;
		fKidsSize = kidsSize;
		fParentRandom = parentRandom;

		/** population size constant*/
		assert fKidsSize>fParentsSize;
	}
	
	/**
	 * Initialize population.
	 * Element of vector from min to max
	 * @param max
	 * @param min
	 * @param random
	 * @return population
	 */
	public TPopulation initialize(double max,double min,Random random) {
		fParents = new TPopulation(fParentsSize);
		fKids = new TPopulation(fKidsSize);
		fRex = new TRex(fDimension,TRex.ProbabilityDistribution.UNIFORM,fRexRandom);
		/** bad point*/
		fPopulation = new TPopulation(fPopulationSize);

		for(int i = 0 ; i<fPopulationSize ; i++) {
			TVector vector = fPopulation.getIndividual(i).getVector();
			vector.setDimension(fDimension);
			for(int j = 0 ; j<fDimension ; j++) {
				double randomValue = random.nextDouble()*(max-min)+min;
				vector.setElement(j,randomValue);
			}
		}
		return fPopulation;
	}
	
	/**
	 * @return fPopulation
	 */
	public TPopulation getPopulation() {
		return fPopulation;
	}
	
	/**
	 * Select parents. 
	 * This method not remove parents from population.
	 * You must change fPopulation[-fParentsSize:] to fKids by nextGenration()
	 * @return fParents
	 */
	public TPopulation selectParents() {
		int indexLimit = fPopulationSize-1;
		for(int i = 0 ; i<fParentsSize ; i++) {
			int index = fParentRandom.nextInt(indexLimit);
			TIndividual selected = fPopulation.getIndividual(index);
			fParents.getIndividual(i).copyFrom(selected);
			TIndividual limitIndividual = fPopulation.getIndividual(indexLimit);
			selected.copyFrom(limitIndividual);
			indexLimit--;
		}
		return fParents;
	}
	
	/**
	 * Make fKids by fRex.
	 * @return fKids
	 */
	public TPopulation makeOffspring() {
		fParents = selectParents();
		fRex.makeOffspring(fParents,fKidsSize,fKids);
		return fKids;
	}
	
	/**
	 * Evaluate individual of population by  function
	 * @param population
	 * @param func 
	 */
	public void evaluatePopulation(TPopulation population,EnumFunction func) {
		for(int i = 0 ; i<population.getPopulationSize() ; i++) {
			TIndividual indv = population.getIndividual(i);
			double eval = func.calc(indv.getVector());
			indv.setEvaluationValue(eval);
		}
	}
	
	/**
	 * Set next population from parents and kids and population
	 * Method is jgg
	 */
	public void nextGeneration() {
		fKids.sortByEvaluationValue();
		for(int i = 0 ; i<fParentsSize ; i++) {
			int parentIndex = fPopulationSize-fParentsSize+i;
			TIndividual indv = fPopulation.getIndividual(parentIndex);
			indv.copyFrom(fKids.getIndividual(i));
		}
	}
	
	/**
	 * @return Best individual in population
	 */
	public TIndividual getBestIndividual() {
		fPopulation.sortByEvaluationValue();
		return fPopulation.getIndividual(0);
	}
	
	/**
	 * @return  EvaluaitonValue of best individual
	 */
	public double getBestEvaluationValue() {
		return getBestIndividual().getEvaluationValue();
	}
	
	/**
	 * @param log Table. noOfEvals * TrialNum
	 * @param trialName Column key for trial
	 * @param trialNo  Column index for trial
	 * @param index Row index.
	 * @param noOfEvals Written value for noOfEvals column
	 * @param bestEvaluationValue Written value for trialNo column
	 */
	private static void putLogData(TCTable log,String trialName,
			int trialNo,int index,long noOfEvals,
			double bestEvaluationValue) {
		log.putData(index,"NoOfEvals",noOfEvals);
		log.putData(index,trialName+"_"+trialNo,bestEvaluationValue);
	}
	
	public void writeTo(PrintWriter pw) {
		/**
		int count = fPopulation.getPopulationSize() > 0 ? 1 : 0;
		count += fParents.getPopulationSize() > 0 ? 1 : 0;
		count += fKids.getPopulationSize() > 0 ? 1 : 0;
		pw.println(count);// for fPopulation,fParents,fKids.
		**/
		pw.println(3);
		fPopulation.writeTo(pw);
		fParents.writeTo(pw);
		fKids.writeTo(pw);
	}
	
	
	/** deprecated difinition here*/
	public static String makeFileName(String trialName,int trialNum,int evaluationNum) {
		String dirName="log/";
		String exp=".txt";
        String populationWriteFileName=dirName+trialName+"_Trl"+trialNum+"_Evl"+evaluationNum+exp;
        return populationWriteFileName;
	}
	
	

	public static void main(String[] args) throws IOException {
		final EnumFunction function = EnumFunction.KTablet;
		int dimension = 20;
		int populationSize = 14*dimension;
		int parentsSize = dimension+1;
		int kidsSize = 2*dimension;
		double maxElement = 5.0;
		double minElement = -5.0;
		double evaluationValueLimit = 1.0e-7;
		Random rexRandom = new Random();
		Random parentRandom = new Random();
		
		long evaluationNumLimit = 4*dimension*10000;
		String trialName = "RexJgg"+"_"+function.toString()+"_"+minElement+"to"+maxElement
				+"_"+"p"+parentsSize+"k"+kidsSize;
		String fileName = trialName+".csv";
		TCTable log = new TCTable();
		int trialNum = 3;
		
        int evaluationNum=0;
        String populationWriteFileName;
		
		for(int i = 0 ; i<trialNum ; i++) {
            TJgg jgg = new TJgg(dimension,populationSize
            		,kidsSize,parentsSize,rexRandom,parentRandom);
            jgg.initialize(maxElement,minElement,new Random());
            jgg.evaluatePopulation(jgg.getPopulation(),function);
            evaluationNum = populationSize;
            double eval = jgg.getBestEvaluationValue();
            
            populationWriteFileName=makeFileName(trialName,i,evaluationNum);
            try(PrintWriter pw = new PrintWriter(populationWriteFileName)){
              jgg.writeTo(pw);
            }
            /** for Viewer*/

            int logIndex = 0;
            jgg.putLogData(log,trialName,i,logIndex,evaluationNum,eval);
            logIndex++;
            while(evaluationNum < evaluationNumLimit &&
            		eval > evaluationValueLimit) {
            	TPopulation kids = jgg.makeOffspring();
            	jgg.evaluatePopulation(kids,function);
            	evaluationNum += kidsSize;
            	jgg.nextGeneration();
            	eval = jgg.getBestEvaluationValue();
            	if(evaluationNum%10 == 0) {
                    jgg.putLogData(log,trialName,i,logIndex,evaluationNum,eval);
                    logIndex++;

                    populationWriteFileName=makeFileName(trialName,i,evaluationNum);
                    try(PrintWriter pw = new PrintWriter(populationWriteFileName)){
                        jgg.writeTo(pw);
                    }
            	}
            }
            putLogData(log,trialName,i,logIndex,evaluationNum,eval);
            System.out.println("TrialNo:"+i+", NoOfEvals:"+evaluationNum+
            		", Best:"+eval);

            populationWriteFileName=makeFileName(trialName,i,evaluationNum);
            try(PrintWriter pw = new PrintWriter(populationWriteFileName)){
                jgg.writeTo(pw);
            }

		}
            log.writeTo(fileName);
	}
}
