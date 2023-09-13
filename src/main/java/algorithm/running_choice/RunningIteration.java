package algorithm.running_choice;

/**
 * This class represents the choice of running the simulation for a set amount of 
 * iterations. This will automatically end when the iterations are done.
 */
public class RunningIteration implements RunningChoice {
	
	/**
	 * the current number of iterations
	 */
	private int nbIterations = 0;
	
	/**
	 * the total number of iterations to have
	 */
	private int totalIterations;
	
	/**
	 * Constructor.
	 * @param totalIterations the total number of iterations to have
	 */
	public RunningIteration(int totalIterations) {
		this.totalIterations = totalIterations;
	}
	
	@Override
	public boolean runningCondition() {
		nbIterations ++;
		return totalIterations >= nbIterations;
	}

}
