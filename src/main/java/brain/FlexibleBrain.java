package brain;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import brain.mutation.MutationAdditionLink;
import brain.mutation.MutationAdditionNode;
import brain.mutation.MutationDeletionLink;
import brain.mutation.MutationDeletionNode;
import brain.mutation.MutationLinkExtremity;
import brain.mutation.MutationLinkFactor;

/**
 * This class represents a flexible brain. There is no node layer, any node can be connected 
 * to any other node.
 * 
 * @author jrl
 *
 */
public class FlexibleBrain extends Brain {
	
	/***********************************************************************************/
	/*                           integrated classes                                    */
	/***********************************************************************************/
	
	/**
	 * this class represents a node.
	 * @author jrl
	 *
	 */
	class Node {
		
		/**
		 * this is the value contained in the node
		 */
		protected float value = 0f;
	}
	
	/**
	 * This class represents a link between two nodes, transmitting the signal.
	 * 
	 * @author jrl
	 *
	 */
	public class Link {
		
		/**
		 * the origin node of the link
		 */
		private Node origin;
		
		/**
		 * the target node of the link
		 */
		private Node target;
		
		/**
		 * the multiplicative factor for the signal while it is transmitted
		 */
		private float factor;
		
		/**
		 * the value of the signal travelling
		 */
		private float value = 0;
		
		/**
		 * the constructor for a link. It takes the two extremities of the signal 
		 * and the factor.
		 * @param origin the origin node for the link
		 * @param target the target node for the link
		 * @param factor the multiplicative factor for the signal while it is transmitted
		 */
		public Link(Node origin, Node target, float factor) {
			this.origin = origin;
			this.target = target;
			this.factor = factor;
		}
		
		/**
		 * method that take the source signal and transfer it inside the link.
		 * The value of the signal is multiplied by the factor.
		 */
		public void takeSignal() {
			this.value = this.origin.value * this.factor;
		}
		
		/**
		 * method that send the source signal to the target node.
		 * The value is resetted after.
		 */
		public void sendSignal() {
			this.target.value += this.value;
			this.value = 0;
		}
	}
	
	/***********************************************************************************/
	/*                                variables                                        */
	/***********************************************************************************/
	
	// structural variables -------------------------------------------------------------
	
	/**
	 * This is the array of inputs. The data is inserted in this array.
	 */
	private Node[] inputs;
	
	/**
	 * This is the array of hidden nodes. 
	 */
	private Node[] hidden;
	
	/**
	 * This is the array of output nodes. the data contained at the end of the analysis 
	 * is taken from here.
	 */
	private Node[] outputs;
	
	/**
	 * this LinkedList contains the links between the nodes.
	 */
	private LinkedList<Link> links;
	
	// working variables ----------------------------------------------------------------
	
	/**
	 * This variable tells how many times the signal will jump from one node to an other.
	 * when the number of transmissions reach this number, the computing will give a result.
	 */
	private static int timeToCompute = 1;
	
	/***********************************************************************************/
	/*                              static functions                                   */
	/***********************************************************************************/
	
	/**
	 * this function allows us to change the number of jumps between nodes.
	 * If the new time is below one, the old time is kept.
	 * @param newTime The new time to compute. It must be above 0 to be taken.
	 */
	public static void setTimeToCompute(int newTime) {
		if (newTime > 0) FlexibleBrain.timeToCompute = newTime;
	}
	
	/***********************************************************************************/
	/*                       useful class-specific functions                           */
	/***********************************************************************************/
	
	/**
	 * function that allows to find the position of an object in an array.
	 * @param element the seached object
	 * @param array the array in witch we search
	 * @return the position of the object if the object has been found, -1 otherwise.
	 */
	private short seekPosition(Object element, Object[] array) {
		for (short i = 0; i < array.length; i++) {
			if (array[i] == element) return i;
		}
		return -1;
	}
	
	/**
	 * find the coordinates of the extremities of a link
	 * @param link the link we want to know about
	 * @return an array containing 4 shorts :<br>
	 * the number of the array containing the origin node (1 inputs, 2 hidden)<br>
	 * the place of the origin node in the array<br>
	 * the number of the array containing the target node (2 hidden, 3 outputs) <br>
	 * the place of the target node in the array
	 */
	private short[] getLinkCoordinates(Link link) {
		short originPosition = seekPosition(link.origin, this.inputs);
		short targetPosition = seekPosition(link.target, this.outputs);
		short originArray = (short) ((originPosition == -1) ? 2 : 1);
		short targetArray = (short) ((targetPosition == -1) ? 2 : 3);
		//correcting the positions if needed
		if (originPosition == -1) originPosition = seekPosition(link.origin, this.hidden);
		if (targetPosition == -1) targetPosition = seekPosition(link.target, this.hidden);
		return new short[] {originArray, originPosition, targetArray, targetPosition};
	}
	
