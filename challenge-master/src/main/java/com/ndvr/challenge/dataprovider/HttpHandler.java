package com.ndvr.challenge.dataprovider;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class HttpHandler {

    private static final int MAX_CONNECTIONS = 100;

    private final HttpClient httpClient;

    @Getter
    private final BasicCookieStore cookieStore;

    public HttpHandler(@Value("${com.ndvr.marketdata.yahoo.timeout:5}") int timeoutSeconds) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeoutSeconds * 1000)
                .setConnectionRequestTimeout(timeoutSeconds * 1000)
                .setSocketTimeout(timeoutSeconds * 1000)
                .setCookieSpec(CookieSpecs.STANDARD)
                .build();

        this.cookieStore = new BasicCookieStore();
        this.httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(MAX_CONNECTIONS)
                .setMaxConnPerRoute(MAX_CONNECTIONS / 2)
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(config).build();
    }

    public HttpResponse fetchResponse(HttpUriRequest request) {
        try {
            log.debug("{}: {}", request.getMethod(), request.getURI());
            HttpResponse response = httpClient.execute(request);
            log.debug("{}({}): {}", request.getMethod(), response.getStatusLine().getStatusCode(), request.getURI());
            return response;
        } catch (IOException e) {
            throw new RuntimeException("Failed " + request.getMethod() + ": " + request.getURI(), e);
        }
    }

    public static String urlEncodeString(String string) {
        return URLEncoder.encode(string, StandardCharsets.UTF_8);
    }

}
