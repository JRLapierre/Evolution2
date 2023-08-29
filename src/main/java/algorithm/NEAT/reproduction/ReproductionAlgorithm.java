package algorithm.NEAT.reproduction;

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
	private Random random = new Random();
	
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
	/* 								 	 methods                                       */
	/***********************************************************************************/
	
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
		if (changeLinkFactor != 0 && random.nextInt(changeLinkFactor) == 1) 
			brain.changeRandomLinkFactor(minMaxChange);
		//link extremity
		if (changeLinkExtremity != 0 && random.nextInt(changeLinkExtremity) == 1) 
			brain.changeRandomLinkExtremity();
		//add node
		if (addNode != 0 && random.nextInt(addNode) == 1) 
			brain.addRandomNode();
		//add link
		if (addLink != 0 && random.nextInt(addLink) == 1) 
			brain.addRandomLink(minMaxFactor);
		//delete node
		if (deleteNode != 0 && random.nextInt(deleteNode) == 1) 
			brain.deleteRandomNode();
		//delete link
		if (deleteLink != 0 && random.nextInt(deleteLink) == 1) 
			brain.deleteRandomLink();
	}
	
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
	
	
	
}
