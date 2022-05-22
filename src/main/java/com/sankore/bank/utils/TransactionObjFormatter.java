package com.sankore.bank.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TransactionObjFormatter {

    // For yyyy-MM-dd
    public static final String DATE_MATCHER = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    public static final String DATE_MATCHER_TIME = "^([0-9]{4})-([0-1][0-9])-([0-3][0-9]):([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    public static final String NGN_PHONE_NUM = "(?:(?:(?:\\+?234(?:\\h1)?|01)\\h*)?(?:\\(\\d{3}\\)|\\d{3})|\\d{4})(?:\\W*\\d{3})?\\W*\\d{4}(?!\\d)";
    public static final String EMAIL_FORMAT = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static Date getDate(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
    }

    public static String getYearMonth(String dateString) throws ParseException {
        return dateString.substring(0, 7);
    }

    public static boolean isMatchDateTime(String dateParam) {
        Pattern pattern = Pattern.compile(DATE_MATCHER_TIME);
        Matcher dateMatcher = pattern.matcher(dateParam);

        return dateMatcher.matches();
    }

    public static boolean isMatchDate(String dateParam) {
        Pattern pattern = Pattern.compile(DATE_MATCHER);
        Matcher dateMatcher = pattern.matcher(dateParam);

        return dateMatcher.matches();
    }

    public static boolean isMatchNigerianPhoneNum(String phoneNum) {

        Pattern pattern = Pattern.compile(NGN_PHONE_NUM);
        Matcher dateMatcher = pattern.matcher(phoneNum);

        return dateMatcher.matches();

    }

    public static boolean isEmailMatch(String yourEmail) {

        Pattern pattern = Pattern.compile(EMAIL_FORMAT);
        Matcher dateMatcher = pattern.matcher(yourEmail);

        return dateMatcher.matches();

    }


}
