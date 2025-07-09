package com.codegym.foody.formatter;

import com.codegym.foody.model.enumable.DiscountType;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DiscountFormatter {
    private static final Locale LOCALE_VIETNAM = new Locale("vi", "VN");
    public static String formatDiscount(BigDecimal discount, DiscountType discountType) {
        NumberFormat formatter = NumberFormat.getInstance(LOCALE_VIETNAM);
        return switch (discountType) {
            case PERCENT -> formatter.format(discount) + " %";
            case FIXED -> formatter.format(discount) + " ₫";
            default -> throw new IllegalArgumentException("Unknown discount type");
        };
    }
}
