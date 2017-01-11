package com.visible.symbolic.jpf;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConcreteValueGeneratorTest {

    @Test
    public void testLesserThan() {
        ConcreteValueGenerator cvg = new ConcreteValueGenerator();
        cvg.addConstraint("x<y");

        Map<String, Integer> values = cvg.getConcreteValues();
        assertNotNull(values);

        assertTrue(values.get("x") < values.get("y"));
    }

    @Test
    public void testGreaterThan() {
        ConcreteValueGenerator cvg = new ConcreteValueGenerator();
        cvg.addConstraint("x>y");

        Map<String, Integer> values = cvg.getConcreteValues();
        assertNotNull(values);

        assertTrue(values.get("x") > values.get("y"));
    }

    @Test
    public void testLesserThanEqual() {
        ConcreteValueGenerator cvg = new ConcreteValueGenerator();
        cvg.addConstraint("x<=y");

        Map<String, Integer> values = cvg.getConcreteValues();
        assertNotNull(values);

        assertTrue(values.get("x") <= values.get("y"));
    }

    @Test
    public void testEqual() {
        ConcreteValueGenerator cvg = new ConcreteValueGenerator();
        cvg.addConstraint("x==y");

        Map<String, Integer> values = cvg.getConcreteValues();
        assertNotNull(values);

        assertTrue(values.get("x") == values.get("y"));
    }

    @Test
    public void testEqual2() {
        ConcreteValueGenerator cvg = new ConcreteValueGenerator();
        cvg.addConstraint("x<=y");
        cvg.addConstraint("x>=y");

        Map<String, Integer> values = cvg.getConcreteValues();
        assertNotNull(values);

        assertTrue(values.get("x") == values.get("y"));
    }

    @Test
    public void testLessThan3Values() {
        ConcreteValueGenerator cvg = new ConcreteValueGenerator();
        cvg.addConstraint("x<y");
        cvg.addConstraint("y<z");

        Map<String, Integer> values = cvg.getConcreteValues();
        assertNotNull(values);

        assertTrue(values.get("x") < values.get("y"));
        assertTrue(values.get("y") < values.get("z"));
        assertTrue(values.get("x") < values.get("z"));
    }

    @Test
    public void testGreaterThanConstant() {
        ConcreteValueGenerator cvg = new ConcreteValueGenerator();
        cvg.addConstraint("5<x");

        Map<String, Integer> values = cvg.getConcreteValues();
        assertNotNull(values);

        assertTrue(values.get("x") > 5);
    }
}