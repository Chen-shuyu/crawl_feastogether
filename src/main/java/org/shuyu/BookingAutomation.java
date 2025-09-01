package org.shuyu;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Feastogether 自動訂位系統
 */
public class BookingAutomation {

    private static final Logger logger = LoggerFactory.getLogger(BookingAutomation.class);
    private static final String BASE_URL = "https://www.feastogether.com.tw/";
    private static String BOOKING_URL = "";
    private static final String BOOKING_URBANPARADISE_URL = "https://www.feastogether.com.tw/booking/URBANPARADISE";
    private static final String BOOKING_Eatogether_URL = "https://www.feastogether.com.tw/booking/Eatogether";

    // 登入資訊 - 請修改為您的實際帳號
    private static final String PHONE_NUMBER = "0972896698";
    private static final String PASSWORD = "59205006";

    // 訂位資訊
    private static final String STORE_NAME = "信義店";
    private static final String ADULT_COUNT = "4";
    private static final String MEAL_PERIOD = "晚餐";
    private static final String BOOKING_DATE = "2025/10/01";

    private WebDriver driver;
    private WebDriverWait wait;

    public BookingAutomation() {
        initializeDriver();
    }

    /**
     * 初始化 WebDriver
     */
    private void initializeDriver() {
        try {
            logger.info("開始初始化 WebDriver...");

//            WebDriverManager.chromedriver().setup();
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-win64/chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36");
            options.addArguments("--disable-web-security");
            options.addArguments("--disable-features=VizDisplayCompositor");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // 如果需要無頭模式運行，取消下面註解
//             options.addArguments("--headless");

            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, 20);

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            logger.info("WebDriver 初始化完成");

        } catch (Exception e) {
            logger.error("WebDriver 初始化失敗: {}", e.getMessage(), e);
            throw new RuntimeException("WebDriver 初始化失敗", e);
        }
    }

    /**
     * 執行完整的訂位流程
     */
    public void executeBookingProcess() {
        try {
            // 步驟 1: 開啟首頁並登入
//            loginToWebsite();

            // 步驟 2: 導航到訂位頁面
            navigateToBookingPage();

            // 步驟 3: 處理說明頁面彈窗
            handleInfoModal();

            // 步驟 4: 填寫訂位資訊
            fillBookingInformation();

            // 步驟 5: 點選搜尋
//            clickSearchButton();

            logger.info("訂位流程執行完成");

        } catch (Exception e) {
            logger.error("訂位流程執行失敗: {}", e.getMessage(), e);
        }
    }

    /**
     * 登入網站
     */
    private void loginToWebsite() throws Exception {
        logger.info("開始登入流程...");

        // 開啟首頁
        driver.get(BASE_URL);
        Thread.sleep(3000);

        // 找到並點選"會員登入"按鈕
        WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), '會員登入') or contains(text(), '登入')]")
                )
        );
        loginButton.click();
        logger.info("已點選會員登入按鈕");

        Thread.sleep(2000);

        // 輸入手機號碼
        WebElement phoneInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@type='tel' or @placeholder='手機號碼' or contains(@name, 'phone')]")
                )
        );
        phoneInput.clear();
        phoneInput.sendKeys(PHONE_NUMBER);
        logger.info("已輸入手機號碼");

        // 輸入密碼
        WebElement passwordInput = driver.findElement(
                By.xpath("//input[@type='password' or @placeholder='密碼' or contains(@name, 'password')]")
        );
        passwordInput.clear();
        passwordInput.sendKeys(PASSWORD);
        logger.info("已輸入密碼");

        // 點選登入按鈕
        WebElement submitButton = driver.findElement(
                By.xpath("//button[contains(text(), '登入') and @type='submit']")
        );
        submitButton.click();
        logger.info("已點選登入按鈕");

        // 等待登入完成（檢查頁面變化）
        Thread.sleep(5000);
        logger.info("登入流程完成");
    }

    /**
     * 導航到訂位頁面
     */
    private void navigateToBookingPage() throws Exception {
        logger.info("導航到訂位頁面: {}", BOOKING_URL);
        driver.get(BOOKING_URL);
        Thread.sleep(3000);
        logger.info("已進入訂位頁面");
    }

    /**
     * 處理說明彈窗
     */
    private void handleInfoModal() throws Exception {
        logger.info("檢查是否有說明彈窗...");
        try {
            // 尋找關閉按鈕 (X)
            List<WebElement> allSvgs = driver.findElements(By.tagName("svg"));

            // 先試第10個位置（快速方式）
            if (allSvgs.size() > 10 && "CloseIcon".equals(allSvgs.get(10).getAttribute("data-testid"))) {
                System.out.println("先試第10個位置（快速方式）");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", allSvgs.get(10));
            } else {
                System.out.println("如果第10個不是，再用遍歷方式");
                // 如果第10個不是，再用遍歷方式
                for (WebElement svg : allSvgs) {
                    if ("CloseIcon".equals(svg.getAttribute("data-testid"))) {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", svg);
                        break;
                    }
                }
            }
            logger.info("已關閉說明彈窗");
            Thread.sleep(2000);

        } catch (Exception e) {
            logger.info("未發現說明彈窗或已關閉");
        }
    }

    /**
     * 填寫訂位資訊
     */
    private void fillBookingInformation() throws Exception {
        logger.info("開始填寫訂位資訊...");

        // 選擇店別
//        selectStore();

        // 選擇成員數量
        selectAdultCount();

        // 選擇用餐餐期
//        selectMealPeriod();

        // 選擇用餐時間
//        selectBookingDate();

        logger.info("訂位資訊填寫完成");
    }

    /**
     * 選擇店別
     */
    private void selectStore() throws Exception {
        logger.info("選擇店別: {}", STORE_NAME);

        try {
            // 方法1: 嘗試下拉選單
            WebElement storeSelect = driver.findElement(
                    By.xpath("//select[contains(@name, 'store') or contains(@id, 'store')]")
            );
            Select storeDropdown = new Select(storeSelect);
            storeDropdown.selectByVisibleText(STORE_NAME);

        } catch (Exception e1) {
            try {
                // 方法2: 嘗試點選選項
                WebElement storeOption = driver.findElement(
                        By.xpath("//option[contains(text(), '" + STORE_NAME + "')]")
                );
                storeOption.click();

            } catch (Exception e2) {
                // 方法3: 嘗試其他可能的元素
                WebElement storeElement = driver.findElement(
                        By.xpath("//*[contains(text(), '" + STORE_NAME + "')]")
                );
                storeElement.click();
            }
        }

        Thread.sleep(1000);
        logger.info("店別選擇完成: {}", STORE_NAME);
    }

    /**
     * 選擇成人數量
     */
    private void selectAdultCount() throws Exception {
        logger.info("選擇成人數量: {}", ADULT_COUNT);

        // 1. 點擊三角形展開人數選擇器
        // 1.1 找到包含「位大人」的區域對應的 ArrowDropDownIcon
        WebElement expandButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[contains(text(), '成員')]/following-sibling::*//svg[@data-testid='ArrowDropDownIcon']")
                )
        );//*[@id="booking-area"]/form/div/div[2]/div[2]/div[2]/div[1]/svg
        System.out.println("找到元素 ");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", expandButton);
        Thread.sleep(500);

        // 2. 找到成人的+按鈕，點擊到目標人數
        WebElement plusButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(text(), '大人')]/following-sibling::*//button[contains(text(), '+')]")
                )
        );

        // 假設預設是2人，需要點擊 (ADULT_COUNT - 1) 次
        for (int i = 2; i < Integer.parseInt(ADULT_COUNT); i++) {
            plusButton.click();
            Thread.sleep(200);
        }

        logger.info("成人數量選擇完成");
    }

    /**
     * 選擇用餐餐期
     */
    private void selectMealPeriod() throws Exception {
        logger.info("選擇用餐餐期: {}", MEAL_PERIOD);

        try {
            WebElement mealSelect = driver.findElement(
                    By.xpath("//select[contains(@name, 'meal') or contains(@id, 'meal') or contains(@name, 'period')]")
            );
            Select mealDropdown = new Select(mealSelect);
            mealDropdown.selectByVisibleText(MEAL_PERIOD);

        } catch (Exception e1) {
            try {
                WebElement mealOption = driver.findElement(
                        By.xpath("//option[contains(text(), '" + MEAL_PERIOD + "')]")
                );
                mealOption.click();

            } catch (Exception e2) {
                logger.warn("無法選擇用餐餐期，將嘗試其他方法");
            }
        }

        Thread.sleep(1000);
        logger.info("用餐餐期選擇完成");
    }

    /**
     * 選擇訂位日期
     */
    private void selectBookingDate() throws Exception {
        logger.info("選擇訂位日期: {}", BOOKING_DATE);

        try {
            // 尋找日期輸入欄位
            WebElement dateInput = driver.findElement(
                    By.xpath("//input[@type='date' or contains(@name, 'date') or contains(@id, 'date')]")
            );
            dateInput.clear();
            dateInput.sendKeys(BOOKING_DATE);

        } catch (Exception e1) {
            try {
                // 如果是日期選擇器，嘗試其他方法
                WebElement dateElement = driver.findElement(
                        By.xpath("//*[contains(@class, 'date') or contains(@class, 'calendar')]")
                );
                dateElement.click();

                // 這裡需要根據實際的日期選擇器介面來調整
                Thread.sleep(2000);

            } catch (Exception e2) {
                logger.warn("無法選擇訂位日期，需要手動檢查頁面結構");
            }
        }

        Thread.sleep(1000);
        logger.info("訂位日期選擇完成");
    }

    /**
     * 點選搜尋按鈕
     */
    private void clickSearchButton() throws Exception {
        logger.info("尋找並點選搜尋按鈕...");

        try {
            WebElement searchButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(text(), '搜尋') or contains(text(), '查詢') or @type='submit']")
                    )
            );
            searchButton.click();
            logger.info("已點選搜尋按鈕");

            // 等待搜尋結果載入
            Thread.sleep(5000);

        } catch (Exception e) {
            logger.error("無法找到搜尋按鈕: {}", e.getMessage());
        }
    }

    /**
     * 關閉瀏覽器
     */
    public void close() {
        if (driver != null) {
            logger.info("正在關閉瀏覽器...");
            driver.quit();
            logger.info("瀏覽器已關閉");
        }
    }

    /**
     * 主程式入口
     */
    public static void main(String[] args) {
        BookingAutomation automation = null;

        try {
            // 檢查登入資訊是否已設定
            if (PHONE_NUMBER.equals("您的手機號碼") || PASSWORD.equals("您的密碼")) {
                logger.error("請先在程式中設定正確的手機號碼和密碼");
                return;
            }

            if (args[0].equals("UP")){
                BOOKING_URL = BOOKING_URBANPARADISE_URL;
            } else {
                BOOKING_URL = BOOKING_Eatogether_URL;
            }

            automation = new BookingAutomation();
            automation.executeBookingProcess();

            // 保持瀏覽器開啟，讓您檢查結果
            logger.info("訂位流程完成，瀏覽器將在 30 秒後關閉，請檢查結果...");
            Thread.sleep(30000);

        } catch (Exception e) {
            logger.error("程式執行失敗: {}", e.getMessage(), e);
        } finally {
            if (automation != null) {
                automation.close();
            }
        }
    }
}