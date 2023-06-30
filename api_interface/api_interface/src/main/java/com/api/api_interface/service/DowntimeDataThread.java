package com.api.api_interface.service;

import com.api.api_interface.entity.DowntimeRawDataEntity;
import com.api.api_interface.repo.DowntimeRawDataRepository;

import java.util.List;

public class DowntimeDataThread implements Runnable
{
    private final DowntimeRawDataRepository downtimeRawDataRepository;
    private String from_date;
    private String to_date;
    List<DowntimeRawDataEntity> downtimeRaw;
    DowntimeDataThread(DowntimeRawDataRepository downtimeRawDataRepository,String from_date,String to_date)
    {
        this.downtimeRawDataRepository = downtimeRawDataRepository;
        this.from_date=from_date;
        this.to_date=to_date;
    }

    @Override
    public void run()
    {
         // downtimeRaw=downtimeRawDataRepository.findAll();
        downtimeRaw=downtimeRawDataRepository.findProductsByIds(this.from_date,this.to_date);
    }
    public List<DowntimeRawDataEntity> getValue() {
        return downtimeRaw;
    }
}

