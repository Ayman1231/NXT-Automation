import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

/**
 * Utility class for interacting with the FastShopping app.
 * Provides reusable methods
 */
public class FastShoppingUtils {

    private final AppiumDriver driver;
    private final WebDriverWait wait;
    private final AndroidDriver androidDriver;

    /**
     * Constructor
     * @param driver Appium driver instance
     */
    public FastShoppingUtils(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.androidDriver = (AndroidDriver) this.driver;
    }

    /**
     * Create a new shopping list
     * @param listName name of the new list
     */
    public void createList(String listName) {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("No list selected"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("NEW LIST"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Add new shopping list")));

        sendkeys(listName);
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("ADD"))).click();
    }

    /**
     * Add a new item to the current shopping list
     * @param itemName name of the item
     */
    public void addItem(String itemName) {
        sendkeys(itemName);
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("ADD"))).click();
    }

    /**
     * Mark all items in the list as checked
     */
    public void checkItems() {
        List<WebElement> checkboxes = driver.findElements(AppiumBy.className("android.widget.CheckBox"));
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.getAttribute("checked").equals("true")) {
                checkbox.click();
            }
        }
    }

    /** Click the Archive button */
    public void clickArchive() {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("ARCHIVE"))).click();
    }

    /**
     * Assert that a toast message appears
     * @param message expected message text
     */
    public void assertToastMessage(String message) {
        WebElement toastMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId(message))
        );
        Assert.assertEquals(toastMessage.getAttribute("contentDescription"), message);
    }

    /**
     * Select a shopping list by name
     * @param listName list name
     */
    public void selectList(String listName) {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(listName))).click();
    }

    /**
     * Select an item by name
     * @param itemName item name
     */
    public void selectItem(String itemName) {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(itemName))).click();
    }

    /** Open the kebab (3-dot) menu */
    public void kebabMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Show menu"))).click();
    }

    /**
     * Rename the currently selected list
     * @param newName new list name
     */
    public void renameList(String newName) {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Rename"))).click();
        sendkeys(newName);
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("RENAME"))).click();
    }

    /** Archive the currently selected list */
    public void archiveList() {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Archive list"))).click();
    }

    /** Edit the selected item (changes its name to "UPDATED") */
    public void editItem() {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("EDIT"))).click();
        sendkeys("UPDATED");
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("SAVE"))).click();
    }

    /** Remove the selected item */
    public void removeItem() {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("REMOVE"))).click();
    }

    /** Undo the last removal action */
    public void undoItem() {
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("UNDO"))).click();
    }

    /**
     * Send keystrokes to the app using Android hardware keys
     * @param input text to type
     */
    public void sendkeys(String input) {
        for (char c : input.toCharArray()) {
            AndroidKey key;
            switch (c) {
                case ' ': key = AndroidKey.SPACE; break;
                case '\n': key = AndroidKey.ENTER; break;
                default: key = AndroidKey.valueOf(String.valueOf(c).toUpperCase());
            }
            androidDriver.pressKey(new KeyEvent(key));
        }
    }
}
