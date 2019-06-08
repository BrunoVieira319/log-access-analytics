package com.company.dto;

import java.util.List;

public class DateQueryDto {

    private String date;
    private List<LogDto> top3url;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<LogDto> getTop3url() {
        return top3url;
    }

    public void setTop3url(List<LogDto> top3url) {
        this.top3url = top3url;
    }
}
