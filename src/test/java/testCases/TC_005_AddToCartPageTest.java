package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.SearchPage;
import testBase.BaseClass;

public class TC_005_AddToCartPageTest extends BaseClass {

	@Test(groups = {"Master"})
	public void verify_addToCart() throws InterruptedException {

		logger.info("********** Starting TC_005_AddToCartPageTest **********");

		try {

			logger.info("Opening Home Page");
			HomePage hp = new HomePage(driver);

			logger.info("Entering product name: iPhone");
			hp.enterProductName("iPhone");

			logger.info("Clicking Search button");
			hp.clickSearch();

			logger.info("Navigated to Search Page");
			SearchPage sp = new SearchPage(driver);

			logger.info("Checking whether product 'iPhone' exists");
			if (sp.isProductExist("iPhone")) {

				logger.info("Product found. Selecting the product.");
				sp.selectProduct("iPhone");

				logger.info("Setting quantity to 2");
				sp.setQuantity("2");

				logger.info("Clicking Add to Cart button");
				sp.addToCart();

			} else {

				logger.error("Product 'iPhone' not found.");
				Assert.fail("Product not found.");

			}

			logger.info("Verifying confirmation message");
			Assert.assertTrue(sp.checkConfMsg());

			logger.info("Product successfully added to cart.");

		} catch (Exception e) {

			logger.error("Test failed due to exception: ", e);
			Assert.fail();

		}

		logger.info("********** Finished TC_005_AddToCartPageTest **********");

	}
}