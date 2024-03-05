package algorithm.NEAT.reproduction;

import java.nio.ByteBuffer;
import java.util.Random;

import algorithm.NEAT.Individual;
import brain.Brain;

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
	 * generator of random numbers
	 */
	protected static Random random = new Random();
	
	/**
	 * The name of the algorithm used.
	 */
	public final String algorithmName = this.getClass().getSimpleName();
	
	/**
	 * The number of guarandeed changement of factor of a link
	 */
	private int nbChangesLinkFactor;
	
	/**
	 * The probability of an additional changement of a factor of a link
	 */
	private float chanceAdditionalChangeLinkFactor;
	
	/**
	 * In case of changeRandomLinkFactor mutation, this is the maximum (and the minimum)
	 * that the change of factor can be.
	 */
	private float minMaxChange = Float.MIN_NORMAL;
	
	/**
	 * The number of guarandeed changement of an extremity of a link
	 */
	private int nbChangeLinkExtremity;
	
	/**
	 * The probability of an additional changement of an extremity of a link
	 */
	private float chanceAdditionalChangeLinkExtremity;
	
	/**
	 * The number of guarandeed additions of nodes
	 */
	private int nbAddNode;
	
	/**
	 * The probability of an additional addition of a node
	 */
	private float chanceAdditionalAddNode;
	
	/**
	 * The number of guarandeed additions of links
	 */
	private int nbAddLink;
	
	/**
	 * The probability of an additional addition of a link
	 */
	private float chanceAdditionalAddLink;
	
	/**
	 * In case of addRandomLink mutation, this is the maximum (and the minimum) that the 
	 * factor can be.
	 */
	private float minMaxFactor = Float.MIN_NORMAL;
	
	/**
	 * The number of guarandeed deletion of nodes
	 */
	private int nbDeleteNode;
	
	/**
	 * The probability of an additional deletion of a node
	 */
	private float chanceAdditionalDeleteNode;
	
	/**
	 * The number of guarandeed deletions of links
	 */
	private int nbDeleteLink;
	
	/**
	 * The probability of an additional deletion of a link
	 */
	private float chanceAdditionalDeleteLink;
	
	/***********************************************************************************/
	/* 									setters                                        */
	/***********************************************************************************/
	
	/**
	 * setter for the changeLinkFactor mutation. <br>
	 * Negative values are not allowed.
	 * @param nbChanges the number of mutation of that kind that will happend. <br>
	 * the integer part will be the number of times this mutation will certainely happend <br>
	 * the decimal part will be a probability of an additional mutation.
	 * @param minMaxChange In case of mutation, this is the maximum (and the minimum)
	 * that the change of factor can be.
	 */
	public void setChangeLinkFactor(float nbChanges, float minMaxChange) {
		this.nbChangesLinkFactor = (int) nbChanges;
		this.chanceAdditionalChangeLinkFactor = nbChanges % 1;
		this.minMaxChange = (minMaxChange < 0) ? this.minMaxChange : minMaxChange;
	}
	
	/**
	 * setter for the changeLinkExtremity mutation. <br>
	 * Negative values are not allowed.
	 * @param nbChanges the number of mutation of that kind that will happend. <br>
	 * the integer part will be the number of times this mutation will certainely happend <br>
	 * the decimal part will be a probability of an additional mutation.
	 */
	public void setChangeLinkExtremity(float nbChanges) {
		this.nbChangeLinkExtremity = (int) nbChanges;
		this.chanceAdditionalChangeLinkExtremity = nbChanges % 1;
	}
	
	/**
	 * setter for the addNode mutation. <br>
	 * Negative values are not allowed.
	 * @param nbChanges the number of mutation of that kind that will happend. <br>
	 * the integer part will be the number of times this mutation will certainely happend <br>
	 * the decimal part will be a probability of an additional mutation.
	 */
	public void setAddNode(float nbChanges) {
		this.nbAddNode = (int) nbChanges;
		this.chanceAdditionalAddNode = nbChanges % 1;
	}
	
	/**
	 * setter for the addLink mutation. <br>
	 * Negative values are not allowed.
	 * @param nbChanges the number of mutation of that kind that will happend. <br>
	 * the integer part will be the number of times this mutation will certainely happend <br>
	 * the decimal part will be a probability of an additional mutation.
	 * @param minMaxFactor In case of mutation, this is the maximum (and the minimum)
	 * that the factor can be.
	 */
	public void setAddLink(float nbChanges, float minMaxFactor) {
		this.nbAddLink = (int) nbChanges;
		this.chanceAdditionalAddLink = nbChanges % 1;
		this.minMaxFactor = (minMaxFactor < 0) ? this.minMaxFactor : minMaxFactor;
	}
	
	/**
	 * setter for the deleteNode mutation. <br>
	 * Negative values are not allowed.
	 * @param nbChanges the number of mutation of that kind that will happend. <br>
	 * the integer part will be the number of times this mutation will certainely happend <br>
	 * the decimal part will be a probability of an additional mutation.
	 */
	public void setDeleteNode(float nbChanges) {
		this.nbDeleteNode = (int) nbChanges;
		this.chanceAdditionalDeleteNode = nbChanges % 1;
	}
	
	/**
	 * setter for the deleteLink mutation. <br>
	 * Negative values are not allowed.
	 * @param nbChanges the number of mutation of that kind that will happend. <br>
	 * the integer part will be the number of times this mutation will certainely happend <br>
	 * the decimal part will be a probability of an additional mutation.
	 */
	public void setDeleteLink(float nbChanges) {
		this.nbDeleteLink = (int) nbChanges;
		this.chanceAdditionalDeleteLink = nbChanges % 1;
	}
	
	/***********************************************************************************/
	/* 								 	 methods                                       */
	/***********************************************************************************/
	
	/**
	 * This function identify the type of reproduction algorithm and return the
	 * corresponding algorithm.
	 * @param bb the ByteBuffer containing the informations.
	 * @return the ReproductionAlgorithm registered.
	 */
	public static ReproductionAlgorithm restore(ByteBuffer bb) {
		byte type = bb.get();
		ReproductionAlgorithm algo;
		switch(type) {
		case (1)://elitism
			algo = new Elitism(bb);
			algo.restoreMutations(bb);
			return algo;
		case (2): //roulette selection
			algo = new RouletteSelection(bb);
			algo.restoreMutations(bb);
			return algo;
		default:
			return null;
		}
	}
	
	/**
	 * This function resores the mutations of a reproductionAlgorithm.
	 * @param bb the ByteBuffer containing the informations.
	 */
	private void restoreMutations(ByteBuffer bb) {
		this.changeLinkFactor = bb.getInt();
		this.minMaxChange = bb.getFloat();
		this.changeLinkExtremity = bb.getInt();
		this.addNode = bb.getInt();
		this.addLink = bb.getInt();
		this.minMaxFactor = bb.getFloat();
		this.deleteNode = bb.getInt();
		this.deleteLink = bb.getInt();
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
	 * private method executing the guarenteed mutations in a brain
	 * @param brain the brain to modify
	 */
	private void guaranteedMutations(Brain brain) {
		for (int i = 0; i < this.nbChangesLinkFactor; i++) {
			brain.changeRandomLinkFactor(this.minMaxChange);
		}
		for (int i = 0; i < this.nbChangeLinkExtremity; i++) {
			brain.changeRandomLinkExtremity();
		}
		for (int i = 0; i < this.nbAddNode; i++) {
			brain.addRandomNode();
		}
		for (int i = 0; i < this.nbAddLink; i++) {
			brain.addRandomLink(this.minMaxFactor);
		}
		for (int i = 0; i < this.nbDeleteNode; i++) {
			brain.deleteRandomNode();
		}
		for (int i = 0; i < this.nbDeleteLink; i++) {
			brain.deleteRandomLink();
		}
	}
	
	/**
	 * Private method to execute the additional and rare mutations on a brain
	 * @param brain the brain to modify
	 */
	private void randomMutations(Brain brain) {
		//link factor
		if (this.chanceAdditionalChangeLinkFactor != 0 
				&& Math.random() < this.chanceAdditionalChangeLinkFactor) 
			brain.changeRandomLinkFactor(minMaxChange);
		//link extremity
		if (this.chanceAdditionalChangeLinkExtremity != 0 
				&& Math.random() < this.chanceAdditionalChangeLinkExtremity) 
			brain.changeRandomLinkExtremity();
		//add node
		if (this.chanceAdditionalAddNode != 0 
				&& Math.random() < this.chanceAdditionalAddNode) 
			brain.addRandomNode();
		//add link
		if (this.chanceAdditionalAddLink != 0 
				&& Math.random() < this.chanceAdditionalAddLink) 
			brain.addRandomLink(minMaxFactor);
		//delete node
		if (this.chanceAdditionalDeleteNode != 0 
				&& Math.random() < this.chanceAdditionalDeleteNode) 
			brain.deleteRandomNode();
		//delete link
		if (this.chanceAdditionalDeleteLink != 0 
				&& Math.random() < this.chanceAdditionalDeleteLink) 
			brain.deleteRandomLink();
	}
	
	/**
	 * this function allows us to make the mutations according to our choices.
	 * @param brain the brain to modify
	 */
	protected void mutate(Brain brain) {
		this.guaranteedMutations(brain);
		this.randomMutations(brain);
	}
	
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
	 * 32 : toByteMutations <br>
	 * <br>
	 * For RouletteSelection : <br>
	 * length of the data : 45 <br>
	 * 1 : kind of algorithm (always 2) <br>
	 * 4 : number of perfect clones <br>
	 * 4 : number of mutated clones <br>
	 * 4 : limit of rank for the reproduction <br>
	 * 32 : toByteMutations <br>
	 * <br>
	 * @return an array containing the necessary informations to recreate the object.
	 */
	public abstract byte[] toByte();
	
	/**
	 * This function puts in a byte array the mutations. <br>
	 * length of the data : 32 <br>
	 * 4 : changeLinkFactor <br>
	 * 4 : minMaxChange <br>
	 * 4 : changeLinkExtremity <br>
	 * 4 : addNode <br>
	 * 4 : addLink <br>
	 * 4 : minMaxFactor <br>
	 * 4 : deleteNode <br>
	 * 4 : deleteLink <br>
	 * @return an array of bytes containing the numbers for the mutations.
	 */
	protected byte[] toByteMutations() {
		ByteBuffer bb = ByteBuffer.allocate(32);
		bb.putInt(changeLinkFactor);
		bb.putFloat(minMaxChange);
		bb.putInt(changeLinkExtremity);
		bb.putInt(addNode);
		bb.putInt(addLink);
		bb.putFloat(minMaxFactor);
		bb.putInt(deleteNode);
		bb.putInt(deleteLink);
		return bb.array();
	}
	

}
