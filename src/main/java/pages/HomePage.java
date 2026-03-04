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


public class HomePage {
    WebDriver driver;
    WebDriverWait wait;
    Logger logger = LogManager.getLogger(HomePage.class);
    By myRecipe = By.xpath("//a[contains(text(),'Công thức của tôi')]");
    By actual_user = By.xpath("//i[contains(@class,'fa-user')]/following-sibling::span[1]");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    public RecipePage navMyRecipe(){
        logger.info("Navigate to My Recipe page");
        wait.until(ExpectedConditions.visibilityOfElementLocated(myRecipe)).click();
        return new RecipePage(driver, wait);
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
}
