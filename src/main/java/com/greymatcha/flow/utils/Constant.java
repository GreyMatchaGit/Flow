package com.greymatcha.flow.utils;

import java.time.LocalTime;
import java.time.ZoneId;

public class Constant {
    public static String EMPTY_STRING = "";
    public static final String REGEX_WHITESPACE_NONALNUM = "[\\s\\W]+";
    public static String USER_TIMEZONE = "Asia/Manila";
    public static final ZoneId USER_ZONEID = ZoneId.of(USER_TIMEZONE);
    public static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59, 999_999_999);
}
