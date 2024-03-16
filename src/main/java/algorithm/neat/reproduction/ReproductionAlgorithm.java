package algorithm.neat.reproduction;

import java.nio.ByteBuffer;

import algorithm.neat.Individual;
import brain.MutationManager;
import tools.RandomManager;

/**
 * This class allow us to have a choice of algorithm for the reproduction.
 * @author jrl
 *
 */
public abstract class ReproductionAlgorithm {
	
	/***********************************************************************************/
	/* 									variables                                      */
	/***********************************************************************************/
	
	/**
	 * The mutationManager to manage the mutations happening during the reproduction 
	 * process. 
	 */
	protected MutationManager mutationManager = new MutationManager();
	
	/**
	 * The name of the algorithm used.
	 */
	public final String algorithmName = this.getClass().getSimpleName();
		
	/***********************************************************************************/
	/* 								 	 methods                                       */
	/***********************************************************************************/
	
	public MutationManager getMutationManager() {
		return this.mutationManager;
	}
	
	/**
	 * This function identify the type of reproduction algorithm and return the
	 * corresponding algorithm.
	 * @param bb the ByteBuffer containing the informations.
	 * @return the ReproductionAlgorithm registered.
	 */
	public static ReproductionAlgorithm restore(ByteBuffer bb) {
		ReproductionAlgorithm algo;
		byte type = bb.get();
		switch(type) {
		case (1)://elitism
			algo = new Elitism(bb);
			break;
		case (2): //roulette selection
			algo = new RouletteSelection(bb);
			break;
		default : 
			return null;
		}
		algo.mutationManager = MutationManager.restore(bb);
		return algo;
	}
	
	/**
	 * This function allows to generate the next generation after the evaluation of the
	 * old one. <br>
	 * The population must be sorted by desending order of score.
	 * @param population the evaluated population used to generate the next generation
	 * @return the new generation
	 */
	public abstract Individual[] reproduce(Individual[] population);
	
	/**
	 * this method generate an array of bytes that allow to save the content of the 
	 * reproduction algorithm.<br>
	 * <br>
	 * For Elitism : <br>
	 * length of the data : 45 <br>
	 * 1 : kind of algorithm (always 1) <br>
	 * 4 : number of perfect clones <br>
	 * 4 : number of mutated clones <br>
	 * 4 : limit of rank for the reproduction <br>
	 * <br>
	 * For RouletteSelection : <br>
	 * length of the data : 45 <br>
	 * 1 : kind of algorithm (always 2) <br>
	 * 4 : number of perfect clones <br>
	 * 4 : number of mutated clones <br>
	 * 4 : limit of rank for the reproduction <br>
	 * <br>
	 * @return an array containing the necessary informations to recreate the object.
	 */
	public abstract byte[] toByte();
	

	

}
