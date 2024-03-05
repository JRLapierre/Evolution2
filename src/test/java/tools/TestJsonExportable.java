package tools;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import algorithm.NEAT.Individual;
import algorithm.NEAT.NEATAlgorithm;
import algorithm.NEAT.reproduction.Elitism;
import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import brain.Brain;
import brain.FlexibleBrain;
import brain.LayeredBrain;

class TestJsonExportable {
	
	private static String testFolder = "saves/json";
	private static Gson gson = new Gson();
	
	private static NEATAlgorithm generateNeatAlgorithm(String folderName) {
		Individual.setCountId(0);
		//set up the initial brain
		Brain brain = new FlexibleBrain(5,5,5);
		for (int i = 0; i < 20; i++) {
			brain.addRandomLink(20);
		}
		//set up the reproduction algo (not important)
		ReproductionAlgorithm reproductionAlgorithm = new Elitism(0, 5, 5, 10);
		reproductionAlgorithm.getMutationManager().setAddLink(2.1f, 3);
		reproductionAlgorithm.getMutationManager().setDeleteLink(2);
		//lambda expression
		Evaluation evaluation = population -> {
			//does nothing and it's fine for this test
		};
		//create the reproduction algorithm
		NEATAlgorithm algo = new NEATAlgorithm(brain, reproductionAlgorithm, evaluation);
		algo.setRegistrationFolderName(folderName);
		return algo;
	}
	
	@BeforeAll
	static void initTestFolder() {
		new File(testFolder).mkdirs();
	}

	@Test
	void testSaveObject() {
		//create a complex object
		List<String> listTest = new LinkedList<>();
		listTest.add("a string");
		listTest.add("an other string");
		listTest.add("a third string");
		//save the object and get back the data
		String filePath = testFolder + File.separator + "linkedList.json";
		JsonExport.saveObject(listTest, filePath);
		try {
			String content = String.join(System.lineSeparator(), Files.readAllLines(Paths.get(filePath)));
			assertEquals(gson.toJson(listTest), content);
		} catch (IOException e) {
			e.printStackTrace();
			fail("an exeption occured");
		}
	}
	
	@Test
	void testExportIndividual() {
		//create an individual
		Brain brain = new FlexibleBrain(5,5,5);
		for (int i = 0; i < 20; i++) {
			brain.addRandomLink(20);
		}
		Individual individual = new Individual(brain);
		//save the individual in binary format
		String binFileName = testFolder + File.separator + "individual.bin";
		String jsonFileName = testFolder + File.separator + "individual.json";
		individual.save(new File(binFileName));
		//restore and compare
		JsonExport.exportIndividual(binFileName, jsonFileName);
		try {
			String content = String.join(System.lineSeparator(), Files.readAllLines(Paths.get(jsonFileName)));
			assertEquals(gson.toJson(individual), content);
		} catch (IOException e) {
			e.printStackTrace();
			fail("an exeption occured");
		}
	}
	
	@Test
	void testExportGeneration() {
		Brain.setTraceMutation(true);
		//create a population
		int populationSize = 10;
		Individual[] population = new Individual[populationSize];
		for (int i = 0; i < populationSize; i++) {
			Brain brain = new LayeredBrain(5,5,1,5);
			for (int j = 0; j < 20; j++) {
				brain.changeRandomLinkFactor(20);;
			}
			population[i] = new Individual(brain);
		}
		//save this population
		String binFolder = testFolder + File.separator + "generation_bin";
		String jsonFolder = testFolder + File.separator + "generation_json";
		new File(binFolder).mkdirs();
		for (Individual individual : population) {
			individual.save(new File(binFolder + File.separator + individual.getId() + ".bin"));
		}
		//export, restore and compare
		JsonExport.exportGeneration(binFolder, jsonFolder);
		try {
			for (Individual individual : population) {
				String fileName = jsonFolder + File.separator + individual.getId() + ".json";
				String content = String.join(System.lineSeparator(), Files.readAllLines(Paths.get(fileName)));
				assertEquals(gson.toJson(individual), content);
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("an exeption occured");
		}
	}
	
	@Test
	void testExportGenealogy() {
		String binFolderName = testFolder + File.separator + "genealogy_bin_1";
		String jsonFileName = testFolder + File.separator + "genealogy.json";
		NEATAlgorithm algo = generateNeatAlgorithm(binFolderName);
		//make saves of a fiew generation
		int nbGenerations = 3;
		Individual[][] population = new Individual[nbGenerations][];
		for (int i = 0; i < nbGenerations; i++) {
			population[i] = algo.getPopulation();
			algo.saveGenealogy();
			algo.next();
		}
		//create the json version
		JsonExport.exportGenealogy(
				binFolderName + File.separator + "generations", 
				jsonFileName);
		File jsonFile = new File(jsonFileName);
		assertTrue(jsonFile.exists());
		
		
	}
	
	@Test
	void testRecuperationGenealogy() {
		String binFolderName = testFolder + File.separator + "genealogy_bin_2";
		NEATAlgorithm algo = generateNeatAlgorithm(binFolderName);
		//make saves of a fiew generation
		int nbGenerations = 3;
		Individual[][] populations = new Individual[nbGenerations][];
		for (int i = 0; i < nbGenerations; i++) {
			Arrays.sort(algo.getPopulation(), Comparator.comparingInt(Individual::getId));
			populations[i] = algo.getPopulation();
			algo.saveGenealogy();
			algo.next();
		}
		PartialIndividual[][] restoredGenealogy = JsonExport.restoreGenealogy(binFolderName + File.separator + "generations");
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
	void testExportSimulation() {
		String binFolderName = testFolder + File.separator + "full_simulation_bin";
		String jsonFolderName = testFolder + File.separator + "full_simulation_json";
		String jsonPartialFolderName = testFolder + File.separator + "partial_simulation_json";
		NEATAlgorithm algo = generateNeatAlgorithm(binFolderName);
		//make saves of a fiew generation
		int nbGenerations = 3;
		for (int i = 0; i < nbGenerations; i++) {
			algo.save();
			algo.saveGenealogy();
			algo.next();
		}
		JsonExport.exportSimulation(binFolderName, jsonFolderName, true);
		JsonExport.exportSimulation(binFolderName, jsonPartialFolderName, false);
		
	}

}
