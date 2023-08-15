package brain;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import brain.mutation.MutationAdditionLink;
import brain.mutation.MutationAdditionNode;
import brain.mutation.MutationDeletionLink;
import brain.mutation.MutationDeletionNode;
import brain.mutation.MutationLinkExtremity;
import brain.mutation.MutationLinkFactor;

class TestBrain {

	@Test
	void testFlexibleBrain() {
		//main test of the computing
		FlexibleBrain b1 = new FlexibleBrain(1,1,1);
		b1.addLink(b1.getInputs()[0], b1.getHidden()[0], 1f);
		b1.addLink(b1.getHidden()[0], b1.getOutputs()[0], 1f);
		FlexibleBrain.setTimeToCompute(1);
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		results = b1.compute(input);
		assertEquals(0f, results[0]);
		FlexibleBrain.setTimeToCompute(2);
		results = b1.compute(input);
		assertEquals(1f, results[0]);
		FlexibleBrain.setTimeToCompute(3);
		results = b1.compute(input);
		assertEquals(2f, results[0]);
		
		FlexibleBrain b2 = new FlexibleBrain(1,0,1);
		b2.addLink(b2.getInputs()[0], b2.getOutputs()[0], 1f);
		FlexibleBrain.setTimeToCompute(1);
		input = new float[1];
		input[0] = 1f;
		results = b2.compute(input);
		assertEquals(1f, results[0]);
		
		//test changeRandomLinkExtremity
		b1.changeRandomLinkExtremity();
		b1.changeRandomLinkExtremity();
		b1.changeRandomLinkExtremity();
		
	}
	
	@Test
	void testFlexibleBrainAdditionLink() {
		FlexibleBrain b2 = new FlexibleBrain(1,0,1);
		b2.addLink(b2.getInputs()[0], b2.getOutputs()[0], 1f);
		
		//test of the addition of a link
		b2.addRandomLink(1);
		assertEquals(2, b2.getLinks().size());
	}
	
	@Test
	void testFlexibleBrainAdditionNode() {
		FlexibleBrain b1 = new FlexibleBrain(1,1,1);
		b1.addLink(b1.getInputs()[0], b1.getHidden()[0], 1f);
		b1.addLink(b1.getHidden()[0], b1.getOutputs()[0], 1f);
		//test of the addition of a node
		b1.addNode();
		b1.addRandomNode();
		assertEquals(3, b1.getHidden().length);
	}
	
	@Test
	void testFlexibleBrainDeleleteLink() {
		float[] input = new float[1];
		input[0] = 1f;
		FlexibleBrain b1 = new FlexibleBrain(1,1,1);
		b1.addLink(b1.getInputs()[0], b1.getHidden()[0], 1f);
		b1.addLink(b1.getHidden()[0], b1.getOutputs()[0], 1f);
		//test of the deletion of a link
		b1.deleteRandomLink();
		assertEquals(1, b1.getLinks().size());
		FlexibleBrain.setTimeToCompute(10);
		assertEquals(0f, b1.compute(input)[0]);
	}
	
	@Test
	void testFlexibleBrainDeleteNode() {
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		//test of the deletion of a node
		FlexibleBrain.setTimeToCompute(2);
		FlexibleBrain b3 = new FlexibleBrain(1,2,2);
		b3.addLink(b3.getInputs()[0], b3.getHidden()[0], 1f);
		b3.addLink(b3.getHidden()[0], b3.getOutputs()[0], 1.5f);
		b3.addLink(b3.getInputs()[0], b3.getHidden()[1], 1f);
		b3.addLink(b3.getHidden()[1], b3.getOutputs()[1], 2f);
		results = b3.compute(input);
		assertEquals(1.5f, results[0]);
		assertEquals(2f, results[1]);
		b3.deleteNode(0);
		results = b3.compute(input);
		assertEquals(0f, results[0]);
		assertEquals(2f, results[1]);
		assertEquals(2, b3.getLinks().size());
		assertEquals(1, b3.getHidden().length);
	}
	
	@Test
	void testFlexibleBrainChangeLinkValue() {
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		//test of the deletion of a node
		FlexibleBrain.setTimeToCompute(2);
		FlexibleBrain b3 = new FlexibleBrain(1,2,2);
		b3.addLink(b3.getInputs()[0], b3.getHidden()[0], 1f);
		b3.addLink(b3.getHidden()[0], b3.getOutputs()[0], 1.5f);
		b3.addLink(b3.getInputs()[0], b3.getHidden()[1], 1f);
		b3.addLink(b3.getHidden()[1], b3.getOutputs()[1], 2f);
		b3.deleteNode(0);
		//test change random link value
		b3.changeRandomLinkFactor(50);
		results = b3.compute(input);
		assertFalse(2f == results[1]);
	}
	
