package algorithm.running_choice;

/**
 * This class represents the default choice of running a simulation : the only way to
 * stop is by clicking the stop button or to kill the program.
 */
public class DefaultRunning implements RunningChoice {

	@Override
	public boolean runningCondition() {
		return true;
	}

}