	/**
	 * finds the coordinates of a node
	 * @param node the searched node
	 * @return an array containing two elements : <br>
	 *  - the number of the array containing the node <br>
	 *  - the position of the node in the array
	 */
	private short[] getNodeCoordinates(Node node) {
		short position;
		position = seekPosition(node, this.inputs);
		if (position != -1) return new short[] {1, position};
		position = seekPosition(node, this.outputs);
		if (position != -1) return new short[] {3, position};
		return new short[] {2, seekPosition(node, this.hidden)};
	}
	
	/**
	 * allows to decode a node from binary data
	 * @param bb the ByteBuffer containing the data
	 * @return the node described in the 3 next bytes
	 */
	private Node decodeNode(ByteBuffer bb) {
		byte arrayByte = bb.get();
		Node[] array;
		switch (arrayByte) {
		case ((byte) 1):
			array = this.inputs;
			break;
		case ((byte) 3):
			array = this.outputs;
			break;
		default:
			array = this.hidden;
		}
		return array[bb.getShort()];
	}
	
	/**
	 * function that gives a node from her coordinates
	 * @param nodeArray the number indicating the array containing the node (1, 2 or 3)
	 * @param position the position of the node in the array
	 * @return the searched node
	 */
	private Node decodeNode(short nodeArray, short position) {
		switch (nodeArray) {
		case(1):
			return this.inputs[position];
		case(2):
			return this.hidden[position];
		default:
			return this.outputs[position];
		}
	}
	
	/***********************************************************************************/
	/*                               constructors                                      */
	/***********************************************************************************/
	
	/**
	 * constructor for a new brain.
	 * @param inputLength the number of input nodes
	 * @param hiddenLength the number of hidden nodes
	 * @param outputLength the number of output nodes
	 */
	public FlexibleBrain(int inputLength, int hiddenLength, int outputLength) {
		this.inputs = new Node[inputLength];
		for (int i = 0; i < inputLength; i++) this.inputs[i] = new Node();
		this.hidden = new Node[hiddenLength];
		for (int i = 0; i < hiddenLength; i++) this.hidden[i] = new Node();
		this.outputs = new Node[outputLength];
		for (int i = 0; i < outputLength; i++) this.outputs[i] = new Node();
		this.links = new LinkedList<>();
	}

	/**
	 * constructor from a save.
	 * @param bb the ByteBuffer containing the datas
	 */
	protected FlexibleBrain(ByteBuffer bb) {
		//basic structure
		int inputLength = bb.getShort();
		int hiddenLength = bb.getShort();
		int outputLength = bb.getShort();
		this.inputs = new Node[inputLength];
		for (int i = 0; i < inputLength; i++) this.inputs[i] = new Node();
		this.hidden = new Node[hiddenLength];
		for (int i = 0; i < hiddenLength; i++) this.hidden[i] = new Node();
		this.outputs = new Node[outputLength];
		for (int i = 0; i < outputLength; i++) this.outputs[i] = new Node();
		//the links
		this.links = new LinkedList<>();
		int nbLinks = bb.getInt();
		for (int i = 0; i < nbLinks; i++) {
			this.links.add(new Link(decodeNode(bb), decodeNode(bb), bb.getFloat()));
		}
	}
	
	/**
	 * constructor for a copy
	 * @param original
	 */
	protected FlexibleBrain(FlexibleBrain original) {
		//base structure
		int inputLength = original.inputs.length;
		int hiddenLength = original.hidden.length;
		int outputLength = original.outputs.length;
		this.inputs = new Node[inputLength];
		for (int i = 0; i < inputLength; i++) this.inputs[i] = new Node();
		this.hidden = new Node[hiddenLength];
		for (int i = 0; i < hiddenLength; i++) this.hidden[i] = new Node();
		this.outputs = new Node[outputLength];
		for (int i = 0; i < outputLength; i++) this.outputs[i] = new Node();
		//the links
		this.links = new LinkedList<>();
		Node origin;
		Node target;
		for (Link originalLink : original.links) {
 			short[] linkCoordinates = original.getLinkCoordinates(originalLink);
 			origin = decodeNode(linkCoordinates[0], linkCoordinates[1]);
 			target = decodeNode(linkCoordinates[2], linkCoordinates[3]);
			//add the element to the list
			this.links.add(new Link(origin, target, originalLink.factor));
		}
	}
	
