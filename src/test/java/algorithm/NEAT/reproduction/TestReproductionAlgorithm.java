package algorithm.NEAT.reproduction;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import algorithm.NEAT.Individual;
import brain.Brain;
import brain.FlexibleBrain;
import brain.LayeredBrain;

class TestReproductionAlgorithm {

	@Test
	void testMutate() {
		ReproductionAlgorithm algo = new Elitism(0,0,0);
		LayeredBrain.setDefaultLinkValue(1);
		LayeredBrain.setDefaultLinkVariation(0);
		Brain b = new LayeredBrain(1,1,1,1);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		//no changes
		algo.mutate(b);
		algo.mutate(b);
		algo.mutate(b);
		algo.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		//changement of link value
		algo.setChangeLinkFactor(1, 1);
		algo.mutate(b);
		assertFalse(1f == b.compute(new float[]{1})[0]);
		//addition of a node
		b = new LayeredBrain(1,1,1,1);
		algo.setChangeLinkFactor(-1, 0);
		for (int i = 0; i<100; i++) algo.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		algo.setAddNode(1);
		algo.mutate(b);
		assertEquals(2f, b.compute(new float[]{1})[0]);
		//deletion of a node
		algo.setAddNode(0);
		for (int i = 0; i<100; i++) algo.mutate(b);
		assertEquals(2f, b.compute(new float[]{1})[0]);
		algo.setDeleteNode(1);
		algo.mutate(b);
		assertEquals(1f, b.compute(new float[]{1})[0]);
		algo.mutate(b);
		assertEquals(0f, b.compute(new float[]{1})[0]);
		algo.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
		//addition of a link
		algo.setDeleteNode(0);
		b = new FlexibleBrain(1,0,1);
		assertEquals(0, b.compute(new float[]{1})[0]);
		algo.setAddLink(1, 2);
		algo.mutate(b);
		float result = b.compute(new float[]{1})[0];
		assertTrue(0 != result);
		//deletion of a link
		algo.setAddLink(0, 12);
		for (int i = 0; i<100; i++) algo.mutate(b);
		assertEquals(result, b.compute(new float[]{1})[0]);
		algo.setDeleteLink(1);
		algo.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
		algo.mutate(b);
		assertEquals(0, b.compute(new float[]{1})[0]);
		//changement of a link extremity
		algo.setDeleteLink(0);
		b.addRandomLink(1);
		b.addRandomNode();
		result = b.compute(new float[]{1})[0];
		assertTrue(0 != result);
		algo.setChangeLinkExtremity(1);
		algo.mutate(b);
		//how can I be sure when the mutation can lead to the initial situation ?
	}
	
	@Test
	void testElitism() {
		ReproductionAlgorithm algo = new Elitism(10, 20, 15);
		Individual[] population = new Individual[10];
		for (int i = 0; i < 10; i++) population[i] = new Individual(new LayeredBrain(0,0,0,0));
		Individual[] result = algo.reproduce(population);
		assertEquals(30, result.length);
		for (int i = 0; i<result.length; i++) {
			assertTrue(result[i] != null);
		}
		Individual[] result2 = algo.reproduce(result);
		assertEquals(30, result2.length);
		for (int i = 0; i<result2.length; i++) {
			assertTrue(result2[i] != null);
		}
	}

}
