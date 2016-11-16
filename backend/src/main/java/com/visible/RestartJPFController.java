package com.visible;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestartJPFController {

		@RequestMapping("/restart")
		public String restart() {
			VisibleServerApplication.restartJPF();
			return "Restarted JPF";
		}

}
