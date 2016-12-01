package com.visible;

import com.visible.jpf.Direction;
import com.visible.jpf.JPFAdapter;
import com.visible.jpf.TreeInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JPFStateController {

	@RequestMapping("/stepleft")
	public String stepLeft() {
		TreeInfo treeInfo = JPFAdapter.getListenerTreeInfo();
		boolean finished = JPFAdapter.moveForward(Direction.LEFT);
		return finished ? "Finished" : ((treeInfo == null) ? "null" : treeInfo.toJSON());
	}

	@RequestMapping("/stepright")
	public String stepRight() {
		TreeInfo treeInfo = JPFAdapter.getListenerTreeInfo();
		boolean finished = JPFAdapter.moveForward(Direction.RIGHT);
		return finished ? "Finished" : ((treeInfo == null) ? "null" : treeInfo.toJSON());
	}
}
