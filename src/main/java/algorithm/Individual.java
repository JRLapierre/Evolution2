package algorithm;

import java.nio.ByteBuffer;

import brain.Brain;

/**
 * This class encapsulate the brain for the NEAT algorithm. <br>
 * This allows to add information.
 * @author jrl
 *
 */
public class Individual {
	
	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
	/**
	 * static number that allows to attribute unique ids
	 */
	private static int countId;
	
	/**
	 * unique id of the individual
	 */
	private int id;
	
	/**
	 * id of the parent of the individual
	 */
	private int parentId;
	
	/**
	 * brain of the individual
	 */
	private Brain brain;
	
	/***********************************************************************************/
	/*                              static functions                                   */
	/***********************************************************************************/
	
	/**
	 * setter for the id count. The new ids will be generated from this number
	 * @param value the new starting value
	 */
	public static void setCountId(int value) {
		Individual.countId = value;
	}
	
	/**
	 * getter for the count of ids
	 * @return the last id given
	 */
	public static int getCountId() {
		return Individual.countId;
	}
	
	/***********************************************************************************/
	/*                               constructors                                      */
	/***********************************************************************************/
	
	/**
	 * Constructor for a root individual.<br>
	 * this individual has no parent and will be a root for the next generations
	 * @param brain the brain of the individual
	 */
	public Individual(Brain brain) {
		this.brain = brain;
		//generate the ID
		Individual.countId ++;
		this.id = Individual.countId;
	
	}
	
	/**
	 * Constructor for basic reproduction.
	 * @param parent the predecessor of the current individual.
	 */
	public Individual(Individual parent) {
		this.parentId = parent.id;
		this.brain = parent.brain.duplicate();
		//generate the ID
		Individual.countId ++;
		this.id = Individual.countId;
	}
	
	/**
	 * Constructor from a save
	 * @param bb the ByteBuffer containing the informations
	 */
	public Individual(ByteBuffer bb) {
		this.id = bb.getInt();
		this.parentId = bb.getInt();
		this.brain = Brain.restore(bb);
	}
	
	/***********************************************************************************/
	/*                                 methods                                         */
	/***********************************************************************************/
	
	/**
	 * getter to get the id of the individual
	 * @return the id of the individual
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * getter for the id of the parent of the individual
	 * @return the id of the parent of the individual
	 */
	public int getParentId() {
		return this.parentId;
	}
	
	/**
	 * getter for a safe access to the brain.
	 * @return the brain
	 */
	public Brain getBrain() {
		return this.brain;
	}
	
	/**
	 * Function that generate the binary code to register an individual
	 * @return a byte array containing the binary code
	 */
	public byte[] toByte() {
		byte[] bytesBrain = this.brain.toBytes();
		ByteBuffer bb = ByteBuffer.allocate(8 + bytesBrain.length);
		bb.putInt(id);
		bb.putInt(parentId);
		bb.put(bytesBrain);
		return bb.array();
		
	}

}
