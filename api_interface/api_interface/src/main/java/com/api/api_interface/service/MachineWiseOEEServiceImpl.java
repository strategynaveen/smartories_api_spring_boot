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
@Component("machineWiseOEE")
public class MachineWiseOEEServiceImpl implements MachineWiseOEEService
{
    @Autowired
    @Qualifier(value = "first")
    private OverallValuesService overallValuesService;

    public Map getMachineWiseOverallValues() {
        return machineWiseOverallValues;
    }

    public void setMachineWiseOverallValues(Map machineWiseOverallValues) {
        this.machineWiseOverallValues = machineWiseOverallValues;
    }

    private  Map machineWiseOverallValues;
	@Autowired
	private MachineWiseOEEServiceImpl machineWiseOEEServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;

    private Map finalValueMap;
    private Map machineWiseOrdering;

	public Map getMachineWiseOEEValues(){
        machineWiseOverallValues = machineWiseOEEServiceImpl.machineWiseOverall();
           machineWiseOEEServiceImpl.machineWiseOrdering = machineWiseOEEServiceImpl.machineWiseOrderingForGraph();
        // machineWiseOEEServiceImpl.sortingMachineWise = machineWiseOEEServiceImpl.sortingMachineWiseOEE();
        return machineWiseOEEServiceImpl.machineWiseOrdering;
   	}

   	@Override
    public Map machineWiseOverall() {
        Map downtime = userServiceImpl.getMachineWiseDowntime();
        List<PartValuesEntity> part = userServiceImpl.getPartValues();
        List<ProductionRawDataEntity> productionData = userServiceImpl.getProductionFilter();

       Map<String, Map> machineWiseOverall = new HashMap<>();
       downtime.forEach((k,v) -> {
           Map downtime1 = (Map) v;
           int planned= Math.round((Float) downtime1.get("Planned"));
           int unplanned=Math.round((Float) downtime1.get("Unplanned"));
           int machineoff=Math.round((Float) downtime1.get("Machine_OFF"));

               int allTime = userServiceImpl.getAllTime();
               float totalCTPP_NICT = 0;
               int totalCTPP = 0;
               int totalReject = 0;
               for (PartValuesEntity p : part) {
                   float tmpCorrectedTPP_NICT = 0;
                   int tmpCorrectedTPP = 0;
                   int tmpReject = 0;
                   for (ProductionRawDataEntity product : productionData) {
                       for (PartValuesEntity partDetails : part) {
                           if (k.equals(product.getMachine_id()) && partDetails.getPart_id().equals(product.getPart_id())) {

                               int correctedTPP = (product.getProduction()) + (product.getCorrections());
                               //For Performance.......
                               tmpCorrectedTPP_NICT = tmpCorrectedTPP_NICT + (correctedTPP * (float) ((partDetails.getNict()) / 60));
                               //For Quality........
                               tmpCorrectedTPP = tmpCorrectedTPP + correctedTPP;
                               tmpReject = tmpReject + product.getRejections();
                               // System.out.println(product.getRejections());
                           }
                       }
                   }
                   totalCTPP_NICT = totalCTPP_NICT + tmpCorrectedTPP_NICT;
                   totalCTPP = totalCTPP + tmpCorrectedTPP;
                   totalReject = totalReject + tmpReject;
               }

               // System.out.println(totalReject);
               float performance;
               if (totalCTPP_NICT <1 ) {
                   performance =0;
                }
                else{
                  performance = ((totalCTPP_NICT)/(allTime-(planned)-(unplanned)-(machineoff)));
                }
               float quality;
               if (totalCTPP <1){
                   quality =0;
               }else {
                   quality = ((totalCTPP - totalReject) / (totalCTPP));
               }

                float availability = ((allTime-planned-unplanned-machineoff)/(allTime-planned-machineoff));
                float availTEEP = ((allTime-planned-unplanned-machineoff)/(allTime-planned));
                float availOOE = ((allTime-planned-unplanned-machineoff)/(allTime-machineoff));
                float oee = (performance*quality*availability);
                float teep = (performance*quality*availTEEP);
                float ooe = (performance*quality*availOOE);

               Map<String, Float> machineWiseOverallTemp = new HashMap<>();
               machineWiseOverallTemp.put("Performance",performance);
               machineWiseOverallTemp.put("Quality",quality);
               machineWiseOverallTemp.put("Availability_OEE",availability);
               machineWiseOverallTemp.put("Availability_TEEP",availTEEP);
               machineWiseOverallTemp.put("Availability_OOE",availOOE);
               machineWiseOverallTemp.put("OEE",oee);
               machineWiseOverallTemp.put("OOE",ooe);
               machineWiseOverallTemp.put("TEEP",teep);

               machineWiseOverall.put(k.toString(),machineWiseOverallTemp);
       });
        return machineWiseOverall;
    }

    @Override
    public Map machineWiseOrderingForGraph(){
        Map machineWise = machineWiseOEEServiceImpl.machineWiseOverallValues;
        List<MachineValuesEntity> machineName = userServiceImpl.getMachineData();
        Map finalValue = new HashMap();
       List<Float> performance = new ArrayList<Float>();
        List<Float> quality = new ArrayList<Float>();
        List<Float> availability = new ArrayList<Float>();
        List<Float> oee = new ArrayList<Float>();
        List<String> machine = new ArrayList<String>();
        machineWise.forEach((k,v) ->{
            Map machineVal = (Map) v;

            quality.add((float) machineVal.get("Quality"));
            performance.add((float) machineVal.get("Performance"));
            oee.add((float) machineVal.get("OEE"));
            availability.add((float) machineVal.get("Availability_OEE"));
            for (MachineValuesEntity name : machineName) {
                if (name.getMachine_id().equals(k)) {
                    machine.add(name.getMachine_name());
                }
            }

        });


        for(int i=0;i<oee.size();i++){
            int min_index= i;
            for(int j=i; j<oee.size();j++){
                if(oee.get(j) < oee.get(min_index)){
                    min_index = j;
                } 
            }
            //for oee
            float temp = oee.get(min_index);
            oee.set(i, oee.get(min_index));
            oee.set(min_index, temp);

            //for performance
            float temp1 = performance.get(min_index);
            performance.set(i, performance.get(min_index));
            performance.set(min_index, temp1);

            //for quality
            float temp3 = quality.get(min_index);
            quality.set(i, quality.get(min_index));
            quality.set(min_index, temp3);

            //for availability
            float temp4 = availability.get(min_index);
            availability.set(i, availability.get(min_index));
            availability.set(min_index, temp4);

            //for Mchine Name
            String temp5 = machine.get(min_index);
            machine.set(i, machine.get(min_index));
            machine.set(min_index, temp5);
        }
  

        finalValue.put("Performance",performance);
        finalValue.put("Quality",quality);
        finalValue.put("Availability",availability);
        finalValue.put("OOE",oee);
        finalValue.put("MachineName",machine);

        machineWiseOEEServiceImpl.finalValueMap = finalValue;

        return finalValue;
    }
}