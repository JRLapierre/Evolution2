package algorithm;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import algorithm.autosave.AutosaveCondition;
import algorithm.autosave.NoAutoSave;
import algorithm.autosave.SaveIteration;
import algorithm.autosave.SaveTime;
import algorithm.neat.NEATAlgorithm;
import algorithm.running_choice.*;
import tools.Evaluation;

/**
 * This class is the root for all learning algorithm. It manages the display and 
 * centralize some methods.
 * @author jrl
 *
 */
public abstract class LearningAlgorithm implements Runnable {
	
	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
	/**
	 * Instance of a functional interface to evaluate the population
	 */
	protected transient Evaluation evaluation;
	
	/**
	 * the name (and the path) of the folder that will contain the saves. <br>
	 */
	protected transient String registrationFolder;
	
	// control parameters ---------------------------------------------------------------
	
	/**
	 * Internal thread to control the program
	 */
	private transient Thread controlThread;
	
	/**
	 * the way to run the simulation.
	 */
	private transient RunningChoice runningChoice = new DefaultRunning();
	
	/**
	 * the condition for automatically saving the simulation.
	 */
	private transient AutosaveCondition autosaveCondition = new NoAutoSave();
	
	/**
	 * the time passed in pause.
	 */
	private transient long timePaused;
	
	/**
	 * This boolean is true as long as the simulation doesn't end. <br>
	 * It is swiched to false when we want to stop the simulation.
	 */
	private transient boolean running = true;
	
	/**
	 * This boolean determines if the simulation is in pause or not. <br>
	 * It is set to true when the simulation is paused.
	 */
	private transient boolean pause = true;
	

	/***********************************************************************************/
	/*                             control methods                                     */
	/***********************************************************************************/
	
	/**
	 * Method to launch the program
	 * @return true if the program was sucessfully lauched
	 */
    public synchronized boolean start() {
    	if (this.controlThread != null) return false;
        this.controlThread = new Thread(this);
        this.controlThread.start();
        return true;
    }
    
    /**
     * Method to stop and kill the current program
     * @return true if the program was successfully stoped.
     */
    public synchronized boolean stop() {
    	if (this.controlThread == null) return false;
        running = false;
        notifyAll();
	    this.controlThread.interrupt();
		this.controlThread = null;
		return true;
    }
	
    /**
     * Method that allows us to switch between a state of playing and a state of pausing
     * @return true if the program is now running
     */
    public synchronized boolean playPause() {
        if (pause) {
            pause = false;
            notifyAll();
        }
        else pause = true;
        return !pause;
    }
    
    /**
     * method that let us know if the simulation is paused.
     * @return true if the simulation is paused, false otherwise.
     */
    public boolean isPaused() {
    	return pause;
    }
    
    /**
     * method that keep waiting as long as the simulation is paused.
     */
	private synchronized void pausing() {
		long startPause = System.currentTimeMillis();
        while (pause && running) {
        	try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
        }
        this.timePaused += System.currentTimeMillis() - startPause;
	}
	
	/**
	 * getter for the time passed in pause.
	 * @return the time passed in pause
	 */
	public long getTimePaused() {
		return this.timePaused;
	}
	
	/***********************************************************************************/
	/*                                    setters                                      */
	/***********************************************************************************/
	
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
	 * Setter that will set the running mode to a set number of iterations.
	 * @param nbIterations the number of iterations that will be run.
	 */
	public void setNbIteration(int nbIterations) {
		this.runningChoice = new RunningIteration(nbIterations);
	}
	
	/**
	 * Setter that will set the running mode to a set time.
	 * @param time the time the simulation will have to run (in milliseconds)<br>
	 */
	public void setRunningTime(long time) {
		this.runningChoice = new RunningTime(this, time);
	}
	
	/**
	 * Setter that will set the simulation to automatically save every x iterations
	 * @param nbIterations the number of iterations necessary between saves
	 */
	public void setAutosaveIterations(int nbIterations) {
		this.autosaveCondition = new SaveIteration(nbIterations);
	}
	
	/**
	 * Setter that will set the simulation to automatically save every x milliseconds
	 * @param time the time between the autosaves
	 */
	public void setAutosaveTime(long time) {
		this.autosaveCondition = new SaveTime(time);
	}
	
	/***********************************************************************************/
	/*                                main methods                                     */
	/***********************************************************************************/
	
	/**
	 * method to restore a learning algorithm from a save
	 * @param folder the folder containing the save
	 * @param evaluation the lambda expression
	 * @return the learning algorithm
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
	 * method that saves the current state of the simulation
	 */
	protected abstract void save();
	
	/**
	 * method that saves informations about each iterations of the simulation.
	 */
	protected abstract void saveGenealogy();
	
	/**
	 * method who takes the learning to the next iteration
	 */
	protected abstract void next();
	
	/***********************************************************************************/
	/*                               running methods                                   */
	/***********************************************************************************/
	
	/**
	 * method to manage the pause and the stopping of the simulations. <br>
	 * @return true if the stop command has been given, false otherwise.
	 */
	private boolean managePauseStop() {
		if (pause || !running) {
			this.save();
			this.pausing();
			if (!running) return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		while (this.runningChoice.runningCondition()) {
			this.saveGenealogy();
			if (managePauseStop()) return;
			if (autosaveCondition.saveCondition()) this.save();
			this.next();
		}
		//at the end
		this.save();
		this.saveGenealogy();
		System.exit(0);
	}
	
	
}


