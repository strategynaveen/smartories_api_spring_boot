package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OverallValuesService {
    @Lazy
    void getOverallValues(String from_time,String to_time);

    List<DowntimeRawDataEntity> getDowntimeRawData();

    List<MachineValuesEntity> getMachineData();

    List<ProductionRawDataEntity> getProductionData();

    List<DowntimeReasonEntity> getDowntimeReasonData();

    List<PartValuesEntity> getPartData();

    List<InactiveMachineValuesEntity> inactiveMachineValues();

    List<OverallTargetValuesEntity> getOverallTargetValues();

    

}

