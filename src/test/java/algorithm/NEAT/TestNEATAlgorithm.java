package algorithm.NEAT;

import static org.junit.jupiter.api.Assertions.*;

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

}
