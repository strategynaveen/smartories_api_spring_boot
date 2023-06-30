package com.api.api_interface.service;

import com.api.api_interface.entity.MachineValuesEntity;
import com.api.api_interface.entity.PartValuesEntity;
import com.api.api_interface.entity.ProductionRawDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Component(value = "machineWisePL")
public class MachineWisePLOpportunityImpl implements MachineWisePLOpportunity{
    @Autowired
    private  UserServiceImpl userServiceImpl;
    @Override
    public Map getMachineWisePLValues() {
        List<ProductionRawDataEntity> production = userServiceImpl.productionFilter;
        List<MachineValuesEntity> machine = userServiceImpl.machineValues;
        List<PartValuesEntity> part = userServiceImpl.partValues;
        Map downData = userServiceImpl.machineWiseDowntime;
        int allTime = userServiceImpl.getAllTime();

        List MachinePLB = new ArrayList();
        List MachinePLP = new ArrayList();
        List MachinePLU = new ArrayList();
        List MachinePLPer = new ArrayList();
        List MachinePLQ = new ArrayList();
        List MachinePLT = new ArrayList();
        List Machine = new ArrayList();

        AtomicReference<Float> GrantTotalPL = new AtomicReference<>((float) 0);


        List plannedDuration= new ArrayList();
        List unplannedDuration= new ArrayList();
        List businessDuration= new ArrayList();
        List qualityDuration= new ArrayList();
        List performanceDuration= new ArrayList();
        List machinewiseDuration= new ArrayList();

        downData.forEach((k,v)->{
            Map val = (Map)v;
            int NICTCorrectTPP = 0;
            float machineRate=1;
            float machineoffopportunity=0;
            int plannedopportunity=0;
            int unplannedopportunity=0;
            int qualityopportunity=0;
            String machineName="";

            int businessDurationTmp=0;
            int plannedDurationTmp=0;
            int unplannedDurationTmp=0;
            int qualityDurationTmp=0;
            int performanceDurationTmp=0;

            int p=0;
            int u=0;
            int q=0;
            int b=0;
            int performancev=0;

            for (PartValuesEntity partDetails:part) {
                for (ProductionRawDataEntity product : production) {
                    if (k.equals(product.getMachine_id()) && partDetails.getPart_id().equals(product.getPart_id())) {
                        for (MachineValuesEntity m: machine){
                            if (m.getMachine_id().equals(k)){
                                float mo = (float) val.get("Machine_OFF");
                                float pd = (float) val.get("Planned");
                                float up = (float) val.get("Unplanned");
                                machineoffopportunity =(int) (m.getMachine_offrate_per_hour()*mo)/60;
                                plannedopportunity = (int) ((m.getRate_per_hour()*pd)/60);
                                unplannedopportunity = (int) ((m.getRate_per_hour()*up)/60);

                                businessDurationTmp= (int) (businessDurationTmp+mo);
                                plannedDurationTmp= (int) (plannedDurationTmp+pd);
                                unplannedDurationTmp= (int) (unplannedDurationTmp+up);
                            }
                        }
                        int TCorrected = (int)product.getProduction()+(int)product.getCorrections();
                        qualityopportunity = (int) (qualityopportunity +(partDetails.getPart_price()*product.getRejections()));
                        NICTCorrectTPP = ((TCorrected*(partDetails.getNict()/60))+NICTCorrectTPP);
                        qualityDurationTmp=qualityDurationTmp+((partDetails.getNict()/60)*product.getRejections());

                    }
                }
            }

            for (MachineValuesEntity mc:machine){
                if (mc.getMachine_id().equals(k)){
                    machineRate = mc.getRate_per_hour();
                    machineName=mc.getMachine_name();
                }
            }
            float pd = (float) val.get("Planned");
            float up = (float) val.get("Unplanned");
            float mo = (float) val.get("Machine_OFF");
            int t= (int) ((allTime-(pd+up+mo))-NICTCorrectTPP);
            int performanceT = (int) ((t)/(60*machineRate));
            performanceDurationTmp=t;

            float MachinePLTotalDuration = (float)(businessDurationTmp)+(float)(plannedDurationTmp)+(float)(unplannedDurationTmp)+(float)(performanceDurationTmp)+(float)(qualityDurationTmp);

            businessDuration.add(businessDurationTmp);
            plannedDuration.add(plannedDurationTmp);
            unplannedDuration.add(unplannedDurationTmp);
            qualityDuration.add(qualityDurationTmp);
            performanceDuration.add(performanceDurationTmp);
            machinewiseDuration.add(MachinePLTotalDuration);

            float MachinePLTotalTmp = machineoffopportunity+plannedopportunity+unplannedopportunity+performanceT+qualityopportunity;
            GrantTotalPL.set(GrantTotalPL.get() + MachinePLTotalTmp);

            MachinePLB.add(machineoffopportunity);
            MachinePLP.add(plannedopportunity);
            MachinePLU.add(unplannedopportunity);
            MachinePLPer.add(performanceT);
            MachinePLQ.add(qualityopportunity);
            MachinePLT.add((int)MachinePLTotalTmp);
            Machine.add(machineName);
        });

        for(int i=0;i<MachinePLT.size();i++) {
            int min_index = i;
            for (int j = i; j < MachinePLT.size(); j++) {
                if ((int)MachinePLT.get(j) > (int) MachinePLT.get(min_index)) {
                    min_index = j;
                }
            }

            int temp = (int)MachinePLT.get(i);
            MachinePLT.set(i, (int)MachinePLT.get(min_index));
            MachinePLT.set(min_index, temp);

            float temp1 = (float)MachinePLB.get(i);
            MachinePLB.set(i, (float)MachinePLB.get(min_index));
            MachinePLB.set(min_index, temp1);

            int temp2 = (int)MachinePLP.get(i);
            MachinePLP.set(i, (int)MachinePLP.get(min_index));
            MachinePLP.set(min_index, temp2);

            int temp3 = (int)MachinePLU.get(i);
            MachinePLU.set(i, (int)MachinePLU.get(min_index));
            MachinePLU.set(min_index, temp3);

            int temp4 = (int)MachinePLPer.get(i);
            MachinePLPer.set(i, (int)MachinePLPer.get(min_index));
            MachinePLPer.set(min_index, temp4);

            int temp5 = (int)MachinePLQ.get(i);
            MachinePLQ.set(i, (int)MachinePLQ.get(min_index));
            MachinePLQ.set(min_index, temp5);

            String temp6 = (String)Machine.get(i);
            Machine.set(i, (String)Machine.get(min_index));
            Machine.set(min_index, temp6);
        }

        Map out = new HashMap();
        out.put("business",MachinePLB);
        out.put("planned",MachinePLP);
        out.put("unplanned",MachinePLU);
        out.put("performance",MachinePLPer);
        out.put("quality",MachinePLQ);
        out.put("total",MachinePLT);
        out.put("machine",Machine);
        out.put("businessDuration",businessDuration);
        out.put("plannedDuration",plannedDuration);
        out.put("unplannedDuration",unplannedDuration);
        out.put("performanceDuration",performanceDuration);
        out.put("qualityDuration",qualityDuration);
        out.put("totalDuration",machinewiseDuration);
        out.put("grand_total",GrantTotalPL);

        return out;
    }
}
