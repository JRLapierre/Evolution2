package algorithm.NEAT.reproduction;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import brain.Brain;
import brain.FlexibleBrain;
import brain.LayeredBrain;

class TestMutationManager {

	@Test
	void testMutate() {
		MutationManager manager = new MutationManager();
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		Brain b = new LayeredBrain(1,1,1,1);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		//no changes
		manager.mutate(b);
		manager.mutate(b);
		manager.mutate(b);
		manager.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		//changement of link value
		manager.setChangeLinkFactor(1, 1);
		manager.mutate(b);
		assertFalse(1f == b.compute(new float[]{1})[0]);
		//addition of a node
		b = new LayeredBrain(1,1,1,1);
		manager.setChangeLinkFactor(-1, 0);
		for (int i = 0; i<100; i++) manager.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		manager.setAddNode(1);
		manager.mutate(b);
		assertEquals(2f, b.compute(new float[]{1})[0]);
		//deletion of a node
		manager.setAddNode(0);
		for (int i = 0; i<100; i++) manager.mutate(b);
		assertEquals(2f, b.compute(new float[]{1})[0]);
		manager.setDeleteNode(1);
		manager.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		manager.mutate(b);
		assertEquals(0f, b.compute(new float[]{1})[0]);
		manager.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
		//addition of a link
		manager.setDeleteNode(0);
		b = new FlexibleBrain(1,0,1);
		assertEquals(0, b.compute(new float[]{1})[0]);
		manager.setAddLink(1, 2);
		manager.mutate(b);
		float result = b.compute(new float[]{1})[0];
		assertTrue(0 != result);
		//deletion of a link
		manager.setAddLink(0, 12);
		for (int i = 0; i<100; i++) manager.mutate(b);
		assertEquals(result, b.compute(new float[]{1})[0]);
		manager.setDeleteLink(1);
		manager.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
		manager.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
		//changement of a link extremity
		manager.setDeleteLink(0);
		b.addRandomLink(1);
		b.addRandomNode();
		result = b.compute(new float[]{1})[0];
		assertTrue(0 != result);
		manager.setChangeLinkExtremity(1);
		manager.mutate(b);
		//how can I be sure when the mutation can lead to the initial situation ?
	}
	
	@Test
	void testToByte() {
		MutationManager manager = new MutationManager();
		manager.setAddLink(0.5f, 12);
		manager.setAddNode(45.4f);
		manager.setDeleteLink(0.2f);
		ByteBuffer bb = ByteBuffer.wrap(manager.toByte());
		MutationManager managerCopy = MutationManager.restore(bb);
		byte[] originalArray = manager.toByte();
		byte[] copyArray = managerCopy.toByte();
		assertEquals(originalArray.length, copyArray.length);
		for (int i = 0; i < originalArray.length; i++) {
			assertEquals(originalArray[i], copyArray[i]);
		}
		
	}
}
