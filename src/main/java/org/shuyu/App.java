package org.shuyu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
/**
 * 基礎測試程式 - 確認 Selenium 可以正常開啟 feastogether.com.tw
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String BASE_URL = "https://www.feastogether.com.tw/";
    private static final String BOOKING_URL = "https://www.feastogether.com.tw/booking/URBANPARADISE";

    public static void main(String[] args) {
        WebDriver driver = null;

        try {
            logger.info("開始初始化 WebDriver...");

            // 手動設定 ChromeDriver 路徑（如果需要）
             System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-win64/chromedriver.exe");

            // 設定 Chrome 選項
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36");
            options.addArguments("--disable-web-security");
            options.addArguments("--disable-features=VizDisplayCompositor");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // 如果需要無頭模式運行，取消下面註解
            // options.addArguments("--headless");

            // 創建 ChromeDriver 實例
            driver = new ChromeDriver(options);

            // 設定等待時間 (Selenium 3.x 語法)
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

            // 最大化瀏覽器視窗
            driver.manage().window().maximize();

            logger.info("WebDriver 初始化完成");

            // 測試 1: 開啟首頁
            logger.info("正在開啟首頁: {}", BASE_URL);
            driver.get(BASE_URL);
            Thread.sleep(3000); // 等待頁面載入

            String homePageTitle = driver.getTitle();
            logger.info("首頁標題: {}", homePageTitle);

            // 測試 2: 開啟訂位頁面
            logger.info("正在開啟訂位頁面: {}", BOOKING_URL);
            driver.get(BOOKING_URL);
            Thread.sleep(5000); // 等待頁面載入

            String bookingPageTitle = driver.getTitle();
            logger.info("訂位頁面標題: {}", bookingPageTitle);

            // 測試 3: 檢查當前網址
            String currentUrl = driver.getCurrentUrl();
            logger.info("當前網址: {}", currentUrl);

            // 保持瀏覽器開啟一段時間，方便觀察
            logger.info("測試完成，瀏覽器將在 15 秒後關閉...");
            Thread.sleep(15000);

        } catch (Exception e) {
            logger.error("程式執行過程中發生錯誤: {}", e.getMessage(), e);
        } finally {
            // 確保關閉瀏覽器
            if (driver != null) {
                logger.info("正在關閉瀏覽器...");
                driver.quit();
                logger.info("瀏覽器已關閉");
            }
        }
    }
}