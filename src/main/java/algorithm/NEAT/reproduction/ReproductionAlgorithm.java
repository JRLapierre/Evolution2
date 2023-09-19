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
	protected Random random = new Random();
	
	/**
	 * a value to choose if we do the changeRandomLinkFactor mutation or not. 
	 * If the value is at 0, the mutation will never append. Otherwise, the higher the 
	 * value, the less likely the mutation is to happend.
	 */
	private int changeLinkFactor;
	
	/**
	 * In case of changeRandomLinkFactor mutation, this is the maximum (and the minimum)
	 * that the change of factor can be.
	 */
	private float minMaxChange = 0.000000001f;
	
	/**
	 * a value to choose if we do the changeRandomLinkExtremity mutation or not. 
	 * If the value is at 0, the mutation will never append. Otherwise, the higher the 
	 * value, the less likely the mutation is to happend.
	 */
	private int changeLinkExtremity;
	
	/**
	 * a value to choose if we do the addRandomNode mutation or not. 
	 * If the value is at 0, the mutation will never append. Otherwise, the higher the 
	 * value, the less likely the mutation is to happend.
	 */
	private int addNode;
	
	/**
	 * a value to choose if we do the addRandomLink mutation or not. 
	 * If the value is at 0, the mutation will never append. Otherwise, the higher the 
	 * value, the less likely the mutation is to happend.
	 */
	private int addLink;
	
	/**
	 * In case of addRandomLink mutation, this is the maximum (and the minimum) that the 
	 * factor can be.
	 */
	private float minMaxFactor = 0.00000001f;
	
	/**
	 * a value to choose if we do the deleteRandomNode mutation or not. 
	 * If the value is at 0, the mutation will never append. Otherwise, the higher the 
	 * value, the less likely the mutation is to happend.
	 */
	private int deleteNode;
	
	/**
	 * a value to choose if we do the deleteRandomLink mutation or not. 
	 * If the value is at 0, the mutation will never append. Otherwise, the higher the 
	 * value, the less likely the mutation is to happend.
	 */
	private int deleteLink;
	
	/***********************************************************************************/
	/* 									setters                                        */
	/***********************************************************************************/
	
	/**
	 * setter for the changeLinkFactor mutation. <br>
	 * Negative values are not allowed.
	 * @param changeLinkFactor the bigger the number, the less frequentely the mutation
	 * will append.
	 * @param minMaxChange In case of mutation, this is the maximum (and the minimum)
	 * that the change of factor can be.
	 */
	public void setChangeLinkFactor(int changeLinkFactor, float minMaxChange) {
		this.changeLinkFactor = (changeLinkFactor < 0) ? 0 : changeLinkFactor;
		this.minMaxChange = (minMaxChange < 0) ? this.minMaxChange : minMaxChange;
	}
	
	/**
	 * setter for the changeLinkExtremity mutation. <br>
	 * Negative values are not allowed.
	 * @param changeLinkExtremity the bigger the number, the less frequentely the mutation
	 * will append.
	 */
	public void setChangeLinkExtremity(int changeLinkExtremity) {
		this.changeLinkExtremity = (changeLinkExtremity < 0) ? 0 : changeLinkExtremity;
	}
	
	/**
	 * setter for the addNode mutation. <br>
	 * Negative values are not allowed.
	 * @param addNode the bigger the number, the less frequentely the mutation
	 * will append.
	 */
	public void setAddNode(int addNode) {
		this.addNode = (addNode < 0) ? 0 : addNode;
	}
	
	/**
	 * setter for the addLink mutation. <br>
	 * Negative values are not allowed.
	 * @param addLink the bigger the number, the less frequentely the mutation
	 * will append.
	 * @param minMaxFactor In case of mutation, this is the maximum (and the minimum)
	 * that the factor can be.
	 */
	public void setAddLink(int addLink, float minMaxFactor) {
		this.addLink = (addLink < 0) ? 0 : addLink;
		this.minMaxFactor = (minMaxFactor < 0) ? this.minMaxFactor : minMaxFactor;
	}
	
	/**
	 * setter for the deleteNode mutation. <br>
	 * Negative values are not allowed.
	 * @param deleteNode the bigger the number, the less frequentely the mutation
	 * will append.
	 */
	public void setDeleteNode(int deleteNode) {
		this.deleteNode = (deleteNode < 0) ? 0 : deleteNode;
	}
	
	/**
	 * setter for the deleteLink mutation. <br>
	 * Negative values are not allowed.
	 * @param deleteLink the bigger the number, the less frequentely the mutation
	 * will append.
	 */
	public void setDeleteLink(int deleteLink) {
		this.deleteLink = (deleteLink < 0) ? 0 : deleteLink;
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
	 * this function allows us to make the mutations according to our choices.
	 * @param brain the brain to modify
	 */
	protected void mutate(Brain brain) {
		//link factor
		if (changeLinkFactor != 0 && random.nextInt(changeLinkFactor) == 0) 
			brain.changeRandomLinkFactor(minMaxChange);
		//link extremity
		if (changeLinkExtremity != 0 && random.nextInt(changeLinkExtremity) == 0) 
			brain.changeRandomLinkExtremity();
		//add node
		if (addNode != 0 && random.nextInt(addNode) == 0) 
			brain.addRandomNode();
		//add link
		if (addLink != 0 && random.nextInt(addLink) == 0) 
			brain.addRandomLink(minMaxFactor);
		//delete node
		if (deleteNode != 0 && random.nextInt(deleteNode) == 0) 
			brain.deleteRandomNode();
		//delete link
		if (deleteLink != 0 && random.nextInt(deleteLink) == 0) 
			brain.deleteRandomLink();
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
