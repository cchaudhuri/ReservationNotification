package com.beaconpoc.ReservationNotification.constant;

/**
 * Created by 472641 on 11/10/2016.
 */
public class EhiNotificationConstants {
    public static final String API_HEADER_KEY_CONTENT_TYPE = "Content-type";
    public static final String API_HEADER_VALUE_CONTENT_TYPE = "application/json";

    public static final String API_HEADER_KEY_APP_PLATFORM = "appPlatform";
    public static final String API_HEADER_VALUE_APP_PLATFORM = "android";

    public static final String API_HEADER_KEY_BOOKING_API = "Booking-API-Key";
    public static final String API_HEADER_VALUE_BOOKING_API = "1Q7VbsO93CPDMxTyzYp0ADnXG56gaRymAsp3vRiTcfw";

    public static final String API_HEADER_KEY_CALLING_ENTITY = "Calling_Identity";
    public static final String API_HEADER_VALUE_CALLING_ENTITY = "MA";

    public static final long HTTP_CACHE_SIZE_IN_BYTES = 1024 * 1024 * 10;
    public static final int HTTP_CONNECTION_TIMEOUT = 125;

    public static final String BASE_URL = "http://booking-layer.cloudhub.io/service/";
}
