package org.shuyu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.shuyu.util.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.TimeUnit;
import java.util.Scanner;

/**
 * 逐步調試程式 - 用於分析網頁結構
 */
public class StepByStepDebug {

    private static final Logger logger = LoggerFactory.getLogger(StepByStepDebug.class);
    private static final String BASE_URL = "https://www.feastogether.com.tw/";

    public static void main(String[] args) {
        WebDriver driver = null;
        Scanner scanner = new Scanner(System.in);

        try {
            logger.info("開始初始化 WebDriver...");

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36");

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            logger.info("WebDriver 初始化完成");

            // 步驟 1: 開啟首頁
            logger.info("開啟首頁...");
            driver.get(BASE_URL);
            Thread.sleep(3000);

            System.out.println("請檢查首頁是否正常載入，然後按 Enter 繼續...");
            scanner.nextLine();

            // 分析首頁結構
            logger.info("分析首頁元素結構...");
            DebugHelper.printElementsContainingText(driver, "登入");
            DebugHelper.printElementsContainingText(driver, "會員");
            DebugHelper.printAllButtons(driver);

            System.out.println("請檢查上面的日誌，找到正確的登入按鈕，然後按 Enter 繼續...");
            scanner.nextLine();

            // 步驟 2: 進入訂位頁面
            logger.info("進入訂位頁面...");
            driver.get("https://www.feastogether.com.tw/booking/URBANPARADISE");
            Thread.sleep(5000);

            System.out.println("請檢查訂位頁面是否正常載入，然後按 Enter 繼續...");
            scanner.nextLine();

            // 分析訂位頁面結構
            logger.info("分析訂位頁面元素結構...");
            DebugHelper.printAllButtons(driver);
            DebugHelper.printAllInputs(driver);
            DebugHelper.printAllSelects(driver);

            System.out.println("請檢查上面的日誌，確認訂位表單的元素結構，然後按 Enter 繼續...");
            scanner.nextLine();

            // 尋找特定元素
            DebugHelper.printElementsContainingText(driver, "信義店");
            DebugHelper.printElementsContainingText(driver, "成人");
            DebugHelper.printElementsContainingText(driver, "晚餐");
            DebugHelper.printElementsContainingText(driver, "搜尋");

            logger.info("調試完成，瀏覽器將在 10 秒後關閉...");
            Thread.sleep(10000);

        } catch (Exception e) {
            logger.error("調試過程中發生錯誤: {}", e.getMessage(), e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
            scanner.close();
        }
    }
}