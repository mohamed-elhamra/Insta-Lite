package com.instalite.api.commons.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final long EXPIRATION_TIME = 864_000_000; // 10 Days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_SECRET = "5367566B5970337336763MOHAMED3F4528ELHAMRAD6251655468576D5A71347437";
    public static final int PUBLIC_ID_LENGTH = 15;
    public static final String ADMIN_FIRST_NAME = "admin";
    public static final String ADMIN_LAST_NAME = "admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "admin123456";
    public static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("JPEG", "PNG", "JPG", "jpeg", "png", "jpg");
    public static final List<String> ALLOWED_VIDEO_EXTENSIONS = Arrays.asList("mp4", "MP4", "avi", "AVI", "mov", "MOV", "wmv", "WMV", "flv", "FLV", "mkv", "MKV", "3gp", "3GP", "webm", "WEBM", "ogg", "OGG", "m4v", "M4V");

}
