package com.api.api_interface.service;

import com.api.api_interface.entity.MachineValuesEntity;
import com.api.api_interface.repo.MachineValuesRepository;

import java.util.List;

public class MachineDataThread implements Runnable
{
    private final MachineValuesRepository machineValuesRepository;
    List<MachineValuesEntity> users;
    MachineDataThread(MachineValuesRepository machineValuesRepository)
    {
        this.machineValuesRepository = machineValuesRepository;
    }

    @Override
    public void run()
    {
        users=machineValuesRepository.findAll();
//        List<String> ids=new ArrayList<String>(10);
//        ids[0]="MC1001";
        // users=machineValuesRepository.findProductsByIds("MC1001","MC1002");
    }
    public List<MachineValuesEntity> getValue() {
        return users;
    }
}