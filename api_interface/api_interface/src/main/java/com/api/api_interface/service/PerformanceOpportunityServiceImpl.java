package com.api.api_interface.service;

import com.api.api_interface.entity.DowntimeRawDataEntity;
import com.api.api_interface.entity.MachineValuesEntity;
import com.api.api_interface.entity.PartValuesEntity;
import com.api.api_interface.entity.ProductionRawDataEntity;
import com.api.api_interface.repo.MachineValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component("performanceOpportunity")
public class PerformanceOpportunityServiceImpl implements PerformanceOpportunityService{
    @Autowired
    private  UserServiceImpl userServiceImpl;


    @Autowired
    private  PerformanceOpportunityServiceImpl performance_service;

//    this global varaiable get the value for opportunity drilldown graph
    protected  int performance_grand_total;

    @Override
    public Map getPerformanceOpportunityValues() {
//        List<DowntimeRawDataEntity> downData = userServiceImpl.downtimeFilter;
        List<ProductionRawDataEntity> production = userServiceImpl.productionFilter;
        List<MachineValuesEntity> machine = userServiceImpl.machineValues;
        List<PartValuesEntity> part = userServiceImpl.partValues;
        int allTime = userServiceImpl.getAllTime();
        Map downData = userServiceImpl.machineWiseDowntime;

        List varDataMachine = new ArrayList();
        downData.forEach((k,v) -> {
            Map d = (Map) v;
            List varData = new ArrayList();
        	for (PartValuesEntity p : part) {
                float corrected_tppNICT=0;
                float machineOFFRate=0;
        		for (ProductionRawDataEntity product : production) {
        			if (k.equals(product.getMachine_id()) && product.getPart_id().equals(p.getPart_id())){
                        corrected_tppNICT = corrected_tppNICT+(p.getNict()/60)+((int)product.getProduction()+((int)product.getCorrections()));
                    }
        		}

                for (MachineValuesEntity m : machine){
                    if (m.getMachine_id().equals(k)){
                        machineOFFRate=m.getRate_per_hour();
                    }
                }
                float pd = (float) d.get("Planned");
                float up = (float) d.get("Unplanned");
                float mo = (float) d.get("Machine_OFF");
                int downtime=allTime-((int)pd+(int)up+(int)mo);
                float opportunity=0;
                if (corrected_tppNICT > 0) {
                     opportunity = (downtime-(int)corrected_tppNICT)/(60*machineOFFRate);
                }

//                //Part running time calculation will different
                int partRunningTime=((int)pd+(int)up-(int)pd-(int)up);
                int partRunningDurationAtIdeal= (int)corrected_tppNICT;
                int speedLoss= partRunningTime-partRunningDurationAtIdeal;

                Map opp = new HashMap();
                opp.put("Opportunity", opportunity);
                opp.put("SpeedLoss",speedLoss);


                Map temp =new HashMap();
                temp.put("part_id",p.getPart_id());
                temp.put("data",corrected_tppNICT);
                temp.put("OppCost",opportunity);
                temp.put("speedLoss",speedLoss);
                // varData.add(temp);
                varData.add(opp);

            }

            Map tmp =new HashMap();
            tmp.put("machine_id",k);
            tmp.put("machineData",varData);
            varDataMachine.add(tmp);
        });

        int length = varDataMachine.size();
        int l=part.size();
        int GrandTotal=0;
        List partTotal = new ArrayList();
        List speedTotal = new ArrayList();
        for (int i=0; i < l ; i++) { 
            float tmpPartTotal=0;
            int tmpSpeedLoss=0;
            for (int j=0; j <length ; j++) {
                Map x = (Map) varDataMachine.get(j);
                List y = (List) x.get("machineData");
                Map z = (Map) y.get(i);
                float a = (float) z.get("Opportunity");
                int b = (int) z.get("SpeedLoss");
                tmpPartTotal=tmpPartTotal+a;
                tmpSpeedLoss=tmpSpeedLoss+b;
            }   
            GrandTotal=GrandTotal+(int)tmpPartTotal;
          partTotal.add((int)tmpPartTotal);
          speedTotal.add(tmpSpeedLoss);
        }

        List partDetails = new ArrayList();
        for (PartValuesEntity p:part){
            partDetails.add(p.getPart_name());
        }

        int len = partTotal.size();
        for (int i = 0; i<len; i++){
            int min_index = i;
            for (int j = j=i+1; j < len; j++){
                if ((int)partTotal.get(j) > (int)partTotal.get(min_index)){
                    min_index = j;
                }
            }
            int temp = (int)partTotal.get(i);
            partTotal.set(i, partTotal.get(min_index));
            partTotal.set(min_index, temp);

            String temp1 = (String) partDetails.get(i);
            partDetails.set(i, partDetails.get(min_index));
            partDetails.set(min_index, temp1);

            int temp2 = (int)speedTotal.get(i);
            speedTotal.set(i, speedTotal.get(min_index));
            speedTotal.set(min_index, temp2);

            int k =varDataMachine.size();
            for (int m=0; m < k; m++) {
                Map x = (Map) varDataMachine.get(m);
                List y = (List) x.get("machineData");
                Map z = (Map) y.get(i);

                y.set(i,y.get(min_index));
                y.set(min_index,z);
            }

        }

        Map res = new HashMap();
        res.put("dataPart",varDataMachine);
        res.put("Part",partDetails);
        res.put("Total",partTotal);
        res.put("SpeedLossTotal",speedTotal);
        res.put("GrandTotal",GrandTotal);

//        this global varaiables get for another graph in opportunity drill down
        performance_service.performance_grand_total = GrandTotal;
        System.out.println("\n\ngrand total performance opportunity:\t"+GrandTotal);
        return res;
    }
}
