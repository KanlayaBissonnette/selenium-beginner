package com.herokuapp.theinternet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class LoginTest {
	private WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional String browser) {
		// Create driver

		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();

			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();

			break;

		default:
			System.out.println("Do not know how to start" + browser + ", starting chrome instead");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();

			break;
		}

		// sleep
		sleep();
		// maximize browser window
		driver.manage().window().maximize();
		// implicit wait
		// WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	}

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveloginTest() {
		System.out.println("Starting LoginTest");

//open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("page is opened");

//enter username
		WebElement username = driver.findElement(By.xpath("/html//input[@id='username']"));
		username.sendKeys("tomsmith");

//enter password
		WebElement password = driver.findElement(By.xpath("/html//input[@id='password']"));
		password.sendKeys("SuperSecretPassword!");
		
		// explicit wait
		
//click login button
		WebElement logInButton = driver.findElement(By.xpath("//form[@id='login']//i[@class='fa fa-2x fa-sign-in']"));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(logInButton));
		logInButton.click();

		// verifications;
		// new url
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");

		// logout button is visible
		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait2.until(ExpectedConditions.elementToBeClickable(logOutButton));
		Assert.assertTrue(logOutButton.isDisplayed());
		

		// successful login message
		WebElement successMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String expectedMessage = "You logged into a secure area!";
		String actualMessage = successMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedMessage));

	}

	
	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeloginTest(String username, String password, String expectedErrorMessage) {
		System.out.println("Starting negativeloginTest with " + username + " and" + password);

//open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("page is opened");

//enter username
		WebElement usernameElement = driver.findElement(By.xpath("/html//input[@id='username']"));
		// WebElement username = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);

//enter password
		WebElement passwordElement = driver.findElement(By.xpath("/html//input[@id='password']"));
		// WebElement password = driver.findElement(By.name("password"));
		passwordElement.sendKeys(password);

//click login button
		WebElement logInButton = driver.findElement(By.xpath("//form[@id='login']//i[@class='fa fa-2x fa-sign-in']"));
		// WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		// verification
		// error login message
		WebElement errorMessage = driver.findElement(By.xpath("/html//div[@id='flash']"));
		String actualErrorMessage = errorMessage.getText();
		Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),
				"Actual error message does not contain expected");

	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
