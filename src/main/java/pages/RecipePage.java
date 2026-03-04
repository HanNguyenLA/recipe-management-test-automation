package pages;

import io.qameta.allure.Step;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RecipePage {
    WebDriver driver;
    WebDriverWait wait;
    Logger logger = LogManager.getLogger(RecipePage.class);
    By create = By.xpath("//a[@href='/recipes/new']");
    By imageFile = By.name("imageFile");
    By title_x = By.name("title");
    By cuisineDropdown = By.id("cuisine");
    By description_x = By.id("description");
    By serve_x = By.id("serve");
    By time_x = By.id("time");

    By ingreList = By.id("ingredientsList");
    By addIngre = By.xpath("//button[@onclick='addIngredient()']");
    By addStep = By.xpath("//button[@onclick='addStep()']");
    By clickCreate = By.xpath("//button[@type='submit' and @form='recipeForm']");

    private By ingredientInputByIndex(int index) {
        return By.name("ingredients[" + index + "]");
    }

    private By removeBtnIngre(Integer index) {
        return By.xpath("//input[@name='ingredients[" + index + "]']/following-sibling::button[contains(@class,'remove-btn')]");
    }

    private By stepInputByIndex(Integer step) {
        return By.name("steps[" + step + "]");
    }

    private By removeBtnStep(Integer step) {
        return By.xpath("//input[@name='steps[" + step + "]']/following-sibling::button[contains(@class,'remove-btn')]");
    }

    public RecipePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Scroll to element if not visible
     */
    private void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(500); // Wait for scroll animation
        } catch (Exception e) {
            logger.warn("Failed to scroll to element: " + e.getMessage());
        }
    }

    /**
     * Find element with scroll retry
     */
    private WebElement findElementWithScroll(By locator, String elementName) {
        try {
            logger.info("Attempting to find element: " + elementName);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            // Try to scroll to element
            scrollToElement(element);

            // Wait for element to be visible after scroll
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element found and visible: " + elementName);
            return element;

        } catch (Exception e) {
            logger.error("==== Element NOT FOUND after scroll ====: " + elementName, e);
            throw new RuntimeException("Failed to find element: " + elementName + ". Error: " + e.getMessage(), e);
        }
    }

    /**
     * Click element with scroll and error handling
     */
    private void clickElementWithScroll(By locator, String elementName) {
        try {
            logger.info("Attempting to click element: " + elementName);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            // Scroll to element
            scrollToElement(element);

            // Wait for clickable
            element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Successfully clicked element: " + elementName);

        } catch (Exception e) {
            logger.error("==== Test FAILED ====: Cannot click element: " + elementName, e);
            throw new RuntimeException("Failed to click element: " + elementName + ". Error: " + e.getMessage(), e);
        }
    }

    @Step("Click feature create recipe.")
    public void clickFeatureCreate() {
        try {
            logger.info("Click btn create recipe.");
            clickElementWithScroll(create, "Create Recipe Button");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }
    private void scrollToTop() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo({top: 0, behavior: 'smooth'});");
            Thread.sleep(500); // Wait for scroll animation
            logger.info("Scrolled to top of page");
        } catch (Exception e) {
            logger.warn("Failed to scroll to top: " + e.getMessage());
        }
    }


    @Step("Click btn create recipe.")
    public void clickCreate() {
        try {
            logger.info("Click btn create recipe.");
            scrollToTop();
            clickElementWithScroll(clickCreate, "Submit Create Button");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Step("Upload file image for recipe.")
    public void uploadImage(String filePath) {
        try {
            logger.info("Upload file image for recipe.");
            WebElement element = findElementWithScroll(imageFile, "Image File Input");
            element.sendKeys(filePath);
            logger.info("Image uploaded successfully: " + filePath);
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Step("Select cuisine on dropdown.")
    public void selectCuisineByText(String cuisineName) {
        try {
            logger.info("Selecting cuisine: " + cuisineName);
            WebElement dropdown = findElementWithScroll(cuisineDropdown, "Cuisine Dropdown");

            // Wait for dropdown to be clickable
            dropdown = wait.until(ExpectedConditions.elementToBeClickable(cuisineDropdown));
            Select select = new Select(dropdown);
            select.selectByVisibleText(cuisineName);
            logger.info("Cuisine selected successfully: " + cuisineName);
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    public void fillBasicInfo(String title, String description, String serve, String time) {
        try {
            logger.info("Filling basic info");

            WebElement titleElement = findElementWithScroll(title_x, "Title Input");
            titleElement.clear();
            titleElement.sendKeys(title);

            WebElement descElement = findElementWithScroll(description_x, "Description Input");
            descElement.clear();
            descElement.sendKeys(description);

            WebElement serveElement = findElementWithScroll(serve_x, "Serve Input");
            serveElement.clear();
            serveElement.sendKeys(serve);

            WebElement timeElement = findElementWithScroll(time_x, "Time Input");
            timeElement.clear();
            timeElement.sendKeys(time);

            logger.info("Basic info filled successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    //Ingredient Method
    public void clickAddIngredient() {
        try {
            logger.info("Clicking add ingredient button");
            clickElementWithScroll(addIngre, "Add Ingredient Button");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    public void fillIngredientByIndex(Integer index, String ingre) {
        try {
            logger.info("Filling ingredient at index " + index);
            By locator = ingredientInputByIndex(index);
            WebElement input = findElementWithScroll(locator, "Ingredient Input [" + index + "]");

            // Wait for element to be visible and interactable
            input = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            input.clear();
            input.sendKeys(ingre);
            logger.info("Ingredient filled at index " + index + ": " + ingre);
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    public void fillMultipleIngre(String[] ingredients) {
        try {
            logger.info("Filling multiple ingredients");
            for (int i = 0; i < Math.min(2, ingredients.length); i++) {
                fillIngredientByIndex(i, ingredients[i]);
            }
            for (int i = 2; i < ingredients.length; i++) {
                clickAddIngredient();
                fillIngredientByIndex(i, ingredients[i]);
            }
            logger.info("All ingredients filled successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    public void removeIngredientByIndex(int index) {
        try {
            logger.info("Removing ingredient at index " + index);
            By locator = removeBtnIngre(index);
            WebElement element = findElementWithScroll(locator, "Remove Ingredient Button [" + index + "]");

            element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Ingredient removed at index " + index);
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    //Step Method
    public void clickAddStep() {
        try {
            logger.info("Clicking add step button");
            clickElementWithScroll(addStep, "Add Step Button");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    public void fillStepByIndex(Integer index, String step) {
        try {
            logger.info("Filling step at index " + index);
            By locator = stepInputByIndex(index);
            WebElement input = findElementWithScroll(locator, "Step Input [" + index + "]");

            input = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            input.clear();
            input.sendKeys(step);
            logger.info("Step filled at index " + index + ": " + step);
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    public void fillMultipleStep(String[] steps) {
        try {
            logger.info("Filling multiple steps");
            for (int i = 0; i < Math.min(2, steps.length); i++) {
                fillStepByIndex(i, steps[i]);
            }
            for (int i = 2; i < steps.length; i++) {
                clickAddStep();
                fillStepByIndex(i, steps[i]);
            }
            logger.info("All steps filled successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    public void removeStepByIndex(int index) {
        try {
            logger.info("Removing step at index " + index);
            By locator = removeBtnStep(index);
            WebElement element = findElementWithScroll(locator, "Remove Step Button [" + index + "]");

            element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Step removed at index " + index);
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }
}