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
	private float[][] nodeValues;
	
	/**
	 * array containing the value of the links between the nodes
	 */
	private float[][][] links;
	
	/***********************************************************************************/
	/*                               constructors                                      */
	/***********************************************************************************/
	
	public LayeredBrain(short numberInputs, short numberOutputs, 
			short numberHiddenLayers, short numberByLayer) {
		//TODO
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
