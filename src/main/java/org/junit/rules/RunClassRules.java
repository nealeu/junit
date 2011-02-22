package org.junit.rules;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * Run the class-specific part of each Rule on a {@link Statement}
 *
 * @author Neale Upstone
 */
public class RunClassRules extends Statement {

	private final Statement statement;

	public RunClassRules(Statement base, List<ComplexRule> rules, Description description, TestClass testClass) {
			statement = applyAll(base, rules, description, testClass);
	}
	
	@Override
	public void evaluate() throws Throwable {
		statement.evaluate();
	}

	private static Statement applyAll(Statement result, Iterable<ComplexRule> rules, Description description, TestClass testClass) {
		for (ComplexRule each : rules) {
			result = each.applyClassRule(result, description, testClass.getJavaClass());
		}
		return result;
	}
}
