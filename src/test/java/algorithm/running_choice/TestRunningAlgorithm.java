package algorithm.running_choice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import algorithm.Evaluation;
import algorithm.LearningAlgorithm;
import algorithm.NEAT.Individual;
import algorithm.NEAT.NEATAlgorithm;
import algorithm.NEAT.reproduction.Elitism;
import algorithm.NEAT.reproduction.ReproductionAlgorithm;
import brain.Brain;
import brain.LayeredBrain;

class TestRunningAlgorithm {

	@Test
	void testDefaultRunningChoice() {
		RunningChoice choice = new DefaultRunning();
		assertTrue(choice.runningCondition());
	}
	
	@Test
	void testRunningIterations() {
		RunningChoice choice = new RunningIteration(50);
		for (int i = 0; i < 50; i++) {
			assertTrue(choice.runningCondition());
		}
		assertFalse(choice.runningCondition());
		assertFalse(choice.runningCondition());
		assertFalse(choice.runningCondition());
	}
	
	@Test
	void testRunningTime() {
		//the algorithm
		Evaluation evaluation = population -> {
			float[] input = new float[] {1};
			float result;
			for (Individual individual : population) {
				result = individual.getBrain().compute(input)[0];
				individual.updateScore(result);
			}
		};
		ReproductionAlgorithm reproduction = new Elitism(0, 0, 0, 5);
		Brain brain = new LayeredBrain(1,1,0, 0);
		LearningAlgorithm algo = new NEATAlgorithm(brain, reproduction, evaluation);
		RunningChoice choice = new RunningTime(algo, 500);//0.5 seconds
		algo.start();
		if (algo.isPaused()) algo.playPause();
		assertTrue(choice.runningCondition());
		//wait 0.2 seconds
		try {
			Thread.sleep(200);
			assertTrue(choice.runningCondition());
			
			//put in pause for 0.2 seconds
			algo.playPause();
			Thread.sleep(200);
			algo.playPause();
			assertTrue(choice.runningCondition());
			//the pause is taken in account
			Thread.sleep(200);
			assertTrue(choice.runningCondition());
			
			//the running time is well set
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertFalse(choice.runningCondition());
		algo.interrupt();
	}

}
