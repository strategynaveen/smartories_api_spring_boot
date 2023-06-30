package com.api.api_interface.service;

import com.api.api_interface.entity.DowntimeReasonEntity;
import com.api.api_interface.repo.DowntimeReasonRepository;

import java.util.List;

public class DowntimeReasonThread implements Runnable
{
    private final DowntimeReasonRepository downtimeReasonRepository;
    List<DowntimeReasonEntity> downtimeReasonData;
    DowntimeReasonThread(DowntimeReasonRepository downtimeReasonRepository)
    {
        this.downtimeReasonRepository = downtimeReasonRepository;
    }

    @Override
    public void run()
    {
        downtimeReasonData = downtimeReasonRepository.findAll();
        // downtimeRaw=downtimeRawDataRepository.findProductsByIds("ME1002");
    }
    public List<DowntimeReasonEntity> getValue() {
        return downtimeReasonData;
    }
}
