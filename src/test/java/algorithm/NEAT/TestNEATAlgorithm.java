package algorithm.NEAT;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import algorithm.Evaluation;
import algorithm.NEAT.reproduction.Elitism;
import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import brain.Brain;
import brain.LayeredBrain;

class TestNEATAlgorithm {
	
	//a function to delete all the files in a folder
	static void deleteFolder(File file){
		for (File subFile : file.listFiles()) {
			if(subFile.isDirectory()) {
				deleteFolder(subFile);
			} else {
				subFile.delete();
			}
		}
		file.delete();
	}
	
	@Test
	void testRegisterInformations() {
		ReproductionAlgorithm reproduction = new Elitism(10, 20, 15);
		Brain brain = new LayeredBrain(1,1,1,1);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, null);
		//test of the default folder name
		algo.registerInformations();
		algo.setRegistrationFolderName("saves/testRelativePath");
		algo.registerInformations();
		//test of the absolute path
		algo.setRegistrationFolderName("C:\\Users\\jrl\\Desktop\\personnel\\code\\java\\Evolution2/saves/testAbsolutePath");
		algo.registerInformations();

	}
	
	@Test
	void testRegisterPopulation() {
		File folder = new File("saves/testPopulation");
		if (folder.exists()) {
			deleteFolder(folder);
		}
		Individual.setCountId(0);
		ReproductionAlgorithm reproduction = new Elitism(10, 20, 15);
		Brain brain = new LayeredBrain(1,1,1,1);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, null);
		algo.setRegistrationFolderName("saves/testPopulation");
		algo.registerGeneration();//should be 1 
		//for the first generation
		File gen0 = new File("saves/testPopulation/generation_0");
		assertEquals(1, gen0.listFiles().length);
		algo.reproduce();
		algo.registerGeneration();//should be 30 individuals
		//for the second generation
		File gen1 = new File("saves/testPopulation/generation_1");
		assertEquals(30, gen1.listFiles().length);
	}
	
	@Test
	void testDataRecuperation() {
		//delete the folder
		File folder = new File("saves/testDataRecuperation");
		if (folder.exists()) {
			deleteFolder(folder);
		}
		ReproductionAlgorithm reproduction = new Elitism(25, 50, 25);
		Brain.setTraceMutation(false);
		Brain brain = new LayeredBrain(5,5,5,5);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, null);
		algo.setRegistrationFolderName("saves/testDataRecuperation");
		//set some random things for more cahos
		reproduction.setAddNode(5);
		reproduction.setChangeLinkFactor(5, 0.5f);
		reproduction.setDeleteNode(5);
		algo.reproduce();
		algo.registerGeneration();
		algo.registerInformations();
		int oldCountId = Individual.getCountId();
		NEATAlgorithm save = new NEATAlgorithm("saves/testDataRecuperation");
		//reproduction algorithm comparaison
		byte[] originalReproduction = algo.getReproductionAlgorithm().toByte();
		byte[] saveReproduction = save.getReproductionAlgorithm().toByte();
		assertEquals(originalReproduction.length, saveReproduction.length);
		for (int i = 0; i < saveReproduction.length; i++) {
			assertEquals(originalReproduction[i], saveReproduction[i]);
		}
		assertEquals(oldCountId, Individual.getCountId());
		assertEquals(algo.getNumGeneration(), save.getNumGeneration());
		assertEquals(algo.getPopulation().length, save.getPopulation().length);
        Arrays.sort(algo.getPopulation(), Comparator.comparingInt(Individual::getId));
        Arrays.sort(save.getPopulation(), Comparator.comparingInt(Individual::getId));
		for (int i = 0; i < algo.getPopulation().length; i++) {
			byte[] originalBytes = algo.getPopulation()[i].toByte();
			byte[] copyBytes = save.getPopulation()[i].toByte();
			assertEquals(originalBytes.length, copyBytes.length);
			for (int j = 0; j < originalBytes.length; j++) {
				assertEquals(originalBytes[j], copyBytes[j]);
			}
		}
	}
	
	@Test
	void testEvaluation() {
		//a lambda expression that seek the highest output
		Evaluation evaluation = population -> {
			float[] input = new float[] {1};
			float result;
			for (Individual individual : population) {
				result = individual.getBrain().compute(input)[0];
				individual.updateScore(result);
			}
		};
		ReproductionAlgorithm reproduction = new Elitism(25, 50, 25);
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(0.5f);
		Brain brain = new LayeredBrain(1,1,2,2);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, evaluation);
		//set some mutations
		reproduction.setAddNode(5);
		reproduction.setChangeLinkFactor(5, 0.5f);
		reproduction.setDeleteNode(5);
		algo.reproduce();
		float bestScore = -10;
		for (int i = 0; i < 10; i++) {
        	algo.evaluate();
        	Arrays.sort(algo.getPopulation(), Comparator.comparingDouble(Individual::getScore).reversed());
        	//comparing the best
        	assertTrue(algo.getPopulation()[0].getScore() >= bestScore);
        	bestScore = algo.getPopulation()[0].getScore();
        	System.out.println(bestScore);
        	algo.reproduce();
        }

	}

}
