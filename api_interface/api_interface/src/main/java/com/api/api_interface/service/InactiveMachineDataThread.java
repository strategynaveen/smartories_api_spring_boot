package com.api.api_interface.service;

import com.api.api_interface.entity.InactiveMachineValuesEntity;
import com.api.api_interface.repo.InactiveMachineValuesRepository;

import java.util.List;

public class InactiveMachineDataThread implements Runnable
{
    private final InactiveMachineValuesRepository inactiveMachineValuesRepository;
    List<InactiveMachineValuesEntity> inactive;
    InactiveMachineDataThread(InactiveMachineValuesRepository inactiveMachineValuesRepository)
    {
        this.inactiveMachineValuesRepository = inactiveMachineValuesRepository;
    }

    @Override
    public void run()
    {
        inactive=inactiveMachineValuesRepository.findProductsByIds();
    }
    public List<InactiveMachineValuesEntity> getValue() {
        return inactive;
    }
}