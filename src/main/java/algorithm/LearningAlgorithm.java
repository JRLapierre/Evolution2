package algorithm;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import algorithm.NEAT.NEATAlgorithm;

/**
 * This class is the root for all learning algorithm. It manages the display and 
 * centralize some methods.
 * @author jrl
 *
 */
public abstract class LearningAlgorithm extends Thread {
	
	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
	/**
	 * the name (and the path) of the folder that will contain the . <br>
	 * By default, the name will be "saves/NEATAlgorithm_" + the current date.
	 */
	protected String registrationFolder;
	
	/**
	 * Instance of a functionnal interface to evaluate the population
	 */
	protected Evaluation evaluation;
	
	/**
	 * This boolean is true as long as the simulation doesn't end. <br>
	 * It is swiched to false when we want to stop the simulation.
	 */
	private boolean running = true;
	
	/**
	 * This boolean determines if the simulation is in pause or not. <br>
	 * It is set to true when the simulation is paused.
	 */
	private boolean pause = true;
	
	/***********************************************************************************/
	/*                             control methods                                     */
	/***********************************************************************************/
	
	/**
	 * Method that allows us to pause and resume the program.
	 */
    public void playPause() {
        if(pause) resumeProgram();
        else pause=true;
    }

    /**
     * Method that allows us to resume the program.
     */
    private synchronized void resumeProgram() {
        pause = false;
        notifyAll();
    }

    /**
     * Method that allows us to kill the program.
     */
    public synchronized void endProgram() {
        running = false;
        notifyAll();
    }
    
    /**
     * method that let us know if the simulation is paused.
     * @return true if the simulation is paused, false otherwise.
     */
    public boolean isPaused() {
    	return pause;
    }
    
    /**
     * method that keep waiting as long as the simuation is paused.
     */
	private synchronized void pausing() {
        while (pause && running) {
        	try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
        }
	}
	
	/***********************************************************************************/
	/*                                main methods                                     */
	/***********************************************************************************/
	
	/**
	 * method to restore a learning algorithm from a save
	 * @param folder the folder containing the save
	 * @param evaluation the lambda expression
	 * @return the learninig algorithm
	 */
	public static LearningAlgorithm restore(String folder, Evaluation evaluation) {
		byte[] settingsFile;
		try {
			settingsFile = Files.readAllBytes(Paths.get(folder + "/settings.bin"));
			ByteBuffer bb = ByteBuffer.wrap(settingsFile);
			byte type = bb.get();
			LearningAlgorithm algorithm;
			switch (type) {
			case (1):
				algorithm = new NEATAlgorithm(folder, bb);
				algorithm.evaluation = evaluation;
				algorithm.registrationFolder = folder;
				return algorithm;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Setter to change the saves folder. If the name is incorrect, the folder will not 
	 * be changed.
	 * @param newFolder The new folder (and his path) where the saves will be.
	 */
	public void setRegistrationFolderName(String newFolder) {
		File folderFile = new File(newFolder);
        if (folderFile.isAbsolute() || !Paths.get(newFolder).isAbsolute()) {
        	this.registrationFolder = newFolder;
        }
	}
	
	/**
	 * method that saves the current state of the simulation
	 */
	protected abstract void save();
	
	/**
	 * method who takes the learning to the next step
	 */
	protected abstract void next();
	
	@Override
	public void run() {
		while (running) {
			if (pause) {
				this.save();
				this.pausing();
				if (!running) return;
			}
			this.next();
		}
		//at the end
		this.save();
	}
	
	//How do I register the intermediary states ?

	//function that run a certain number of iterations ?
	
	//function that run a certain time ?
	
	//How do I take back a simulation from a save ?
	
}
