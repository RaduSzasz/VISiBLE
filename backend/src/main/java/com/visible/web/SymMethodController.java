package com.visible.web;

import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.jpf.JPFAdapter;
import com.visible.symbolic.state.State;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
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

    private String className;
    private String methodName;
    private int numArgs;
    private boolean[] isSymb;

//    @PostMapping
    @GetMapping
    public State runSelectedSymMethod(@RequestParam("class_name") String className, @RequestParam("method_name") String methodName, @RequestParam("no_args") int numArgs, /*8RequestParam("is_symb") boolean[] isSymb,*/  RedirectAttributes redirectAttributes)
            throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException {
        this.className = className;
        this.methodName = methodName;
        this.numArgs = numArgs;
//        this.isSymb = isSymb;
        this.isSymb = array();

        if (!(isSymb.length == numArgs)) {
            return new State().withError("Mismatch in number of arguments");
        }

        SymbolicExecutor symbolicExecutor = symbolicExecutor();
        return executorService().submit(symbolicExecutor).get();
    }

    @Bean
    @Scope("session")
    public SymbolicExecutor symbolicExecutor() {
        return new JPFAdapter(className, methodName, numArgs, isSymb, executorService());
    }

    // TODO: DEBUGGING ONLY, DELETE
    private boolean[] array() {
        boolean[] array = new boolean[numArgs];
        for (int i = 0; i < numArgs; i++) {
            array[i] = true;
        }
        return array;
    }

    @Bean
    @ApplicationScope
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(8);
    }
}