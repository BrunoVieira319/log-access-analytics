package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExternalServiceHealthCheck extends HealthCheck {

    private OkHttpClient client;
    private String url;

    public ExternalServiceHealthCheck(String url, OkHttpClient client) {
        this.client = client;
        this.url = url;
    }

    @Override
    protected Result check() throws Exception {
        Request request = new Request.Builder()
                .get()
                .url(this.url)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return Result.healthy();
        }
        return Result.unhealthy(response.message());
    }
}
