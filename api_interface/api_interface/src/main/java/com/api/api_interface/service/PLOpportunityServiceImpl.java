package com.api.api_interface.service;

import com.api.api_interface.entity.MachineValuesEntity;
import com.api.api_interface.entity.PartValuesEntity;
import com.api.api_interface.entity.ProductionRawDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Component("plOpportunity")
public class PLOpportunityServiceImpl implements  PLOpportunityService{
	@Autowired
    private  UserServiceImpl userServiceImpl;
    @Override
    public Map getPLOpprtunityValues() {
    	List<ProductionRawDataEntity> production = userServiceImpl.productionFilter;
        List<MachineValuesEntity> machine = userServiceImpl.machineValues;
        List<PartValuesEntity> part = userServiceImpl.partValues;
        Map downData = userServiceImpl.machineWiseDowntime;
        int allTime = userServiceImpl.getAllTime();

        AtomicInteger business = new AtomicInteger();
        AtomicInteger planned = new AtomicInteger();
        AtomicInteger unplanned = new AtomicInteger();
        AtomicInteger performance = new AtomicInteger();
        AtomicInteger quality = new AtomicInteger();

        AtomicInteger plannedDuration= new AtomicInteger();
        AtomicInteger UnplannedDuration= new AtomicInteger();
        AtomicInteger MachineOFFDuration= new AtomicInteger();
        AtomicInteger QualityDuration= new AtomicInteger();
        AtomicInteger PerformanceDuration= new AtomicInteger();

        for (MachineValuesEntity m:machine){
            downData.forEach((k,v)->{
                if (k.equals(m.getMachine_id())){
                    Map val = (Map) v;
                    float mo = (float)val.get("Machine_OFF");
                    float pd = (float)val.get("Planned");
                    float up = (float)val.get("Unplanned");
                    int machineoffopportunity = (int)((m.getMachine_offrate_per_hour()*(int)mo))/60;
                    int plannedopportunity = (int) ((m.getRate_per_hour()*(int)pd)/60);
                    int unplannedopportunity = (int) ((m.getRate_per_hour()*(int)up)/60);

                    business.set(business.get() + machineoffopportunity);
                    planned.set(planned.get() + plannedopportunity);
                    unplanned.set(unplanned.get() + unplannedopportunity);

//                    //For duration find-out
                    plannedDuration.set(plannedDuration.get() + (int) pd);
                    UnplannedDuration.set(UnplannedDuration.get() + (int) up);
                    MachineOFFDuration.set(MachineOFFDuration.get() + (int) mo);
                }
            });
        }

        downData.forEach((k,v)->{
            Map val = (Map)v;
            int NICTCorrectTPP = 0;
            for (PartValuesEntity p:part) {
                for (ProductionRawDataEntity product : production) {
                    if (k.equals(product.getMachine_id()) && p.getPart_id().equals(product.getPart_id())) {
                        int TCorrected = (int) product.getProduction() + (int) (product.getCorrections());
                        quality.set(quality.get() + (int) (p.getPart_price() * product.getRejections()));
                        NICTCorrectTPP = ((TCorrected * (p.getNict() / 60)) + NICTCorrectTPP);
                        QualityDuration.set(QualityDuration.get() + ((p.getNict() / 60) * product.getRejections()));
                    }
                }
            }
                float machineRate=0;
                for(MachineValuesEntity m:machine){
                    if (m.getMachine_id().equals(k)){
                        machineRate = m.getRate_per_hour();
                    }
                }
                float pd = (float) val.get("Planned");
                float up = (float) val.get("Unplanned");
                float mo = (float) val.get("Machine_OFF");
                int t=(allTime-((int)pd+(int)up+(int)mo));
                int tmpOp = (t-NICTCorrectTPP)/(60*(int)machineRate);
                performance.set(performance.get() + tmpOp);
                PerformanceDuration.set(t - NICTCorrectTPP);
        });

            int operation = planned.get() + unplanned.get() + performance.get() + quality.get();
            Map out = new HashMap();
        out.put("business",business);
        out.put("planned",planned);
        out.put("unplanned",unplanned);
        out.put("performance",performance);
        out.put("quality",quality);
        out.put("operation",operation);
        out.put("all",operation+ business.get());
        out.put("businessDuration",MachineOFFDuration);
        out.put("plannedDuration",plannedDuration);
        out.put("unplannedDuration",UnplannedDuration);
        out.put("performanceDuration",PerformanceDuration);
        out.put("qualityDuration",QualityDuration);

        return out;
    }
}
