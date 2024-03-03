package algorithm.autosave;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

class TestAutosave {

	@Test
	void testNoAutoSave() {
		AutosaveCondition condition = new NoAutoSave();
		assertFalse(condition.saveCondition());
	}
	
	@Test
	void testSaveIterations() {
		AutosaveCondition condition = new SaveIteration(5);
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertTrue(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertTrue(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertFalse(condition.saveCondition());
		assertTrue(condition.saveCondition());
	}
	
	@Test
	void testSaveTime() {
		//if we are in the github ci environnement
        Assumptions.assumeFalse("true".equals(System.getenv("CI")));
        //test
		AutosaveCondition condition = new SaveTime(200);
		assertFalse(condition.saveCondition());
		try {
			Thread.sleep(150);
			assertFalse(condition.saveCondition());
			Thread.sleep(50);
			assertTrue(condition.saveCondition());
			Thread.sleep(200);
			assertTrue(condition.saveCondition());
			Thread.sleep(100);
			assertFalse(condition.saveCondition());
			Thread.sleep(200);
			assertTrue(condition.saveCondition());
			Thread.sleep(115);
			assertFalse(condition.saveCondition());
			Thread.sleep(200);
			assertTrue(condition.saveCondition());
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
	}

}
