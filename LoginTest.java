package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * LoginTest.java - Single File Selenium Maven TestNG
 * URL    : https://www.saucedemo.com
 * Author : Kowsalya
 */
public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    // ── Config - Update these values ──────────────────────────────────────
    String url      = "https://www.saucedemo.com";  // Your app URL
    String username = "standard_user";               // Your username
    String password = "secret_sauce";                // Your password

    // ── Locators ──────────────────────────────────────────────────────────
    By usernameField  = By.id("user-name");
    By passwordField  = By.id("password");
    By loginButton    = By.id("login-button");
    By errorMessage   = By.cssSelector("[data-test='error']");
    By dashboardTitle = By.className("title");

    // ── Setup ─────────────────────────────────────────────────────────────
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(url);
        System.out.println("Browser opened: " + url);
    }

    // ── Teardown ──────────────────────────────────────────────────────────
    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
        System.out.println("Browser closed.");
    }

    // ── Helper: Login ─────────────────────────────────────────────────────
    private void doLogin(String user, String pass) {
        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        userField.clear();
        userField.sendKeys(user);

        WebElement passField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passField.clear();
        passField.sendKeys(pass);

        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    // ── TC01: Valid Login ─────────────────────────────────────────────────
    @Test(priority = 1, description = "Login with valid credentials")
    public void testValidLogin() {
        doLogin(username, password);
        boolean isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardTitle)).isDisplayed();
        Assert.assertTrue(isVisible, "Dashboard should be visible after login.");
        System.out.println("✅ TC01 - Valid Login: PASSED");
    }

    // ── TC02: Invalid Username ────────────────────────────────────────────
    @Test(priority = 2, description = "Login with invalid username")
    public void testInvalidUsername() {
        doLogin("wrong_user", password);
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        Assert.assertFalse(error.isEmpty(), "Error message should appear.");
        System.out.println("✅ TC02 - Invalid Username Error: " + error);
    }

    // ── TC03: Invalid Password ────────────────────────────────────────────
    @Test(priority = 3, description = "Login with invalid password")
    public void testInvalidPassword() {
        doLogin(username, "wrong_pass");
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        Assert.assertFalse(error.isEmpty(), "Error message should appear.");
        System.out.println("✅ TC03 - Invalid Password Error: " + error);
    }

    // ── TC04: Empty Fields ────────────────────────────────────────────────
    @Test(priority = 4, description = "Login with empty credentials")
    public void testEmptyCredentials() {
        doLogin("", "");
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        Assert.assertFalse(error.isEmpty(), "Error message should appear for empty fields.");
        System.out.println("✅ TC04 - Empty Fields Error: " + error);
    }
}
