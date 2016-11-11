package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;

public class JPFAdapter {

    public static void runJPF(String name) {
        String[] args = new String[2];
        args[0] = "backend/jpf-core/src/examples" + name;
        args[1] = "+site=backend/site.properties";

        Config config = JPF.createConfig(args);
        config.setProperty("symbolic.dp", "choco");
        config.setProperty("target", name);

        JPF jpf = new JPF(config);
        TreeInfo treeInfo = new TreeInfo();
        PropertyListenerAdapter visualiser = new VisualiserListener(treeInfo);

        jpf.addListener(visualiser);
        jpf.run();
    }
}
