package com.api.api_interface.service;

import com.api.api_interface.entity.Quality_ReasonEntity;
import com.api.api_interface.repo.Quality_Reason_repo;

import java.util.List;

public class Quality_Data_thread implements Runnable{

    private final Quality_Reason_repo quality_repo;

    List<Quality_ReasonEntity> quality_reason_list;

    Quality_Data_thread(Quality_Reason_repo quality_repo){
        this.quality_repo = quality_repo;

    }
    @Override
    public void run()
    {
        quality_reason_list=quality_repo.findAll();
        //System.out.println(quality_reason_list);
        // downtimeRaw=downtimeRawDataRepository.findProductsByIds("ME1002");
    }

    public  List<Quality_ReasonEntity> getValue(){
        return quality_reason_list;
    }


}
