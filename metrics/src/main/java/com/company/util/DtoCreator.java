package com.company.util;

import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.RegionDto;

import java.util.List;

public class DtoCreator {

    public static RegionDto regionDto(int region, List<LogDto> url) {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegion(region);
        regionDto.setTop3url(url);

        return regionDto;
    }

    public static DateQueryDto dateQueryDto(String date, List<LogDto> urls) {
        DateQueryDto dateDto = new DateQueryDto();
        dateDto.setDate(date);
        dateDto.setTop3url(urls);

        return dateDto;
    }

    public static LogDto logDto(Integer region, String url, Integer count) {
        LogDto logDto = new LogDto();
        logDto.setRegion(region);
        logDto.setUrl(url);
        logDto.setCount(count);

        return logDto;
    }
}
