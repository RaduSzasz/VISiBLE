package com.visible;

import com.visible.jpf.Direction;
import com.visible.jpf.JPFAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JPFStateController {

	@RequestMapping("/stepleft")
	public String stepLeft() {
		State state = JPFAdapter.getListenerState();
		String json = (state == null) ? "null" : state.toJSON();
		boolean finished = JPFAdapter.moveForward(Direction.LEFT);
		System.out.println(json);
		return finished ? "Finished" : json;
	}

	@RequestMapping("/stepright")
	public String stepRight() {
		State state = JPFAdapter.getListenerState();
		String json = (state == null) ? "null" : state.toJSON();
		boolean finished = JPFAdapter.moveForward(Direction.RIGHT);
		System.out.println(json);
		return finished ? "Finished" : json;
	}
}
