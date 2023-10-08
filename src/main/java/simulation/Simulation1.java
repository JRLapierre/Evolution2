package simulation;

import algorithm.Controller;
import algorithm.Evaluation;
import algorithm.LearningAlgorithm;
import algorithm.NEAT.Individual;
import algorithm.NEAT.NEATAlgorithm;
import algorithm.NEAT.reproduction.Elitism;
import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import algorithm.NEAT.reproduction.RouletteSelection;
import brain.Brain;
import brain.LayeredBrain;

/**
 * This is the first simulation I will do. This will serve as a simple test to see if 
 * everything works as expected.
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
	 * This lambda expression serve to determine the fintess of each individuals. In this
	 * example, the fitness is determined by the power of the output of the brain.
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
		//choosing the reproduction algorithm and his settings
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
		//choosing the default value for new brains
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(0.5f);
		//creating the brain
		Brain brain = new LayeredBrain(1,1,2,2);
		//creation the learning algoritm
		LearningAlgorithm algo = new NEATAlgorithm(brain, reproduction, evaluation);
		//if the folder name is not specified, a default name will be generated.
		algo.setRegistrationFolderName(folderName);
		//the possible mutations for the brains in the reproduction
		reproduction.setAddNode(5);
		reproduction.setChangeLinkFactor(5, 0.5f);
		reproduction.setDeleteNode(5);
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
		//arguments that can be set regardless of the simulation beeing new or restored 
		//from a save
		algo.setRunningTime(10000);
		algo.setAutosaveTime(1000);
		//creation of the controller for the algorithm
		new Controller(algo);
	}
	
}
