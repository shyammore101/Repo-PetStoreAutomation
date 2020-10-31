package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author Shyam More
 *
 */
public class PetStoreAPIUtilities {
	protected Properties property;
	protected String appUrl;
	protected Response response;

	/**
	 * @throws IOException
	 */
	protected void setUp() throws IOException {

		appUrl = System.getProperty("url"); // Taking application URL as command line argument
		property = dataSetUp(); // This method will load properties file will return a properties class object

	}

	/**
	 * Method to add a new pet
	 * 
	 * @param petID
	 * @param categoryID
	 * @param categoryName
	 * @param petName
	 * @param tagID
	 * @param tagName
	 * @param status
	 * @param photoUrl
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Response addPet(int petID, int categoryID, String categoryName, String petName, int tagID, String tagName,
			String status, String photoUrl) {
		RestAssured.baseURI = "https://" + appUrl + "/v2/pet";
		;
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.relaxedHTTPSValidation();
		httpRequest.contentType(property.getProperty("contentType"));

		// Below code will create a JSON object and make a post call to create new pet

		JSONObject categoryData = new JSONObject();
		categoryData.put("id", categoryID);
		categoryData.put("name", categoryName);
		JSONObject petData = new JSONObject();
		petData.put("id", petID);
		petData.put("category", categoryData);
		petData.put("name", petName);
		JSONArray photoUrls = new JSONArray();
		petData.put("photoUrls", photoUrls);
		JSONObject tagsData = new JSONObject();
		tagsData.put("id", tagID);
		tagsData.put("name", tagName);
		JSONArray tags = new JSONArray();
		tags.add(tagsData);
		petData.put("tags", tags);
		petData.put("status", status);
		httpRequest.body(petData);
		return httpRequest.post();
	}

	/**
	 * Method to delete a pet
	 * 
	 * @param petID
	 * @return
	 */
	protected Response deletePet(String petID) {
		RestAssured.baseURI = "https://" + appUrl + "/v2/pet/" + Integer.valueOf(petID);
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.relaxedHTTPSValidation();
		httpRequest.contentType(property.getProperty("contentType"));
		httpRequest.header(new Header("api_key", "special-key"));
		return httpRequest.delete();

	}

	/**
	 * Method to place a order for pet
	 * 
	 * @param petID
	 * @param quantity
	 * @param orderStatus
	 * @param complete
	 * @param shipDate
	 * @return
	 */
	protected Response placeOrder(String petID, String quantity, String orderStatus, String complete, String shipDate) {
		RestAssured.baseURI = "https://" + appUrl + "/v2/store/order";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.relaxedHTTPSValidation();
		httpRequest.contentType(property.getProperty("contentType"));

		// code below will create a JSON object and make post call to place a order

		Map<String, String> orderBody = new HashMap<String, String>();
		orderBody.put("id", petID);
		orderBody.put("petId", petID);
		orderBody.put("quantity", quantity);
		orderBody.put("shipDate", shipDate);
		orderBody.put("status", orderStatus);
		orderBody.put("complete", complete);
		httpRequest.body(orderBody);
		return httpRequest.post();

	}

	/**
	 * Method to load properties file
	 * 
	 * @return
	 * @throws IOException
	 */

	protected Properties dataSetUp() throws IOException {

		Properties property = new Properties();
		FileInputStream dataFile = new FileInputStream("Data.properties");
		property.load(dataFile);
		return property;

	}

}
