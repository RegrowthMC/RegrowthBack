package org.lushplugins.regrowthback.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

    public static double round(double num) {
        return new BigDecimal(num)
            .setScale(4, RoundingMode.HALF_UP)
            .doubleValue();
    }

    public static float round(float num) {
        return new BigDecimal(num)
            .setScale(4, RoundingMode.HALF_UP)
            .floatValue();
    }
}
