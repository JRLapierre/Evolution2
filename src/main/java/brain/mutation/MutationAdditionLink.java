package brain.mutation;

public class MutationAdditionLink extends Mutation {
	private short originArray;
	private short originPosition;
	private short targetArray;
	private short targetPosition;
	private float factor;
	
	/**
	 * Constructor to create a representation of a addition of a link between two nodes.
	 * @param originArray the number of the array of the origin node
	 * @param originPosition the position of the origin node in the array
	 * @param targetArray the number of the array of the target node
	 * @param targetPosition the position of the target node in the array
	 * @param factor the factor of the link
	 */
	public MutationAdditionLink(
			short originArray, 
			short originPosition, 
			short targetArray, 
			short targetPosition,
			float factor
			) {
		super();
		this.originArray = originArray;
		this.originPosition = originPosition;
		this.targetArray = targetArray;
		this.targetPosition = targetPosition;
		this.factor = factor;
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
}