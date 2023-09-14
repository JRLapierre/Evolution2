package algorithm.running_choice;

import algorithm.LearningAlgorithm;

/**
 * This class represent the choice of running the simulation for a set amount of times. 
 * This will end after the time is passed. The time passed in pause will not count. <br>
 */
public class RunningTime implements RunningChoice {
	
	/**
	 * The concerned learning algorithm.
	 */
	private LearningAlgorithm algorithm;
	
	/**
	 * The time (in milliseconds) for the simulation to run.
	 */
	private long runningTime;
	
	/**
	 * the moment where the simulation started to run
	 */
	long beginningTime = System.currentTimeMillis();
	
	/**
	 * Constructor.
	 * @param algorithm the learning algorithm concerned
	 * @param runningTime the time (in milliseconds) for the simulation to run.
	 */
	public RunningTime(LearningAlgorithm algorithm, long runningTime) {
		this.runningTime = runningTime;
		this.algorithm = algorithm;
	}

	@Override
	public boolean runningCondition() {
		return System.currentTimeMillis() - beginningTime < runningTime + algorithm.getTimePaused();
	}

}
