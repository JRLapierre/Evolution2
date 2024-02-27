package algorithm.NEAT;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import algorithm.Evaluation;
import algorithm.LearningAlgorithm;
import algorithm.NEAT.reproduction.Elitism;
import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import algorithm.NEAT.reproduction.RouletteSelection;
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
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
		Brain brain = new LayeredBrain(1,1,1,1);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, null);
		//test of the default folder name
		algo.saveInformations();
		algo.setRegistrationFolderName("saves/testRelativePath");
		algo.saveInformations();
		//test of the absolute path
		//algo.setRegistrationFolderName("C:\\Users\\jrl\\Desktop\\personnel\\code\\java\\Evolution2/saves/testAbsolutePath");
		algo.saveInformations();

	}
	
	@Test
	void testRegisterPopulation() {
		File folder = new File("saves/testPopulation");
		if (folder.exists()) {
			deleteFolder(folder);
		}
		Individual.setCountId(0);
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
		Brain brain = new LayeredBrain(1,1,1,1);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, null);
		algo.setRegistrationFolderName("saves/testPopulation");
		algo.saveGeneration();//should be 1 
		//for the first generation
		File gen0 = new File("saves/testPopulation/generation_0");
		assertEquals(1, gen0.listFiles().length);
		float[] input = new float[] {1};
		Individual[] population = NEATAlgorithm.restorePopulation("saves/testPopulation/generation_0");
		assertEquals(algo.getPopulation()[0].getId(), population[0].getId());
		assertEquals(algo.getPopulation()[0].getParentId(), population[0].getParentId());
		assertEquals(algo.getPopulation()[0].getParent2Id(), population[0].getParent2Id());
		assertEquals(algo.getPopulation()[0].getScore(), population[0].getScore());
		assertEquals(algo.getPopulation()[0].getBrain().compute(input)[0], population[0].getBrain().compute(input)[0]);
		algo.reproduce();
		algo.saveGeneration();//should be 30 individuals
		//for the second generation
		File gen1 = new File("saves/testPopulation/generation_1");
		assertEquals(30, gen1.listFiles().length);
		population = NEATAlgorithm.restorePopulation("saves/testPopulation/generation_1");
		Arrays.sort(algo.getPopulation(), Comparator.comparingInt(Individual::getId));
		Arrays.sort(population, Comparator.comparingInt(Individual::getId));
		for (int i = 0; i < 30; i++) {
			assertEquals(algo.getPopulation()[i].getId(), population[i].getId());
			assertEquals(algo.getPopulation()[i].getParentId(), population[i].getParentId());
			assertEquals(algo.getPopulation()[i].getParent2Id(), population[i].getParent2Id());
			assertEquals(algo.getPopulation()[i].getScore(), population[i].getScore());
			assertEquals(algo.getPopulation()[i].getBrain().compute(input)[0], population[i].getBrain().compute(input)[0]);
		}
	}
	
	@Test
	void testRecuperationGenealogy() {
		File folder = new File("saves/testGenealogy");
		if (folder.exists()) {
			deleteFolder(folder);
		}
		Individual.setCountId(0);
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
		Brain brain = new LayeredBrain(1,1,1,1);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, null);
		algo.setRegistrationFolderName("saves/testGenealogy");
		Individual[][] populations = new Individual[3][];
		algo.saveGenealogy();
		Arrays.sort(algo.getPopulation(), Comparator.comparingInt(Individual::getId));
		populations[0] = algo.getPopulation();
		algo.reproduce();
		algo.saveGenealogy();
		Arrays.sort(algo.getPopulation(), Comparator.comparingInt(Individual::getId));
		populations[1] = algo.getPopulation();
		algo.reproduce();
		algo.saveGenealogy();
		Arrays.sort(algo.getPopulation(), Comparator.comparingInt(Individual::getId));
		populations[2] = algo.getPopulation();
		PartialIndividual[][] restoredGenealogy = NEATAlgorithm.restoreGenealogy("saves/testGenealogy/generations");
		for (int i = 0; i < 3; i++) {
			assertEquals(populations[i].length, restoredGenealogy[i].length);
			for (int j = 0; j < populations[i].length; j++) {
				assertEquals(populations[i][j].getId(), restoredGenealogy[i][j].getId());
				assertEquals(populations[i][j].getParentId(), restoredGenealogy[i][j].getParentId());
				assertEquals(populations[i][j].getParent2Id(), restoredGenealogy[i][j].getParent2Id());
			}
		}
	}
	
	@Test
	void testDataRecuperation() {
		//delete the folder
		File folder = new File("saves/testDataRecuperation");
		if (folder.exists()) {
			deleteFolder(folder);
		}
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
		Brain.setTraceMutation(false);
		Brain brain = new LayeredBrain(5,5,5,5);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, null);
		algo.setRegistrationFolderName("saves/testDataRecuperation");
		//set some random things for more cahos
		reproduction.setAddNode(5);
		reproduction.setChangeLinkFactor(5, 0.5f);
		reproduction.setDeleteNode(5);
		algo.reproduce();
		algo.saveGeneration();
		algo.saveInformations();
		int oldCountId = Individual.getCountId();
		NEATAlgorithm save = (NEATAlgorithm) LearningAlgorithm.restore("saves/testDataRecuperation", null);
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
		ReproductionAlgorithm reproduction = new Elitism(5, 10, 15, 15);
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
        	algo.reproduce();
        }

	}
	
	@Test
	void testSaveGenealogy() {
		//delete the folder
		File folder = new File("saves/testGenealogy");
		if (folder.exists()) {
			deleteFolder(folder);
		}
		//a lambda expression that seek the highest output
		Evaluation evaluation = population -> {
			float[] input = new float[] {1};
			float result;
			for (Individual individual : population) {
				result = individual.getBrain().compute(input)[0];
				individual.updateScore(result);
			}
		};
		ReproductionAlgorithm reproduction = new RouletteSelection(5, 10, 15, 15);
		Brain.setTraceMutation(false);
		Brain brain = new LayeredBrain(1,5,5,5);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction, evaluation);
		algo.setRegistrationFolderName("saves/testGenealogy");
		//set some random things for more cahos
		reproduction.setAddNode(5);
		reproduction.setChangeLinkFactor(5, 0.5f);
		reproduction.setDeleteNode(5);
		//check for each generation
		for (int i = 0; i < 3; i++) {
			algo.saveGenealogy();
			File infosGeneration = new File(folder + "/generations/generation_"+i+".bin");
			try {
				ByteBuffer bb = ByteBuffer.wrap(Files.readAllBytes(infosGeneration.toPath()));
				for (Individual individual : algo.getPopulation()) {
					assertEquals(individual.getId(), bb.getInt());
					assertEquals(individual.getParentId(), bb.getInt());
					assertEquals(individual.getParent2Id(), bb.getInt());
				}
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			algo.evaluate();
			algo.reproduce();
		}
	}

}
