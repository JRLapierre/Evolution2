package brain.mutation;

public class MutationDeletionNode extends Mutation {
	private short nodeArray;
	private short nodePosition;
	
	/**
	 * Constructor for the representation of the deletion of a node.
	 * @param nodeArray the number of the array of the deleted node
	 * @param nodePosition the position in the array of the deleted node.
	 */
	public MutationDeletionNode(int nodeArray, int nodePosition) {
		super();
		this.nodeArray = (short) nodeArray;
		this.nodePosition = (short) nodePosition;
	}

	/**
	 * @return the nodeArray
	 */
	public short getNodeArray() {
		return nodeArray;
	}

	/**
	 * @return the nodePosition
	 */
	public short getNodePosition() {
		return nodePosition;
	}
	
	
}