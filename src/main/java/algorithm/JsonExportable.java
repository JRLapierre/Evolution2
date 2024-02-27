package algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import algorithm.NEAT.Individual;
import algorithm.NEAT.NEATAlgorithm;
import algorithm.NEAT.PartialIndividual;

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
		JsonExportable.saveObject(individual, 
				target + File.separator + individual.getId() + ".json");
	}
	
	/**
	 * This function takes a folder containing multiple save file of individuals and
	 * creates a new folder with json files containing the datas of the individuals.
	 * @param src The path to the source folder
	 * @param target The path of the new folder we want to create
	 */
	public static void exportGeneration(String src, String target) {
		File[] individuals = new File(src).listFiles();
		new File(target).mkdirs();
		for (File individual : individuals) {
			JsonExportable.exportIndividual(individual.getPath(), target);
		}
	}
	
	/**
	 * This function takes the files of the genealogy in a simulation and creates a 
	 * single json file containing all the datas.
	 * @param src The path to the source folder
	 * @param target The path of the new file we want to create (containing the name of
	 *  the file).
	 */
	public static void exportGenealogy(String src, String target) {
		PartialIndividual[][] genealogy = NEATAlgorithm.restoreGenealogy(src);
		JsonExportable.saveObject(genealogy, target);
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
		File srcFolder = new File(src);
		File targetFolder = new File(target);
		targetFolder.mkdirs();
		// save the setings
		JsonExportable.saveObject(simulation, target + File.separator + "settings.json");
		if (simulation instanceof NEATAlgorithm neatAlgorithm) {
			// save the genealogy
			JsonExportable.exportGenealogy(src + File.separator + "generations", target + File.separator + "genealogy.json");
			if (fullSave) { //to export all the saves
				File[] generationFolders = srcFolder.listFiles(file -> file.isDirectory() && file.getName().startsWith("generation_"));
				for (File folder : generationFolders) {
					JsonExportable.exportGeneration(folder.getPath(), target + File.separator + folder.getName());
				}
			}
			else {
				// save the latest population
				for (Individual individual : neatAlgorithm.getPopulation()) {
					JsonExportable.saveObject(individual, 
							target + File.separator + "generation_" + neatAlgorithm.getNumGeneration() + File.separator
							+ individual.getId() + ".json");
				}
			}

		}
	}

}
