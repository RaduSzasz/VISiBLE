package com.visible.web;

import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.jpf.JPFAdapter;
import com.visible.symbolic.state.State;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RestController
@Scope("session")
@RequestMapping("/symbolicmethod")

public class SymMethodController {

    private static final int NUMBER_OF_THREADS = 8;
    private String jarName;
    private String className;
    private String methodName;
    private int numArgs;
    private boolean[] isSymb;

    @PostMapping
    public State runSelectedSymMethod(@RequestParam("jar_name") String jarName,
                                      @RequestParam("class_name") String className,
                                      @RequestParam("method_name") String methodName,
                                      @RequestParam("no_args") int numArgs,
                                      @RequestParam("is_symb") boolean[] isSymb, RedirectAttributes redirectAttributes)
            throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException {

        this.jarName = jarName;
        this.className = className;
        this.methodName = methodName;
        this.numArgs = numArgs;
        this.isSymb = isSymb;

        if (!(this.isSymb.length == numArgs)) {
            return State.createErrorState(State.ERR_ARG_MISMATCH);
        }

        return symbolicExecutor().execute();
    }


    @Bean
    @Scope("session")
    public SymbolicExecutor symbolicExecutor() {
        return new JPFAdapter(jarName, className, methodName, numArgs, isSymb);
    }

    @Bean
    @ApplicationScope
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }
}