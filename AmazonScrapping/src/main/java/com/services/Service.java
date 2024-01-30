package com.services;

import java.io.IOException;
import java.util.List;

import com.model.Laptop;

public interface Service {

	List<String> deliveryChargeAndDelTime(String location, String pincode);
	
	List<Laptop> scrapeLaptopsRaw(String location, String pincode) throws IOException;
	
	void savingDataInNdJson(List<Laptop> list,String path);
	


}
