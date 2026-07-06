package testCases;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pageObjects.AccountRegistrationPage;
import pageObjects.CheckoutPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import pageObjects.SearchPage;
import pageObjects.ShoppingCartPage;
import testBase.BaseClass;

public class TC_006_EndToEndTest extends BaseClass {

	@Test(groups = { "Master" })
	public void verifyEndToEndCheckoutFlow() throws InterruptedException {

		logger.info("********** Starting TC_006_EndToEndTest **********");

		try {

			SoftAssert softAssert = new SoftAssert();

			logger.info("===== Account Registration =====");

			HomePage homePage = new HomePage(driver);
			homePage.clickMyAccount();
			homePage.clickRegister();

			AccountRegistrationPage registrationPage = new AccountRegistrationPage(driver);

			registrationPage.setFirstName(randomString().toUpperCase());
			registrationPage.setLastName(randomString().toUpperCase());

			String email = randomString() + "@gmail.com";
			logger.info("Generated email: {}", email);

			registrationPage.setEmail(email);
			registrationPage.setTelephone("1234567");
			registrationPage.setPassword("test123");
			registrationPage.setConfirmPassword("test123");
			registrationPage.setPrivacyPolicy();
			registrationPage.clickContinue();

			Thread.sleep(3000);

			String confirmationMessage = registrationPage.getConfirmationMsg();
			logger.info("Registration confirmation message: {}", confirmationMessage);

			softAssert.assertEquals(confirmationMessage, "Your Account Has Been Created!");

			MyAccountPage myAccountPage = new MyAccountPage(driver);

			logger.info("Logging out from newly created account.");
			myAccountPage.clickLogout();

			Thread.sleep(3000);

			logger.info("===== Login =====");

			homePage.clickMyAccount();
			homePage.clickLogin();

			LoginPage loginPage = new LoginPage(driver);

			loginPage.setEmail(email);
			loginPage.setPassword("test123");
			loginPage.clickLogin();

			logger.info("My Account page displayed: {}", myAccountPage.isMyAccountPageExists());

			softAssert.assertTrue(myAccountPage.isMyAccountPageExists(),
					"My Account page should be displayed after login.");

			logger.info("===== Search Product and Add to Cart =====");

			homePage.enterProductName(p.getProperty("searchProductName"));
			homePage.clickSearch();

			SearchPage searchPage = new SearchPage(driver);

			if (searchPage.isProductExist(p.getProperty("searchProductName"))) {

				logger.info("Product found: {}", p.getProperty("searchProductName"));

				searchPage.selectProduct(p.getProperty("searchProductName"));
				searchPage.setQuantity("2");
				searchPage.addToCart();
			} else {
				logger.warn("Product not found: {}", p.getProperty("searchProductName"));
			}

			Thread.sleep(3000);

			logger.info("Product added to cart: {}", searchPage.checkConfMsg());

			softAssert.assertTrue(searchPage.checkConfMsg(), "Product should be added to cart successfully.");

			logger.info("===== Shopping Cart =====");

			ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);

			shoppingCartPage.clickItemsToNavigateToCart();
			shoppingCartPage.clickViewCart();

			Thread.sleep(3000);

			String totalPrice = shoppingCartPage.getTotalPrice();
			logger.info("Shopping cart total price: {}", totalPrice);

			softAssert.assertEquals(totalPrice, "$246.40");

			logger.info("Proceeding to checkout.");

			shoppingCartPage.clickOnCheckout();

			Thread.sleep(3000);

			/*logger.info("===== Checkout =====");

			CheckoutPage checkoutPage = new CheckoutPage(driver);

			checkoutPage.setfirstName(randomString().toUpperCase());
			Thread.sleep(1000);

			checkoutPage.setlastName(randomString().toUpperCase());
			Thread.sleep(1000);

			checkoutPage.setaddress1("address1");
			Thread.sleep(1000);

			checkoutPage.setaddress2("address2");
			Thread.sleep(1000);

			checkoutPage.setcity("Delhi");
			Thread.sleep(1000);

			checkoutPage.setpin("500070");
			Thread.sleep(1000);

			checkoutPage.setCountry("India");
			Thread.sleep(1000);

			checkoutPage.setState("Delhi");
			Thread.sleep(1000);

			checkoutPage.clickOnContinueAfterBillingAddress();
			Thread.sleep(1000);

			checkoutPage.clickOnContinueAfterDeliveryAddress();
			Thread.sleep(1000);

			checkoutPage.setDeliveryMethodComment("testing...");
			Thread.sleep(1000);

			checkoutPage.clickOnContinueAfterDeliveryMethod();
			Thread.sleep(1000);

			checkoutPage.selectTermsAndConditions();
			Thread.sleep(1000);

			checkoutPage.clickOnContinueAfterPaymentMethod();
			Thread.sleep(2000);

			String totalPriceInOrderPage = checkoutPage.getTotalPriceBeforeConfOrder();
			logger.info("Order page total price: {}", totalPriceInOrderPage);

			softAssert.assertEquals(totalPriceInOrderPage, "$207.00");*/

			/*
			 * Below code works only if SMTP email configuration is enabled.
			 * 
			 * checkoutPage.clickOnConfirmOrder(); Thread.sleep(3000);
			 * 
			 * boolean orderConfirmed = checkoutPage.isOrderPlaced();
			 * 
			 * logger.info("Order placed successfully: {}", orderConfirmed);
			 * 
			 * softAssert.assertTrue(orderConfirmed,
			 * "Order should be placed successfully.");
			 */
			
			homePage.clickMyAccount();
			homePage.clickLogout();
			

			logger.info("********** TC_006_EndToEndTest completed successfully **********");

			softAssert.assertAll();

		} catch (AssertionError ae) {

			logger.error("Assertion failed in TC_006_EndToEndTest.", ae);
			throw ae;

		} catch (Exception e) {

			logger.error("Unexpected exception occurred in TC_006_EndToEndTest.", e);
			throw e;
		}
	}
}