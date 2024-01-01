package algorithm.NEAT;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

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
	 * id of the second parent of the individual. If the individual is a clone, the value
	 * is set at -1
	 */
	private int parent2Id = -1;
	
	/**
	 * brain of the individual
	 */
	private Brain brain;
	
	/**
	 * the score of an individual
	 */
	private float score = 0;
	
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
	 * Constructor to combine two individuals
	 * @param parent1 the main predecessor of the current individual
	 * @param parent2 the secondary predecessor of the current individual
	 */
	public Individual(Individual parent1, Individual parent2) {
		this.parentId = parent1.id;
		this.parent2Id = parent2.id;
		this.brain = Brain.combine(parent1.brain, parent2.brain);
		//generate the ID
		Individual.countId ++;
		this.id = Individual.countId;
	}
	
	/**
	 * Constructor from a saved file
	 * @param file the file containing the binary save
	 * @throws IOException if the file does not exists
	 */
	public Individual(File file) throws IOException {
		ByteBuffer bb = ByteBuffer.wrap(Files.readAllBytes(file.toPath()));
		this.id = bb.getInt();
		this.parentId = bb.getInt();
		this.parent2Id = bb.getInt();
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
	 * getter for the id of the second parent of the individual.
	 * @return the id of the second parent of the individual if the individual has two 
	 * parents, -1 otherwise.
	 */
	public int getParent2Id() {
		return this.parent2Id;
	}
	
	/**
	 * getter for a safe access to the brain.
	 * @return the brain
	 */
	public Brain getBrain() {
		return this.brain;
	}
	
	/**
	 * getter for the score
	 * @return the score of the individual
	 */
	public float getScore() {
		return this.score;
	}
	
	/**
	 * function that allows us to update the score of an individual easily.
	 * @param update the changement of score
	 */
	public void updateScore(float update) {
		this.score += update;
	}
	
	/**
	 * Function that generate the binary code to register an individual <br>
	 * the returning array contains : <br>
	 * 4 bytes for the id; <br>
	 * 4 bytes for the id of the parent; <br>
	 * 4 bytes for the id of the second parent; <br>
	 * the rest for the brain.
	 * @return a byte array containing the binary code
	 */
	public byte[] toByte() {
		byte[] bytesBrain = this.brain.toBytes();
		ByteBuffer bb = ByteBuffer.allocate(12 + bytesBrain.length);
		bb.putInt(id);
		bb.putInt(parentId);
		bb.putInt(parent2Id);
		bb.put(bytesBrain);
		return bb.array();
		
	}

}
