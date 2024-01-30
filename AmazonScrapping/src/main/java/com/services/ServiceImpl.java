package com.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.model.Laptop;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

	// This method take URL and return responses
	public String getresponse(String url) {

		StringBuffer response = new StringBuffer();

		try {
			URL urlForGetRequest = new URL(url);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");

			int responseCode = conection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				response = new StringBuffer();
				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
					response.append("\n");
				}
				in.close();

			} else {
				System.out.println("http code is differenet for this url connection");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response.toString();
	}

	@Override
	public List<Laptop> scrapeLaptopsRaw(String location, String pincode) throws IOException {
		// TODO Auto-generated method stub

		List<Laptop> laptops = new ArrayList<>();

		// path for saving data we can make it dynamic as well by taking it in argument
		String path = "D:\\ndjson.json";
//		String path = "ndjson.json";

		// Using third party API i.e scraperapi
		String apiKey = "7c186b1ad89177dd26e8f06d3b8a5ca9";
		String baseUrl = "https://api.scraperapi.com/?";
		String queryParameters = "api_key=%s&url=https%%3A%%2F%%2Fwww.amazon.in%%2Fs%%3F%s";
		String searchQuery = "k=laptops";

		
		 String formattedQueryParameters = String.format(queryParameters, apiKey, searchQuery + "&location=" + location + "&pincode=" + pincode);
	        String url = baseUrl + formattedQueryParameters;


		// Construct the dynamic URL
	
		// Holding response of all laptops
		String res = getresponse(url);

		// Jsoup library for parsing html data
		Document document = Jsoup.parse(res);
		Elements products = document.select("div[data-component-type='s-search-result']");

		// Iterate over product elements and extract information and mapping as well
		for (Element product : products) {
			Laptop laptop = new Laptop();
			String title = product.select("span.a-text-normal").text();
			String price = product.select("span.a-price").text();
			String skuid = product.attr("data-asin");
			String link = product.select("a.a-link-normal").attr("href");
			String mrp = product.select(" span.a-text-price span.a-offscreen").text();
			String sellingPrice = product.select("span.a-price-whole").text();
			String imgLink = product.select("img.s-image").attr("src");
			String category = product.select("span.a-size-base a-color-base").text();
			String subCategory = product.select("span.a-color-base").text();

			// generating url from third party
			String amazonUrl2 = "https://www.amazon.in/" + link;
			String encodedUrl1 = URLEncoder.encode(amazonUrl2, StandardCharsets.UTF_8);
			String url1 = "https://api.scraperapi.com/?api_key=" + apiKey + "&url=" + encodedUrl1;

//			second redirect page response
			String res2 = getresponse(url1);

			Document document1 = Jsoup.parse(res2);
			Elements description = document1.select("div[id='dp']");

			String productName = description.select("tr.po-model_name td.a-span9 span.po-break-word").text();
			String brandName = description.select("tr.po-brand td.a-span9 span.po-break-word").text();
			String des = description.select("ul.a-spacing-mini").text();
			String weight = product.select("td.prodDetAttrValue").text();
			String delCharge = product.select("a.a-link-normal").text();
			String delTime = product.attr("span.data-csa-c-delivery-time");

			System.out.println("delcharge  " + delCharge);
			System.out.println("deltime     " + delTime);

			laptop.setBrand_name(brandName);
			laptop.setCategory("Laptop");
			laptop.setDescription(des);
			laptop.setImage_url(imgLink);
			laptop.setMrp(mrp);
			laptop.setProduct_name(productName);
			laptop.setProduct_title(title);
			laptop.setSelling_price(sellingPrice);
			laptop.setSkuid(skuid);
			laptop.setSpecification(des);
			laptop.setSubcategory("trending Laptop");

			laptop.setWeight(weight);

			laptops.add(laptop);
			// Print or process the extracted data

		}
		// calling this method to save data of laptops in ndjson format
		savingDataInNdJson(laptops, path);

		// printing raw response
//		System.out.println("Raw html response after hitting laptops url of pincode: " + pincode + " location: "
//				+ location + " " + "\n" + res);
		return laptops;
	}

	@Override
	public void savingDataInNdJson(List<Laptop> list, String path) {

		// Convert data to NDJSON format
		Gson gson = new Gson();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			for (Laptop jsonData : list) {
				String json = gson.toJson(jsonData);
				writer.write(json + "\n");
			}

			System.out.println("data saved successfully");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public List<String> deliveryChargeAndDelTime(String location, String pincode){
		// TODO Auto-generated method stub

		List<String> laptops = new ArrayList<>();

//		C:\one drive\OneDrive\Desktop\afterJob\AmazonScrapping

		// Using third party API i.e scraperapi
		String apiKey = "7c186b1ad89177dd26e8f06d3b8a5ca9";
		String baseUrl = "https://api.scraperapi.com/?";
		String queryParameters = "api_key=%s&url=https%%3A%%2F%%2Fwww.amazon.in%%2Fs%%3F%s";
		String searchQuery = "k=laptops";

		
		 String formattedQueryParameters = String.format(queryParameters, apiKey, searchQuery + "&location=" + location + "&pincode=" + pincode);
	        String url = baseUrl + formattedQueryParameters;



		// Holding response of all laptops
		String res = getresponse(url);

		// Jsoup library for parsing html data
		Document document = Jsoup.parse(res);
		Elements products = document.select("div[data-component-type='s-search-result']");

		// Iterate over product elements and extract information and mapping as well
		for (Element product : products) {
			
			String delTime = product.select("div.a-row span.a-color-base").text();
		
	    laptops.add("delivery time and delivery charge for the laptops are:  "+delTime);
	
			// Print or process the extracted data
         }
	
		


		return laptops;
	}

}
