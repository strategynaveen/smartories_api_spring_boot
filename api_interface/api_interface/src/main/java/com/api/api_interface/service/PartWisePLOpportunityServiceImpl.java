package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import com.api.api_interface.repo.DowntimeRawDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component(value = "partWisePL")
public class PartWisePLOpportunityServiceImpl implements PartWisePLOpportunityService {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private PartWisePLOpportunityService partWisePLOpportunityService;
    @Override
    public List getPartWisePLValues() {
        List<ProductionRawDataEntity> production = userServiceImpl.productionFilter;
        List<MachineValuesEntity> machine = userServiceImpl.machineValues;
        List<PartValuesEntity> partDetails = userServiceImpl.partValues;
        List<DowntimeRawDataEntity> downtime = userServiceImpl.downtimeFilter;
        List<DowntimeReasonEntity> downReason = userServiceImpl.downtimeReason;
        int allTime = userServiceImpl.getAllTime();
       // List partWiseDowntime = userServiceImpl.partWiseDowntimeValues;

        List PartWisePL =new ArrayList();
        List Total = new ArrayList();
        for (PartValuesEntity part: partDetails){
            float material_cost=0;
            float production_cost=0;
            float profit_loss=0;
            float total=0;
            for (MachineValuesEntity m:machine) {
                float planned = 0;
                float unplanned = 0;
                float machineoff = 0;

                int TCorrected=0;
                int TRejection=0;

                for (ProductionRawDataEntity product : production) {
                    if (product.getPart_id().equals(part.getPart_id()) && product.getMachine_id().equals(m.getMachine_id())) {
                        TCorrected =TCorrected + (int)product.getProduction()+(int)product.getCorrections();
                        TRejection = TRejection+(int)product.getRejections();
                    }
                }
                for (DowntimeRawDataEntity down : downtime) {
                    if (down.getPart_id().equals(part.getPart_id()) && m.getMachine_id().equals(down.getMachine_id())) {
                        for (DowntimeReasonEntity reason : downReason) {
                            if (reason.getDowntime_reason_id().equals(down.getDowntime_reason_id())) {
                                if (reason.getDowntime_category().equals("Unplanned")) {
                                    unplanned = unplanned + down.getSplit_duration();
                                } else if (reason.getDowntime_category().equals("Planned") && reason.getDowntime_reason().equals("Machine_OFF")) {
                                    machineoff = machineoff + down.getSplit_duration();
                                } else {
                                    planned = planned + down.getSplit_duration();
                                }
                            }
                        }
                    }
                }
                float UMaterialCost  = part.getPart_price()*TCorrected*(part.getPart_weight()/1000);
                float PartInMachine = planned+unplanned;
                float UProductionCost  = (PartInMachine/60)*m.getRate_per_hour();
                float UTotalPartProducedCost = UMaterialCost+UProductionCost;
                float GoodRevenu = part.getPart_price()*(TCorrected-TRejection);
                float ProfitLoss = GoodRevenu-UTotalPartProducedCost;
                float tt=UMaterialCost + UProductionCost + ProfitLoss;

                material_cost = material_cost +UMaterialCost;
                production_cost = production_cost + UProductionCost;
                profit_loss = profit_loss + ProfitLoss;
                total =total+tt;

            }
            Map tmp =new HashMap();
            tmp.put("part_id",part.getPart_id());
            tmp.put("Part_Name",part.getPart_name());
            tmp.put("Material_Cost",(int)material_cost);
            tmp.put("Production_Cost",(int)production_cost);
            tmp.put("Profit_Loss",(int)profit_loss);
            tmp.put("Total",(int)total);
            PartWisePL.add(tmp);

            Total.add((int)total);
        }

        int len = PartWisePL.size();
        for (int i=0;i<len-1;i++){
            int max_index = i;
            for (int j = j=i+1; j < len; j++){
                if ((int)Total.get(j) > (int)Total.get(max_index)){
                    max_index = j;
                }
            }
            Map temp = (Map) PartWisePL.get(i);
            PartWisePL.set(i, PartWisePL.get(max_index));
            PartWisePL.set(max_index, temp);

            int temp1 = (int) Total.get(i);
            Total.set(i, Total.get(max_index));
            Total.set(max_index, temp1);


        }

        return PartWisePL;
    }
}