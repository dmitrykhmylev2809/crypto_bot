package com.skillbox.cryptobot.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TextUtil {

    public static String toString(double value) {
        return String.format("%.3f", value);
    }

    public static String bigToString(BigDecimal value) {
        return value.setScale(3, RoundingMode.HALF_UP).toString(); // Округление до 3 знаков после запятой
    }
}
