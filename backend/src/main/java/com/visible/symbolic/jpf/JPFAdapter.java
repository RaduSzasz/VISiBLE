package com.visible.symbolic.jpf;

import com.visible.symbolic.Direction;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@SessionScope
public class JPFAdapter implements SymbolicExecutor {

    private static VisualiserListener visualiser;
    private String name;
    private String method;
    private int argNum;
    private static final String PATH_TO_INPUT = "input/";
    private static final String JPF_EXTENSION = ".jpf";
    private static final String SITE_PROPERTIES_PRE_PATH = "+site=";
    private static final String SITE_PROPERTIES = "site.properties";
    private static final String SOLVER = "no_solver";

    @Autowired
    private ExecutorService service;

    public JPFAdapter(String name, String method, int argNum, ExecutorService service) {
        this.name = name;
        this.method = method;
        this.argNum = argNum;
        this.service = service;
    }

    private void runJPF(String mainClassName, String method, int argNum, CountDownLatch jpfInitialised) {
        String[] args = new String[2];
        String path = System.getProperty("user.dir") + "/" + PATH_TO_INPUT;
        System.out.println(path);
        File jpfFile = new File(path + mainClassName + JPF_EXTENSION);
        try {
            jpfFile.createNewFile();
        } catch (IOException e) {
            System.err.println(mainClassName + JPF_EXTENSION + " could not be created");
            return;
        }

        args[0] = PATH_TO_INPUT + mainClassName + JPF_EXTENSION;
        try {
            File f = new File(JPFAdapter.class.getResource(SITE_PROPERTIES).toURI());
            System.out.println(f.toPath().toString());
            args[1] = SITE_PROPERTIES_PRE_PATH + f.toPath().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Config config = JPF.createConfig(args);
        config.setProperty("symbolic.dp", SOLVER);
        config.setProperty("target", mainClassName);
        String symbolicMethod = mainClassName + "." + method + getSymbArgs(argNum);
        config.setProperty("symbolic.method", symbolicMethod);

        JPF jpf = new JPF(config);
        visualiser = new VisualiserListener(config, jpf, jpfInitialised);

        jpf.addListener(visualiser);

        service.submit(jpf);
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
    public State call() {
        CountDownLatch jpfInitialised = new CountDownLatch(1);
        runJPF(name, method, argNum, jpfInitialised);
        try {
            jpfInitialised.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return visualiser.getCurrentState();
    }

    private State makeStep(Direction direction) {
        moveForward(direction).ifPresent(latch -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return visualiser.getCurrentState();
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