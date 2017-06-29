package com.scientiamobile.imageenginedemo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Intercept the current HTTP request in order to override the User-Agent header with the app custom version
 * Created by Andrea Castello on 29/06/2017.
 */

final class UserAgentInterceptor implements Interceptor {

    final static String HEADER_USER_AGENT = "User-Agent";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .header(HEADER_USER_AGENT, createCustomUserAgent(originalRequest))
                .build();
        return chain.proceed(requestWithUserAgent);
    }

    private String createCustomUserAgent(Request originalRequest) {
        // App name can be also retrieved programmatically, but no need to do it for this sample needs
        String ua = "ImageEngineDemo";
        String baseUa = System.getProperty("http.agent");
        if(baseUa!=null){
            ua = ua + " " + baseUa;
        }
        return ua;
    }
}
