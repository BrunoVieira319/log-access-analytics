package com.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class LogIngestConfiguration extends Configuration {

    @JsonProperty
    private String defaultName = "LogIngest";

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }
}
