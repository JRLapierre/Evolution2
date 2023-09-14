package algorithm.running_choice;

/**
 * This interface allows us to choose how to run the simulation : <br>
 * No matter what we choose, we will always be able to manually stop the simulation. <br>
 * If we want to run for an undetermined amount of time, there is the DefaultRunning 
 * class. <br>
 * If we want to run for a set time, there is the RunningTime class. <br>
 * If we want to run for a set amount of iterations, there is the RunningIteration 
 * class. <br>
 */
public interface RunningChoice {

	/**
	 * This method will check if the program must stop or continue.
	 * @return true if we continue to run, false to stop.
	 */
	public boolean runningCondition();
}
