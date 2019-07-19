package com.dashlines.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UserService {
	
	@Value("${user.categories}")
	private  String userCategories;
	
	
	@RequestMapping(value = "/user/getCategories")
	@ResponseBody
	public List<String> getUserCategories() {
		String[] categories = userCategories.split(",");
		return Arrays.asList(categories);
	}
}
