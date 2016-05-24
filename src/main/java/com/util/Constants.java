package com.util;

/**
 * Created by chao on 2016/4/14.
 */
public class Constants {
    // API_HTTP_SCHEMA
    public static String API_HTTP_SCHEMA = "https";
    // API_SERVER_HOST
    public static String API_SERVER_HOST = PropertiesUtil.prop().getProperty("API_SERVER_HOST");
    // APPKEY
    public static String APPKEY = PropertiesUtil.prop().getProperty("APPKEY");
    // APP_CLIENT_ID
    public static String APP_CLIENT_ID = PropertiesUtil.prop().getProperty("APP_CLIENT_ID");
    // APP_CLIENT_SECRET
    public static String APP_CLIENT_SECRET = PropertiesUtil.prop().getProperty("APP_CLIENT_SECRET");
    // DEFAULT_PASSWORD
    public static String DEFAULT_PASSWORD = "123456";

    public final static int DATANUMBER = 500;
}
