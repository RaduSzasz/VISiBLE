package webppl;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;

public class RunJPF {
    public static void main(String[] args) {
        String configArgs[] = new String[1];
        configArgs[0] = "/Desktop/Year3/jpf-symbc/src/examples/demo/NumericExample.jpf";
        Config config = JPF.createConfig(configArgs);

        JPF jpf = new JPF(config);
        jpf.addListener(VisualiserListener);
        jpf.run();
    }
}
