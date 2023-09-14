package algorithm.autosave;

/**
 * This class allows us to activate the autosave every x iterations. <br>
 * @author jean-remi.lapierre
 *
 */
public class SaveIteration implements AutosaveCondition{
	
	/**
	 * The total number of iterations of the simulation
	 */
	private int totalIterations = 0;
	
	/**
	 * the number of iterations needed to make an autosave
	 */
	private int nbIterations;
	
	/**
	 * Constructor.
	 * @param nbIterations the number of iterations needed to make an autosave
	 */
	public SaveIteration(int nbIterations) {
		this.nbIterations = nbIterations;
	}

	@Override
	public boolean saveCondition() {
		totalIterations ++;
		return totalIterations % nbIterations == 0;
	}
	
	
}
