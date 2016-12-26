package com.visible.jpf;

import com.visible.State;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;

import java.io.File;
import java.io.IOException;

public class JPFAdapter implements Runnable {

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

    private static void runJPF(String mainClassName, String method, int argNum) {
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
        visualiser = new VisualiserListener(config, jpf);

        jpf.addListener(visualiser);

        jpf.run();
    }

    private static String getSymbArgs(int n) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < n - 1; i++) {
            sb.append("sym#");
        }
        sb.append("sym)");
        return sb.toString();
    }

    public static State getListenerState() {
        try {
            return visualiser.getCurrentState();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean moveForward(Direction direction) {
        return visualiser.moveForward(direction);
    }

    @Override
    public void run() {
        JPFAdapter.runJPF(name, method, argNum);
    }
}