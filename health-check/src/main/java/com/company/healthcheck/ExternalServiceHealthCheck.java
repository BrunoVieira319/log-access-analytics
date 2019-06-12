package com.company.healthcheck;

import com.codahale.metrics.health.HealthCheck;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class ExternalServiceHealthCheck extends HealthCheck {

    private Client client;
    private String url;

    public ExternalServiceHealthCheck(String url, Client client) {
        this.client = client;
        this.url = url;
    }

    @Override
    protected Result check() {
        Response response = client
                .target(url)
                .request()
                .get();

        if (response.getStatus() == 200) {
            return Result.healthy();
        }
        return Result.unhealthy(response.readEntity(String.class));
    }
}