	@Test
	void testFlexibleBrainBinary() {
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		//test of the save
		FlexibleBrain.setTimeToCompute(2);
		FlexibleBrain b4 = new FlexibleBrain(1,2,2);
		b4.addLink(b4.getInputs()[0], b4.getHidden()[0], 1f);
		b4.addLink(b4.getHidden()[0], b4.getOutputs()[0], 1.5f);
		b4.addLink(b4.getInputs()[0], b4.getHidden()[1], 1f);
		b4.addLink(b4.getHidden()[1], b4.getOutputs()[1], 2f);
		
		byte[] byteArray = b4.toBytes();
		ByteBuffer bb = ByteBuffer.wrap(byteArray);
		Brain b5 = Brain.restore(bb);
		results = b5.compute(input);
		assertEquals(1.5f, results[0]);
		assertEquals(2f, results[1]);
	}
	
	@Test
	void testFlexibleBrainCopy() {
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		//test of the save
		FlexibleBrain.setTimeToCompute(2);
		FlexibleBrain b4 = new FlexibleBrain(1,2,2);
		b4.addLink(b4.getInputs()[0], b4.getHidden()[0], 1f);
		b4.addLink(b4.getHidden()[0], b4.getOutputs()[0], 1.5f);
		b4.addLink(b4.getInputs()[0], b4.getHidden()[1], 1f);
		b4.addLink(b4.getHidden()[1], b4.getOutputs()[1], 2f);
		
		Brain b5 = b4.duplicate();
		results = b5.compute(input);
		assertEquals(1.5f, results[0]);
		assertEquals(2f, results[1]);
	}
	
