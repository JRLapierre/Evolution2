package brain.mutation;

import java.nio.ByteBuffer;

public interface Mutation {
	
	/**
	 * static function to recreate mutations from binary data
	 * @param bb the ByteBuffer containing the informations
	 * @return the mutation
	 */
	public static Mutation restore(ByteBuffer bb) {
		byte type = bb.get();
		switch(type) {
		case(1):
			return new MutationAdditionNode(bb);
		case(2):
			return new MutationDeletionNode(bb);
		case(3):
			return new MutationAdditionLink(bb);
		case(4):
			return new MutationDeletionLink(bb);
		case(5):
			return new MutationLinkFactor(bb);
		case(6):
			return new MutationLinkExtremity(bb);
		default:
			return null;
		}
	}
	
	/**
	 * method to get a binary code corresponding to the mutation. <br>
	 * for MutationAdditionNode (3 bytes): <br>
	 * 1 : type of mutation (always 1) <br>
	 * 2 : layer containing the new node <br>
	 * <br>
	 * for MutationDeletionNode (5 bytes) : <br>
	 * 1 : type of mutation (always 2) <br>
	 * 2 : layer containing the old node <br>
	 * 2 : position of the old node in the old layer <br>
	 * <br>
	 * for MutationAdditionLink : (13 bytes) : <br>
	 * 1 : type of mutation (always 3) <br>
	 * 2 : layer of the origin node <br>
	 * 2 : position of the origin node in the layer <br>
	 * 2 : layer of the target node <br>
	 * 2 : position of the target node in the layer <br>
	 * 4 : multiplicative factor between the two nodes <br>
	 * <br>
	 * for MutationDeletionLink : (13 bytes) : <br>
	 * 1 : type of mutation (always 4) <br>
	 * 2 : layer of the origin node <br>
	 * 2 : position of the origin node in the layer <br>
	 * 2 : layer of the target node <br>
	 * 2 : position of the target node in the layer <br>
	 * 4 : multiplicative factor between the two nodes <br>
	 * <br>
	 * for MutationLinkFactor : (17 bytes) : <br>
	 * 1 : type of mutation (always 5) <br>
	 * 2 : layer of the origin node <br>
	 * 2 : position of the origin node in the layer <br>
	 * 2 : layer of the target node <br>
	 * 2 : position of the target node in the layer <br>
	 * 4 : old multiplicative factor between the two nodes <br>
	 * 4 : changement to the multiplicative factor <br>
	 * <br>
	 * for MutationLinkExtremity : (14 bytes) : <br>
	 * 1 : type of the mutation (always 6) <br>
	 * 2 : array of the origin node of the link <br>
	 * 2 : position of the origin node of the link <br>
	 * 2 : array of the target node of the link <br>
	 * 2 : position of the target node of the link <br>
	 * 1 : 1 if the changed node is the origin, 0 otherwise <br>
	 * 2 : array of the old node of the link <br>
	 * 2 : position of the old node of the link <br>
	 * @return the code
	 */
	public abstract byte[] toByte();

}
