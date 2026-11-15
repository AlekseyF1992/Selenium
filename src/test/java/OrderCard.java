package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCard {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSubmitRequest() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Мраморов Алексей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79220001111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        WebElement element = driver.findElement(By.cssSelector("[data-test-id='order-success']"));


        assertTrue(element.isDisplayed());
        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", element.getText());
    }

    //негативные кейсы
    // ФИО: латиница
    @Test
    void shouldShowErrorForLatinName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan Petrov");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79220001111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='name']")).getAttribute("class").contains("input_invalid"));
    }

    // ФИО: пустое
    @Test
    void shouldShowErrorForEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79220001111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='name']")).getAttribute("class").contains("input_invalid"));
    }

    // Телефон: пустое
    @Test
    void shouldShowErrorForEmptyPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Мраморов Алексей");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='phone']")).getAttribute("class").contains("input_invalid"));
    }

    // Телефон: неверный формат (короткий номер)
    @Test
    void shouldShowErrorForInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Мраморов Алексей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7922000111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='phone']")).getAttribute("class").contains("input_invalid"));
    }

    // Чекбокс: не отмечен
    @Test
    void shouldShowErrorForUncheckedAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Мраморов Алексей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79220001111");
        driver.findElement(By.tagName("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement']")).getAttribute("class").contains("input_invalid"));
    }
}
