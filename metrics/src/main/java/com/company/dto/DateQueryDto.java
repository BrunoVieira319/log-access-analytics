package com.company.dto;

import java.util.List;

public class DateQueryDto {

    private String date;
    private List<LogDto> topUrls;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<LogDto> getTopUrls() {
        return topUrls;
    }

    public void setTopUrls(List<LogDto> topUrls) {
        this.topUrls = topUrls;
    }
}
