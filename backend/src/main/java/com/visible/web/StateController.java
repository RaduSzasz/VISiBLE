package com.visible.web;

import com.visible.symbolic.SymbolicExecutor;
import com.visible.symbolic.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Import(FileUploadController.class)
public class StateController {

    @Autowired
    private SymbolicExecutor symbolicExecutor;

    @RequestMapping("/stepleft")
    public State stepLeft() {
        return symbolicExecutor.stepLeft();
    }

    @RequestMapping("/stepright")
    public State stepRight() {
        return symbolicExecutor.stepRight();
    }
}
