package algorithm.NEAT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import algorithm.Evaluation;
import algorithm.LearningAlgorithm;
import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import brain.Brain;

/**
 * This class is the basis for a NEAT algorithm.
 * @author jrl
 *
 */
public class NEATAlgorithm extends LearningAlgorithm{
	
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
	 * The number of generations
	 */
	private int numGeneration;
	
	/***********************************************************************************/
	/*                               constructors                                      */
	/***********************************************************************************/
	
	/**
	 * Constructor for a new NEAT Algorithm. 
	 * @param initialBrain the root brain for all that will be next
	 * @param reproductionAlgorithm the algorithm that chooses how to make the next 
	 * generation
	 * @param evaluation a lambda expression that decide the score of each individual
	 */
	public NEATAlgorithm(Brain initialBrain, ReproductionAlgorithm reproductionAlgorithm, 
			Evaluation evaluation) {
		this.evaluation = evaluation;
		Individual original = new Individual(initialBrain);
		this.population = new Individual[] {original};
		this.reproductionAlgorithm = reproductionAlgorithm;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        this.registrationFolder = "saves/NEATAlgorithm_" + LocalDateTime.now().format(formatter);
	}
	
	/**
	 * Constructor to restore from save.
	 * @param folder the folder containing the saves
	 * @throws IOException if the files are not found
	 */
	public NEATAlgorithm(String folder, ByteBuffer settings) throws IOException {
		//settings file
		this.reproductionAlgorithm = ReproductionAlgorithm.restore(settings);
		this.numGeneration = settings.getInt();
		Individual.setCountId(settings.getInt());
		//individual files
		File[] individuals=new File(folder + "/generation_"+this.numGeneration).listFiles();
		this.population = new Individual[individuals.length];
		//for each file
		for (int i = 0; i < individuals.length; i++) {
			this.population[i] = new Individual(individuals[i]);
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
		evaluation.evaluate(this.population);
	}
	
	/***********************************************************************************/
	/*                              saving methods                                     */
	/***********************************************************************************/
	
	/**
	 * This functions saves a entire generation.
	 */
	public void registerGeneration() {
		//create the folder if it is not created
		String folderName = this.registrationFolder + "/generation_" + this.numGeneration;
		File settingsFile = new File(folderName);
		settingsFile.mkdirs();
		//create the files for each individual
		for (Individual individual : this.population) {
			individual.save(new File(folderName + "/" + individual.getId() + ".bin"));
		}
	}
	
	/**
	 * saves the simutation parameters.
	 */
	public void registerInformations() {
		//create the needed folder
		File settingsFile = new File(this.registrationFolder);
		settingsFile.mkdirs();
		//informations about the reproductionAlgorithm
		byte[] reproductionSettings = this.reproductionAlgorithm.toByte();
		ByteBuffer bb = ByteBuffer.allocate(reproductionSettings.length + 9);
		bb.put((byte) 1); //1 for the NEATAlgorithm
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
	
	@Override
	public void save() {
		this.registerGeneration();
		this.registerInformations();
	}
	
	@Override
	public void saveGenealogy() {
		//create the folder and the file if it is not created
		String folderName = this.registrationFolder + "/generations";
		File settingsFile = new File(folderName);
		settingsFile.mkdirs();
		//get the informations about the individuals (12 bytes for each individual)
		ByteBuffer bb = ByteBuffer.allocate(12 * this.population.length);
		for (Individual individual : this.population) {
			bb.putInt(individual.getId());
			bb.putInt(individual.getParentId());
			bb.putInt(individual.getParent2Id());
		}
    	//file registration
		String path = folderName + "/generation_"+ this.numGeneration +".bin";
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
	
	@Override
	public void next() {
		this.evaluate();
		this.reproduce();
	}
	

}
