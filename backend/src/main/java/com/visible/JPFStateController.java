package com.visible;

import com.visible.jpf.JPFAdapter;
import com.visible.jpf.TreeInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JPFStateController {

	@RequestMapping("/nextstate")
	public String moveForward() {
		TreeInfo treeInfo = JPFAdapter.getListenerTreeInfo();
		boolean finished = JPFAdapter.moveForward();
		return finished ? "Finished" : ((treeInfo == null) ? "null" : treeInfo.toString());
	}
}
