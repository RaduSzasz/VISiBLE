package com.visible.web;

import com.visible.symbolic.state.State;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Configuration
@RestController
@Scope("session")
@RequestMapping("/symbolicmethod")

public class SymMethodController {

    @PostMapping
    public State runSelectedSymMethod(@RequestParam("class_name") String className, @RequestParam("method_name") String methodName, @RequestParam("no_args") int numArgs, @RequestParam("is_symb") boolean[] isSymb,  RedirectAttributes redirectAttributes)
            throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException {

        if (!(isSymb.length == numArgs)) {
            return new State().withError("Mismatch in number of arguments");
        }
        return new State().withError("Work in Progress");
    }
}
