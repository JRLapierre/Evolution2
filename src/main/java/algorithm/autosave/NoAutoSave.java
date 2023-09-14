package algorithm.autosave;

/**
 * This class is for the case without any autosave. The method saveCondition() will 
 * always return false.
 * @author jean-remi.lapierre
 *
 */
public class NoAutoSave implements AutosaveCondition {

	@Override
	public boolean saveCondition() {
		return false;
	}

}
