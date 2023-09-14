package algorithm.autosave;

/**
 * This class allows to save every x time (in milliseconds).
 * @author jean-remi.lapierre
 *
 */
public class SaveTime implements AutosaveCondition {
	
	/**
	 * The time (in milliseconds) between each autosaves
	 */
	private long saveTime;
	
	/**
	 * the moment where the simulation started to run
	 */
	long lastSaveTime = System.currentTimeMillis();
	
	/**
	 * Constructor.
	 * @param saveTime the time (in milliseconds) for the simulation to autosave.
	 */
	public SaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	@Override
	public boolean saveCondition() {
		boolean result = lastSaveTime + saveTime < System.currentTimeMillis();
		lastSaveTime = System.currentTimeMillis();
		return result;
	}

}
