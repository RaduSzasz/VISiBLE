package com.visible.web;

import com.visible.VisibleServerApplication;
import com.visible.conditions.DockerCondition;
import com.visible.conditions.MainServerCondition;
import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.docker.DockerContainer;
import com.visible.symbolic.docker.DockerizedExecutor;
import com.visible.symbolic.jpf.JPFAdapter;
import com.visible.symbolic.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@Configuration
@RestController
@Import(VisibleServerApplication.class)
@RequestMapping("/symbolicmethod")
public class SymMethodController {

    private static final int NUMBER_OF_THREADS = 8;
    private boolean isRestartCall = false;

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping
    public State runSelectedSymMethod(@RequestParam("jar_name") String jarName,
                                      @RequestParam("class_name") String className,
                                      @RequestParam("method_name") String methodName,
                                      @RequestParam("no_args") int numArgs,
                                      @RequestParam("is_symb") boolean[] isSymb,
                                      RedirectAttributes redirectAttributes)
            throws java.io.IOException, InterruptedException, ExecutionException, ClassNotFoundException, URISyntaxException {

        if (!(isSymb.length == numArgs)) {
            return State.createErrorState("Mismatch in number of argument.");
        }

        SymbolicExecutor symbolicExecutor = applicationContext.getBean(SymbolicExecutor.class);

        symbolicExecutor.setJarName(jarName);
        symbolicExecutor.setClassName(className);
        symbolicExecutor.setMethod(methodName);
        symbolicExecutor.setArgNum(numArgs);
        symbolicExecutor.setIsSymb(isSymb);

        // This piece of code only executes on first run
        isRestartCall = true;
        return symbolicExecutor.execute();
    }

    @Bean
    @Conditional(MainServerCondition.class)
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SymbolicExecutor dockerizedContainerExecutor(DockerContainer dockerContainer) {
        return new DockerizedExecutor(dockerContainer);
    }

    @Bean
    @Conditional(DockerCondition.class)
    public SymbolicExecutor mainServerSymbolicExecutor() {
        System.out.println("GENERATING ADAPTER");
        return new JPFAdapter();
    }

}