	/***********************************************************************************/
	/*                                    getters                                      */
	/***********************************************************************************/
	
	/**
	 * getter used in the tests.
	 * @return this.inputs
	 */
	Node[] getInputs() {
		return this.inputs;
	}
	
	/**
	 * getter used in the tests.
	 * @return this.outputs
	 */
	Node[] getOutputs() {
		return this.outputs;
	}
	
	/**
	 * getter used in the tests.
	 * @return this.hidden
	 */
	Node[] getHidden() {
		return this.hidden;
	}
	
	/**
	 * getter used in the tests.
	 * @return this.links
	 */
	LinkedList<Link> getLinks() {
		return this.links;
	}
	
	/***********************************************************************************/
	/*                       controlled mutation functions                             */
	/***********************************************************************************/
	
	/**
	 * Create a new link between two nodes.
	 * @param origin the origin node of the signal
	 * @param target the target node of the signal
	 * @param factor the multiplicative factor of the signal
	 */
	void addLink(Node origin, Node target, float factor) {
		this.links.add(new Link(origin, target, factor));
	}
	
	/**
	 * Add a new hidden node.
	 */
	void addNode() {
		this.hidden = Arrays.copyOf(this.hidden, this.hidden.length + 1);
		this.hidden[this.hidden.length - 1] = new Node();
	}
	
	/**
	 * delete a link
	 * @param position the position of the link in the list
	 */
	void deleteLink(int position) {
		this.links.remove(position);
	}
	
	/**
	 * delete a hidden node and all the links referencing it
	 * @param position the position in the list
	 */
	void deleteNode(int position) {
		//delete the links using this node
		Iterator<Link> iterator = this.links.iterator();
		while (iterator.hasNext()) {
		    Link link = iterator.next();
		    if (link.origin == this.hidden[position] || link.target == this.hidden[position]) 
		        iterator.remove();
		}
		//redo the node list
		Node[] newArray = new Node[this.hidden.length - 1];
		int j = 0;
		for (int i = 0; i < this.hidden.length; i++) {
		    if (i != position) newArray[j++] = this.hidden[i];
		}
		this.hidden = newArray;
	}
	
	/***********************************************************************************/
	/*                              mutations                                          */
	/***********************************************************************************/

	//changement mutations --------------------------------------------------------------

	@Override
	public void changeRandomLinkFactor(float minMaxChange) {
		if (this.links.isEmpty()) return;
		float changement = random.nextFloat(-minMaxChange, minMaxChange);
		Link link = this.links.get(random.nextInt(this.links.size()));
		if (traceMutation) {
			short[] coordinates = getLinkCoordinates(link);
			this.mutations.add(new MutationLinkFactor(coordinates[0], coordinates[1], 
					coordinates[2], coordinates[3], link.factor, changement));
		}
		link.factor += changement;
	}

	@Override
	public void changeRandomLinkExtremity() {
		if (this.links.isEmpty()) return;
		int position;
		//choosing the link to modify
		Link link = this.links.get(random.nextInt(this.links.size()));
		boolean origin = random.nextBoolean();
		Node oldNode = (origin) ? link.origin : link.target;
		//if we change the origin
		if (origin) {
			position = random.nextInt(this.inputs.length + this.hidden.length);
			link.origin = (position >= this.inputs.length) ? 
					this.hidden[position - this.inputs.length] : this.inputs[position];
		}
		//if we change the target
		else {
			position = random.nextInt(this.hidden.length + this.outputs.length);
			link.target = (position >= this.hidden.length) ?
					this.outputs[position - this.hidden.length] : this.hidden[position];
		}
		//to register the mutation
		if (traceMutation) {
			short[] nodeCoordinates = getNodeCoordinates(oldNode);
			this.mutations.add(new MutationLinkExtremity(
					link, origin, nodeCoordinates[0], nodeCoordinates[1]));
		}
	}

	//addition mutations ----------------------------------------------------------------

	@Override
	public void addRandomNode() {
		this.addNode();
		if (traceMutation) this.mutations.add(new MutationAdditionNode(2));
		
	}

