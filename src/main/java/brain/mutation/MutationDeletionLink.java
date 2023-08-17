package brain.mutation;

import java.nio.ByteBuffer;

public class MutationDeletionLink extends Mutation {
	private short originArray;
	private short originPosition;
	private short targetArray;
	private short targetPosition;
	private float factor;
	
	/**
	 * Constructor to create a representation of a deletion of a link between two nodes.
	 * @param originArray the number of the array of the origin node
	 * @param originPosition the position of the origin node in the array
	 * @param targetArray the number of the array of the target node
	 * @param targetPosition the position of the target node in the array
	 * @param factor the factor of the link
	 */
	public MutationDeletionLink(
			int originArray, 
			int originPosition, 
			int targetArray, 
			int targetPosition,
			float factor
			) {
		this.originArray = (short) originArray;
		this.originPosition = (short) originPosition;
		this.targetArray = (short) targetArray;
		this.targetPosition = (short) targetPosition;
		this.factor = factor;
	}
	
	/**
	 * constructor to recreate the trace of the mutation from a save
	 * @param bb the ByteBuffer containing the informations
	 */
	protected MutationDeletionLink(ByteBuffer bb) {
		this.originArray = bb.getShort();
		this.originPosition = bb.getShort();
		this.targetArray = bb.getShort();
		this.targetPosition = bb.getShort();
		this.factor = bb.getFloat();
	}

	/**
	 * @return the originArray
	 */
	public short getOriginArray() {
		return originArray;
	}

	/**
	 * @return the originPosition
	 */
	public short getOriginPosition() {
		return originPosition;
	}

	/**
	 * @return the targetArray
	 */
	public short getTargetArray() {
		return targetArray;
	}

	/**
	 * @return the targetPosition
	 */
	public short getTargetPosition() {
		return targetPosition;
	}

	/**
	 * @return the factor
	 */
	public float getFactor() {
		return factor;
	}

	@Override
	public byte[] toByte() {
		ByteBuffer bb = ByteBuffer.allocate(13);
		//type byte (4)
		bb.put((byte) 4);
		//for each element
		bb.putShort(originArray);
		bb.putShort(originPosition);
		bb.putShort(targetArray);
		bb.putShort(targetPosition);
		bb.putFloat(factor);
		return bb.array();
	}
	
}
