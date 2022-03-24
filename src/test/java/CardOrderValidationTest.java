import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderValidationTest {
    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }
    @AfterEach
    void teardown() {

        driver.quit();
        driver = null;
    }
    @Test
    public void shouldSendFormWithEmptyFieldSurnameAndName() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+70123456789");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }
    @Test
    public void shouldSendFormWithEmptyFieldPhone() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Фомина Марина");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }
    @Test
    public void shouldSendFormWithoutClickInCheckbox() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Фомина Марина");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+70123456789");

        String actual = driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__text")).getText().trim();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        assertEquals(expected, actual);
    }
    @Test
    public void shouldSendFormWithSurnameAndNameInLatin() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Fomina Marina");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+70123456789");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }
    @Test
    public void shouldSendPhoneNumberOf10Digits() {

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Фомина Марина");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7012345678");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }
}
