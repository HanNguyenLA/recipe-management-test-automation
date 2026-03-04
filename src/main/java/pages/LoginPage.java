package pages;

import io.qameta.allure.Step;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;
    private Logger logger = LogManager.getLogger(LoginPage.class);
    By username = By.name("username");
    By password = By.name("password");
    By btn_login = By.cssSelector("button[type='submit']");
    By actual_user = By.xpath("//i[contains(@class,'fa-user')]/following-sibling::span[1]");
    By mess = By.xpath("//div[@class='alert alert-danger mt-3']");
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @Step("Login with username: {0} and password: {1}")
    public HomePage login(String user, String pass){
        logger.info("Login with - User: "+ user );
        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(btn_login).click();
        return new HomePage(driver,wait);
    }
    @Step("Get logged-in username")
    public String validLogin(){
        try {
            WebElement userElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(actual_user)
            );
            String user = userElement.getText();
            logger.info("Logged in user displayed: " + user);
            return user;
        } catch (TimeoutException e) {
            logger.error("User element not found after login");
            throw e;
        }
    }
    @Step("Check error message displayed")
    public boolean isErrorMessageDisplayed(){
        try {
            WebElement error = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(mess)
            );
            String message = error.getText();
            logger.debug("Error message displayed: " + message);
            return message.contains("Sai username");
        } catch (TimeoutException e) {
            logger.warn("Error message not displayed within timeout");
            return false;
        }
    }



}
