package algorithm.NEAT.reproduction;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

import algorithm.NEAT.Individual;

/**
 * This class represents the roulette selection algorithm. <br>
 * The selection will be partially random : the higher the score, the more likely the 
 * individual will be selected.
 * @author jrl
 *
 */
public class RouletteSelection extends ReproductionAlgorithm {

	/***********************************************************************************/
	/* 									variables                                      */
	/***********************************************************************************/
	
	/**
	 * the number of perfect clones for the next generation
	 */
	private int nbPerfectClones;
	
	/**
	 * the number of mutated clones for the next generation
	 */
	private int nbMutatedClones;
	
	/**
	 * the number of children for the next generation
	 */
	private int nbCombinedChildren;
	
	/**
	 * the rank of the last individual that can be selected for the next generation.
	 */
	private int limit;
	
	/***********************************************************************************/
	/* 								   constructor                                     */
	/***********************************************************************************/
	
	/**
	 * Constructor for an roulette selection.
	 * @param nbPerfectClones the number of perfect clones for the next generation
	 * @param nbMutatedClones the number of mutated clones for the next generation
	 * @param nbCombinedChildren the number of children of combinaison for the next
	 * generation.
	 * @param limit the rank of the last individual that can be selected for the next 
	 * generation.
	 */
	public RouletteSelection(int nbPerfectClones, int nbMutatedClones, int nbCombinedChildren, int limit) {
		this.nbPerfectClones = nbPerfectClones;
		this.nbMutatedClones = nbMutatedClones;
		this.nbCombinedChildren = nbCombinedChildren;
		this.limit = limit;
	}
	
	/**
	 * Constructor to restore from a save.
	 * @param bb The ByteBuffer containing the informations
	 */
	protected RouletteSelection(ByteBuffer bb) {
		this.nbPerfectClones = bb.getInt();
		this.nbMutatedClones = bb.getInt();
		this.nbCombinedChildren = bb.getInt();
		this.limit = bb.getInt();
	}
	
	
	/***********************************************************************************/
	/* 									 methods                                       */
	/***********************************************************************************/
	
	/**
	 * This method allows us to select an individual in the population.
	 * @param population the population from witch we pick
	 * @param sumScore the sum of the scores of the selected individuals
	 * @return the selected individual
	 */
	private Individual pickIndividual(Individual[] population, float sumScore) {
		float randomNumber = random.nextFloat(sumScore);
		float localSum = 0;
		for (Individual individual : population) {
			localSum += individual.getScore();
			if (localSum > randomNumber) return individual;
		}
		return population[0];//should never happend
	}
	
	@Override
	public Individual[] reproduce(Individual[] population) {
		Arrays.sort(population, Comparator.comparingDouble(Individual::getScore).reversed());
		Individual[] newPopulation = new Individual[this.nbPerfectClones + this.nbMutatedClones + this.nbCombinedChildren];
		//ajust the score to only have positive scores (the lowest score will become 1)
		float changeScore = population[population.length-1].getScore() * (-1) + 1;
		for (Individual individual : population) individual.updateScore(changeScore);
		//calculate the sum of the scores of the allowed ones
		float sumScore = 0;
		for (int i = 0; i < population.length && i != this.limit; i++) {
			sumScore += population[i].getScore();
		}
		//perfect clones
		for (int i = 0; i < this.nbPerfectClones; i++) {
			newPopulation[i] = new Individual(pickIndividual(population, sumScore));
		}
		//mutated clones
		for (int i = 0; i < this.nbMutatedClones; i++) {
			newPopulation[nbPerfectClones + i] = new Individual(pickIndividual(population, sumScore));
			mutate(newPopulation[nbPerfectClones + i].getBrain());
		}
		//combined childrens
		for (int i = 0; i < this.nbCombinedChildren; i++) {
			newPopulation[nbPerfectClones + nbMutatedClones + i] = new Individual(
					pickIndividual(population, sumScore), 
					pickIndividual(population, sumScore));
			mutate(newPopulation[nbPerfectClones + nbMutatedClones + i].getBrain());			
		}
		
		return newPopulation;
	}

	@Override
	public byte[] toByte() {
		//1 (type) + 16 (class parameters) + 32 (mutation parameters)
		ByteBuffer bb = ByteBuffer.allocate(49);
		bb.put((byte) 2); //2 for Roulette selection
		bb.putInt(nbPerfectClones);
		bb.putInt(nbMutatedClones);
		bb.putInt(nbCombinedChildren);
		bb.putInt(limit);
		bb.put(toByteMutations());
		return bb.array();
	}

}
