package org.shuyu.util;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 調試輔助工具 - 用於分析網頁元素結構
 */
public class DebugHelper {

    private static final Logger logger = LoggerFactory.getLogger(DebugHelper.class);

    /**
     * 列印頁面上所有的按鈕
     */
    public static void printAllButtons(WebDriver driver) {
        logger.info("=== 頁面上所有按鈕 ===");
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        for (int i = 0; i < buttons.size(); i++) {
            WebElement button = buttons.get(i);
            String text = button.getText();
            String className = button.getAttribute("class");
            String id = button.getAttribute("id");
            logger.info("按鈕 {}: 文字='{}', class='{}', id='{}'", i+1, text, className, id);
        }
    }

    /**
     * 列印頁面上所有的輸入欄位
     */
    public static void printAllInputs(WebDriver driver) {
        logger.info("=== 頁面上所有輸入欄位 ===");
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        for (int i = 0; i < inputs.size(); i++) {
            WebElement input = inputs.get(i);
            String type = input.getAttribute("type");
            String name = input.getAttribute("name");
            String placeholder = input.getAttribute("placeholder");
            String id = input.getAttribute("id");
            logger.info("輸入欄位 {}: type='{}', name='{}', placeholder='{}', id='{}'",
                    i+1, type, name, placeholder, id);
        }
    }

    /**
     * 列印頁面上所有的下拉選單
     */
    public static void printAllSelects(WebDriver driver) {
        logger.info("=== 頁面上所有下拉選單 ===");
        List<WebElement> selects = driver.findElements(By.tagName("select"));
        for (int i = 0; i < selects.size(); i++) {
            WebElement select = selects.get(i);
            String name = select.getAttribute("name");
            String id = select.getAttribute("id");
            logger.info("下拉選單 {}: name='{}', id='{}'", i+1, name, id);

            // 列印選項
            List<WebElement> options = select.findElements(By.tagName("option"));
            for (WebElement option : options) {
                String optionText = option.getText();
                String optionValue = option.getAttribute("value");
                logger.info("  選項: text='{}', value='{}'", optionText, optionValue);
            }
        }
    }

    /**
     * 列印所有包含特定文字的元素
     */
    public static void printElementsContainingText(WebDriver driver, String text) {
        logger.info("=== 包含文字 '{}' 的元素 ===", text);
        List<WebElement> elements = driver.findElements(
                By.xpath("//*[contains(text(), '" + text + "')]")
        );
        for (int i = 0; i < elements.size(); i++) {
            WebElement element = elements.get(i);
            String tagName = element.getTagName();
            String className = element.getAttribute("class");
            String id = element.getAttribute("id");
            logger.info("元素 {}: 標籤='{}', class='{}', id='{}', 完整文字='{}'",
                    i+1, tagName, className, id, element.getText());
        }
    }

    /**
     * 截圖功能 (如果需要)
     */
    public static void takeScreenshot(WebDriver driver, String fileName) {
        try {
            // 這裡可以加入截圖功能，如果後續需要的話
            logger.info("截圖功能：{}", fileName);
        } catch (Exception e) {
            logger.error("截圖失敗: {}", e.getMessage());
        }
    }
}