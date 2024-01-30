package com.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model.Laptop;
import com.services.Service;

@RestController
//@RequestMapping("/www.amazon.in/s")
public class Controller {

	@Autowired
	private Service scraperService;

	@GetMapping("/scrape-laptops")
	public List<Laptop> scrapeLaptops(@RequestParam String location, @RequestParam String pincode) throws IOException {
		return scraperService.scrapeLaptopsRaw(location, pincode);

	}
	
	@GetMapping("/scrape-laptops/del")
	public List<String> scrapeDelDetail(@RequestParam String location, @RequestParam String pincode) throws IOException {
		return scraperService.deliveryChargeAndDelTime(location, pincode);

	}

}
