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
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@SessionScope
public class JPFAdapter implements SymbolicExecutor {

    private static final String RELATIVE_PATH_TO_INPUT = "backend/input/";
    private static final String ABSOLUTE_PATH_TO_INPUT = System.getProperty("user.dir") + "/" + RELATIVE_PATH_TO_INPUT;
    private static final String JPF_EXTENSION = ".jpf";
    private static final String SITE_PROPERTIES_PRE_PATH = "+site=";
    private static final String SITE_PROPERTIES = "/site.properties";
    private static final String SOLVER = "no_solver";

    private static VisualiserListener visualiser;
    private String jarName;
    private String className;
    private String method;
    private int argNum;
    private State errorState;
    private boolean[] isSymb;

    @Autowired
    private ExecutorService service;

    public JPFAdapter(String jarName, String className, String methodName, int numArgs, boolean[] isSymb, ExecutorService executorService) {
        this.jarName = jarName;
        this.className = className;
        this.method = methodName;
        this.argNum = numArgs;
        this.service = executorService;
        this.isSymb = isSymb;
    }

    private void runJPF(CountDownLatch jpfInitialised) {
        String[] args = new String[2];
        String mainClassName;

        try {
            Manifest manifest = new JarFile(RELATIVE_PATH_TO_INPUT + "/" + jarName).getManifest();
            mainClassName = manifest.getMainAttributes().getValue("Main-Class");
        } catch (IOException e) {
            errorState = new State().withError("Manifest file in " + jarName + " could not be read.");
            return;
        }

        int indexOfDot = mainClassName.lastIndexOf('.');
        String jpfFileName = (indexOfDot == -1) ? mainClassName : mainClassName.substring(indexOfDot, mainClassName.length());
        jpfFileName += JPF_EXTENSION;

        File jpfFile = new File(ABSOLUTE_PATH_TO_INPUT + jpfFileName);
        try {
            boolean jpfFileCreated = true;
            if (!jpfFile.getParentFile().exists()) {
                jpfFileCreated = jpfFile.getParentFile().mkdirs();
            }

            if (jpfFile.exists()) {
                jpfFileCreated = jpfFileCreated && jpfFile.delete();
            }

            jpfFileCreated = jpfFileCreated && jpfFile.createNewFile();

            if (!jpfFileCreated) {
                throw new IOException();
            }
        } catch (IOException e) {
            errorState = new State().withError(jpfFileName + " could not be created");
            return;
        }

        args[0] = RELATIVE_PATH_TO_INPUT + jpfFileName;
        args[1] = SITE_PROPERTIES_PRE_PATH + System.getProperty("user.dir") + SITE_PROPERTIES;

        Config config = JPF.createConfig(args);
        config.setProperty("classpath", ABSOLUTE_PATH_TO_INPUT + jarName);
        config.setProperty("symbolic.dp", SOLVER);
        config.setProperty("target", mainClassName);
        String symbolicMethod = className + "." + method + getSymbArgs(isSymb, argNum);
        config.setProperty("symbolic.method", symbolicMethod);

        JPF jpf = new JPF(config);
        visualiser = new VisualiserListener(config, jpf, jpfInitialised);

        jpf.addListener(visualiser);

        service.submit(jpf);
    }

    private String getSymbArgs(boolean[] isSymb, int argNum) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < argNum - 1; i++) {
            if (isSymb[i]) {
                sb.append("sym#");
            } else {
                sb.append("con#");
            }
        }
        sb.append(isSymb[argNum - 1] ? "sym)" : "con)");
        return sb.toString();
    }

    private Optional<CountDownLatch> moveForward(Direction direction) {
        return visualiser.moveForward(direction);
    }

    @Override
    public State call() {
        CountDownLatch jpfInitialised = new CountDownLatch(1);
        runJPF(jpfInitialised);
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

    public State getErrorState() {
        return errorState;
    }
}