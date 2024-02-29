package algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

import com.google.gson.Gson;

import algorithm.NEAT.Individual;
import algorithm.NEAT.NEATAlgorithm;

/**
 * This interface allows the exportation of objects and saves in a json format.
 * 
 * @author jrl
 *
 */
public interface JsonExportable {
	
	/**
	 * Gson object allowing us to create json from our objects
	 */
	public static final Gson gson = new Gson();
	
	/**
	 * Function that allows to generate a json file from an object. 
	 * @param object The object we want to save.
	 * @param fileName The path of the new file we want to create (containing the name of
	 *  the file).
	 */
	public static void saveObject(Object object, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(gson.toJson(object));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
	}
	
	/**
	 * Function that restores a saved generation
	 * @param file the file to be read
	 * @return an array of partial individuals
	 */
	private static PartialIndividual[] restoreSimplifiedGeneration(File file) {
		try {
			byte[] datas = Files.readAllBytes(file.toPath());
			int nbIndividuals = datas.length/12; //determine the number of individuals
			PartialIndividual[] savedPopulation = new PartialIndividual[nbIndividuals];
			ByteBuffer bb = ByteBuffer.wrap(datas);
			for (int i = 0; i < nbIndividuals; i++) {
				savedPopulation[i] = new PartialIndividual(bb.getInt(), bb.getInt(), bb.getInt());
			}
			return savedPopulation;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return new PartialIndividual[0]; //should never happend

	}
	
	/**
	 * Function that generate a two-dimentionnal array
	 * @param folder the folder containing the genealogy
	 * @return a two-dimentionnal array containing the informations about
	 */
	public static PartialIndividual[][] restoreGenealogy(String folder) {
		File[] files = new File(folder).listFiles();
		Arrays.sort(files, Comparator.comparing(File::getName));
		PartialIndividual[][] genealogy = new PartialIndividual[files.length][];
		//for each file
		for (int i = 0; i < genealogy.length; i++) {
			genealogy[i] = restoreSimplifiedGeneration(files[i]);
		}
		return genealogy;
	}
	
	/***********************************************************************************/
	/*                            exportation function                                 */
	/***********************************************************************************/
	
	/**
	 * This function takes the binary save file of an individual in parameter and create 
	 * a json file containing the datas of the file.
	 * @param src The path to the source file
	 * @param target The path of the new file we want to create (containing the name of
	 *  the file).
	 */
	public static void exportIndividual(String src, String target) {
		Individual individual = new Individual(new File(src));
		JsonExportable.saveObject(individual, target);
	}
	
	/**
	 * This function takes a folder containing multiple save file of individuals and
	 * creates a new folder with json files containing the datas of the individuals.
	 * @param src The path to the source folder
	 * @param target The path of the new folder we want to create
	 */
	public static void exportGeneration(String src, String target) {
		File[] files = new File(src).listFiles();
		new File(target).mkdirs();
		for (File file : files) {
			Individual individual = new Individual(file);
			JsonExportable.saveObject(individual, 
					target + File.separator + individual.getId() + ".json");		}
	}
	
	/**
	 * This function takes the files of the genealogy in a simulation and creates a 
	 * single json file containing all the datas.
	 * @param src The path to the source folder
	 * @param target The path of the new file we want to create (containing the name of
	 *  the file).
	 */
	public static void exportGenealogy(String src, String target) {
		PartialIndividual[][] genealogy = restoreGenealogy(src);
		JsonExportable.saveObject(genealogy, target);
	}
	
	/**
	 * Private function that exports in json format the elements unique to the NEAT 
	 * algorithm
	 * @param src The path to the source folder
	 * @param target The path of the new folder we want to create
	 * @param fullSave True if we want to export all the generations, False if we want
	 * only the last one.
	 * @param neatAlgorithm The learning algorithm
	 */
	private static void exportNeatAlgorithm(
			String src, 
			String target, 
			boolean fullSave, 
			NEATAlgorithm neatAlgorithm) {
		// save the genealogy
		File srcFolder = new File(src);
		JsonExportable.exportGenealogy(src + File.separator + "generations", target + File.separator + "genealogy.json");
		if (fullSave) { //to export all the saves
			File[] generationFolders = srcFolder.listFiles(file -> file.isDirectory() && file.getName().startsWith("generation_"));
			for (File folder : generationFolders) {
				JsonExportable.exportGeneration(folder.getPath(), target + File.separator + folder.getName());
			}
		}
		else {
			// save the latest population
			String generationFolder = target + File.separator + "generation_" + neatAlgorithm.getNumGeneration();
			new File(generationFolder).mkdirs();
			for (Individual individual : neatAlgorithm.getPopulation()) {
				JsonExportable.saveObject(individual, 
						generationFolder + File.separator + individual.getId() + ".json");
			}
		}
	}
	
	/**
	 * This function exports the data of a simulation into a json format.
	 * @param src The path to the source folder
	 * @param target The path of the new folder we want to create
	 * @param fullSave True if we want to export all the generations, False if we want
	 * only the last one.
	 */
	public static void exportSimulation(String src, String target, boolean fullSave) {
		LearningAlgorithm simulation = LearningAlgorithm.restore(src, null);
		File targetFolder = new File(target);
		targetFolder.mkdirs();
		// save the setings
		JsonExportable.saveObject(simulation, target + File.separator + "settings.json");
		if (simulation instanceof NEATAlgorithm neatAlgorithm) {
			exportNeatAlgorithm(src, target, fullSave, neatAlgorithm);
		}
	}


}
