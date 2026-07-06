package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.SearchPage;
import testBase.BaseClass;

public class TC_004_SearchProductTest extends BaseClass {

	@Test(groups = { "Master" })
	public void verify_pruductSearch() throws InterruptedException {

		logger.info("***** Starting TC_004_SearchProductTest *****");

		try {

			logger.info("Creating HomePage object");
			HomePage hm = new HomePage(driver);

			logger.info("Entering product name: mac");
			hm.enterProductName("mac");

			logger.info("Clicking on Search button");
			hm.clickSearch();

			logger.info("Creating SearchPage object");
			SearchPage sp = new SearchPage(driver);

			logger.info("Verifying whether product 'MacBook' exists in search results");
			boolean status = sp.isProductExist("MacBook");

			logger.info("Product existence status: " + status);

			Assert.assertEquals(status, true);

			logger.info("Product search verification completed successfully");

		} catch (Exception e) {

			logger.error("Test failed due to exception", e);
			Assert.fail();

		}

		logger.info("***** Finished TC_004_SearchProductTest *****");
	}
}