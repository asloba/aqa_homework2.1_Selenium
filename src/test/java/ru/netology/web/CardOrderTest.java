package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

//import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.cssSelector;

//@ExtendWith(SeleniumJupiter.class)

public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendForm() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.strip());
    }

    @Test
    void shouldSendEmptyForm() {
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector("[class='input__sub']")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void shouldSendFormWithoutPhone() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Сергеев Сергей");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void shouldSendFormWithoutName() {
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999111");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", message.strip());
    }

    @Test
    void shouldSendFormWithoutAgreement() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Сергеев Сергей");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999111");
        driver.findElement(cssSelector("button")).click();
        assertTrue(driver.findElement(cssSelector(".input_invalid > .checkbox__box")).isEnabled());
    }

    @Test
    void shouldSendFormWithIncorrectName() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Ivanov Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+79999999999");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", message.strip());
    }

    @Test
    void shouldSendFormWithIncorrectPhone() {
        driver.findElement(cssSelector("[data-test-id=name] input")).sendKeys("Смирнов Иван");
        driver.findElement(cssSelector("[data-test-id='phone'] input")).sendKeys("+799999111");
        driver.findElement(cssSelector(".checkbox__box")).click();
        driver.findElement(cssSelector("button")).click();
        String message = driver.findElement(cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message.strip());
    }
}
