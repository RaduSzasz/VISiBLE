package com.visible;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.*;

@JsonSerialize(using=ClassMethodsSerializer.class)
public final class ClassMethods {

	private String jarName = "";

	private final Map<String, Set<MethodData>> classes = new HashMap<>();

	private String errorMsg;
	void addMethodToClass(String className, String methodName,
                          int numArgs, String signature) {
		Set<MethodData> methods = classes.get(className);
		if (methods == null) {
			methods = new HashSet<>();
		}
		MethodData method = new MethodData(methodName, numArgs, signature);
		methods.add(method);
		classes.put(className, methods);
	}

	public Map<String, Set<MethodData>> getClasses() {
		return classes;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public String getJarName() {
		return jarName;
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

		@Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            MethodData md = (MethodData) obj;
            return this.name.equals(md.name) && this.numArgs == md.numArgs && this.signature.equals(md.signature);
        }

        @Override
        public int hashCode() {
		    return numArgs;
        }

	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ClassMethods cm = (ClassMethods) obj;
        if (!(this.classes.equals(cm.classes))) return false;
        if (!(this.jarName.equals(cm.jarName))) return false;
        if (this.errorMsg == null && cm.errorMsg == null) return true;
        return this.errorMsg.equals(cm.errorMsg);
    }

    @Override
    public int hashCode() {
	    return classes.hashCode();
    }

}
