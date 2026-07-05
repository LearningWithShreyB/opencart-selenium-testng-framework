package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;
import utilities.ExcelUtility;

public class TC_003_LoginDDT extends BaseClass {

	// Class-level tracker to monitor rows sequentially (Row 0 is the header row)
	private int xlRow = 1;

	@Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class)
	public void verify_loginDDT(String email, String password, String exp) {
		logger.info("**** Starting TC_003_LoginDDT loop for row index: " + xlRow + " *****");
		logger.info("Testing Data - Email: " + email + " | Expected Result: " + exp);

		String path = "src/test/resources/testdata/Opencart_LoginData.xlsx";
		ExcelUtility xlutil = new ExcelUtility(path);

		try {
			// Step 1: Navigate through Home Page
			logger.info("Navigating to Login Page via Home Page...");
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();

			// Step 2: Handle Credentials on Login Page
			logger.info("Entering credentials...");
			LoginPage lp = new LoginPage(driver);
			lp.setEmail(email);
			lp.setPassword(password);
			lp.clickLogin();

			// Small explicit pause for UI synchronization and page landing
			Thread.sleep(3000);

			// Step 3: Validation Step using the dedicated logout link check
			MyAccountPage macc = new MyAccountPage(driver);
			boolean targetPage = macc.isLogoutLinkExists();
			logger.info("Login process evaluation completed. Landing dashboard validated: " + targetPage);

			// Step 4: Scenario Assertions & Excel Reporting Matrix
			if (exp.equalsIgnoreCase("Valid")) {
				if (targetPage == true) {
					logger.info("Data Scenario [Valid Login] -> Success matches expected.");
					macc.clickLogout(); // Mandatory session reset step

					xlutil.setCellData("Sheet1", xlRow, 3, "PASS");
					xlutil.fillGreenColor("Sheet1", xlRow, 3);
					xlRow++;
					Assert.assertTrue(true);
				} else {
					logger.error("Data Scenario [Valid Login] -> Failed unexpectedly.");
					xlutil.setCellData("Sheet1", xlRow, 3, "FAIL");
					xlutil.fillRedColor("Sheet1", xlRow, 3);
					xlRow++;
					Assert.assertTrue(false);
				}
			}

			if (exp.equalsIgnoreCase("Invalid")) {
				if (targetPage == true) {
					logger.error("Data Scenario [Invalid Login] -> Logged in with incorrect data! Security Failure.");
					macc.clickLogout(); // Reset system session container anyway

					xlutil.setCellData("Sheet1", xlRow, 3, "FAIL");
					xlutil.fillRedColor("Sheet1", xlRow, 3);
					xlRow++;
					Assert.assertTrue(false);
				} else {
					logger.info("Data Scenario [Invalid Login] -> Login blocked as intended.");
					xlutil.setCellData("Sheet1", xlRow, 3, "PASS");
					xlutil.fillGreenColor("Sheet1", xlRow, 3);
					xlRow++;
					Assert.assertTrue(true);
				}
			}

		} catch (Exception e) {
			logger.error("A critical runtime script interruption occurred: " + e.getMessage());
			try {
				xlutil.setCellData("Sheet1", xlRow, 3, "ERROR");
				xlutil.fillRedColor("Sheet1", xlRow, 3);
				xlRow++;
			} catch (Exception xlEx) {
				logger.error("Failed writing fallback exception error to target Excel workbook: " + xlEx.getMessage());
			}
			Assert.fail("An exception occurred: " + e.getMessage());
		}

		logger.info("**** Finished processing loop segment for TC_003_LoginDDT *****");
	}
}