package com.visible.symbolic.jpf;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.HashMap;
import java.util.Map;

public class ConcreteValueGenerator {

    private Model model;
    private Map<String, IntVar> vars;

    private static final int LOWER = 0;
    private static final int UPPER = 100;

    public ConcreteValueGenerator() {
        this.model = new Model("Model");
        this.vars = new HashMap<>();
    }

    public boolean addConstraint(String constraint) {
        String[] delims = {"!=", "<=", ">=", "==", "<", ">"};

        String op = null;
        String[] variables = null;
        for (String delim : delims) {
            String[] splitStrings = constraint.split(delim);
            if (splitStrings.length > 1) {
                // Operator matched
                op = delim;
                variables = splitStrings;
                break;
            }
        }

        if (op == null || variables == null || variables.length < 2) {
            // Parsing did not work
            return false;
        }

        IntVar a = storeVariable(variables[0].trim());
        IntVar b = storeVariable(variables[1].trim());

        // Add the correct constraint
        switch(op) {
            case "!=":
                a.ne(b).post();
                break;
            case "<=":
                a.le(b).post();
                break;
            case ">=":
                a.ge(b).post();
                break;
            case "==":
                a.eq(b).post();
                break;
            case "<":
                a.lt(b).post();
                break;
            case ">":
                a.gt(b).post();
                break;
        }

        return true;
    }

    private IntVar storeVariable(String varName) {

        try {
            // Check if varName is actually an integer constant
            int constant = Integer.parseInt(varName);
            return model.intVar(constant);
        } catch (NumberFormatException e) {
            // varName is not a constant, treat it as a variable
            vars.computeIfAbsent(varName, k -> model.intVar(varName, LOWER, UPPER));
            return vars.get(varName);
        }

    }

    public Map<String, Integer> getConcreteValues() {
        Solver solver = model.getSolver();
        if (solver.solve()) {
            Map<String, Integer> values = new HashMap<>();
            for (String varName : vars.keySet()) {
                IntVar var = vars.get(varName);
                values.put(var.getName(), var.getValue());
            }

            solver.reset();
            return values;
        } else {
            // Could not find feasible solution
            return null;
        }
    }
}