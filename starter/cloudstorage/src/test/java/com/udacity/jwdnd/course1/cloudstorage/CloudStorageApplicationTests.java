package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
        Assertions.assertTrue(webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg"))).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
    }

    @Test
    public void verifiedAccessHomePageTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:" + this.port + "/home");
        boolean isLoginPageDisplayed = driver.findElement(By.id("inputUsername")).isDisplayed();
        if (isLoginPageDisplayed) {
            System.out.println("Test Passed: Home page is not accessible without logging in");
        } else {
            System.out.println("Test Failed: Home page is accessible without logging in");
        }
        driver.quit();
    }

    @Test
    public void signUpLoginTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:" + this.port + "/signup");
        // Fill in the sign-up form with user details
        driver.findElement(By.id("inputFirstName")).sendKeys("firstName");
        driver.findElement(By.id("inputLastName")).sendKeys("lastName");
        driver.findElement(By.id("inputUsername")).sendKeys("abc");
        driver.findElement(By.id("inputPassword")).sendKeys("password");
        // Submit the form to create the new user
        driver.findElement(By.id("buttonSignUp")).click();

        // Log in the new user
        driver.findElement(By.id("inputUsername")).sendKeys("abc");
        driver.findElement(By.id("inputPassword")).sendKeys("password");
        driver.findElement(By.id("login-button")).click();

        boolean isHomePageDisplayedLogout = driver.findElement(By.id("logoutDiv")).isDisplayed();
        boolean isHomePageDisplayedContent = driver.findElement(By.id("contentDiv")).isDisplayed();
        if (isHomePageDisplayedLogout && isHomePageDisplayedContent) {
            System.out.println("Test Passed: New user can access the home page");
        } else {
            System.out.println("Test Failed: New user cannot access the home page");
        }

        WebElement logoutButton = driver.findElement(By.cssSelector("#logoutDiv form button[type='submit']"));
        logoutButton.click();

        // Verify that the home page is no longer accessible
        boolean isLoginPageDisplayed = driver.findElement(By.id("login-button")).isDisplayed();
        if (isLoginPageDisplayed) {
            System.out.println("Test Passed: Home page is not accessible after logging out");
        } else {
            System.out.println("Test Failed: Home page is still accessible after logging out");
        }

        // Close the browser
        driver.quit();
    }

    @Test
    public void notesTest() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 2);

        driver.get("http://localhost:" + this.port + "/signup");
        // Fill in the sign-up form with user details
        driver.findElement(By.id("inputFirstName")).sendKeys("firstName");
        driver.findElement(By.id("inputLastName")).sendKeys("lastName");
        driver.findElement(By.id("inputUsername")).sendKeys("abcd");
        driver.findElement(By.id("inputPassword")).sendKeys("password");
        // Submit the form to create the new user
        driver.findElement(By.id("buttonSignUp")).click();

        // Log in the new user
        driver.findElement(By.id("inputUsername")).sendKeys("abcd");
        driver.findElement(By.id("inputPassword")).sendKeys("password");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("nav-notes-tab")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonAddNote")));
        driver.findElement(By.id("buttonAddNote")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        driver.findElement(By.id("note-title")).sendKeys("abc");
        driver.findElement(By.id("note-description")).sendKeys("abc");
        driver.findElement(By.id("buttonNoteSubmit")).click();
        driver.findElement(By.id("nav-notes-tab")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditNote1")));
        boolean isNoteEdited = driver.findElement(By.id("buttonEditNote1")).isDisplayed();
        if (isNoteEdited) {
            System.out.println("Test Passed: Created note successful");
        } else {
            System.out.println("Test Failed: Error while creating note");
        }


        driver.findElement(By.id("nav-notes-tab")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditNote1")));
        driver.findElement(By.id("buttonEditNote1")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        driver.findElement(By.id("note-title")).sendKeys("abcd");
        driver.findElement(By.id("note-description")).sendKeys("abcd");
        driver.findElement(By.id("buttonNoteSubmit")).click();

        driver.findElement(By.id("nav-notes-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("deleteNote1")).click();
        boolean isNoteDeleted = driver.findElements(By.xpath("//td[contains(text(),'abcabcd')]")).isEmpty();
        if (isNoteDeleted) {
            System.out.println("Test Passed: Deleted");
        } else {
            System.out.println("Test Failed: Error while deleting note");
        }
        // Close the browser
        driver.quit();
    }

    @Test
    public void credentialsTest() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 2);

        driver.get("http://localhost:" + this.port + "/signup");
        // Fill in the sign-up form with user details
        driver.findElement(By.id("inputFirstName")).sendKeys("firstName");
        driver.findElement(By.id("inputLastName")).sendKeys("lastName");
        driver.findElement(By.id("inputUsername")).sendKeys("abcde");
        driver.findElement(By.id("inputPassword")).sendKeys("password");
        // Submit the form to create the new user
        driver.findElement(By.id("buttonSignUp")).click();

        // Log in the new user
        driver.findElement(By.id("inputUsername")).sendKeys("abcde");
        driver.findElement(By.id("inputPassword")).sendKeys("password");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("nav-credentials-tab")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonAddCredential")));
        driver.findElement(By.id("buttonAddCredential")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        driver.findElement(By.id("credential-url")).sendKeys("qwer");
        driver.findElement(By.id("credential-username")).sendKeys("qwer");
        driver.findElement(By.id("credential-password")).sendKeys("qwer");
        driver.findElement(By.id("buttonCredentialSubmit")).click();
        driver.findElement(By.id("nav-credentials-tab")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditCredential1")));
        boolean isCredentialsEdited = driver.findElement(By.id("buttonEditCredential1")).isDisplayed();
        if (isCredentialsEdited) {
            System.out.println("Test Passed: Created credential successful");
        } else {
            System.out.println("Test Failed: Error while creating credential");
        }


        driver.findElement(By.id("nav-credentials-tab")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditCredential1")));
        driver.findElement(By.id("buttonEditCredential1")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        driver.findElement(By.id("credential-url")).sendKeys("abcd");
        driver.findElement(By.id("credential-username")).sendKeys("abcd");
        driver.findElement(By.id("credential-password")).sendKeys("abcd");
        driver.findElement(By.id("buttonCredentialSubmit")).click();

        driver.findElement(By.id("nav-credentials-tab")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("deleteCredential1")).click();
        boolean isCredentialsDeleted = driver.findElements(By.xpath("//td[contains(text(),'abcabcd')]")).isEmpty();
        if (isCredentialsDeleted) {
            System.out.println("Test Passed: Deleted");
        } else {
            System.out.println("Test Failed: Error while deleting note");
        }
        // Close the browser
        driver.quit();
    }
}
