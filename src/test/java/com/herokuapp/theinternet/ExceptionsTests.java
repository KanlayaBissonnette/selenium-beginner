package com.herokuapp.theinternet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class ExceptionsTests {
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
		// implicit
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

//challenge

//create new test method called notVisibleTest
	@Test(priority = 1)
	public void notVisibleTest() {
		System.out.println("Starting notVisibleTest");

//open test page. In the method, first open the page http://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");

//Then find locator for startButton and click on start button
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//Then get finish element text 		
		WebElement finishElement = driver.findElement(By.id("finish"));

//Explicit wait		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(finishElement));

		String finishText = finishElement.getText();

//compare actual finish element with expected "Hello World!" using testNG assert class			
		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text:" + finishText);

		// startButton.click(); //this will fail the test since there is startButton
		// after Hello World! shows.
	}

	@Test(priority = 2)
	public void timeoutTest() {
		System.out.println("Starting notVisibleTest");

//open test page. In the method, first open the page http://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");

//Then find locator for startButton and click on start button
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

//Then get finish element text 		
		WebElement finishElement = driver.findElement(By.id("finish"));

//Explicit wait		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		try {
			wait.until(ExpectedConditions.visibilityOf(finishElement));
		} catch (TimeoutException exception) {
			System.out.println("Exception catched: " + exception.getMessage());
			sleep();
		}
		String finishText = finishElement.getText();

//compare actual finish element with expected "Hello World!" using testNG assert class			
		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text:" + finishText);

		// startButton.click(); //this will fail the test since there is startButton
		// after Hello World! shows.
	}

	@Test(priority = 3)
	public void NoSuchElemementTest() {
		System.out.println("Starting notVisibleTest");

//open test page. In the method, first open the page http://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/2");

//Then find locator for startButton and click on start button
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button[.='Start']"));
		startButton.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Assert.assertTrue(
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), "Hello World!")),
				"Couldn't verify expected text 'Hello World!'");

//		WebElement finishElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
//		String finishText = finishElement.getText();
////compare actual finish element with expected "Hello World!" using testNG assert class			
//		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text:" + finishText);
	}

	@Test
	public void staleElementTest() {

		driver.get("http://the-internet.herokuapp.com/dynamic_controls");

		WebElement checkbox = driver.findElement(By.xpath("//div[@id='checkbox']"));
		WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(), 'Remove')]"));

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		removeButton.click();
		// Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkbox)), "A
		// check box should not be visable");
		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)), "A check box should not be visable");

		WebElement addButton = driver.findElement(By.xpath("//button[contains(text(), 'Add')]"));
		addButton.click();

		WebElement checkbox2 = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='checkbox']")));
		Assert.assertTrue(checkbox2.isDisplayed(), "Checkbox should be reappears");

	}
@Test
public void disableElementTest() {
	driver.get("http://the-internet.herokuapp.com/dynamic_controls");
	
	WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(),'Enable')]"));
	WebElement textField = driver.findElement(By.xpath("//input[@type='text']"));
	
	enableButton.click();
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	wait.until(ExpectedConditions.elementToBeClickable(textField));
	
	textField.sendKeys("My name is Kanlaya");
	Assert.assertEquals(textField.getAttribute("value"), "My name is Kanlaya");
		
}

	
	

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.close();
	}

	private void sleep() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
