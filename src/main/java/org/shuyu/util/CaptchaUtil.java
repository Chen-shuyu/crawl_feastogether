package org.shuyu.util;

import net.sourceforge.tess4j.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class CaptchaUtil {
    static {
        // 載入 OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 處理驗證碼圖片並回傳辨識字串
     * @param captchaFile 驗證碼圖片檔案
     * @param tessdataPath tessdata 路徑 (內含 eng.traineddata / chi_sim.traineddata)
     * @return OCR 結果字串
     */
    public static String recognizeCaptcha(File captchaFile, String tessdataPath) {
        try {
            // 1. OpenCV 前處理
            String cleanPath = preprocessImage(captchaFile.getAbsolutePath());

            // 2. OCR 辨識
            ITesseract instance = new Tesseract();
            instance.setDatapath(tessdataPath);
            instance.setLanguage("eng"); // 根據驗證碼語言修改: eng / chi_sim / chi_tra

            String result = instance.doOCR(new File(cleanPath));
            return result.trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 使用 OpenCV 做灰階 + 二值化
     */
    private static String preprocessImage(String inputPath) {
        Mat src = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_GRAYSCALE);
        if (src.empty()) {
            throw new RuntimeException("讀取圖片失敗: " + inputPath);
        }

        // 二值化，閾值可依實際情況調整
        Imgproc.threshold(src, src, 128, 255, Imgproc.THRESH_BINARY);

        String outputPath = inputPath.replace(".png", "_clean.png");
        Imgcodecs.imwrite(outputPath, src);

        return outputPath;
    }
}