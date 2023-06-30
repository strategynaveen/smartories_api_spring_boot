package com.api.api_interface.service;

import com.api.api_interface.entity.OverallTargetValuesEntity;
import com.api.api_interface.repo.OverallTargetValuesRepository;

import java.util.List;

public class OverallTargetValuesThread implements Runnable
{
    private final OverallTargetValuesRepository overallTargetValuesRepository;
    List<OverallTargetValuesEntity> users;
    OverallTargetValuesThread(OverallTargetValuesRepository overallTargetValuesRepository)
    {
        this.overallTargetValuesRepository = overallTargetValuesRepository;
    }

    @Override
    public void run()
    {
        users=overallTargetValuesRepository.findProducts();
    }
    public List<OverallTargetValuesEntity> getValue() {
        return users;
    }
}