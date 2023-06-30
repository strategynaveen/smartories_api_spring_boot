package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import com.api.api_interface.repo.*;
import com.api.api_interface.repo.MachineValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Component("overallOEE")
public class OverallMonitoringValuesServiceImpl implements OverallMonitoringValuesService{
    @Autowired
    private OverallMonitoringValuesServiceImpl overallMonitoringValuesServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;
    // private Map overallGraphValues;

    @Autowired
    private MachineWiseOEEServiceImpl machineWiseOEEServiceImpl;

    public Map overallAverage(){
            Map machineWise = machineWiseOEEServiceImpl.getMachineWiseOverallValues();
            List<OverallTargetValuesEntity> target = userServiceImpl.getOverallTargetValues();
            Map<String, Float> overallPercentage = new HashMap<>();
            machineWise.forEach((k,v) ->{
                Map machineVal = (Map) v;
                userServiceImpl.setOee((float) machineVal.get("OEE"));
                userServiceImpl.setOoe((float) machineVal.get("OOE"));
                userServiceImpl.setTeep((float) machineVal.get("TEEP"));
            });
        userServiceImpl.setOeePercentage((userServiceImpl.getOee()*100)/machineWise.size());
        userServiceImpl.setOoePercentage((userServiceImpl.getOoe()*100)/machineWise.size());
        userServiceImpl.setTeepPercentage((userServiceImpl.getTeep()*100)/machineWise.size());

            overallPercentage.put("OEE",userServiceImpl.getOeePercentage());
            overallPercentage.put("OOE",userServiceImpl.getOoePercentage());
            overallPercentage.put("TEEP", userServiceImpl.getTeepPercentage());

             for (OverallTargetValuesEntity p : target) {
                 overallPercentage.put("Target_OEE", p.getOverall_oee());
                 overallPercentage.put("Target_OOE", p.getOverall_ooe());
                 overallPercentage.put("Target_TEEP", p.getOverall_teep());
             }
            return overallPercentage;
    }

}

