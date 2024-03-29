package brain;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

class TestMutationManager {
	
	@Test
	void testNoChanges() {
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
	}
	
	@Test
	void testChangeLinkFactor() {
		//init
		MutationManager manager = new MutationManager();
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		//case 1 : no additional mutation
		Brain b = new LayeredBrain(1,1,0,0);
		manager.setChangeLinkFactor(1.2f, 1);
		manager.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0], 1f);
		//case 2 : additional mutation
		b = new LayeredBrain(1,1,0,0);
		manager.setChangeLinkFactor(1.6f, 1);
		manager.mutate(b);
		assertEquals(2f, b.compute(new float[]{1})[0], 2f);
		//case 3 : no changes
		b = new LayeredBrain(1,1,0,0);
		manager.setChangeLinkFactor(50, 0);
		manager.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0]);
	}
	
	@Test
	void testAdditionNode() {
		MutationManager manager = new MutationManager();
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		LayeredBrain b = new LayeredBrain(1,1,1,1);
		manager.setAddNode(1);
		manager.mutate(b);
		assertEquals(2f, b.compute(new float[]{1})[0]);
		b = new LayeredBrain(1,1,1,1);
		manager.setAddNode(1.5f);
		manager.mutate(b);
		assertEquals(2.5f, b.compute(new float[]{1})[0], 0.5f);
	}
	
	@Test
	void testDeleteNode() {
		MutationManager manager = new MutationManager();
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		Brain b = new LayeredBrain(1,1,1,1);
		manager.setDeleteNode(1);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		manager.mutate(b);
		assertEquals(0f, b.compute(new float[]{1})[0]);
		manager.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
	}
	
	@Test
	void testAddLink() {
		MutationManager manager = new MutationManager();
		FlexibleBrain b = new FlexibleBrain(1,0,1);
		assertEquals(0, b.compute(new float[]{1})[0]);
		manager.setAddLink(1, 2);
		manager.mutate(b);
		assertEquals(1, b.getLinks().size());
		manager.setAddLink(3, 0);
		manager.mutate(b);
		assertEquals(1, b.getLinks().size());
	}
	
	@Test
	void testDeleteLink() {
		MutationManager manager = new MutationManager();
		Brain b = new FlexibleBrain(1,0,1);
		manager.setDeleteLink(1);
		manager.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
		manager.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
	}
	
	@Test
	void testChangeLinkExtremity() {
		MutationManager manager = new MutationManager();
		Brain b = new FlexibleBrain(1,0,1);
		//changement of a link extremity
		manager.setDeleteLink(0);
		b.addRandomLink(1);
		b.addRandomNode();
		float result = b.compute(new float[]{1})[0];
		assertNotEquals(0, result);
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
