package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.listener.ChoiceTracker;

public class JPFAdapter {

  public static void runJPF(String name) {
    String[] args = new String[2];
    args[0] = "backend/jpf-core/src/examples/" + name;
    args[1] = "+site=backend/site.properties";
    System.out.println(args[0]);
    Config config = JPF.createConfig(args);
    JPF jpf = new JPF(config);
    Logger logger = new Logger();
    VisualiserListener listener = new VisualiserListener(name, logger);
    jpf.addListener(listener);
    jpf.run();
    System.out.println(logger);
  }


}
