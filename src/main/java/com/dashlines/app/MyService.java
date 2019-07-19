package com.dashlines.app;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyService {

	@RequestMapping("greet/hello/{name}")
	public String greetHello(@PathVariable String name){
		return "Howdy "+name;
	}
	
	@RequestMapping("greet/hello1/{name}")
	public String greetHello1(@PathVariable String name){
		return "Howdy "+name;
	}
}
