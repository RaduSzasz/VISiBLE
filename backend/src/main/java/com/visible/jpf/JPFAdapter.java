package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;

public class JPFAdapter {

  public static void runJPF(String name) {
    String[] args = new String[2];
    args[0] = "backend/jpf-core/src/examples/" + name + ".jpf";
    args[1] = "+site=backend/site.properties";

    Config config = JPF.createConfig(args);
    config.setProperty("symbolic.dp", "no_solver");
    config.setProperty("target", name);

    JPF jpf = new JPF(config);

    Logger logger = new Logger();
    PropertyListenerAdapter visualiser = new VisualiserListener(name, logger);
    jpf.addListener(visualiser);
    jpf.run();

    System.out.println(logger);
  }


}
