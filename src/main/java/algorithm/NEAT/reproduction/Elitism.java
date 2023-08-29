package algorithm.NEAT.reproduction;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

import algorithm.NEAT.Individual;

/**
 * This class represents a selection algorithm that makes x perfect clones of the x best
 * and y mutated clones of the y best.
 * @author jrl
 *
 */
public class Elitism extends ReproductionAlgorithm {
	
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
	 * the rank of the last individual that can be selected for the next generation.
	 */
	private int limit;
	
	/***********************************************************************************/
	/* 								   constructor                                     */
	/***********************************************************************************/
	
	/**
	 * Constructor for an elitist selection.
	 * @param nbPerfectClones the number of perfect clones for the next generation
	 * @param nbMutatedClones the number of mutated clones for the next generation
	 * @param limit the rank of the last individual that can be selected for the next 
	 * generation.
	 */
	public Elitism(int nbPerfectClones, int nbMutatedClones, int limit) {
		this.nbPerfectClones = nbPerfectClones;
		this.nbMutatedClones = nbMutatedClones;
		this.limit = limit;
	}
	
	/**
	 * Constructor to restore from a save.
	 * @param bb The ByteBuffer containing the informations
	 */
	protected Elitism(ByteBuffer bb) {
		this.nbPerfectClones = bb.getInt();
		this.nbMutatedClones = bb.getInt();
		this.limit = bb.getInt();
	}
	
	/***********************************************************************************/
	/* 									 methods                                       */
	/***********************************************************************************/
	
	@Override
	public Individual[] reproduce(Individual[] population) {
        Arrays.sort(population, Comparator.comparingDouble(Individual::getScore).reversed());
		Individual[] newPopulation = new Individual[this.nbPerfectClones + this.nbMutatedClones];
		//perfect clones
		for (int i = 0; i < this.nbPerfectClones; i++) {
			newPopulation[i] = new Individual(population[(i % this.limit) % population.length]);
		}
		//mutated clones
		for (int i = 0; i < this.nbMutatedClones; i++) {
			newPopulation[nbPerfectClones + i] = new Individual(population[(i % this.limit) % population.length]);
			mutate(newPopulation[nbPerfectClones + i].getBrain());			
		}
		
		return newPopulation;
	}
	
	@Override
	public byte[] toByte() {
		//1 (type) + 12 (class parameters) + 32 (mutation parameters)
		ByteBuffer bb = ByteBuffer.allocate(45);
		bb.put((byte) 1); //1 for Elitism
		bb.putInt(nbPerfectClones);
		bb.putInt(nbMutatedClones);
		bb.putInt(limit);
		bb.put(toByteMutations());
		return bb.array();
	}
}
