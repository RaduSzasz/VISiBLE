package com.visible;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visible.symbolic.State;

@RestController
public class StateController {
    @RequestMapping("/stepleft")
    public State stepLeft() {
        // TODO: Fix this
        return null;
    }

    @RequestMapping("/stepright")
    public State stepRight() {
        // TODO: Fix this
        return null;
    }
}
