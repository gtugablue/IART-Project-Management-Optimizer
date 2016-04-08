package solver.genetic_algorithm;

public class Algorithm {

	private double mutationRate;
	private boolean elitism;
	
	public Algorithm(double mutationRate, boolean elitism){
		this.mutationRate = mutationRate;
		this.elitism = elitism;
	}
	
	public double getMutationRate(){
		return mutationRate;
	}
	
	public boolean getElitism(){
		return elitism;
	}
	
	/**
	 * Create a new population with 
	 * @param oldPopulation
	 * @return Population
	 */
	private Population evolution(Population oldPopulation){
		
		Chromosome fittest;
		
		if(elitism)
			fittest = oldPopulation.getFittest();
		else
			fittest = oldPopulation.getRandom();
			
		Population newPopulation = new Population();
		
		newPopulation.populate(fittest);
		
		return newPopulation;
			
	}
	
	
}
