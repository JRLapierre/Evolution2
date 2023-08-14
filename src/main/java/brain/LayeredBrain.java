package brain;

import java.nio.ByteBuffer;

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
	public LayeredBrain(short numberInputs, short numberOutputs, 
			short numberHiddenLayers, short numberByLayer) {
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
		this.nodes[2 + numberHiddenLayers] = new float[numberOutputs];		
		//connecting the layers
		for (int i = 0; i < this.links.length; i++) {
			for (int j = 0; j < this.links[i].length; i++) {
				this.links[i][j] = createLinks(i);
			}
		}
		
	}
	
	protected LayeredBrain(ByteBuffer bb) {
		//TODO
	}
	
	protected LayeredBrain(LayeredBrain original) {
		//TODO
	}
	
	/***********************************************************************************/
	/*                       controlled mutation functions                             */
	/***********************************************************************************/
	
	/***********************************************************************************/
	/*                              mutations                                          */
	/***********************************************************************************/
	
	//changement mutations --------------------------------------------------------------

	@Override
	public void changeRandomLinkFactor(float minMaxChange) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeRandomLinkExtremity() {
		// TODO Auto-generated method stub
		
	}
	
	//addition mutations ----------------------------------------------------------------

	@Override
	public void addRandomNode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRandomLink(float minMaxFactor) {
		// TODO Auto-generated method stub
		
	}

	//deletions mutations --------------------------------------------------------------

	@Override
	public void deleteRandomNode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRandomLink() {
		// TODO Auto-generated method stub
		
	}
	
	/***********************************************************************************/
	/* 							     other functions                                   */
	/***********************************************************************************/

	@Override
	public float[] compute(float[] inputs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}
