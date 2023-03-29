package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
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
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
		WebElement inputFirstName = driver.findElement(By.id("firstname"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lastname")));
		WebElement inputLastName = driver.findElement(By.id("lastname"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
		WebElement inputUsername = driver.findElement(By.id("username"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
		WebElement inputPassword = driver.findElement(By.id("password"));
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
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
		WebElement loginUserName = driver.findElement(By.id("username"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
		WebElement loginPassword = driver.findElement(By.id("password"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Files"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
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
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
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
	public void testUnauthorizedUserCannotAccessApp() {
		// Try to access the different endpoints
		// Files
		driver.get("http://localhost:" + this.port + "/files");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		// Notes
		driver.get("http://localhost:" + this.port + "/notes");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		// Credentials
		driver.get("http://localhost:" + this.port + "/credentials");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		// Try to access Signup and Login pages
		// Signup
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());

		// Login
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	public void testSignup() {
		// Try to signup a new user
		doMockSignUp("SignupTest", "Test", "SignupTest", "SignupTest");
		// Login
		doLogIn("SignupTest", "SignupTest");

		// Assert that the user was logged in successfully
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		Assertions.assertEquals("http://localhost:" + this.port + "/files", driver.getCurrentUrl());

		// Logout
		doLogout();

		// Assert that app cannot be accessed anymore
		driver.get("http://localhost:" + this.port + "/files");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	public void testNoteCreation() {
		String noteTitle = "noteTitle";
		String noteDescription = "noteDescription";

		doMockSignUp("NoteCreation", "NoteCreation", "NoteCreation", "NoteCreation");
		doLogIn("NoteCreation", "NoteCreation");

		// Assert that the user was logged in successfully
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/notes");
		Assertions.assertEquals("http://localhost:" + this.port + "/notes", driver.getCurrentUrl());

		createNote(noteTitle, noteDescription);

		// Assert that note was created successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement userTable = driver.findElement(By.id("userTable"));

		Assertions.assertTrue(userTable.getText().contains(noteTitle));
		Assertions.assertTrue(userTable.getText().contains(noteDescription));
	}

	@Test
	public void testNoteEdit() {
		String noteTitle = "noteTitle";
		String noteDescription = "noteDescription";
		String newTitle = "newTitle";
		String newDescription = "newDescription";

		doMockSignUp("NoteEdit", "NoteEdit", "NoteEdit", "NoteEdit");
		doLogIn("NoteEdit", "NoteEdit");

		// Assert that the user was logged in successfully
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/notes");
		Assertions.assertEquals("http://localhost:" + this.port + "/notes", driver.getCurrentUrl());

		createNote(noteTitle, noteDescription);

		editNote(newTitle, newDescription);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement userTable = driver.findElement(By.id("userTable"));

		Assertions.assertTrue(userTable.getText().contains(newTitle));
		Assertions.assertTrue(userTable.getText().contains(newDescription));
	}

	@Test
	public void testNoteDeletion() {
		String noteTitle = "noteTitle";
		String noteDescription = "noteDescription";

		doMockSignUp("NoteDeletion", "NoteDeletion", "NoteDeletion", "NoteDeletion");
		doLogIn("NoteDeletion", "NoteDeletion");

		// Assert that the user was logged in successfully
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/notes");
		Assertions.assertEquals("http://localhost:" + this.port + "/notes", driver.getCurrentUrl());

		createNote(noteTitle, noteDescription);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement userTable = driver.findElement(By.id("userTable"));

		// Verify that note has been created
		Assertions.assertTrue(userTable.getText().contains(noteTitle));
		Assertions.assertTrue(userTable.getText().contains(noteDescription));

		deleteNote();

		// Verify that note is no longer present
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		userTable = driver.findElement(By.id("userTable"));

		Assertions.assertFalse(userTable.getText().contains(noteTitle));
		Assertions.assertFalse(userTable.getText().contains(noteDescription));
	}

	@Test
	public void testCredentialCreation() {
		String credentialUrl = "credentialUrl";
		String credentialUsername = "credentialUsername";
		String credentialPassword = "credentialPassword";

		doMockSignUp("CredentialCreation", "CredentialCreation", "CredentialCreation", "CredentialCreation");
		doLogIn("CredentialCreation", "CredentialCreation");

		// Assert that the user was logged in successfully
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/credentials");
		Assertions.assertEquals("http://localhost:" + this.port + "/credentials", driver.getCurrentUrl());

		createCredentials(credentialUrl, credentialUsername, credentialPassword);

		// Assert that note was created successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));

		// Assert that credentials are displayed
		Assertions.assertTrue(credentialTable.getText().contains(credentialUrl));
		Assertions.assertTrue(credentialTable.getText().contains(credentialUsername));
	}

	@Test
	public void testCredentialsEdit() {
		String credentialUrl = "credentialUrl";
		String credentialUsername = "credentialUsername";
		String credentialPassword = "credentialPassword";
		String editCredentialUrl = "editCredentialUrl";
		String editCredentialUsername = "editCredentialUsername";
		String editCredentialPassword = "editCredentialPassword";

		doMockSignUp("CredentialEdit", "CredentialEdit", "CredentialEdit", "CredentialEdit");
		doLogIn("CredentialEdit", "CredentialEdit");

		// Assert that the user was logged in successfully
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/credentials");
		Assertions.assertEquals("http://localhost:" + this.port + "/credentials", driver.getCurrentUrl());

		createCredentials(credentialUrl, credentialUsername, credentialPassword);

		editCredentials(editCredentialUrl, editCredentialUsername, editCredentialPassword, credentialPassword);

		// Assert that credentials are displayed
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));

		Assertions.assertTrue(credentialTable.getText().contains(editCredentialUrl));
		Assertions.assertTrue(credentialTable.getText().contains(editCredentialUsername));
	}

	@Test
	public void testCredentialsDeletion() {
		String credentialUrl = "credentialUrl";
		String credentialUsername = "credentialUsername";
		String credentialPassword = "credentialPassword";
		doMockSignUp("CredentialDeletion", "CredentialDeletion", "CredentialDeletion", "CredentialDeletion");
		doLogIn("CredentialDeletion", "CredentialDeletion");

		// Assert that the user was logged in successfully
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/credentials");
		Assertions.assertEquals("http://localhost:" + this.port + "/credentials", driver.getCurrentUrl());

		createCredentials(credentialUrl, credentialUsername, credentialPassword);

		// Assert that credentials are displayed
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));

		Assertions.assertTrue(credentialTable.getText().contains(credentialUrl));
		Assertions.assertTrue(credentialTable.getText().contains(credentialUsername));

		deleteCredentials();

		// Verify that credentials are deleted
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		credentialTable = driver.findElement(By.id("credentialTable"));

		Assertions.assertFalse(credentialTable.getText().contains(credentialUrl));
		Assertions.assertFalse(credentialTable.getText().contains(credentialUsername));
	}

	private void createCredentials(String credentialUrl, String credentialUsername, String credentialPassword) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-credentials-button")));
		WebElement createCredentialsButton = driver.findElement(By.id("create-credentials-button"));
		createCredentialsButton.click();

		insertCredentialsDetails(credentialUrl, credentialUsername, credentialPassword);
	}

	private void editCredentials(String editCredentialUrl, String editCredentialUsername, String editCredentialPassword, String oldPassword) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credentials-button")));
		WebElement editCredentialsButton = driver.findElement(By.id("edit-credentials-button"));
		editCredentialsButton.click();

		// Clear previous data
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialsUrl = driver.findElement(By.id("credential-url"));
		credentialsUrl.clear();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialsUsername = driver.findElement(By.id("credential-username"));
		credentialsUsername.clear();

		// Verify that password is shown unencrypted before clearing
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialsPassword = driver.findElement(By.id("credential-password"));
		credentialsPassword.click();

		Assertions.assertEquals(oldPassword, credentialsPassword.getAttribute("value"));
		credentialsPassword.clear();

		insertCredentialsDetails(editCredentialUrl, editCredentialUsername, editCredentialPassword);
	}

	private void deleteCredentials() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credentials-button")));
		WebElement deleteCredentialsButton = driver.findElement(By.id("delete-credentials-button"));
		deleteCredentialsButton.click();

		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	private void insertCredentialsDetails(String credentialUrl, String credentialUsername, String credentialPassword) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement url = driver.findElement(By.id("credential-url"));
		url.click();
		url.sendKeys(credentialUrl);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement username = driver.findElement(By.id("credential-username"));
		username.click();
		username.sendKeys(credentialUsername);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement password = driver.findElement(By.id("credential-password"));
		password.click();
		password.sendKeys(credentialPassword);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit")));
		WebElement createCredentials = driver.findElement(By.id("credential-submit"));
		createCredentials.click();
	}

	private void createNote(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-note-button")));
		WebElement createNoteButton = driver.findElement(By.id("create-note-button"));
		createNoteButton.click();

		insertNoteDetails(title, description);
	}

	private void editNote(String newTitle, String newDescription) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-button")));
		WebElement editNoteButton = driver.findElement(By.id("edit-note-button"));
		editNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.clear();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.clear();

		insertNoteDetails(newTitle, newDescription);
	}

	private void deleteNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-button")));
		WebElement deleteNoteButton = driver.findElement(By.id("delete-note-button"));
		deleteNoteButton.click();

		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	private void insertNoteDetails(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys(title);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys(description);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit")));
		WebElement createNote = driver.findElement(By.id("note-submit"));
		createNote.click();
	}

	private void doLogout() {
		// Logout
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logoutButton")));
		WebElement logoutButton = driver.findElement(By.id("logoutButton"));
		logoutButton.click();
	}

}
