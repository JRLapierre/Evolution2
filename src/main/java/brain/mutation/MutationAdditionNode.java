package brain.mutation;

import java.nio.ByteBuffer;

public class MutationAdditionNode extends Mutation {
	private short layer;
	
	/**
	 * This mutation represents the addition of a hidden node.
	 * @param layer the layer of the new node in the brain.
	 */
	public MutationAdditionNode(int layer) {
		this.layer = (short) layer;
	}
	
	/**
	 * constructor to recreate the trace of the mutation from a save
	 * @param bb the ByteBuffer containing the informations
	 */
	protected MutationAdditionNode(ByteBuffer bb) {
		this.layer = bb.getShort();
	}
	
	/**
	 * getter for the layer
	 * @return the layer of the new node
	 */
	public short getLayer() {
		return layer;
	}

	@Override
	public byte[] toByte() {
		ByteBuffer bb = ByteBuffer.allocate(3);
		//the type
		bb.put((byte) 1);
		//the datas
		bb.putShort(layer);
		return bb.array();
	}
}
