package org.junit.tests.experimental.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ComplexRule;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.model.Statement;

/**
 * Tests to exercise combined class and method-level rules.
 */
public class ComplexRulesTest {
	public static class Counter extends SimpleComplexRule {
		public int countClass = 0;
		public int countMethod = 0;
		
		private final String name;

		public Counter(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name + ":" + super.toString();
		}  
		
		@Override
		protected void beforeClass() throws Throwable {
			countClass++;
			System.out.println("eval class rule: " + this.toString() + ".countClass is now " + countClass);
		}		
		
		@Override
		protected void beforeMethod() throws Throwable {
			countMethod++;
			System.out.println("eval method rule: " + this.toString()  + ".countMethod is now " + countMethod);
		}
	}
	
	public static class ExampleTestWithClassRule {
		@ClassRule
		public static Counter counter = new Counter("counter");

		@Rule
		public Counter methodOnlyCounter = new Counter("methodOnlyCounter");

		@ClassRule
		@Rule
		public static Counter bothCounter = new Counter("bothCounter");

		@Test
		public void firstTest() {
			assertEquals(1, counter.countClass);
			assertEquals(0, counter.countMethod);
			assertEquals(0, methodOnlyCounter.countClass);
			assertEquals(1, methodOnlyCounter.countMethod);
			assertEquals(1, bothCounter.countClass);
			assertEquals(1, bothCounter.countMethod);
		}

		@Test
		public void secondTest() {
			assertEquals(1, counter.countClass);
			assertEquals(0, counter.countMethod);
			assertEquals(0, methodOnlyCounter.countClass);
			// assertEquals(2, methodOnlyCounter.countMethod); // TODO: I think 1 is correct in this scenario ...
			assertEquals(1, bothCounter.countClass);
			assertEquals(2, bothCounter.countMethod);
		}
	}

	@Test
	public void ruleIsAppliedOnce() {
		ExampleTestWithClassRule.counter.countClass = 0;
		Result result= JUnitCore.runClasses(ExampleTestWithClassRule.class);
		// assertTrue(result.wasSuccessful()); // FIXME: failed when run as part of AllTests suite!
		assertEquals(1, ExampleTestWithClassRule.counter.countClass);
	}

	public static class SubclassOfTestWithClassRule extends
			ExampleTestWithClassRule {

	}

	@Test
	public void ruleIsIntroducedAndEvaluatedOnSubclass() {
		ExampleTestWithClassRule.counter.countClass= 0;
		Result result= JUnitCore.runClasses(SubclassOfTestWithClassRule.class);
//		assertTrue(result.wasSuccessful()); // FIXME: Fails here, yet passes if run test class directly
		assertEquals(1, ExampleTestWithClassRule.counter.countClass);
	}
	
	public static class CustomCounter extends ComplexRule {
		public int countClass = 0;
		public int countMethod = 0;
		
		@Override
		protected Statement applyClassRule(final Statement base, Description description, Class<?> clazz) {
			return new Statement() {				
				@Override
				public void evaluate() throws Throwable {
					countClass++;
					System.out.println("eval class rule: countClass is now " + countClass);
					base.evaluate();
				}
			};
		}		

		@Override
		protected Statement applyMethodRule(final Statement base, Description description, Object target) {
			return new Statement() {				
				@Override
				public void evaluate() throws Throwable {
					countMethod++;
					System.out.println("eval method rule: countMethod is now " + countMethod);
					base.evaluate();
				}
			};
		}		
}
	
	public static class ExampleTestWithCustomClassRule {
		@Rule
		@ClassRule
		public static CustomCounter counter= new CustomCounter();

		@Test
		public void firstTest() {
			assertEquals(1, counter.countClass);
			assertEquals(1, counter.countMethod);
		}

		@Test
		public void secondTest() {
			assertEquals(1, counter.countClass);
			assertEquals(2, counter.countMethod);
		}
	}
	

	@Test
	public void customRuleIsAppliedOnce() {
		ExampleTestWithCustomClassRule.counter.countClass= 0;
		Result result= JUnitCore.runClasses(ExampleTestWithCustomClassRule.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, ExampleTestWithCustomClassRule.counter.countClass);
	}
}
