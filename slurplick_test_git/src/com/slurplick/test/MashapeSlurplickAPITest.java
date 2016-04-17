package com.slurplick.test;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MashapeSlurplickAPITest {
	public static void main(String[] str) {
		String mashapeKey = ""; // Input the Key 
		String mashapeurl = "https://slurplick.p.mashape.com";
		
		String[] usernames = {"User 1", "User 2", "User 3", "User 4", "User 5"};
		String[] products = {"Product 1", "Product 2", "Product 3", "Product 4", "Product 5"};
		String[] attributeNames = {"attribute1, attribute2, attribute3, attribute4, attribute5",
				"attribute1, attribute2, attribute3, attribute4, attribute5",
				"attribute1, attribute2, attribute3, attribute4, attribute5",
				"attribute1, attribute2, attribute3, attribute4",
				"attribute2"};
		String[] imgUrls = {"",
				"",
				"",
				"",
				""}; // Input Product Image URL 
		String[] attributeValues = {"1, 2, 3, 4, 5", "6, 7, 8, 9, 10", "1, 3, 8, 12, 15, 19", "5, 10, 15, 20, 25", "22"};
		int[] prodquantity = {1, 1, 1, 1, 1};

		String custIds[] = new String[5];
		String prodIds[] = new String[5];
		try {
			// Add User
			for (int i = 0; i < usernames.length; i++ ) {
				HttpResponse<JsonNode> response1 = Unirest.post(mashapeurl + "/customerinfoendpoint/v1/customerinfo/{username}")
				.header("Authorization", "<required>")
				.header("X-Mashape-Key", mashapeKey)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Accept", "application/json")
				.field("username", usernames[i])
				.asJson();
				System.out.println(response1.getBody().toString());
				custIds[i] = (String) response1.getBody().getObject().getJSONObject("id").get("id");
				System.out.println(" custIds = " + custIds[i]);
			}
			// Add Product
			for (int i = 0; i < products.length; i++ ) {
				HttpResponse<JsonNode> response2 = Unirest.post(mashapeurl + "/recipeinfoendpoint/v1/recipeinfo/{name}/{attributes}/{values}/{imageUrl}")
					.header("Authorization", "<required>")
					.header("X-Mashape-Key", mashapeKey)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Accept", "application/json")
					.field("name", products[i])
					.field("attributes", attributeNames[i])
					.field("values", attributeValues[i])
					.field("imageUrl", imgUrls[i])
					.asJson();

				System.out.println(response2.getBody().toString());
				prodIds[i] = (String) response2.getBody().getObject().getJSONObject("recipeID").get("id");
				System.out.println(" prodIds = "  + prodIds[i] );
			}

			// Add Order
			for (String cust : custIds)
				for (int i = 0; i < prodIds.length; i++ ) {
					for (int j = 0; j <= prodquantity[i]; j++) {
						HttpResponse<JsonNode> response3 = Unirest.post(mashapeurl + "/orderLogendpoint/v1/orderlog/{customerId}/{recipeId}") //"+ cust +"/"+prodIds[i])
								.header("Authorization", "<required>")
								.header("X-Mashape-Key", mashapeKey)
								.header("Content-Type", "application/x-www-form-urlencoded")
								.header("Accept", "application/json")
								.field("customerId", cust)
								.field("recipeId", prodIds[i])
								.asJson();

						System.out.println(response3.getBody().toString());
					}
				}
			// Get Recommendation
			for (int i = 0; i < custIds.length; i++ ) {
				HttpResponse<JsonNode> response4 = Unirest.post(mashapeurl + "/_ah/api/recommenddataendpoint/v1/recommendTopItems/{custId}") //"+custIds[i])
						.header("Authorization", "<required>")
						.header("X-Mashape-Key", mashapeKey)
						.header("Content-Type", "application/x-www-form-urlencoded")
						.header("Accept", "application/json")
						.field("custId", custIds[i])
						.asJson();
				System.out.println(response4.getBody().toString());
			}

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
