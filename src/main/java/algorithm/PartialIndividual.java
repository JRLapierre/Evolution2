package algorithm;

/**
 * This class is a simplified version of the Individual class used for the genealogy.
 * The partial individuals have no brain, so no score. Only the ids remain.
 * @author jrl
 *
 */
public class PartialIndividual {

	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
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
	
	/***********************************************************************************/
	/*                               constructor                                       */
	/***********************************************************************************/
	
	/**
	 * Constructor for a partial individual.
	 * @param id the id of the individual
	 * @param parentId the id of the parent of the individual
	 * @param parent2Id the id of the second parent of the individual.
	 */
	PartialIndividual(int id, int parentId, int parent2Id) {
		this.id = id;
		this.parentId = parentId;
		this.parent2Id = parent2Id;
	}
	
	/***********************************************************************************/
	/*                                 getters                                         */
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
	
}
