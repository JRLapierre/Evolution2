package brain;

import java.nio.ByteBuffer;
import java.util.Arrays;

import brain.mutation.MutationAdditionNode;
import brain.mutation.MutationDeletionNode;
import brain.mutation.MutationLinkFactor;

/**
 * This class represents a brain organized in layers.<br>
 * The first layer is the inputs layer, the last layer is the output layer.
 * Between these two unavoidable layers, there is a certain number of hidden layers.
 * The number of layers initially defined can't change, but the number of their node 
 * can.<br>
 * In this model, every node of a layer is connected to every node of the next layer.
 * @author jrl
 *
 */
public class LayeredBrain extends Brain {
	
	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
	/**
	 * array containing the values for each node
	 */
	private float[][] nodes;
	
	/**
	 * array containing the value of the links between the nodes
	 */
	private float[][][] links;
	
	/**
	 * At the initialisation of the brain, the links will take this value.
	 */
	private static float defaultLinkValue = 0;
	
	/**
	 * When new links will be created, the value of the link will vary depending of this
	 * parameter. <br>
	 * The value cannot be negative.
	 */
	private static float defaultLinkVariation = 0;
	
	/***********************************************************************************/
	/*                              static functions                                   */
	/***********************************************************************************/
	
	/**
	 * This function allows us to change the default value of the links between the nodes.
	 * By default, the value is set to 0.
	 * @param value the new default value of the links.
	 */
	public static void setDefaultLinkValue(float value) {
		defaultLinkValue = value;
	}
	
	/**
	 * This function allows us to change the default value of the variation of the default 
	 * value of the links. <br>
	 * @param value the new default link variation
	 */
	public static void setDefaultLinkVariation(float value) {
		defaultLinkVariation = (value > 0) ? value : -value;
	}
	
	/***********************************************************************************/
	/*                       useful class-specific functions                           */
	/***********************************************************************************/
	
	/**
	 * function that creates a new array of links from a node to the next layer.
	 * @param originLayer the layer of the origin node
	 * @return the array of links
	 */
	private float[] createLinks(int originLayer) {
		int size = this.nodes[originLayer + 1].length;
		float[] array = new float[size];
		for (int i = 0; i < size; i++) {
			array[i] = defaultLinkValue + ((defaultLinkVariation != 0) ? 
					random.nextFloat(-defaultLinkVariation, defaultLinkVariation) : 0);
		}
		return array;
	}
	
	/**
	 * fuction that allows to delete an element from an array knowing the index
	 * @param array the array to modify
	 * @param position the position of the element
	 * @return the array without the delete element
	 */
	private float[] deleteFromArray(float[] array, int position) {
		float[] newArray = new float[array.length - 1];
		int j = 0;
		for (int i = 0; i < array.length; i++) {
		    if (i != position) newArray[j++] = array[i];
		}
		return newArray;
	}
	
	/***********************************************************************************/
	/*                               constructors                                      */
	/***********************************************************************************/
	
	/**
	 * Constructor for a new LayeredBrain. <br>
	 * The values of the links will be set do defaultLinkValue.
	 * @param numberInputs The number of inputs nodes
	 * @param numberOutputs the number of output nodes
	 * @param numberHiddenLayers the number of hidden layers between the inputs an the 
	 * outputs nodes
	 * @param numberByLayer the number of hidden nodes by hidden layer
	 */
	public LayeredBrain(int numberInputs, int numberOutputs, 
			int numberHiddenLayers, int numberByLayer) {
		this.nodes = new float[2 + numberHiddenLayers][];
		this.links = new float[1 + numberHiddenLayers][][];//nothing coming from the outputs
		//input array
		this.nodes[0] = new float[numberInputs];
		this.links[0] = new float[numberInputs][];
		//hidden layers
		for (int i = 1; i < numberHiddenLayers + 1; i++) {
			this.nodes[i] = new float[numberByLayer];
			this.links[i] = new float[numberByLayer][];
		}
		//output layer
		this.nodes[1 + numberHiddenLayers] = new float[numberOutputs];		
		//connecting the layers
		for (int i = 0; i < this.links.length; i++) {
			for (int j = 0; j < this.links[i].length; j++) {
				this.links[i][j] = createLinks(i);
			}
		}
		
	}
	
	protected LayeredBrain(ByteBuffer bb) {
		//TODO
	}
	
	/**
	 * constructor to create a copy of a LayeredBrain
	 * @param original the brain we want to copy
	 */
	protected LayeredBrain(LayeredBrain original) {
		this.nodes = new float[original.nodes.length][];
		this.links = new float[original.links.length][][];
		//nodes arrays
		for (int i = 0; i < this.nodes.length; i++) {
			this.nodes[i] = new float[original.nodes[i].length];
		}
		//links arrays
		for (int i = 0; i < this.links.length; i++) {
			this.links[i] = new float[original.links[i].length][];
			for (int j = 0; j < this.links[i].length; j++) {
				this.links[i][j] = new float[original.links[i][j].length];
				//copy of the values
				for (int k = 0; k < this.links[i][j].length; k++) {
			        System.arraycopy(original.links[i][j], 0, 
			        		this.links[i][j], 0, original.links[i][j].length);
				}
			}
		}
	}
	
