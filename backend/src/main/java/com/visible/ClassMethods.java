package com.visible;

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

	@Override
	public String toString() {
		System.out.println(classes.size());
		try {
			return new ObjectMapper().writer()
					                 .withDefaultPrettyPrinter()
					                 .writeValueAsString(this);
        } catch (JsonProcessingException e) {
        	System.out.println("Unable to serialise ClassMethods");
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
