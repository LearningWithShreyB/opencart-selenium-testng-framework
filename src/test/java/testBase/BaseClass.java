package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import org.apache.logging.log4j.LogManager; //log4j
import org.apache.logging.log4j.Logger;   //log4j

public class BaseClass {

    // Global static reference so Extent Report listeners can capture screenshots from the exact active session
    public static WebDriver driver;
    public Logger logger;
    public Properties p;
    
    @BeforeClass(groups = {"sanity", "regression", "master"})
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {
        
        // 1. Loading properties file configuration explicitly
        FileReader file = new FileReader(".//src//test//resources//config.properties");
        p = new Properties();
        p.load(file);
        
        // 2. Initializing Log4j logging engine
        logger = LogManager.getLogger(this.getClass());
                
        // 3. Dynamic browser initialization based on testng.xml parameter
        switch(br.toLowerCase()) {
            case "chrome": 
                driver = new ChromeDriver(); 
                break;
            case "edge": 
                driver = new EdgeDriver(); 
                break;
            default: 
                // Throws an explicit exception to instantly halt the execution if there is a typo in testng.xml
                throw new IllegalArgumentException("Unsupported browser provided: " + br);
        }
        
        // 4. Session Environment & Timeout Safeguards
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        
        // 5. Navigate to application URL defined in config.properties
        driver.get(p.getProperty("appURL"));
        driver.manage().window().maximize();
    }
    
    @AfterClass(groups = {"sanity", "regression", "master"})
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Fully terminates the active browser driver process cleanly
        }
    }
    
    // --- Data Generation Utilities for Form Fills ---

    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }
    
    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }
    
    public String randomAlphaNumeric() {
        String str = RandomStringUtils.randomAlphabetic(3);
        String num = RandomStringUtils.randomNumeric(3);
        return (str + "@" + num);
    }
    
    // --- Screenshot Capture Utility Engine for ExtentReports Listener ---
    
    public String captureScreen(String tname) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        
        // Formulating target file location in the local project repository directory
        String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);
        
        // Native Java file migration - removes dependency on external FileUtils
        sourceFile.renameTo(targetFile);
            
        return targetFilePath;
    }
}