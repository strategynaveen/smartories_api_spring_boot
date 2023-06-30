package com.api.api_interface.service;

import com.api.api_interface.entity.PartValuesEntity;
import com.api.api_interface.repo.PartValuesRepository;

import java.util.List;

public class PartDataThread implements Runnable
{
    private final PartValuesRepository partValuesRepository;
    List<PartValuesEntity> partDataRaw;
    PartDataThread(PartValuesRepository partValuesRepository)
    {
        this.partValuesRepository = partValuesRepository;
    }

    @Override
    public void run()
    {
        partDataRaw=partValuesRepository.findAll();
        // downtimeRaw=downtimeRawDataRepository.findProductsByIds("ME1002");
    }
    public List<PartValuesEntity> getValue() {
        return partDataRaw;
    }
}

