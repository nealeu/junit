package org.junit.rules;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Run the method-specific part of each Rule on a {@link Statement}
 *
 * @author Neale Upstone
 */
public class RunMethodRules extends Statement {

	private final Statement statement;

	public RunMethodRules(Statement base, List<ComplexRule> rules, Description description, Object target) {
			statement = applyAll(base, rules, description, target);
	}
	
	@Override
	public void evaluate() throws Throwable {
		statement.evaluate();
	}

	private static Statement applyAll(Statement result, Iterable<ComplexRule> rules, Description description, Object target) {
		for (ComplexRule each : rules) {
			result = each.applyMethodRule(result, description, target);
		}
		return result;
	}
}
