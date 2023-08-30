package algorithm.NEAT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import brain.Brain;

/**
 * This class is the basis for a NEAT algorithm.
 * @author jrl
 *
 */
public class NEATAlgorithm {
	
	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
	/**
	 * an array containing the population
	 */
	private Individual[] population;
	
	/**
	 * chosen reproduction algorithm
	 */
	private ReproductionAlgorithm reproductionAlgorithm;
	
	/**
	 * the name (and the path) of the folder that will contain the . <br>
	 * By default, the name will be "saves/NEATAlgorithm_" + the current date.
	 */
	private String registrationFolder;
	
	/**
	 * The number of generations
	 */
	private int numGeneration;
	
	/***********************************************************************************/
	/*                               constructors                                      */
	/***********************************************************************************/
	
	public NEATAlgorithm(Brain initialBrain, ReproductionAlgorithm reproductionAlgorithm) {
		Individual original = new Individual(initialBrain);
		this.population = new Individual[] {original};
		this.reproductionAlgorithm = reproductionAlgorithm;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        this.registrationFolder = "saves/NEATAlgorithm_" + LocalDateTime.now().format(formatter);
	}
	
	/**
	 * Constructor to restore from save.
	 * @param folder the folder containing the saves
	 */
	public NEATAlgorithm(String folder) {
		try {
			//settings file
			byte[] settingsFile = Files.readAllBytes(Paths.get(folder + "/settings.bin"));
			ByteBuffer bb = ByteBuffer.wrap(settingsFile);
			this.reproductionAlgorithm = ReproductionAlgorithm.restore(bb);
			this.numGeneration = bb.getInt();
			Individual.setCountId(bb.getInt());
			//individual files
			File[] individuals=new File(folder + "/generation_"+this.numGeneration).listFiles();
			this.population = new Individual[individuals.length];
			//for each file
			for (int i = 0; i < individuals.length; i++) {
				bb = ByteBuffer.wrap(Files.readAllBytes(individuals[i].toPath()));
				this.population[i] = new Individual(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
	        System.exit(1);
		}

	}
	
	/***********************************************************************************/
	/*                                 getters                                         */
	/***********************************************************************************/
	
	/**
	 * getter for the reproduction algorithm
	 * @return the reproduction algorithm
	 */
	public ReproductionAlgorithm getReproductionAlgorithm() {
		return this.reproductionAlgorithm;
	}
	
	/**
	 * getter for the number of the last generation
	 * @return the numero of the last generation
	 */
	public int getNumGeneration() {
		return this.numGeneration;
	}
	
	/**
	 * getter to obtain the population
	 * @return the array containing the population
	 */
	protected Individual[] getPopulation() {
		return this.population;
	}
	
	/***********************************************************************************/
	/*                           functionnal methods                                   */
	/***********************************************************************************/
	
	/**
	 * Setter to change the saves folder. If the name is incorrect, the folder will not 
	 * be changed.
	 * @param newFolder The new folder (and his path) where the saves will be.
	 */
	public void setRegistrationFolderName(String newFolder) {
		File folderFile = new File(newFolder);
        if (folderFile.isAbsolute() || !Paths.get(newFolder).isAbsolute()) {
        	this.registrationFolder = newFolder;
        }
	}
	
	/**
	 * this function manages the selection and the mutation for the next generation.
	 */
	protected void reproduce() {
		this.population = reproductionAlgorithm.reproduce(population);
		this.numGeneration ++;
	}
	
	/**
	 * This function allows us to evaluate the population.
	 */
	protected void evaluate() {
		//TODO use a lambda expression or something like that
	}
	
	/**
	 * This functions saves a entire generation.
	 */
	public void registerGeneration() {
		//create the folder if it is not created
		String folderName = this.registrationFolder + "/generation_" + this.numGeneration;
		File settingsFile = new File(folderName);
		settingsFile.mkdirs();
		//create the files for each individual
		try {
			for (Individual individual : this.population) {
				FileOutputStream fos = new FileOutputStream(
						folderName + "/" + individual.getId() + ".bin");
            	fos.write(individual.toByte()); 
            	fos.flush();
            	fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
	        System.exit(1);
		}
	}
	
	/**
	 * saves the simutation parameters
	 */
	public void registerInformations() {
		//create the needed folder
		File settingsFile = new File(this.registrationFolder);
		settingsFile.mkdirs();
		//informations about the reproductionAlgorithm
		byte[] reproductionSettings = this.reproductionAlgorithm.toByte();
		ByteBuffer bb = ByteBuffer.allocate(reproductionSettings.length + 8);
		bb.put(reproductionSettings);
		bb.putInt(numGeneration);
		bb.putInt(Individual.getCountId());
    	//file registration
		String path = this.registrationFolder + "/settings.bin";
		try {
	    	FileOutputStream fos = new FileOutputStream(path);
			fos.write(bb.array());
	    	fos.flush();
	    	fos.close();
		} catch (IOException e) {
			e.printStackTrace();
            System.exit(1);
		}
	}
	
	//main algorithm
	public void run() {

		
		//in a loop
		this.evaluate();
		this.reproduce();
		//register after reproduction
		
		//register simulation settings on pause or on end
	}
	

}
