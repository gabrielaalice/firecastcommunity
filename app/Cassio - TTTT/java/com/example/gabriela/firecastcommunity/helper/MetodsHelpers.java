package com.example.gabriela.firecastcommunity.helper;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by user on 11/06/2017.
 */

public class MetodsHelpers {

    public static String normalizeString(String str) {
        if (str != null){
            str = Normalizer.normalize(str, Normalizer.Form.NFD);
            str = str.replaceAll("[^\\p{ASCII}]", "");
        }
        return str;
    }

    public static String convertNumberInText(Locale locale, double number){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        return numberFormat.format(number);
    }

    public static String convertNumberInText(String language, String country, double number){
        Locale locale = new Locale(language, country);
        return convertNumberInText(locale, number);
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