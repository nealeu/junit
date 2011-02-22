package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A rule that can apply behaviour to at both class and method level.
 *
 * @author Neale Upstone
 */
public abstract class ComplexRule {
	protected abstract Statement applyClassRule(Statement base, 
	        Description description, Class<?> clazz);

	protected abstract Statement applyMethodRule(Statement base,  
	        Description description, Object target);

}
