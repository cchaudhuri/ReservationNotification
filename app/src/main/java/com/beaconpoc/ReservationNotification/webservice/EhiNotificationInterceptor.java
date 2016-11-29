package com.beaconpoc.ReservationNotification.webservice;



import com.beaconpoc.ReservationNotification.constant.EhiNotificationConstants;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 472641 on 11/10/2016.
 */
public class EhiNotificationInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        requestBuilder.addHeader(EhiNotificationConstants.API_HEADER_KEY_CONTENT_TYPE, EhiNotificationConstants.API_HEADER_VALUE_CONTENT_TYPE);
        requestBuilder.addHeader(EhiNotificationConstants.API_HEADER_KEY_APP_PLATFORM, EhiNotificationConstants.API_HEADER_VALUE_APP_PLATFORM);
        requestBuilder.addHeader(EhiNotificationConstants.API_HEADER_KEY_BOOKING_API, EhiNotificationConstants.API_HEADER_VALUE_BOOKING_API);
        requestBuilder.addHeader(EhiNotificationConstants.API_HEADER_KEY_CALLING_ENTITY, EhiNotificationConstants.API_HEADER_VALUE_CALLING_ENTITY);

        return chain.proceed(requestBuilder.build());
    }
}
