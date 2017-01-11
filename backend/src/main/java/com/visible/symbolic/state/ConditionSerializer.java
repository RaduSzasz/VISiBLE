package com.visible.symbolic.state;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ConditionSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        // Currently assumes that no &&, ||
        String[] delims = {"<=", ">=", "==", "<", ">"};

        String op = null;
        String[] vars = null;
        for (String delim : delims) {
            String[] splitStrings = value.split(delim);
            if (splitStrings.length > 1) {
                // Operator matched
                op = delim;
                vars = splitStrings;
                break;
            }
        }

        if (op == null || vars == null ) {
            // Parsing did not work
            gen.writeString(value);
            return;
        }

        String[] newVars = new String[vars.length];

        // Get rid of extra stuff in variable name
        for (int i = 0; i < newVars.length; i++) {
            newVars[i] = vars[i].split("_", 2)[0];
        }

        String condition = String.join(" " + op + " ", newVars);
        gen.writeString(condition);
    }


}
