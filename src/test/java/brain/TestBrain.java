package brain;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import brain.mutation.Mutation;
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
		Brain.setTraceMutation(true);
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		//test of the save
		FlexibleBrain.setTimeToCompute(2);
		FlexibleBrain b4 = new FlexibleBrain(1,2,2);
		b4.addRandomLink(5f);
		b4.addRandomLink(5f);
		b4.addRandomLink(5f);
		b4.addRandomLink(5f);
		b4.changeRandomLinkFactor(2f);
		b4.changeRandomLinkFactor(2f);
		b4.changeRandomLinkFactor(2f);
		b4.changeRandomLinkFactor(2f);
		b4.changeRandomLinkExtremity();
		b4.changeRandomLinkExtremity();
		b4.changeRandomLinkExtremity();
		b4.changeRandomLinkExtremity();
		
		byte[] byteArray = b4.toBytes();
		ByteBuffer bb = ByteBuffer.wrap(byteArray);
		Brain b5 = Brain.restore(bb);
		results = b4.compute(input);
		float[] results2 = b5.compute(input);
		assertEquals(results[0], results2[0]);
		assertEquals(results[1], results2[1]);
		for (int i = 0; i < b4.mutations.size(); i++) {
			assertEquals(b4.mutations.get(i).getClass(), b5.mutations.get(i).getClass());
		}
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
		assertEquals(4,  b.mutations.size());
		assertTrue(b.mutations.get(3) instanceof MutationDeletionNode);
		assertEquals(2, ((MutationDeletionNode) b.mutations.get(3)).getNodeArray());
		assertEquals(0, ((MutationDeletionNode) b.mutations.get(3)).getNodePosition());
	}
	
	@Test
	void testFlexibleBrainMutationLogDeleteLink() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1, 0, 1);
		b.addLink(b.getInputs()[0], b.getOutputs()[0], 1);
		b.deleteRandomLink();
		assertEquals(0, b.getLinks().size());
		assertEquals(2, b.mutations.size());
		assertTrue(b.mutations.get(1) instanceof MutationDeletionLink);
		assertEquals(1, ((MutationDeletionLink) b.mutations.get(1)).getOriginArray());
		assertEquals(0, ((MutationDeletionLink) b.mutations.get(1)).getOriginPosition());
		assertEquals(3, ((MutationDeletionLink) b.mutations.get(1)).getTargetArray());
		assertEquals(0, ((MutationDeletionLink) b.mutations.get(1)).getTargetPosition());
		assertEquals(1, ((MutationDeletionLink) b.mutations.get(1)).getFactor());
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
		assertTrue(b.mutations.get(1) instanceof MutationLinkFactor);
		assertEquals(1, ((MutationLinkFactor) b.mutations.get(1)).getOriginArray());
		assertEquals(0, ((MutationLinkFactor) b.mutations.get(1)).getOriginPosition());
		assertEquals(3, ((MutationLinkFactor) b.mutations.get(1)).getTargetArray());
		assertEquals(0, ((MutationLinkFactor) b.mutations.get(1)).getTargetPosition());
		assertEquals(1, ((MutationLinkFactor) b.mutations.get(1)).getOldFactor());
		float[] input = new float[1];
		input[0] = 1f;
		float[] results;
		//test of the save
		FlexibleBrain.setTimeToCompute(1);
		results = b.compute(input);
		assertEquals(results[0], ((MutationLinkFactor) b.mutations.get(1)).getOldFactor()
				+ ((MutationLinkFactor) b.mutations.get(1)).getChangement());
	}
	
	@Test
	void testFlexibleBrainMutationLogModifyExtremity() {
		Brain.setTraceMutation(true);
		FlexibleBrain b = new FlexibleBrain(1, 50, 1);
		b.addLink(b.getInputs()[0], b.getOutputs()[0], 1);
		b.changeRandomLinkExtremity();
		assertTrue(b.mutations.get(1) instanceof MutationLinkExtremity);
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
		Brain.setTraceMutation(true);
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
		for (int i = 0; i < original.mutations.size(); i++) {
			assertEquals(original.mutations.get(i).getClass(), copy.mutations.get(i).getClass());
		}
	}
	
	@Test
	void testMutationsToByte() {
		//add node
		Mutation m1 = new MutationAdditionNode(2);
		ByteBuffer bb1 = ByteBuffer.wrap(m1.toByte());
		Mutation m1Copy = Mutation.restore(bb1);
		assertTrue(Arrays.equals(m1.toByte(), m1Copy.toByte()));
		//delete node
		Mutation m2 = new MutationDeletionNode(2, 3);
		ByteBuffer bb2 = ByteBuffer.wrap(m2.toByte());
		Mutation m2Copy = Mutation.restore(bb2);
		assertTrue(Arrays.equals(m2.toByte(), m2Copy.toByte()));
		//add link
		Mutation m3 = new MutationAdditionLink(2, 4, 3, 0, 0.5f);
		ByteBuffer bb3 = ByteBuffer.wrap(m3.toByte());
		Mutation m3Copy = Mutation.restore(bb3);
		assertTrue(Arrays.equals(m3.toByte(), m3Copy.toByte()));
		assertEquals(0.5f, ((MutationAdditionLink) m3).getFactor());
		//delete link
		Mutation m4 = new MutationDeletionLink(2, 4, 4, 0, 0.5f);
		ByteBuffer bb4 = ByteBuffer.wrap(m4.toByte());
		Mutation m4Copy = Mutation.restore(bb4);
		assertTrue(Arrays.equals(m4.toByte(), m4Copy.toByte()));
		//change factor
		Mutation m5 = new MutationLinkFactor(2, 5, 5, 0, 0.5f, 0.6f);
		ByteBuffer bb5 = ByteBuffer.wrap(m5.toByte());
		Mutation m5Copy = Mutation.restore(bb5);
		assertTrue(Arrays.equals(m5.toByte(), m5Copy.toByte()));
		//change extremity
		Mutation m6 = new MutationLinkExtremity(new short[] {2, 6, 6, 0}, true, 2, 3);
		ByteBuffer bb6 = ByteBuffer.wrap(m6.toByte());
		Mutation m6Copy = Mutation.restore(bb6);
		assertTrue(Arrays.equals(m6.toByte(), m6Copy.toByte()));
		MutationLinkExtremity m6cast = (MutationLinkExtremity) m6;
		assertEquals(2, m6cast.getLinkCoordinates()[0]);
		assertEquals(6, m6cast.getLinkCoordinates()[1]);
		assertEquals(6, m6cast.getLinkCoordinates()[2]);
		assertEquals(0, m6cast.getLinkCoordinates()[3]);
		assertTrue(m6cast.isOrigin());
		assertEquals(2, m6cast.getOldNodeArray());
		assertEquals(3, m6cast.getOldNodePosition());
	}
	
	@Test 
	void testLayeredBrainCombinaison() {
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(1f);
		//test of combinaison of two brains with the same model
		LayeredBrain b1 = new LayeredBrain(1, 1, 2, 1);
		LayeredBrain b2 = new LayeredBrain(1, 1, 2, 1);
		Brain b3 = Brain.combine(b1, b2);
		float[] input = new float[] {1};
		assertNotNull(b3.compute(input));
		//test of more cahotic fusions
		LayeredBrain b4 = new LayeredBrain(1, 1, 2, 3);
		Brain b5 = Brain.combine(b1, b4);
		assertNotNull(b5.compute(input));
		Brain b6 = Brain.combine(b4, b1);
		assertNotNull(b6.compute(input));
		b1.deleteRandomNode();
		Brain b7 = Brain.combine(b1, b4);
		assertNotNull(b7.compute(input));
	}
	
	@Test
	void testFlexibleBrainCombinaison() {
		FlexibleBrain.setTimeToCompute(2);
		float[] input = new float[] {1};
		//test for the FlexibleBrain
		FlexibleBrain b7 = new FlexibleBrain(1, 2, 1);
		FlexibleBrain b8 = new FlexibleBrain(1, 5, 1);
		Brain b9 = Brain.combine(b7, b8);
		assertEquals(5, ((FlexibleBrain) b9).getHidden().length);
		b7.addLink(b7.getInputs()[0], b7.getOutputs()[0], 1);
		Brain b10 = Brain.combine(b7, b8);
		assertEquals(b7.compute(input)[0]/2 , b10.compute(input)[0]);
		FlexibleBrain b11 = new FlexibleBrain(b7);
		b11.changeRandomLinkFactor(1);
		b7.addLink(b7.getInputs()[0], b7.getOutputs()[0], 0.5f);
		Brain b12 = Brain.combine(b7, b11);
		assertEquals(1, ((FlexibleBrain) b12).getLinks().size());
		assertEquals((b7.compute(input)[0] + b11.compute(input)[0])/2 , b12.compute(input)[0]);
		b11.addRandomLink(5);
		b11.addRandomLink(2);
		b11.addRandomLink(4);
		Brain b13 = Brain.combine(b7, b11);
		assertEquals((b7.compute(input)[0] + b11.compute(input)[0])/2 , b13.compute(input)[0]);
	}
	
	@Test
	void testBigBrain() {
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		LayeredBrain brain = new LayeredBrain(1, 1, 2, 20);
		assertEquals(400, brain.compute(new float[] {1})[0]);
		FlexibleBrain.setTimeToCompute(4);
		FlexibleBrain b2 = new FlexibleBrain(1, 300, 100);
		for (int i = 0; i < 100; i++) {
			b2.addLink(b2.getInputs()[0], b2.getHidden()[i], 1.0f);
		}
		for (int i = 100; i < 200; i++) {
			b2.addLink(b2.getHidden()[i-100], b2.getHidden()[i], 1.0f);
		}
		for (int i = 200; i < 300; i++) {
			b2.addLink(b2.getHidden()[i-100], b2.getHidden()[i], 1.0f);
		}
		for (int i = 0; i < 100; i++) {
			b2.addLink(b2.getHidden()[i+200], b2.getOutputs()[i], 1.0f);
		}
		float[] output = b2.compute(new float[] {1});
		for (int i = 0; i < 100; i++) {
			assertEquals(1.0f, output[i]);
		}
	}
	
	@Test
	void testFlexibleBrainNbOfCores() {
		FlexibleBrain b1 = new FlexibleBrain(1, 10, 1);
		for (int i = 0; i < 100; i++) {
			b1.addRandomLink(1);
			b1.changeRandomLinkFactor(0.5f);
		}
		FlexibleBrain b2 = new FlexibleBrain(1, 1, 1);
		for (int i = 0; i < 6; i++) {
			b2.addRandomLink(1);
			b2.changeRandomLinkFactor(0.5f);
		}
		//compare results
		Brain.setMaxUsableCores(8);
		float resultb11 = b1.compute(new float[] {1})[0];
		float resultb21 = b2.compute(new float[] {1})[0];
		Brain.setMaxUsableCores(1);
		float resultb12 = b1.compute(new float[] {1})[0];
		float resultb22 = b2.compute(new float[] {1})[0];
		assertEquals(resultb11, resultb12);
		assertEquals(resultb21, resultb22);
	}

}
