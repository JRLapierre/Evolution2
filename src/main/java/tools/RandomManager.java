package tools;

import java.util.Random;

/**
 * This class is a singleton designed to centralize and control the generation of random 
 * numbers.
 * 
 * @author jrl
 *
 */
public class RandomManager {

	/**
	 * unique instance of this class
	 */
	private static final RandomManager INSTANCE = new RandomManager();
	
    /**
     * The internal random object used for generating random numbers.
     */
    private final Random random = new Random();
	
    /**
     * Private constructor to prevent external instantiation.
     */
    private RandomManager() {}

    /**
     * Returns the singleton instance of RandomManager.
     * 
     * @return The singleton instance of RandomManager.
     */
    public static RandomManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed float value between 0.0 and 
     * 1.0 from this random number generator's sequence.
     * 
     * @return The next pseudorandom, uniformly distributed float value between 0.0 and 
     * 1.0.
     */
    public float nextFloat() {
        return random.nextFloat();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed float value between 0.0 and 
     * the specified bound from this random number generator's sequence.
     * 
     * @param bound The upper bound (exclusive) of the random float value to be returned.
     * @return The next pseudorandom, uniformly distributed float value between 0.0 and 
     * the specified bound.
     */
    public float nextFloat(float bound) {
        return random.nextFloat(bound);
    }

    /**
     * Returns a pseudorandom, uniformly distributed float value between the specified 
     * origin (inclusive) and the specified bound (exclusive).
     * 
     * @param origin The lower bound (inclusive) of the random float value to be returned.
     * @param bound The upper bound (exclusive) of the random float value to be returned.
     * @return A pseudorandom, uniformly distributed float value between the specified 
     * origin (inclusive) and the specified bound (exclusive).
     */
    public float nextFloat(float origin, float bound) {
        return random.nextFloat(origin, bound);
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and 
     * the specified bound (exclusive).
     * 
     * @param bound The upper bound (exclusive) of the random int value to be returned.
     * @return A pseudorandom, uniformly distributed int value between 0 (inclusive) and 
     * the specified bound (exclusive).
     */
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
    
    /**
     * Returns a pseudorandom, uniformly distributed int value between the specified 
     * origin (inclusive) and the specified bound (exclusive).
     * 
     * @param origin The lower bound (inclusive) of the random int value to be returned.
     * @param bound The upper bound (exclusive) of the random int value to be returned.
     * @return A pseudorandom, uniformly distributed int value between the specified 
     * origin (inclusive) and the specified bound (exclusive).
     */
    public int nextInt(int origin, int bound) {
    	return random.nextInt(origin, bound);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed boolean value from this 
     * random number generator's sequence.
     * 
     * @return The next pseudorandom, uniformly distributed boolean value.
     */
    public boolean nextBoolean() {
        return random.nextBoolean();
    }
	
	
}
