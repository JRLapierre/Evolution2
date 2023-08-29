package algorithm.NEAT;

import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import brain.Brain;

/**
 * This class is the basis for a NEAT algorithm.
 * @author jrl
 *
 */
public abstract class NEATAlgorithm {
	
	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
	/**
	 * an array containing the population
	 */
	private Individual[] population;
	
	/**
	 * chosen reproduction algorithm
	 */
	private ReproductionAlgorithm reproductionAlgorithm;
	
	/***********************************************************************************/
	/*                               constructors                                      */
	/***********************************************************************************/
	
	protected NEATAlgorithm(Brain initialBrain, ReproductionAlgorithm reproductionAlgorithm) {
		Individual original = new Individual(initialBrain);
		this.population = new Individual[] {original};
		this.reproductionAlgorithm = reproductionAlgorithm;
	}
	
	/***********************************************************************************/
	/*                                 methods                                         */
	/***********************************************************************************/
	
	/**
	 * getter for the reproduction algorithm
	 * @return the reproduction algorithm
	 */
	public ReproductionAlgorithm getReproductionAlgorithm() {
		return this.reproductionAlgorithm;
	}
	
	/**
	 * this function manages the selection and the mutation for the next generation.
	 */
	private void reproduce() {
		this.population = reproductionAlgorithm.reproduce(population);
	}
	
	/**
	 * This function allows us to evaluate the population.
	 */
	protected abstract void evaluate();
	
	//TODO something to register the generation
	public void registerGeneration(String targetFolder) {
		//create a folder
		//register the individuals in this folder
	}
	
	//TODO something to register simulation parameters
	public void registerInformations(String targetFolder) {
		//register in a file
	}
	
	//main algorithm
	public void run() {
		
		//in a loop
		this.evaluate();
		this.reproduce();
	}
	

}
