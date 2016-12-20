package com.visible;

import com.visible.jpf.Direction;
import com.visible.jpf.JPFAdapter;
import com.visible.jpf.State;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JPFStateController {

	@RequestMapping("/stepleft")
	public String stepLeft() {
		State state = JPFAdapter.getListenerState();
		String json = (state == null) ? "null" : state.toJSON();
		boolean finished = JPFAdapter.moveForward(Direction.LEFT);
		return finished ? "Finished" : json;
	}

	@RequestMapping("/stepright")
	public String stepRight() {
		State state = JPFAdapter.getListenerState();
		String json = (state == null) ? "null" : state.toJSON();
		boolean finished = JPFAdapter.moveForward(Direction.RIGHT);
		return finished ? "Finished" : json;
	}
}
