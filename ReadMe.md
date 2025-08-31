# Feastogether 自動訂位系統

## 專案描述
自動化訂位系統，用於 feastogether.com.tw 網站的餐廳預訂流程。

## 技術架構
- Java 1.8 (OpenJDK)
- Maven 3.9.x
- Selenium 3.141.59
- WebDriverManager 4.4.3

## 安裝和執行
1. 確保已安裝 Java 8 和 Maven
2. 執行 `mvn clean compile`
3. 修改程式中的登入資訊
4. 執行主程式

## 功能特色
- 自動登入會員
- 自動選擇餐廳和訂位資訊
- 支援多種元素查找策略
- 詳細的日誌記錄

## 注意事項
- 請勿濫用此工具
- 遵守網站使用條款
- 僅供學習和個人使用

## 建議的開發步驟：
1. 先執行調試程式分析頁面結構
   執行 StepByStepDebug.java 來：

   * 分析首頁的登入按鈕位置
   * 分析訂位頁面的表單元素
   * 找到正確的元素選擇器

2. 修改主程式中的登入資訊 
在 BookingAutomation.java 中修改這兩行：
    ```JAVA
   private static final String PHONE_NUMBER = "您的實際手機號碼";
   private static final String PASSWORD = "您的實際密碼";
   ```
3. 程式特點說明
   * 主程式 (BookingAutomation.java)：

     * 包含完整的訂位流程
     * 使用多種元素查找策略（增加成功率）
     * 有詳細的日誌記錄
     * 包含錯誤處理機制

   * 調試工具 (DebugHelper.java)：

     * 可以分析頁面上所有按鈕、輸入欄位、下拉選單
     * 幫助找到正確的元素選擇器
     * 可以搜尋包含特定文字的元素

   * 逐步調試 (StepByStepDebug.java)：

     * 讓您可以一步步檢查每個階段
     * 會暫停等待您確認
     * 會列印出所有相關元素的資訊

4. 使用建議
   第一次執行請使用調試程式：
   ```bash
    # 在 IntelliJ 中執行 StepByStepDebug.java
    # 或使用 Maven
    mvn exec:java -Dexec.mainClass="com.booking.StepByStepDebug"
   ```
這樣可以幫助我們：

1. 確認登入按鈕的正確選擇器
2. 分析訂位表單的實際結構
3. 找到所有需要填寫的欄位
4. 確認搜尋按鈕的位置

執行後，請告訴我調試程式列印出來的元素資訊，這樣我就可以根據實際的頁面結構來優化選擇器！