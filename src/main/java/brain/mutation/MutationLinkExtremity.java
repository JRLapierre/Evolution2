package brain.mutation;

import java.nio.ByteBuffer;

public class MutationLinkExtremity extends Mutation {
	private short[] linkCoordinates;
	private boolean origin;
	private short oldNodeArray;
	private short oldNodePosition;
	
	/**
	 * Constructor to create the representation of the changement of an extremity of a link.
	 * @param link the coordinates of the changed link
	 * @param origin true if the node is the origin of the link, false if it is the target.
	 * @param oldNodeArray The number of the node array
	 * @param oldNodePosition the position of the node in the array
	 */
	public MutationLinkExtremity(short[] linkCoordinates, boolean origin, short oldNodeArray, short oldNodePosition) {
		super();
		this.linkCoordinates = linkCoordinates;
		this.origin = origin;
		this.oldNodeArray = oldNodeArray;
		this.oldNodePosition = oldNodePosition;
	}
	
	/**
	 * @return the link
	 */
	public short[] getLinkCoordinates() {
		return linkCoordinates;
	}
	/**
	 * @return the origin
	 */
	public boolean isOrigin() {
		return origin;
	}
	/**
	 * @return the oldNodeArray
	 */
	public short getOldNodeArray() {
		return oldNodeArray;
	}
	/**
	 * @return the nodePosition
	 */
	public short getOldNodePosition() {
		return oldNodePosition;
	}

	@Override
	public byte[] toByte() {
		ByteBuffer bb = ByteBuffer.allocate(14);
		//type byte (6)
		bb.put((byte) 6);
		//coordinates of the link
		bb.putShort(linkCoordinates[0]);
		bb.putShort(linkCoordinates[1]);
		bb.putShort(linkCoordinates[2]);
		bb.putShort(linkCoordinates[3]);
		//others values
		bb.put((byte) ((origin) ? 1 : 0));
		bb.putShort(oldNodeArray);
		bb.putShort(oldNodePosition);
		
		return bb.array();
	}
	
}