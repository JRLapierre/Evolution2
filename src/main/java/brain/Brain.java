package brain;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Random;

import brain.mutation.Mutation;

/**
 * This abstract class will allow to have common methods for all of the brains.
 * 
 * @author jrl
 *
 */
public abstract class Brain {
	
	/***********************************************************************************/
	/* 									variables                                      */
	/***********************************************************************************/
	
	/**
	 * the source of random numbers for the mutations.
	 */
	protected Random random = new Random();
	
	/**
	 * boolean to decide if we track the mutations or not.
	 */
	protected static boolean traceMutation = false;
	
	/**
	 * list containing the mutations affecting the brain
	 */
	protected LinkedList<Mutation> mutations = new LinkedList<>();
	
	/***********************************************************************************/
	/* 							    concrete methods                                   */
	/***********************************************************************************/
	
	/**
	 * setter for the trace of the mutation.
	 * @param trace true if we want to track the mutation, false otherwise.
	 */
	public static void setTraceMutation(boolean trace) {
		Brain.traceMutation = trace;
	}
	
	// method to recreate brains from savings --------------------------------------------

	/**
	 * method to recrate a brain from binary informations
	 * @param bb the ByteBuffer containing the informations
	 * @return the brain described in the ByteBuffer.
	 */
	public static Brain restore(ByteBuffer bb) {
		byte type = bb.get();
		switch (type) {
		case (1):
			return new FlexibleBrain(bb);
		case (2):
			return new LayeredBrain(bb);
		default:
			System.err.println("error while recuperating the datas : the type " + type 
					+ " is unrecognized");
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * bit of code that allows to recreate the trace of the mutations from the binary 
	 * data. Used in the constructors.
	 * @param bb the ByteBuffer containing the informations
	 */
	protected void restoreMutation(ByteBuffer bb) {
		short nbMutations = bb.getShort();
		for (int i = 0; i < nbMutations; i++) {
			this.mutations.add(Mutation.restore(bb));
		}
	}
	
	//method to dublicate a brain -------------------------------------------------------
	
	/**
	 * Create a copy of the current brain.
	 * @return A new brain reproducing the previous one
	 */
	public Brain duplicate() {
		if (this instanceof FlexibleBrain flexibleBrain)
			return new FlexibleBrain(flexibleBrain);
		if (this instanceof LayeredBrain layeredBrain)
			return new LayeredBrain(layeredBrain);
		return null;
	}
	
	/***********************************************************************************/
	/*                              mutations                                          */
	/***********************************************************************************/
	/**
	 * these method may not work for all kind of brains
	 */
	
	//changement mutations --------------------------------------------------------------
	/**
	 * method that change the factor of a random link
	 * @param minMaxChange the limit for the changement of factor of the link.
	 * The changement will be between -minMaxChange and +minMaxChange.
	 */
	public abstract void changeRandomLinkFactor(float minMaxChange);
	
	/**
	 * method that change the node at an extremity of a random link
	 */
	public abstract void changeRandomLinkExtremity();
	
	//addition mutations ----------------------------------------------------------------
	/**
	 * method that add randomely a node
	 */
	public abstract void addRandomNode();
	
	/**
	 * method that add a random link
	 * @param minMaxValue the limit for the value of the link.
	 * The factor will be between -minMaxFactor and +minMaxFactor.
	 */
	public abstract void addRandomLink(float minMaxFactor);
	
	//deletions mutations --------------------------------------------------------------
	/**
	 * method that delete a random node
	 */
	public abstract void deleteRandomNode();
	
	/**
	 * method that delete a random link
	 */
	public abstract void deleteRandomLink();
	
	/***********************************************************************************/
	/* 							     other functions                                   */
	/***********************************************************************************/
	
	// method of computing --------------------------------------------------------------
	
	/**
	 * main method of the brain. 
	 * take an array with the same size as the inputs and gives the resutls by the brain.
	 * @param inputs an array containing the values to inject in the brain
	 * @return an array of outputs
	 */
	public abstract float[] compute(float[] inputs);
	
	// method of saving -----------------------------------------------------------------
	
	/**
	 * this method generate an array of bytes that allow to save the content of the brain.<br>
	 * <br>
	 * For a FlexibleBrain : <br>
 	 * length of the data : 11 + 10 * this.links.size()<br>
	 * 1 : kind of brain (1 for FlexibleBrain)<br>
	 * 2 : length of the inputs array<br>
	 * 2 : length of the outputs array<br>
	 * 2 : length of the hidden array<br>
	 * 4 : number of links<br>
	 * for each link : (10 bytes each)<br>
	 * 1 : number of the containing array of the origin node(1 for inputs, 2 for outputs, 3 for hidden)<br>
	 * 2 : position of the origin node in the array<br>
	 * 1 : number of the containing array of the target node(1 for inputs, 2 for outputs, 3 for hidden)<br>
	 * 2 : position of the target node in the array<br>
	 * 4 : factor of the link<br>
	 * <br>
	 * For a LayeredBrain : <br>
	 * 1 : kind of brain (2 for LayeredBrain)<br>
	 * 2 : number of layers (total) <br>
	 * for each layer : <br>
	 * 2 : number of nodes <br>
	 * for each link : <br>
	 * 4 : multiplicative factor <br>
	 * <br>
	 * At the end of each brain, there can be data about mutations. If it is the case, 
	 * the remaining bytes will be : <br>
	 * 2 : the number of mutations<br>
	 * for each mutation<br>
	 * between 3 and 17 bytes (determined by the toByte of Mutation)<br>
	 * @return an array of bytes giving information on the brain
	 */
	public abstract byte[] toBytes();
	
	/**
	 * If we need to save the mutations, this function allows us to add the bytes 
	 * describing the mutations to the ByteBuffer. <br>
	 * This function requires the ByteBuffer to have enougth space to store all the data
	 * about the mutations and changes the ByteBuffer to get rid of the excess length.
	 * @param bb The ByteBuffer containing the informations about the rest of the brain
	 */
	protected void toByteMutation(ByteBuffer bb) {
		//the number of mutations
		bb.putShort((short) this.mutations.size());
		for(Mutation mutation : this.mutations) {
			bb.put(mutation.toByte());
		}
		//limit the size of the array
	    // Create a new byte array with the exact size of the data
	    byte[] result = new byte[bb.position()];
	    bb.flip(); // Prepare ByteBuffer for reading
	    bb.get(result); // Copy data to the result array
	}
}
