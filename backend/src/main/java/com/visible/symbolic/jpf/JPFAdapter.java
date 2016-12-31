package com.visible.symbolic.jpf;

import com.visible.symbolic.Direction;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class JPFAdapter implements SymbolicExecutor {

    private static VisualiserListener visualiser;
    private String name;
    private String method;
    private int argNum;
    private static final String PATH_TO_INPUT = "backend/input/";
    private static final String JPF_EXTENSION = ".jpf";
    private static final String SITE_PROPERTIES = "+site=backend/site.properties";
    private static final String SOLVER = "no_solver";

    public JPFAdapter(String name, String method, int argNum) {
        this.name = name;
        this.method = method;
        this.argNum = argNum;
    }

    private void runJPF(String mainClassName, String method, int argNum, CountDownLatch jpfInitialised) {
        String[] args = new String[2];
        String path = System.getProperty("user.dir") + "/" + PATH_TO_INPUT;
        File jpfFile = new File(path + mainClassName + JPF_EXTENSION);
        try {
            jpfFile.createNewFile();
        } catch (IOException e) {
            System.err.println(mainClassName + JPF_EXTENSION + " could not be created");
            return;
        }

        args[0] = PATH_TO_INPUT + mainClassName + JPF_EXTENSION;
        args[1] = SITE_PROPERTIES;

        Config config = JPF.createConfig(args);
        config.setProperty("symbolic.dp", SOLVER);
        config.setProperty("target", mainClassName);
        String symbolicMethod = mainClassName + "." + method + getSymbArgs(argNum);
        config.setProperty("symbolic.method", symbolicMethod);

        JPF jpf = new JPF(config);
        visualiser = new VisualiserListener(config, jpf, jpfInitialised);

        jpf.addListener(visualiser);

        jpf.run();
    }

    private String getSymbArgs(int n) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < n - 1; i++) {
            sb.append("sym#");
        }
        sb.append("sym)");
        return sb.toString();
    }

    public Optional<CountDownLatch> moveForward(Direction direction) {
        return visualiser.moveForward(direction);
    }

    @Override
    public CountDownLatch call() {
        CountDownLatch jpfInitialised = new CountDownLatch(1);
        runJPF(name, method, argNum, jpfInitialised);
        return jpfInitialised;
    }

    private State makeStep(Direction direction) {
        State state = visualiser.getCurrentState();
        moveForward(direction).ifPresent(latch -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return state;
    }

    @Override
    public State stepLeft() {
        return makeStep(Direction.LEFT);
    }

    @Override
    public State stepRight() {
        return makeStep(Direction.RIGHT);
    }
}