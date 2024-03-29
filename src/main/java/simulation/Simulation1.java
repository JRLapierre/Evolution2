package simulation;

import algorithm.LearningAlgorithm;
import algorithm.neat.Individual;
import algorithm.neat.NEATAlgorithm;
import algorithm.neat.reproduction.Elitism;
import algorithm.neat.reproduction.ReproductionAlgorithm;
import brain.Brain;
import brain.LayeredBrain;
import tools.View;
import tools.Evaluation;

/**
 * This is the first simulation I will do. This will serve as a simple test to see if 
 * everything works as expected. <br>
 * This will also serve as a tutorial for the different functionnalities.
 * @author jrl
 *
 */
public class Simulation1 {

	/**
	 * The name of the folder that will contains the saves. If you take back a simulation
	 * from a save, this is the folder in witch the data is stored.
	 */
	private static String folderName = "saves/simulation_test";
	
	/**
	 * This lambda expression serve to determine the fintess of each individuals. <br>
	 * In this example, the fitness is determined by the power of the output of the brain.
	 */
	private static Evaluation evaluation = population -> {
		float best = -1000f;
		float[] input = new float[] {1};
		float result;
		for (Individual individual : population) {
			result = individual.getBrain().compute(input)[0];
			individual.updateScore(result);
			if (result > best) best = result;
		}
		System.out.println(best);
	};
	
	/**
	 * This private function concentrates what is needed to start a new simulation from 
	 * zero.
	 * @return the object corresponding to the learning algorithm
	 */
	private static LearningAlgorithm setInitialSimulation() {
		/*
		 * choosing the reproduction algorithm and his settings.
		 * The two available algorithms are the elitism, who simply takes the best 
		 * individuals to make the next generation, and the roulette selection, who
		 * will pick semi-randomely the next generation, giving the individuals with the
		 * highest score the highest chance.
		 */
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
		/*
		 * choosing the default value for new brains.
		 * If we choose to work with LayeredBrain, we can call the following methods : 
		 *  - LayeredBrain.setDefaultLinkValue(value)
		 *  - LayeredBrain.setDefaultLinkVariation(value)
		 * If we choose to work with FlexibleBrain, we can call the following method :
		 *  - FlexibleBrain.setTimeToCompute(newTime)
		 * Check the documentation to get more information about those methods.
		 */
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(0.5f);
		/**
		 * creating the brain.
		 * We can choose between a FlexibleBrain and a LayeredBrain.
		 *  - The FlexibleBrain allows any two hidden node to be connected, allowing for 
		 *  complex behaviour. However, this kind of brain starts without any connection. 
		 * To fix this problem from the begining, we can use the 'addRandomLink' mutation.
		 *  - The LayeredBrain has a more rigid structure, more optimised but offering 
		 *  less possibilities. 
		 */
		Brain brain = new LayeredBrain(1,1,2,2);
		/*
		 * creation the learning algoritm.
		 * For now, only the NEAT algorithm is available.
		 */
		LearningAlgorithm algo = new NEATAlgorithm(brain, reproduction, evaluation);
		/*
		 * if the folder name is not specified, a default name will be generated.
		 */
		algo.setRegistrationFolderName(folderName);
		/*
		 * the possible mutations for the brains in the reproduction.
		 * There are currently 6 available mutation :
		 *  - setChangeLinkFactor
		 *  - setChangeLinkExtremity
		 *  - setAddNode
		 *  - setAddLink
		 *  - setDeleteNode
		 *  - setDeleteLink
		 *  All those mutation will have an effect on the FlexibleBrain, but only 
		 *  setChangeLinkFactor, setAddNode and setDeleteNode will have an effect on the
		 *  LayeredBrain.
		 */
		reproduction.getMutationManager().setAddNode(0.2f);
		reproduction.getMutationManager().setChangeLinkFactor(1.1f, 0.5f);
		reproduction.getMutationManager().setDeleteNode(0.2f);
		return algo;
	}
	
	/**
	 * This private function concentrates what is needed to restore a simulation from a 
	 * save.
	 * @return the learning algorithm
	 */
	private static LearningAlgorithm setSaveSimulation() {
		return LearningAlgorithm.restore(folderName, evaluation);
	}

	
	public static void main(String[] args) {
		LearningAlgorithm algo = setInitialSimulation();
		/*
		 * Some arguments can be set regardless of the simulation beeing new or restored 
		 * from a save.
		 * For the brain, we have the method :
		 *  - Brain.setMaxUsableCores(nbThreads)
		 *  - Brain.setTraceMutation(trace)
		 * By default, the Learning algorithm will run until stopped. To automatically 
		 * control that, we have : 
		 *  - setNbIteration
		 *  - setRunningTime
		 * By default, the learning algorithm will only save when stopped. To get 
		 * autosaves, we have : 
		 *  - setAutosaveIterations
		 *  - setAutosaveTime
		 */
		Brain.setMaxUsableCores(3);
		algo.setRunningTime(10000);
		algo.setAutosaveTime(1000);
		/*
		 * You can either control the algorithm in the code, or control it with a
		 * graphical interface.
		 * To control the algorithm in the code, you can use the following methods : 
		 *  - start
		 *  - stop
		 *  - playPause
		 *  - isPaused
		 * To use the graphical interface, you can use the View class and the 
		 * method openWindow. This method will open a window with the necessary buttons 
		 * to control the code manually.
		 */
		var view = new View(algo);
		view.openWindow();

	}
	
}
