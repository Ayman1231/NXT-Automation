import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Test class for the "FastShopping" Android app.
 * Uses Appium + TestNG + Allure for functional and UI validation.
 *
 * Scenarios tested:
 * 1. Archiving a list after marking all items as completed.
 * 2. Renaming and editing an existing list.
 * 3. Removing and undoing items, including screenshot comparison.
 */
public class FastShoppingTest {

    private AppiumDriver driver;
    private WebDriverWait wait;
    private FastShoppingUtils utils;
    private WebElement addButton;

    /**
     * Setup method to initialize Appium driver and load the app.
     */
    @BeforeClass
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:deviceName", "Android Emulator");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:platformVersion", "16.0"); // Adjust to your emulator/device version
        caps.setCapability("appium:app", System.getProperty("user.dir") + "/apps/fastshopping.apk");
        caps.setCapability("appium:autoGrantPermissions", true);
        caps.setCapability("appium:appWaitActivity", "me.wolszon.fastshopping.MainActivity");
        caps.setCapability("unicodeKeyboard", true);
        caps.setCapability("resetKeyboard", true);

        // Initialize driver and wait
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Utility class instance for reusing actions
        utils = new FastShoppingUtils(driver);
    }

    /**
     * Method to locate the "Add" button inside the app.
     */
    public void getAddBtn() {
        addButton = driver.findElement(
                AppiumBy.xpath("//android.view.View[@content-desc='Add some items to your list!']/android.widget.Button")
        );
    }


    @Test(priority = 1, description = "Testing that user can archive a list")
    @Description("When user marks all items as completed, then archive the list")
    @Severity(SeverityLevel.NORMAL)
    public void archiveList() {
        utils.createList("LISTONE");
        getAddBtn();

        // Add two items
        addButton.click();
        utils.addItem("ITEMONE");
        addButton.click();
        utils.addItem("ITEMTWO");

        // Mark all items and archive
        utils.checkItems();
        utils.clickArchive();
        utils.assertToastMessage("List has been archived successfully.");
    }


    @Test(priority = 2, description = "Testing that user can rename and edit a list")
    @Description("When user creates a list, user can rename and edit the list")
    @Severity(SeverityLevel.NORMAL)
    public void rename_editList() {
        utils.createList("LISTTWO");

        // Add items
        getAddBtn();
        addButton.click();
        utils.addItem("ITEMONE");
        addButton.click();
        utils.addItem("ITEMTWO");

        // Rename the list
        utils.selectList("listtwo");
        utils.kebabMenu();
        utils.renameList("UPDATED");

        // Verify renamed list and perform edit/remove
        utils.selectList("listtwoupdated\n2 items • Created a moment ago");
        utils.selectItem("itemone");
        utils.editItem();
        utils.removeItem();
        utils.assertToastMessage("Item has been removed from the list.");

        // Archive the list
        utils.kebabMenu();
        utils.archiveList();
    }


    @Test(priority = 3, description = "Testing that user can remove all items and undo the removal")
    @Description("When user creates a list, user can remove all items and undo the removal")
    @Severity(SeverityLevel.NORMAL)
    public void remove_undoItems() throws IOException {
        utils.createList("LISTTHREE");

        // Add an item
        getAddBtn();
        addButton.click();
        utils.addItem("ITEMONE");

        // Select and remove item
        utils.selectList("listthree");
        utils.selectList("listthree\n1 item • Created a moment ago");
        utils.selectItem("itemone");
        utils.removeItem();
        utils.assertToastMessage("Item has been removed from the list.");
        utils.undoItem();

        // Take a screenshot after undo
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage actualImage = ImageIO.read(screenshotFile);

        // Crop out status bar (first 100px)
        int cropY = 100;
        actualImage = actualImage.getSubimage(0, cropY, actualImage.getWidth(), actualImage.getHeight() - cropY);

        File expectedFile = new File("expected_screenshot.png");
        if (!expectedFile.exists()) {
            // Save baseline screenshot if first run
            ImageIO.write(actualImage, "png", expectedFile);
            System.out.println("Baseline screenshot saved as expected_screenshot.png");
        } else {
            // Compare actual screenshot with baseline
            BufferedImage expectedImage = ImageIO.read(expectedFile);

            if (expectedImage.getWidth() != actualImage.getWidth() ||
                    expectedImage.getHeight() != actualImage.getHeight()) {
                Assert.fail("Screenshot dimensions do not match!");
            }

            boolean equal = imagesAreEqual(expectedImage, actualImage);
            Assert.assertTrue(equal, "Screenshots do not match! See actual_screenshot.png");

            // Save actual screenshot for debugging
            ImageIO.write(actualImage, "png", new File("actual_screenshot.png"));
        }
    }

    /**
     * Method to compare two screenshots pixel by pixel.
     *
     * @param imgA First image
     * @param imgB Second image
     * @return true if images are identical, false otherwise
     */
    private boolean imagesAreEqual(BufferedImage imgA, BufferedImage imgB) {
        int width = imgA.getWidth();
        int height = imgA.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false; // Pixel mismatch found
                }
            }
        }
        return true;
    }

    /**
     * Cleanup method to quit the driver after all tests are executed.
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
