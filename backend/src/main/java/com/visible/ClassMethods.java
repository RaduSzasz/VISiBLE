package com.visible;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(using=ClassMethodsSerializer.class)
public class ClassMethods {

    private final Map<String, List<MethodData>> classes = new HashMap<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY) private String errorMsg;

	void addMethodToClass(String className, String methodName,
                          int numArgs, String signature) {
		List<MethodData> methods = classes.get(className);
		if (methods == null) {
			methods = new ArrayList<>();
		}
		MethodData method = new MethodData(methodName, numArgs, signature);
		methods.add(method);
		classes.put(className, methods);
	}

	public Map<String, List<MethodData>> getClasses() {
		return classes;
	}

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setError() {
        this.errorMsg = "Invalid JAR file.";
    }

    @Override
	public String toString() {
		try {
			return new ObjectMapper().writer()
					                 .withDefaultPrettyPrinter()
					                 .writeValueAsString(this);
        } catch (JsonProcessingException e) {
        	System.err.println("Unable to serialise ClassMethods");
            return null;
        }
	}
	
	private class MethodData {
		private String name;
		private int numArgs;
		private String signature;
		
		private MethodData(String name, int numArgs, String signature) {
			this.name = name;
			this.numArgs = numArgs;
			this.signature = signature;
		}

		public String getName() {
			return name;
		}

		public int getNumArgs() {
			return numArgs;
		}

		public String getSignature() {
			return signature;
		}
	}

}
