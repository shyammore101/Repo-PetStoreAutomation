package tests;

import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import static org.testng.Assert.assertEquals;
import java.io.IOException;
import org.testng.annotations.BeforeClass;
import utilities.PetStoreAPIUtilities;

/**
 * @author Shyam More
 *
 */

public class PetStore extends PetStoreAPIUtilities {

	@BeforeClass
	public void setUp() throws IOException {
		super.setUp();
	}

	/**
	 * Validate addition of new pet
	 * 
	 * @param petID
	 * @param categoryID
	 * @param categoryName
	 * @param petName
	 * @param tagID
	 * @param tagName
	 * @param status
	 * @param photoUrl
	 */
	@Test
	@Parameters({ "petID", "categoryID", "categoryName", "petName", "tagID", "tagName", "status", "photoUrl" })
	public void validateAdditionOfNewPet(int petID, int categoryID, String categoryName, String petName, int tagID,
			String tagName, String status, String photoUrl) {
		response = addPet(petID, categoryID, categoryName, petName, tagID, tagName, status, photoUrl);
		assertEquals(response.getStatusCode(), Integer.parseInt(property.getProperty("expectedRespCodeForPetAddition")),
				"New pet entry was not created");

	}

	/**
	 * Validation of deletion of pet
	 * 
	 * @param petID
	 */
	@Test
	@Parameters({ "petID" })
	public void validateDeletionOfPetRecord(String petID) {
		response = deletePet(petID);
		assertEquals(response.getStatusCode(),
				Integer.parseInt(property.getProperty("expectedResponseCodeForPetDeletion")),
				"Pet entry was not deleted");

	}

	/**
	 * Validate order placing.
	 * 
	 * @param petID
	 * @param quantity
	 * @param orderStatus
	 * @param complete
	 * @param shipDate
	 */
	@Test(dependsOnMethods = { "validateAdditionOfNewPet", "validateDeletionOfPetRecord" })
	@Parameters({ "petID", "quantity", "orderStatus", "complete", "shipDate" })
	public void validateOrderPlacingForPet(String petID, String quantity, String orderStatus, String complete,
			String shipDate) {
		response = placeOrder(petID, quantity, orderStatus, complete, shipDate);
		String orderStatusInResp = response.path("status").toString();
		String checkOrderCompleteness = response.path("complete").toString();
		assertEquals(orderStatusInResp, property.getProperty("expectedOrderStatusInPetOrder"),
				"Status of order is not as placed");
		assertEquals(checkOrderCompleteness, property.getProperty("expValForCompleteInOrderResponse"),
				"Order is not complete");

	}

}
