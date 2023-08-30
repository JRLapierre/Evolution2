package algorithm.NEAT;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import algorithm.NEAT.reproduction.Elitism;
import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import brain.Brain;
import brain.LayeredBrain;

class TestNEATAlgorithm {

	@Test
	void testRegisterInformations() {
		ReproductionAlgorithm reproduction = new Elitism(10, 20, 15);
		Brain brain = new LayeredBrain(1,1,1,1);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction);
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
		Individual.setCountId(0);
		ReproductionAlgorithm reproduction = new Elitism(10, 20, 15);
		Brain brain = new LayeredBrain(1,1,1,1);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction);
		algo.setRegistrationFolderName("saves/testPopulation");
		algo.registerGeneration();//should be 1 individual
		algo.reproduce();
		algo.registerGeneration();//should be 30 individuals
	}
	
	@Test
	void testDataRecuperation() {
		ReproductionAlgorithm reproduction = new Elitism(25, 50, 25);
		Brain brain = new LayeredBrain(5,5,5,5);
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproduction);
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

}
