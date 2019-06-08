package com.company.dto;

import java.util.List;

public class RegionDto {

    private int region;
    private List<LogDto> top3url;

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public List<LogDto> getTop3url() {
        return top3url;
    }

    public void setTop3url(List<LogDto> top3url) {
        this.top3url = top3url;
    }
}