	@Test
	void testFlexibleBrainMutationLogDeleteNode() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1,1,1);
		b.addLink(b.getInputs()[0], b.getOutputs()[0], 1);
		b.addLink(b.getInputs()[0], b.getHidden()[0], 1);
		b.addLink(b.getHidden()[0], b.getOutputs()[0], 1);
		b.deleteRandomNode();
		assertEquals(0, b.getHidden().length);
		assertEquals(1, b.getLinks().size());
		assertEquals(1,  b.mutations.size());
		assertTrue(b.mutations.get(0) instanceof MutationDeletionNode);
		assertEquals(2, ((MutationDeletionNode) b.mutations.get(0)).getNodeArray());
		assertEquals(0, ((MutationDeletionNode) b.mutations.get(0)).getNodePosition());
	}
	
	@Test
	void testFlexibleBrainMutationLogDeleteLink() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1, 0, 1);
		b.addLink(b.getInputs()[0], b.getOutputs()[0], 1);
		b.deleteRandomLink();
		assertEquals(0, b.getLinks().size());
		assertEquals(1, b.mutations.size());
		assertTrue(b.mutations.get(0) instanceof MutationDeletionLink);
		assertEquals(1, ((MutationDeletionLink) b.mutations.get(0)).getOriginArray());
		assertEquals(0, ((MutationDeletionLink) b.mutations.get(0)).getOriginPosition());
		assertEquals(3, ((MutationDeletionLink) b.mutations.get(0)).getTargetArray());
		assertEquals(0, ((MutationDeletionLink) b.mutations.get(0)).getTargetPosition());
		assertEquals(1, ((MutationDeletionLink) b.mutations.get(0)).getFactor());
	}
	
	@Test
	void testFlexibleBrainMutationLogAddNode() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1, 0, 1);
		b.addRandomNode();
		b.addRandomNode();
		assertEquals(2, b.getHidden().length);
		assertTrue(b.mutations.get(0) instanceof MutationAdditionNode);
		assertTrue(b.mutations.get(1) instanceof MutationAdditionNode);
	}
	
	@Test
	void testFlexibleBrainMutationLogAddLink() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1, 0, 1);
		b.addRandomLink(0.1f);
		assertEquals(1, b.getLinks().size());
		assertTrue(b.mutations.get(0) instanceof MutationAdditionLink);
		assertEquals(1, ((MutationAdditionLink) b.mutations.get(0)).getOriginArray());
		assertEquals(0, ((MutationAdditionLink) b.mutations.get(0)).getOriginPosition());
		assertEquals(3, ((MutationAdditionLink) b.mutations.get(0)).getTargetArray());
		assertEquals(0, ((MutationAdditionLink) b.mutations.get(0)).getTargetPosition());
	}
	
	@Test
	void testFlexibleBrainMutationLogModifyFactor() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1, 0, 1);
		b.addLink(b.getInputs()[0], b.getOutputs()[0], 1);
		b.changeRandomLinkFactor(50);
		assertTrue(b.mutations.get(0) instanceof MutationLinkFactor);
		assertEquals(1, ((MutationLinkFactor) b.mutations.get(0)).getOriginArray());
		assertEquals(0, ((MutationLinkFactor) b.mutations.get(0)).getOriginPosition());
		assertEquals(3, ((MutationLinkFactor) b.mutations.get(0)).getTargetArray());
		assertEquals(0, ((MutationLinkFactor) b.mutations.get(0)).getTargetPosition());
		assertEquals(1, ((MutationLinkFactor) b.mutations.get(0)).getOldFactor());
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		//test of the save
		FlexibleBrain.setTimeToCompute(1);
		results = b.compute(input);
		assertEquals(results[0], ((MutationLinkFactor) b.mutations.get(0)).getOldFactor()
				+ ((MutationLinkFactor) b.mutations.get(0)).getChangement());
	}
	
	@Test
	void testFlexibleBrainMutationLogModifyExtremity() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1, 50, 1);
		b.addLink(b.getInputs()[0], b.getOutputs()[0], 1);
		b.changeRandomLinkExtremity();
		assertTrue(b.mutations.get(0) instanceof MutationLinkExtremity);
		assertTrue(b.getLinks().get(0) == ((MutationLinkExtremity) b.mutations.get(0)).getLink());
	}
	
	@Test
	void testLayeredBrainLinkFactor() {
		Brain.setTraceMutation(true);
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(0);
		LayeredBrain b = new LayeredBrain(2, 2, 0, 0);
		b.changeLinkFactor(1, 0, 0, 0);
		float[] inputs = new float[] {1, 1};
		float[] outputs = b.compute(inputs);
		assertEquals(1, outputs[0]);
		assertEquals(0, outputs[1]);
		
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain b2 = new LayeredBrain(1, 1, 0, 0);
		outputs = b2.compute(new float[] {1});
		assertTrue(outputs[0] == 1);
		b2.changeRandomLinkFactor(50);
		outputs = b2.compute(new float[] {1});
		assertFalse(outputs[0] == 1);
		
		assertTrue(b2.mutations.get(0) instanceof MutationLinkFactor);
		assertEquals(0, ((MutationLinkFactor) b2.mutations.get(0)).getOriginArray());
		assertEquals(0, ((MutationLinkFactor) b2.mutations.get(0)).getOriginPosition());
		assertEquals(1, ((MutationLinkFactor) b2.mutations.get(0)).getTargetArray());
		assertEquals(0, ((MutationLinkFactor) b2.mutations.get(0)).getTargetPosition());
		assertEquals(1, ((MutationLinkFactor) b2.mutations.get(0)).getOldFactor());
		assertEquals(outputs[0] - 1, ((MutationLinkFactor) b2.mutations.get(0)).getChangement());
	}
	
	@Test
	void testLayeredBrainAdditionNode() {
		Brain.setTraceMutation(true);
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		LayeredBrain b = new LayeredBrain(1, 1, 1, 0);
		float[] inputs = new float[] {1};
		float[] outputs = b.compute(inputs);
		assertEquals(0, outputs[0]);
		b.addRandomNode();
		outputs = b.compute(inputs);
		assertEquals(1, outputs[0]);
		
		assertTrue(b.mutations.get(0) instanceof MutationAdditionNode);
		assertEquals(1, ((MutationAdditionNode) b.mutations.get(0)).getLayer());

	}
	
	@Test
	void testLayeredBrainDeletionNode() {
		Brain.setTraceMutation(true);
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		LayeredBrain b = new LayeredBrain(1, 1, 2, 2);
		float[] inputs = new float[] {1};
		float[] outputs = b.compute(inputs);
		assertEquals(4, outputs[0]);
		b.deleteRandomNode();
		outputs = b.compute(inputs);
		assertEquals(2, outputs[0]);
		
		assertTrue(b.mutations.get(0) instanceof MutationDeletionNode);
	}
	
	@Test
	void testLayeredBrainCopy() {
		LayeredBrain.setDefaultLinkValue(0.7f);
		LayeredBrain.setDefaultLinkVariation(0.3f);
		LayeredBrain original = new LayeredBrain(1, 1, 2, 3);
		original.addRandomNode();
		original.deleteRandomNode();
		original.changeRandomLinkFactor(0.5f);
		original.changeRandomLinkFactor(0.5f);
		Brain copy = original.duplicate();
		float[] inputs = new float[] {1};
		float[] outputsOriginal = original.compute(inputs);
		float[] outputsCopy = copy.compute(inputs);
		assertEquals(outputsOriginal[0], outputsCopy[0]);
	}
	
	@Test
	void testLayeredBrainToByte() {
		LayeredBrain.setDefaultLinkValue(0.7f);
		LayeredBrain.setDefaultLinkVariation(0.3f);
		LayeredBrain original = new LayeredBrain(1, 1, 2, 3);
		original.addRandomNode();
		original.deleteRandomNode();
		original.changeRandomLinkFactor(0.5f);
		original.changeRandomLinkFactor(0.5f);
		byte[] array = original.toBytes();
		ByteBuffer bb = ByteBuffer.wrap(array);
		Brain copy = Brain.restore(bb);
		float[] inputs = new float[] {1};
		float[] outputsOriginal = original.compute(inputs);
		float[] outputsCopy = copy.compute(inputs);
		assertEquals(outputsOriginal[0], outputsCopy[0]);
	}

}
