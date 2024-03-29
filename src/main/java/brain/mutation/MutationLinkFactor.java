package brain.mutation;

import java.nio.ByteBuffer;

public class MutationLinkFactor implements Mutation {
	private short originArray;
	private short originPosition;
	private short targetArray;
	private short targetPosition;
	private float oldFactor;
	private float changement;
	
	/**
	 * Constructor to create a representation of a mutation of the factor of a link.
	 * @param originArray the number of the array of the origin node
	 * @param originPosition the position of the origin node in the array
	 * @param targetArray the number of the array of the target node
	 * @param targetPosition the position of the target node in the array
	 * @param oldFactor the factor before the mutation
	 * @param changement the changement of the factor
	 */
	public MutationLinkFactor(
			int originArray, 
			int originPosition, 
			int targetArray, 
			int targetPosition,
			float oldFactor, 
			float changement
			) {
		this.originArray = (short) originArray;
		this.originPosition = (short) originPosition;
		this.targetArray = (short) targetArray;
		this.targetPosition = (short) targetPosition;
		this.oldFactor = oldFactor;
		this.changement = changement;
	}
	
	/**
	 * constructor to recreate the trace of the mutation from a save
	 * @param bb the ByteBuffer containing the informations
	 */
	protected MutationLinkFactor(ByteBuffer bb) {
		this.originArray = bb.getShort();
		this.originPosition = bb.getShort();
		this.targetArray = bb.getShort();
		this.targetPosition = bb.getShort();
		this.oldFactor = bb.getFloat();
		this.changement = bb.getFloat();
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
	 * @return the oldFactor
	 */
	public float getOldFactor() {
		return oldFactor;
	}

	/**
	 * @return the changement
	 */
	public float getChangement() {
		return changement;
	}

	@Override
	public byte[] toByte() {
		ByteBuffer bb = ByteBuffer.allocate(17);
		//type byte (5)
		bb.put((byte) 5);
		//for each element
		bb.putShort(originArray);
		bb.putShort(originPosition);
		bb.putShort(targetArray);
		bb.putShort(targetPosition);
		bb.putFloat(oldFactor);
		bb.putFloat(changement);
		return bb.array();
	}
}