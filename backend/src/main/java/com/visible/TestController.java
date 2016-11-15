package com.visible;

import com.visible.jpf.JPFAdapter;
import com.visible.jpf.TreeInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@RequestMapping("/test")
	public String test() {
		TreeInfo treeInfo = JPFAdapter.getListenerTreeInfo();
		JPFAdapter.moveForward();
		return (treeInfo == null) ? "null" : treeInfo.toString();
	}
}
