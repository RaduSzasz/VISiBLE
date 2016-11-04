package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;

public class JPFAdapter {

  public static void runJPF(String name) {
    String[] args = new String[2];
    args[0] = "backend/jpf-symbc/src/examples/" + name;
    args[1] = "+site=backend/site.properties";
    System.out.println(args[0]);
    Config config = JPF.createConfig(args);
    config.setProperty("symbolic.dp", "choco");
    JPF jpf = new JPF(config);
    Logger logger = new Logger();
    VisualiserListener listener = new VisualiserListener(name, logger);
    jpf.addListener(listener);
    jpf.run();
    System.out.println(logger);
  }


}
