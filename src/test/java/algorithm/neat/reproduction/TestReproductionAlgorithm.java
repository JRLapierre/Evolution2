package algorithm.neat.reproduction;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import algorithm.neat.Individual;
import brain.LayeredBrain;

class TestReproductionAlgorithm {
	
	@Test
	void testElitism() {
		ReproductionAlgorithm algo = new Elitism(5, 10, 15, 15);
		Individual[] population = new Individual[10];
		for (int i = 0; i < 10; i++) population[i] = new Individual(new LayeredBrain(0,0,0,0));
		Individual[] result = algo.reproduce(population);
		assertEquals(30, result.length);
		for (int i = 0; i<result.length; i++) {
			assertNotNull(result[i]);
		}
		Individual[] result2 = algo.reproduce(result);
		assertEquals(30, result2.length);
		for (int i = 0; i<result2.length; i++) {
			assertNotNull(result2[i]);
		}
	}
	
	@Test
	void testRouletteSelection() {
		ReproductionAlgorithm algo = new RouletteSelection(5, 10, 15, 15);
		Individual[] population = new Individual[10];
		for (int i = 0; i < 10; i++) population[i] = new Individual(new LayeredBrain(0,0,0,0));
		Individual[] result = algo.reproduce(population);
		assertEquals(30, result.length);
		for (int i = 0; i<result.length; i++) {
			assertNotNull(result[i]);
		}
		Individual[] result2 = algo.reproduce(result);
		assertEquals(30, result2.length);
		for (int i = 0; i<result2.length; i++) {
			assertNotNull(result2[i]);
		}
	}
	
	@Test
	void testToByteElitism() {
		ReproductionAlgorithm algo = new Elitism(5, 10, 15, 15);
		ByteBuffer bb = ByteBuffer.wrap(algo.toByte());
		ReproductionAlgorithm copy = ReproductionAlgorithm.restore(bb);
		byte[] originalArray = algo.toByte();
		byte[] copyArray = copy.toByte();
		assertEquals(originalArray.length, copyArray.length);
		for (int i = 0; i < originalArray.length; i++) {
			assertEquals(originalArray[i], copyArray[i]);
		}
	}
	
	@Test
	void testToByteRouletteSelection() {
		ReproductionAlgorithm algo = new RouletteSelection(5, 10, 15, 15);
		ByteBuffer bb = ByteBuffer.wrap(algo.toByte());
		ReproductionAlgorithm copy = ReproductionAlgorithm.restore(bb);
		byte[] originalArray = algo.toByte();
		byte[] copyArray = copy.toByte();
		assertEquals(originalArray.length, copyArray.length);
		for (int i = 0; i < originalArray.length; i++) {
			assertEquals(originalArray[i], copyArray[i]);
		}
	}

}
