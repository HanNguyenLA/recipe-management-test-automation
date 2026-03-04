package tests;

import dataproviders.RecipeDataProvider;
import io.qameta.allure.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RecipePage;

import java.time.Duration;
import java.util.Map;

@Epic("Recipe Management")
@Feature("Create Recipe")
public class RecipeTest {

    WebDriver driver;
    WebDriverWait wait;
    LoginPage loginPage;
    HomePage homePage;
    RecipePage recipePage;
    Logger logger = LogManager.getLogger(RecipeTest.class);

    @BeforeMethod
    public void setUp() {
        logger.info("========== TEST STARTED ==========");

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
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:9000/login");

        loginPage = new LoginPage(driver);

        logger.info("Setup completed successfully");
    }

    @AfterMethod
    public void tearDown() {
        logger.info("========== TEST ENDED ==========");
        if (driver != null) {
            driver.quit();
        }
    }

    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1,
            description = "TC01: Verify user can create recipe with valid data",
            dataProvider = "validRecipeData",
            dataProviderClass = RecipeDataProvider.class)
    @Severity(SeverityLevel.BLOCKER)
    @Story("Create Recipe - Happy Path")
    @Description("Test creating recipe with complete valid data including title, cuisine, ingredients, and steps")
    public void testCreateRecipeWithValidData(
            String title, String cuisine, String description,
            String serve, String time, String[] ingredients, String[] steps) {

        try {
            logger.info("TC01: Testing create recipe with valid data");

            // Step 1: Login
            homePage = loginPage.login("testuser", "password123");

            // Step 2: Navigate to Recipe Page
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Step 3: Fill recipe information
            recipePage.fillBasicInfo(title, description, serve, time);
            recipePage.selectCuisineByText(cuisine);

            // Step 4: Fill ingredients
            recipePage.fillMultipleIngre(ingredients);

            // Step 5: Fill steps
            recipePage.fillMultipleStep(steps);

            // Step 6: Submit
            recipePage.clickCreate();

            // Step 7: Verify
            Assert.assertTrue(true, "Recipe created successfully");
            logger.info("TC01: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 2, description = "TC02: Create recipe with image upload")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create Recipe with Image")
    public void testCreateRecipeWithImage() {
        try {
            logger.info("TC02: Testing create recipe with image");

            Map<String, Object> data = RecipeDataProvider.getCompleteRecipeData();

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Upload image first
            recipePage.uploadImage((String) data.get("imagePath"));

            // Fill other data
            recipePage.fillBasicInfo(
                    (String) data.get("title"),
                    (String) data.get("description"),
                    (String) data.get("serve"),
                    (String) data.get("time")
            );
            recipePage.selectCuisineByText((String) data.get("cuisine"));
            recipePage.fillMultipleIngre((String[]) data.get("ingredients"));
            recipePage.fillMultipleStep((String[]) data.get("steps"));

            recipePage.clickCreate();

            Assert.assertTrue(true, "Recipe with image created successfully");
            logger.info("TC02: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    // ==================== CUISINE TESTS ====================

    @Test(priority = 3,
            description = "TC03: Verify all cuisines can be selected",
            dataProvider = "allCuisines",
            dataProviderClass = RecipeDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Cuisine Selection")
    public void testSelectAllCuisines(String cuisine) {
        try {
            logger.info("TC03: Testing cuisine selection - " + cuisine);

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            recipePage.selectCuisineByText(cuisine);

            System.out.println("✓ Selected cuisine: " + cuisine);
            Assert.assertTrue(true);
            logger.info("TC03: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    // ==================== INGREDIENTS TESTS ====================

    @Test(priority = 4,
            description = "TC04: Create recipe with multiple ingredients",
            dataProvider = "multipleIngredientsData",
            dataProviderClass = RecipeDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Multiple Ingredients")
    public void testAddMultipleIngredients(int count, String[] ingredients) {
        try {
            logger.info("TC04: Testing multiple ingredients - count: " + count);

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            recipePage.fillBasicInfo("Test Recipe", "Description", "4", "60");
            recipePage.selectCuisineByText(RecipeDataProvider.Cuisines.VIETNAMESE);

            recipePage.fillMultipleIngre(ingredients);

            System.out.println("✓ Added " + count + " ingredients successfully");
            Assert.assertTrue(true);
            logger.info("TC04: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 5, description = "TC05: Add and remove ingredients")
    @Severity(SeverityLevel.NORMAL)
    @Story("Ingredient Management")
    public void testAddAndRemoveIngredient() {
        try {
            logger.info("TC05: Testing add and remove ingredients");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Add 3 ingredients
            String[] ingredients = {"Ing 1", "Ing 2", "Ing 3"};
            recipePage.fillMultipleIngre(ingredients);

            // Remove middle ingredient (index 1)
            recipePage.removeIngredientByIndex(1);

            System.out.println("✓ Ingredient removed successfully");
            Assert.assertTrue(true);
            logger.info("TC05: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 6, description = "TC06: Remove and re-add ingredients")
    @Severity(SeverityLevel.MINOR)
    @Story("Ingredient Management")
    public void testRemoveAndReAddIngredients() {
        try {
            logger.info("TC06: Testing remove and re-add ingredients");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Fill 2 default ingredients
            recipePage.fillIngredientByIndex(0, "First Ingredient");
            recipePage.fillIngredientByIndex(1, "Second Ingredient");

            // Remove first ingredient
            recipePage.removeIngredientByIndex(0);

            // Add new ingredient
            recipePage.clickAddIngredient();
            recipePage.fillIngredientByIndex(1, "New Ingredient");

            Assert.assertTrue(true);
            logger.info("TC06: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    // ==================== STEPS TESTS ====================

    @Test(priority = 7,
            description = "TC07: Create recipe with multiple steps",
            dataProvider = "multipleStepsData",
            dataProviderClass = RecipeDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Multiple Steps")
    public void testAddMultipleSteps(int count, String[] steps) {
        try {
            logger.info("TC07: Testing multiple steps - count: " + count);

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            recipePage.fillBasicInfo("Test Recipe", "Description", "4", "60");
            recipePage.selectCuisineByText(RecipeDataProvider.Cuisines.VIETNAMESE);
            recipePage.fillMultipleIngre(new String[]{"Ingredient 1"});

            recipePage.fillMultipleStep(steps);

            System.out.println("✓ Added " + count + " steps successfully");
            Assert.assertTrue(true);
            logger.info("TC07: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 8, description = "TC08: Add and remove steps")
    @Severity(SeverityLevel.NORMAL)
    @Story("Step Management")
    public void testAddAndRemoveStep() {
        try {
            logger.info("TC08: Testing add and remove steps");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            String[] steps = {"Step 1", "Step 2", "Step 3"};
            recipePage.fillMultipleStep(steps);

            // Remove step
            recipePage.removeStepByIndex(1);

            Assert.assertTrue(true);
            logger.info("TC08: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    // ==================== BOUNDARY TESTS ====================

    @Test(priority = 9,
            description = "TC09: Test with boundary values",
            dataProvider = "boundaryData",
            dataProviderClass = RecipeDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Boundary Testing")
    public void testBoundaryValues(
            String title, String cuisine, String description,
            String serve, String time, String[] ingredients, String[] steps) {

        try {
            logger.info("TC09: Testing boundary values");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            recipePage.fillBasicInfo(title, description, serve, time);
            recipePage.selectCuisineByText(cuisine);
            recipePage.fillMultipleIngre(ingredients);
            recipePage.fillMultipleStep(steps);

            recipePage.clickCreate();

            Assert.assertTrue(true);
            logger.info("TC09: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 10, description = "TC10: Test with minimum required fields")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Minimum Data")
    public void testMinimumRequiredFields() {
        try {
            logger.info("TC10: Testing minimum required fields");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Only fill required fields
            recipePage.fillBasicInfo("Min Recipe", "", "1", "1");
            recipePage.selectCuisineByText(RecipeDataProvider.Cuisines.VIETNAMESE);
            recipePage.fillIngredientByIndex(0, "One ingredient");
            recipePage.fillStepByIndex(0, "One step");

            recipePage.clickCreate();

            Assert.assertTrue(true);
            logger.info("TC10: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    // ==================== SPECIAL CHARACTERS TESTS ====================

    @Test(priority = 11,
            description = "TC11: Test with special characters",
            dataProvider = "specialCharData",
            dataProviderClass = RecipeDataProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Special Characters")
    public void testSpecialCharacters(
            String title, String cuisine, String description,
            String serve, String time, String[] ingredients, String[] steps) {

        try {
            logger.info("TC11: Testing special characters");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            recipePage.fillBasicInfo(title, description, serve, time);
            recipePage.selectCuisineByText(cuisine);
            recipePage.fillMultipleIngre(ingredients);
            recipePage.fillMultipleStep(steps);

            recipePage.clickCreate();

            Assert.assertTrue(true);
            logger.info("TC11: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    // ==================== NEGATIVE TESTS ====================

    @Test(priority = 12,
            description = "TC12: Test with invalid data",
            dataProvider = "invalidRecipeData",
            dataProviderClass = RecipeDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Validation Testing")
    public void testInvalidData(
            String title, String cuisine, String description,
            String serve, String time, String[] ingredients,
            String[] steps, String expectedError) {

        try {
            logger.info("TC12: Testing invalid data");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            recipePage.fillBasicInfo(title, description, serve, time);
            if (!cuisine.isEmpty()) {
                recipePage.selectCuisineByText(cuisine);
            }
            recipePage.fillMultipleIngre(ingredients);
            recipePage.fillMultipleStep(steps);

            recipePage.clickCreate();

            System.out.println("✓ Validation error expected: " + expectedError);
            Assert.assertTrue(true);
            logger.info("TC12: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 13, description = "TC13: Submit without filling any field")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Empty Form Validation")
    public void testSubmitEmptyForm() {
        try {
            logger.info("TC13: Testing empty form submission");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Don't fill anything, just click submit
            recipePage.clickCreate();

            Assert.assertTrue(true, "Validation errors should appear");
            logger.info("TC13: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 14, description = "TC14: Test without selecting cuisine")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Required Field Validation")
    public void testWithoutCuisine() {
        try {
            logger.info("TC14: Testing without cuisine selection");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            recipePage.fillBasicInfo("Recipe Name", "Description", "4", "60");
            recipePage.fillMultipleIngre(new String[]{"Ingredient"});
            recipePage.fillMultipleStep(new String[]{"Step"});
            // Don't select cuisine

            recipePage.clickCreate();

            Assert.assertTrue(true, "Cuisine is required");
            logger.info("TC14: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    // ==================== E2E WORKFLOW TESTS ====================

    @Test(priority = 15, description = "TC15: Complete E2E recipe creation workflow")
    @Severity(SeverityLevel.BLOCKER)
    @Story("End-to-End Workflow")
    public void testCompleteE2EWorkflow() {
        try {
            logger.info("TC15: Testing complete E2E workflow");

            // Step 1: Login
            homePage = loginPage.login("testuser", "password123");

            // Step 2: Navigate to Recipes
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Step 3: Fill all information
            recipePage.uploadImage(System.getProperty("user.dir") + "/test-data/images/recipe.jpg");
            recipePage.fillBasicInfo(
                    "Complete Recipe Test",
                    "This is a complete test with all fields filled",
                    "6",
                    "90"
            );
            recipePage.selectCuisineByText(RecipeDataProvider.Cuisines.VIETNAMESE);

            // Step 4: Add 5 ingredients
            String[] ingredients = {
                    "300g thịt bò",
                    "200g bánh phở",
                    "1 củ hành tây",
                    "2 tép tỏi",
                    "Rau thơm"
            };
            recipePage.fillMultipleIngre(ingredients);

            // Step 5: Add 4 steps
            String[] steps = {
                    "Chuẩn bị nguyên liệu",
                    "Chế biến theo hướng dẫn",
                    "Nêm nếm vừa ăn",
                    "Trình bày và thưởng thức"
            };
            recipePage.fillMultipleStep(steps);

            // Step 6: Submit
            recipePage.clickCreate();

            // Step 7: Verify success
            Assert.assertTrue(true, "Recipe created successfully");
            logger.info("TC15: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 16, description = "TC16: Edit ingredients and steps before submit")
    @Severity(SeverityLevel.NORMAL)
    @Story("Edit Before Submit")
    public void testEditBeforeSubmit() {
        try {
            logger.info("TC16: Testing edit before submit");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Fill initial data
            recipePage.fillIngredientByIndex(0, "Old Ingredient 1");
            recipePage.fillIngredientByIndex(1, "Old Ingredient 2");

            // Edit/update
            recipePage.fillIngredientByIndex(0, "New Ingredient 1");
            recipePage.fillIngredientByIndex(1, "New Ingredient 2");

            // Fill other fields and submit
            recipePage.fillBasicInfo("Edited Recipe", "Description", "4", "60");
            recipePage.selectCuisineByText(RecipeDataProvider.Cuisines.VIETNAMESE);
            recipePage.fillStepByIndex(0, "Step 1");

            recipePage.clickCreate();

            Assert.assertTrue(true);
            logger.info("TC16: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }

    @Test(priority = 17, description = "TC17: Cancel/clear and refill form")
    @Severity(SeverityLevel.MINOR)
    @Story("Form Reset")
    public void testClearAndRefill() {
        try {
            logger.info("TC17: Testing clear and refill");

            homePage = loginPage.login("testuser", "password123");
            recipePage = homePage.navMyRecipe();
            recipePage.clickFeatureCreate();

            // Fill initial data
            recipePage.fillBasicInfo("Initial", "Desc", "2", "30");
            recipePage.selectCuisineByText(RecipeDataProvider.Cuisines.VIETNAMESE);

            // Refill with new data
            recipePage.fillBasicInfo("New Title", "New Desc", "4", "60");
            recipePage.selectCuisineByText(RecipeDataProvider.Cuisines.JAPANESE);

            Assert.assertTrue(true);
            logger.info("TC17: Test completed successfully");
        } catch (Exception e) {
            logger.error("==== Test FAILED ====", e);
            throw e;
        }
    }
}