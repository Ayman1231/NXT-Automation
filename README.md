# 📘 FastShopping Automation

Automated UI tests for the **FastShopping** Android app using **Appium, Selenium, TestNG, and Allure Reports**.

---

## 🚀 Features
- Written in **Java 11** with **Maven** build system  
- Uses **Appium (UiAutomator2)** for Android automation  
- **TestNG** for test orchestration  
- **Allure Reports** for rich test reporting (steps, screenshots, severity levels)  
- Utility layer (`FastShoppingUtils`) for reusable actions  
- Includes **screenshot comparison** for visual regression validation  

---

## 📂 Project Structure
```
.
├── apps/fastshopping.apk       # AUT (Android app under test)
├── src/test/java/
│   ├── FastShoppingTest.java    # Main TestNG test class
│   ├── FastShoppingUtils.java   # Utility/helper methods
├── testng.xml                   # TestNG suite configuration
├── pom.xml                      # Maven dependencies & plugins
└── README.md                    # Project documentation
```

---

## ⚙️ Prerequisites
- **Java 11+**  
- **Maven 3.6+**  
- **Node.js + Appium Server**  
  ```sh
  npm install -g appium
  appium --version
  ```
- **Android SDK / Emulator** or real device with USB debugging enabled  
- [Allure Commandline](https://docs.qameta.io/allure/#_installing_a_commandline)  
  ```sh
  npm install -g allure-commandline
  ```

---

## ▶️ Running Tests
1. Start the Appium server:
   ```sh
   appium
   ```
2. Ensure your emulator or device is connected:
   ```sh
   adb devices
   ```
3. Run tests with Maven:
   ```sh
   mvn clean test
   ```

---

## 📊 Generating Reports
After test execution:

- Generate static report:
  ```sh
  allure serve target/allure-results
  ```

Reports include:
- Test descriptions (`@Description`)  
- Severity levels (`@Severity`)  
- Inline screenshots on failures or checkpoints  

---

## 🛠️ Example Test Flows
- ✅ Create a shopping list  
- ✅ Add items and mark them completed  
- ✅ Archive or rename lists  
- ✅ Remove items and undo actions  
- ✅ Screenshot validation against baseline  

---

✍️ Developed with ❤️ using **Java + Appium + TestNG + Allure**
