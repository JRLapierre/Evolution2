package algorithm.NEAT;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import brain.Brain;
import brain.FlexibleBrain;
import brain.LayeredBrain;

class TestIndividual {

	@Test
	void testIndividualBrain() {
		Individual.setCountId(0);
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		Brain brain = new LayeredBrain(1,1,1,1);
		Individual individual = new Individual(brain);
		assertTrue(brain == individual.getBrain());
		assertEquals(1, individual.getBrain().compute(new float[] {1})[0]);
		assertEquals(1, individual.getId());
		assertEquals(0, individual.getParentId());
		assertEquals(-1, individual.getParent2Id());
		assertEquals(1, Individual.getCountId());
	}
	
	@Test
	void testIndividualReproduction() {
		Individual.setCountId(0);
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(2);
		Brain brain = new LayeredBrain(1,1,1,1);
		Individual original = new Individual(brain);
		Individual children1 = new Individual(original);
		assertFalse(original.getBrain() == children1.getBrain());
		assertEquals(original.getBrain().compute(new float[] {1})[0], 
				children1.getBrain().compute(new float[] {1})[0]);
		assertEquals(2, children1.getId());
		assertEquals(1, children1.getParentId());
		assertEquals(-1, children1.getParent2Id());
		assertEquals(2, Individual.getCountId());
	}
	
	@Test
	void testIndividualCombinaison() {
		Individual.setCountId(0);
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(2);
		Brain brain = new LayeredBrain(1,1,1,1);
		Individual original = new Individual(brain);
		Individual children1 = new Individual(original);
		children1.getBrain().addRandomNode();
		children1.getBrain().changeRandomLinkFactor(1);
		children1.getBrain().changeRandomLinkFactor(1);
		children1.getBrain().changeRandomLinkFactor(1);
		Individual children2 = new Individual(original, children1);
		assertFalse(original.getBrain() == children2.getBrain());
		assertFalse(children1.getBrain() == children2.getBrain());
		assertEquals(2, children1.getId());
		assertEquals(3, children2.getId());
		assertEquals(1, children1.getParentId());
		assertEquals(-1, children1.getParent2Id());
		assertEquals(1, children2.getParentId());
		assertEquals(2, children2.getParent2Id());
		assertEquals(3, Individual.getCountId());
	}
	
	@Test
	void testIndividualToByte() {
		Individual.setCountId(0);
		LayeredBrain.setDefaultLinkValue(0);
		LayeredBrain.setDefaultLinkVariation(2);
		Brain brain = new LayeredBrain(1,1,2,6);
		Individual original = new Individual(brain);
		ByteBuffer bb = ByteBuffer.wrap(original.toByte());
		Individual copy = new Individual(bb);
		assertEquals(original.getId(), copy.getId());
		assertEquals(original.getParentId(), copy.getParentId());
		assertEquals(original.getParent2Id(), copy.getParent2Id());
		assertEquals(original.getBrain().compute(new float[] {1})[0], 
				copy.getBrain().compute(new float[] {1})[0]);
		}
	
	@Test
	void testIndividualScore() {
		Individual individual = new Individual(new FlexibleBrain(1, 1, 1));
		assertEquals(0, individual.getScore());
		individual.updateScore(2);
		assertEquals(2, individual.getScore());
		individual.updateScore(-3.5f);
		assertEquals(-1.5f, individual.getScore());
	}

}
