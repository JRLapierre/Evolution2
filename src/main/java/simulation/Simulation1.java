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
 * This is the first simulation I will do.
 * @author jrl
 *
 */
public class Simulation1 {

	private static String folderName = "saves/simulation_test";
	
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
	
	private static LearningAlgorithm setInitialSimulation() {
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(0.5f);
		Brain brain = new LayeredBrain(1,1,2,2);
		LearningAlgorithm algo = new NEATAlgorithm(brain, reproduction, evaluation);
		algo.setRegistrationFolderName(folderName);
		reproduction.setAddNode(5);
		reproduction.setChangeLinkFactor(5, 0.5f);
		reproduction.setDeleteNode(5);
		return algo;
	}
	
	private static LearningAlgorithm setSaveSimulation() {
		return LearningAlgorithm.restore(folderName, evaluation);
	}

	
	public static void main(String[] args) {
		LearningAlgorithm algo = setInitialSimulation();
		//algo.setRunningTime(1000);
		//algo.setAutosaveTime(1000);
		new Controller(algo);
	}
	
}
