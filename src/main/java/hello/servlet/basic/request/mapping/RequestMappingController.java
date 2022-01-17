package hello.servlet.basic.request.mapping;

import java.awt.*;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RequestMappingController {

	@GetMapping("/get-mapping")
	public String getMapping(){
		log.info("GET");
		return "ok";
	}

	@GetMapping("/get-mapping/{userId}")
	public String mappingPath(@PathVariable("userId") String data){
		log.info("mappingPath userId = {}", data);
		return "ok";
	}

	@GetMapping(value = "/mapping-param", params = "mode=debug")
	public String mappingParam(){
		log.info("mappingParam");
		return "ok";
	}

	// content-type = "application/json"
	@PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String mappingConsumes(){
		log.info("mappingConsumes");
		return "ok";
	}

	// Accept = "text/html"
	@PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
	public String mappingProduces(){
		log.info("mappingProduces");
		return "ok";
	}
}
