package algorithm.autosave;

/**
 * This interface allows us to know if we need to do a automatic save. <br>
 * If we don't want autosaves, we use the NoAutoSave class. <br>
 * If we want to save every x iterations, we use the SaveIteration class. <br>
 * If we want to save every x time, we use the SaveTime class.
 * @author jean-remi.lapierre
 *
 */
public interface AutosaveCondition {

	/**
	 * The condition to know if we need to do an autosave
	 * @return true if we need to save, false otherwise
	 */
	public boolean saveCondition();
}
