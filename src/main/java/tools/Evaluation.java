package tools;

import algorithm.NEAT.Individual;

/**
 * This functional interface allows us to evaluate a population and set the scores.
 * @author jrl
 *
 */
@FunctionalInterface
public interface Evaluation {

	/**
	 * This method is used to determine the score of each individual.
	 * @param population The population that will be evaluated
	 */
	public void evaluate(Individual[] population);
	
}
