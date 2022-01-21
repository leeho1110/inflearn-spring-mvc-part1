package hello.servlet.basic.response;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import hello.servlet.basic.HelloData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ResponseBodyController {

	@GetMapping("/response-body-string-v1")
	public void responseBodyV1(HttpServletResponse response) throws Exception {
		response.getWriter().write("ok");
	}

	@GetMapping("/response-body-string-v2")
	public ResponseEntity<String> responseBodyV2(HttpServletResponse response) {
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/response-body-string-v3")
	public String responseBodyV3() {
		return "ok";
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@GetMapping("/response-body-string-v4")
	public HelloData responseBodyV4() {
		HelloData helloData = new HelloData();
		helloData.setAge(1);

		return helloData;
	}
}
