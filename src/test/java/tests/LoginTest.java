package tests;

import dataproviders.LoginDataProvider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

import io.qameta.allure.*;
import utils.AllureUtil;

@Epic("Authentication")
@Feature("Login")
public class LoginTest {
    WebDriver driver;
    LoginPage p;
    private Logger logger  = LogManager.getLogger(LoginTest.class);
    @BeforeMethod
    public void init(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-save-password-bubble");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9000/login");
        p = new LoginPage(driver);
    }
    @Test(dataProvider = "validLoginData", dataProviderClass = LoginDataProvider.class)
    @Story("Valid login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with valid username and password")
    public void validLoginTest(String user, String pass){
        logger.info("=== Starting validLoginTest ====");
        Reporter.log("Bat dau test login voi user: "+ user);
        try {
            p.login(user, pass);
            String actual_username = p.validLogin();
            logger.debug("Actual username: " + actual_username + ", Expected: " + user);
            Reporter.log("Ket qua: " + actual_username + "\n");
            Assert.assertEquals(actual_username, user, "==== Test FAILED ====");
            logger.info("=== Test PASSED ====");
        }catch (Exception e){
            logger.error("==== Test FAILED ====",e);
            throw e;
        }
    }
    @Test(dataProvider = "invalidAllLogin", dataProviderClass = LoginDataProvider.class)
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error message when login fails")
    public void invalidLoginTest(String user, String pass){
        logger.info("=== Starting invalidAllLogin ===");
        Reporter.log("Bat dau test login voi user: "+ user);
        try{
            p.login(user,pass);
            Assert.assertTrue(p.isErrorMessageDisplayed(),"==== Test FAILED ====");
            logger.info("====  Test PASSED ====");
        }catch (Exception e){
            logger.error("==== Test FAILED ====",e);
            throw e;
        }
    }
    @AfterMethod(alwaysRun = true)
    public void close(ITestResult result){
        if(result.getStatus() == ITestResult.FAILURE){
            AllureUtil.attachScreenshot(driver);
        }
        driver.quit();
    }


}
