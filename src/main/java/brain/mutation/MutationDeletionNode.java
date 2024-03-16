package brain.mutation;

import java.nio.ByteBuffer;

public class MutationDeletionNode implements Mutation {
	private short nodeArray;
	private short nodePosition;
	
	/**
	 * Constructor for the representation of the deletion of a node.
	 * @param nodeArray the number of the array of the deleted node
	 * @param nodePosition the position in the array of the deleted node.
	 */
	public MutationDeletionNode(int nodeArray, int nodePosition) {
		this.nodeArray = (short) nodeArray;
		this.nodePosition = (short) nodePosition;
	}
	
	/**
	 * constructor to recreate the trace of the mutation from a save
	 * @param bb the ByteBuffer containing the informations
	 */
	protected MutationDeletionNode(ByteBuffer bb) {
		this.nodeArray = bb.getShort();
		this.nodePosition = bb.getShort();
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

	@Override
	public byte[] toByte() {
		ByteBuffer bb = ByteBuffer.allocate(5);
		//byte for the type (2)
		bb.put((byte) 2);
		//shorts for the values
		bb.putShort(nodeArray);
		bb.putShort(nodePosition);
		return bb.array();
	}
	
	
}