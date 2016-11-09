package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.symbc.SymbolicListener;

public class JPFAdapter {

  public static void runJPF(String name) {
    String[] args = new String[2];
    args[0] = "backend/jpf-symbc/src/examples/demo/" + name;
    args[1] = "+site=backend/site.properties";
    String targetName = name.substring(0, name.lastIndexOf("."));
    Config config = JPF.createConfig(args);
    config.setProperty("symbolic.dp", "coral");
    config.setProperty("target", targetName);
    config.setProperty("symbolic.method", targetName + ".maxOf3(sym#sym#sym)");
    JPF jpf = new JPF(config);
    Logger logger = new Logger();
    //PropertyListenerAdapter symbolic = new SymbolicListener(config, jpf);
    PropertyListenerAdapter visualiser = new VisualiserListener(targetName, logger);
    jpf.addListener(visualiser);
    //jpf.addListener(symbolic);
    jpf.run();
    //System.out.println(logger);
  }


}