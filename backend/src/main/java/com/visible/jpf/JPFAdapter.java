package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;

public class JPFAdapter {

    public static void runJPF(String name, String method, int argNum) {
        String[] args = new String[2];
        args[0] = "backend/jpf-core/src/examples/" + name + ".jpf";
        args[1] = "+site=backend/site.properties";

        Config config = JPF.createConfig(args);
        config.setProperty("symbolic.dp", "no_solver");
        config.setProperty("target", name);
        String symbolicMethod = name + "." + method + getSymbArgs(argNum);
        config.setProperty("symbolic.method", symbolicMethod);

        JPF jpf = new JPF(config);
        TreeInfo treeInfo = new TreeInfo();
        PropertyListenerAdapter visualiser = new VisualiserListener(config, jpf, treeInfo);

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
}
