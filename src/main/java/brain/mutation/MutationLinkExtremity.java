package brain.mutation;

import brain.FlexibleBrain;
import brain.FlexibleBrain.Link;

public class MutationLinkExtremity extends Mutation {
	private FlexibleBrain.Link link;
	private boolean origin;
	private short oldNodeArray;
	private short oldNodePosition;
	
	/**
	 * Constructor to create the representation of the changement of an extremity of a link.
	 * @param link the changed link
	 * @param origin true if the node is the origin of the link, false if it is the target.
	 * @param oldNodeArray The number of the node array
	 * @param oldNodePosition the position of the node in the array
	 */
	public MutationLinkExtremity(Link link, boolean origin, short oldNodeArray, short oldNodePosition) {
		super();
		this.link = link;
		this.origin = origin;
		this.oldNodeArray = oldNodeArray;
		this.oldNodePosition = oldNodePosition;
	}
	
	/**
	 * @return the link
	 */
	public FlexibleBrain.Link getLink() {
		return link;
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
	
}