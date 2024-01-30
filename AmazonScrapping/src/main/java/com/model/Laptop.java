package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Laptop {

	private String skuid;
	private String product_title;
	private String description;
	private String product_name;
	private String category;
	private String mrp;
	private String selling_price;
	private String weight;
	private String subcategory;
	private String brand_name;
	private String image_url;
	private String specification;

}
