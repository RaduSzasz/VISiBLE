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
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RestController
@Import(VisibleServerApplication.class)
@RequestMapping("/symbolicmethod")
public class SymMethodController {

    private static final int NUMBER_OF_THREADS = 8;
    private String jarName;
    private String className;
    private String methodName;
    private int numArgs;
    private boolean[] isSymb;
    private boolean isRestartCall = false;
    private SymbolicExecutor symbolicExecutor;

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

        this.jarName = jarName;
        this.className = className;
        this.methodName = methodName;
        this.numArgs = numArgs;
        this.isSymb = isSymb;

        if (!(this.isSymb.length == numArgs)) {
            return new State().withError("Mismatch in number of argument.");
        }

        if (isRestartCall) {
            if (symbolicExecutor == null) {
                return new State().withError("Restart not allowed at this point.");
            }
            return symbolicExecutor.restart();
        }

        // This piece of code only executes on first run
        isRestartCall = true;
        this.symbolicExecutor = applicationContext.getBean(SymbolicExecutor.class);

        System.out.println("DOCKER CONTAINER SHOULD BE CREATED AT THIS POINT");
        if (this.symbolicExecutor == null) {
            System.out.println("BUT IT WAS NULL");
        } else {
            System.out.println("AND IT WAS INDEED CREATED");
        }

        return symbolicExecutor.execute();
    }


    @Bean
    @Conditional(MainServerCondition.class)
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SymbolicExecutor dockerizedContainerExecutor(DockerContainer dockerContainer) {
        return new DockerizedExecutor(jarName, className, methodName, numArgs, isSymb, dockerContainer);
    }

    @Bean
    @Conditional(DockerCondition.class)
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SymbolicExecutor mainServerSymbolicExecutor() {
        System.out.println("GENERATING ADAPTER");
        return new JPFAdapter(jarName, className, methodName, numArgs, isSymb);
    }

    @Bean
    @ApplicationScope
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }
}