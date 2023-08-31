package algorithm;

import algorithm.NEAT.Individual;

/**
 * This functionnal interface allows us to evaluate a population and set the scores.
 * @author jrl
 *
 */
@FunctionalInterface
public interface Evaluation {

	/**
	 * This method is used to determine the score of each individual.
	 * @param population The population that will be evalated
	 */
	public void evaluate(Individual[] population);
	
}
