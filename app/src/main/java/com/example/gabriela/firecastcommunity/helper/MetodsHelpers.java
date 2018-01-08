package com.example.gabriela.firecastcommunity.helper;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Locale;

public class MetodsHelpers {

    public static String normalizeString(String str) {
        if (str != null){
            str = Normalizer.normalize(str, Normalizer.Form.NFD);
            str = str.replaceAll("[^\\p{ASCII}]", "");
        }
        return str;
    }

    public static String convertNumberInText(double number){
        return String.format("%.1f", number);
    }

    public static String convertDoubleInText(double number){
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        String str = nf.format(number);
        return str;
    }

    public static String convertDateTimeInString(String dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.substring(8,10) + "/"
                + dateTime.substring(5,7) + "/"
                + dateTime.substring(0,4) + " | "
                + dateTime.substring(11,13) + ":"
                + dateTime.substring(14,16);
    }

    public static String insertZeroInStartNumber(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }
}