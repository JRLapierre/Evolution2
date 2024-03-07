package brain;

import java.nio.ByteBuffer;

/**
 * This class allows us to manage easyly the mutations
 * @author jrl
 *
 */
public class MutationManager {

	/***********************************************************************************/
	/* 									variables                                      */
	/***********************************************************************************/
	
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
	private float minMaxChange = Float.MIN_VALUE;
	
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
	private float minMaxFactor = Float.MIN_VALUE;
	
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
		this.minMaxChange = (minMaxChange < 0) ? Float.MIN_VALUE : minMaxChange;
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
		this.minMaxFactor = (minMaxFactor < 0) ? Float.MIN_VALUE : minMaxFactor;
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
	 * This function resores the mutations settings.
	 * @param bb the ByteBuffer containing the informations.
	 * @return the MutationManager saved.
	 */
	public static MutationManager restore(ByteBuffer bb) {
		MutationManager manager = new MutationManager();
		manager.setChangeLinkFactor(bb.getFloat(), bb.getFloat());
		manager.setChangeLinkExtremity(bb.getFloat());
		manager.setAddNode(bb.getFloat());
		manager.setAddLink(bb.getFloat(), bb.getFloat());
		manager.setDeleteNode(bb.getFloat());
		manager.setDeleteLink(bb.getFloat());
		return manager;
	}
	
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
	public void mutate(Brain brain) {
		this.guaranteedMutations(brain);
		this.randomMutations(brain);
	}
	
	/**
	 * This function puts in a byte array the mutations. <br>
	 * length of the data : 32 <br>
	 * 4 : the number of changement of the factor of a link that will happend. <br>
	 * 4 : the maximum changement to the factor of the link <br>
	 * 4 : the number of changement of an extremity of a link that will happend. <br>
	 * 4 : the number of nodes that will be added <br>
	 * 4 : the number of links that will be added <br>
	 * 4 : the maximum range of the value of the new link <br>
	 * 4 : the number of nodes that will be deleted <br>
	 * 4 : the number of links that will be deleted <br>
	 * @return an array of bytes containing the numbers configuring the mutations.
	 */
	public byte[] toByte() {
		ByteBuffer bb = ByteBuffer.allocate(32);
		bb.putFloat(this.nbChangesLinkFactor + this.chanceAdditionalChangeLinkFactor);
		bb.putFloat(minMaxChange);
		bb.putFloat(this.nbChangeLinkExtremity + this.chanceAdditionalChangeLinkExtremity);
		bb.putFloat(this.nbAddNode + this.chanceAdditionalAddNode);
		bb.putFloat(this.nbAddLink + this.chanceAdditionalAddLink);
		bb.putFloat(minMaxFactor);
		bb.putFloat(this.nbDeleteNode + this.chanceAdditionalDeleteNode);
		bb.putFloat(this.nbDeleteLink + this.chanceAdditionalDeleteLink);
		return bb.array();
	}
}
