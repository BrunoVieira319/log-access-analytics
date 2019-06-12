package com.company.dto;

import java.util.List;

public class RegionDto {

    private int region;
    private List<LogDto> topUrls;

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public List<LogDto> getTopUrls() {
        return topUrls;
    }

    public void setTopUrls(List<LogDto> topUrls) {
        this.topUrls = topUrls;
    }
}