	@Override
	public void addRandomLink(float minMaxFactor) {
		short posOrigin;
		short posTarget;
		Node origin;
		Node target;
		Float factor = random.nextFloat(-minMaxFactor , minMaxFactor);
		//getting the origin node
		posOrigin = (short) random.nextInt(this.inputs.length + this.hidden.length);
		origin = (posOrigin >= this.inputs.length) ?
			this.hidden[posOrigin - this.inputs.length] : this.inputs[posOrigin];
		//getting the target node
		posTarget = (short) random.nextInt(this.outputs.length + this.hidden.length);
		target = (posTarget >= this.hidden.length) ?
			this.outputs[posTarget - this.hidden.length] : this.outputs[posTarget];
		//creating the new link
		this.addLink(origin, target, factor);
		//if we want to keep trace of the change
		if (traceMutation) {
			short[] coordinates = getLinkCoordinates(this.links.getLast());
			this.mutations.add(new MutationAdditionLink(coordinates[0], coordinates[1], 
					coordinates[2], coordinates[3], factor));
		}
	}

	//deletions mutations --------------------------------------------------------------

	@Override
	public void deleteRandomNode() {
		if (this.hidden.length == 0) return;
		int position = random.nextInt(
				this.hidden.length);
		this.deleteNode(position);
		if (traceMutation) {
			this.mutations.add(new MutationDeletionNode((short) 2, (short) position));
		}
	}

	@Override
	public void deleteRandomLink() {
		if (this.links.isEmpty()) return;
		int linkPosition = random.nextInt(this.links.size());
		if (traceMutation) {
			Link link = this.links.get(linkPosition);
			short[] coordinates = getLinkCoordinates(link);
			//adding the mutation to the list
			this.mutations.add(new MutationDeletionLink(coordinates[0], coordinates[1], 
					coordinates[2], coordinates[3], link.factor));
		}
		this.deleteLink(linkPosition);
	}
	
	/***********************************************************************************/
	/*                              other functions                                    */
	/***********************************************************************************/
	
	@Override
	public float[] compute(float[] inputs) {
		//assign the inputs value into the inputs nodes
		for (int i = 0 ; i < inputs.length; i++) this.inputs[i].value = inputs[i];
		//transmission of the signal
		for (int i = 0 ; i < timeToCompute ; i++) {
			//taking the signal inside the links
			for (Link link : this.links) link.takeSignal();
			//resetting the value in the hidden nodes
			for (int j = 0 ; j < this.hidden.length; j++) this.hidden[j].value = 0f;
			//sending the signal in the next node
			for (Link link : this.links) link.sendSignal();
		}
		//transmitting the outputs value into a new array (just to be safe)
		float[] outputsCopy = new float[this.outputs.length];
		for (int i = 0 ; i < this.outputs.length; i++) outputsCopy[i] = this.outputs[i].value;
		//resetting the outputs and the hidden nodes
		for (int i = 0 ; i < this.hidden.length; i++) this.hidden[i].value = 0f;
		for (int i = 0 ; i < this.outputs.length; i++) this.outputs[i].value = 0f;
		return outputsCopy;
	}

	@Override
	public byte[] toBytes() {
		//calcul of the data length
		int size = this.links.size();
		int dataLength = 11 + size * 10;
		ByteBuffer bb = ByteBuffer.allocate(dataLength);
		//information about the structure of the brain
		bb.put((byte) 1); //kind of brain (1 for FlexibleBrain)
		bb.putShort((short) this.inputs.length); //length of the inputs array
		bb.putShort((short) this.hidden.length); //length of the hidden array
		bb.putShort((short) this.outputs.length); //length of the outputs array
		bb.putInt(size); //number of links
		//loop for each link
		for (Link link : this.links) {
			this.writeNode(bb, link.origin, true); // origin node
			this.writeNode(bb, link.target, false); //target node
			bb.putFloat(link.factor); //factor
		}
		return bb.array();
	}
	
	/**
	 * this function allows to find the 3 bytes to describe a node.
	 * @param bb the ByteBuffer that will contain the informations about the brain
	 * @param node the node we want to encode
	 * @param origin true if the node is the origin node of the link, false if it 
	 * is the target node.
	 */
	private void writeNode(ByteBuffer bb, Node node, boolean origin) {
		Node[] array = (origin) ? this.inputs : this.outputs;
		byte arrayByte = (byte) ((origin) ? 1 : 3);
		Short position = seekPosition(node, array);
		if (position != -1) {
			bb.put(arrayByte);
			bb.putShort(position);
		}
		else {
			bb.put((byte) 2);
			bb.putShort(seekPosition(node, this.hidden));
		}
	}

}