	/***********************************************************************************/
	/*                       controlled mutation functions                             */
	/***********************************************************************************/
	
	/**
	 * function that allows to change the factor of a link between two nodes.
	 * @param newFactor the new factor of the link
	 * @param layer the layer of the origin node of the link
	 * @param origin the position of the origin node of the link
	 * @param target the position of the target node of the link
	 */
	void changeLinkFactor(float newFactor, int layer, int origin, int target) {
		//registration
		if (traceMutation) this.mutations.add(new MutationLinkFactor(
				(short) layer, 
				(short) origin, 
				(short) (layer + 1), 
				(short) target, 
				this.links[layer][origin][target], 
				newFactor - this.links[layer][origin][target]));
		//mutation
		this.links[layer][origin][target] = newFactor;
	}
	
	/**
	 * fuction that allows to add a hidden node.
	 * @param layer the hidden layer where we add the node
	 */
	void addNode(int layer) {
		if (layer <= 0 || layer >= this.nodes.length - 1) return;
		//add the new node
		this.nodes[layer] = Arrays.copyOf(this.nodes[layer], this.nodes[layer].length + 1);
		//add the new links from the new node
		this.links[layer] = Arrays.copyOf(this.links[layer], this.links[layer].length + 1);
		this.links[layer][this.links[layer].length -1] = createLinks(layer);
		//add one link for each node of the previous layer
		for (int i = 0; i < this.links[layer-1].length; i++) {
			this.links[layer-1][i] = Arrays.copyOf(
					this.links[layer-1][i], this.links[layer-1][i].length + 1);
			this.links[layer-1][i][this.links[layer-1][i].length-1] = 
					defaultLinkValue + ((defaultLinkVariation != 0) ? 
							random.nextFloat(-defaultLinkVariation, defaultLinkVariation) : 0);
		}
		//registration
		if (traceMutation) this.mutations.add(new MutationAdditionNode(layer));
	}
	
	/**
	 * function that allows to delete a hidden node
	 * @param layer the layer of the node
	 * @param position the position of the node in the layer
	 */
	void deleteNode(int layer, int position) {
		//delete in the node array
		this.nodes[layer] = new float[this.nodes[layer].length -1 ];
		//delete in the links coming from the deleted node
		float[][] newArray = new float[this.links[layer].length - 1][];
		int j = 0;
		for (int i = 0; i < this.links[layer].length; i++) {
		    if (i != position) newArray[j++] = this.links[layer][i];
		}
		this.links[layer] = newArray;
		//delete the links going to the deleted node
		for (int i = 0; i < this.links[layer-1].length; i++) {
			this.links[layer-1][i] = deleteFromArray(this.links[layer-1][i], position);
		}
		//registration
		if (traceMutation) this.mutations.add(new MutationDeletionNode(layer, position));
	}
	
	/***********************************************************************************/
	/*                              mutations                                          */
	/***********************************************************************************/
	
	//changement mutations --------------------------------------------------------------

	@Override
	public void changeRandomLinkFactor(float minMaxChange) {
		short layer = (short) random.nextInt(this.links.length);
		short origin = (short) random.nextInt(this.links[layer].length);
		short target = (short) random.nextInt(this.links[layer][origin].length);
		float changement = random.nextFloat(-minMaxChange, minMaxChange);
		//mutation
		this.changeLinkFactor(this.links[layer][origin][target] + changement, 
				layer, origin, target);
		
	}

	@Override
	public void changeRandomLinkExtremity() {
		// impossible operation
		
	}
	
	//addition mutations ----------------------------------------------------------------

	@Override
	public void addRandomNode() {
		this.addNode(random.nextInt(1, this.links.length));
	}

	@Override
	public void addRandomLink(float minMaxFactor) {
		// impossible operation
	}

	//deletions mutations --------------------------------------------------------------

	@Override
	public void deleteRandomNode() {
		int layer = random.nextInt(1, this.links.length);
		int position = random.nextInt(this.nodes[layer].length);
		this.deleteNode(layer, position);
	}

	@Override
	public void deleteRandomLink() {
		// impossible operation
	}
	
	/***********************************************************************************/
	/* 							     other functions                                   */
	/***********************************************************************************/

	@Override
	public float[] compute(float[] inputs) {
		//getting the inputs
		this.nodes[0] = Arrays.copyOf(inputs, inputs.length);
		//transmitting in the layers
		for (int i = 0; i < this.links.length; i++) { //for each layer
			//transmitting the signals to the other layers
			for (int j = 0; j < this.links[i].length; j++) {//for each source node
				for (int k = 0; k < this.links[i][j].length; k++) {//for each target node
					//take the value from the source and transmitting it to the target
					this.nodes[i+1][k] += this.nodes[i][j] * this.links[i][j][k];
				}
			}
		}
		//copiing the results
		float[] results = Arrays.copyOf(this.nodes[this.nodes.length - 1], 
				this.nodes[this.nodes.length - 1].length);
		//resetting the nodes
		for (int i = 0; i < this.nodes.length; i++) {
			for (int j = 0; j < this.nodes[i].length; j++) {
				this.nodes[i][j] = 0;
			}
		}
		return results;
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		
		return null;
	}

}
