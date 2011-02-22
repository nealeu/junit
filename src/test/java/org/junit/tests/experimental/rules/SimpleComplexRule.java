package org.junit.tests.experimental.rules;

import org.junit.rules.ComplexRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Simple {@link ComplexRule} implementation to provide before and after for
 * methods and classes.
 */
public class SimpleComplexRule extends ComplexRule {
	@Override
	protected Statement applyMethodRule(final Statement base, Description description, Object target) {

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				beforeMethod();
				try {
					base.evaluate();
				} finally {
					afterMethod();
				}
			}
		};
	}

	protected void beforeMethod() throws Throwable {
		// for override
	}

	protected void afterMethod() {
		// for override
	}

	@Override
	protected Statement applyClassRule(final Statement base, Description description, Class<?> clazz) {

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				beforeClass();
				try {
					base.evaluate();
				} finally {
					afterClass();
				}
			}
		};
	}

	protected void beforeClass() throws Throwable {
		// for override
	}

	protected void afterClass() {
		// for override
	}

}
