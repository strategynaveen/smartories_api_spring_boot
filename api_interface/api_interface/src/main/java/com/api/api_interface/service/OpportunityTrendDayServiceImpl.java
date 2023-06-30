package com.api.api_interface.service;

import com.api.api_interface.entity.DowntimeRawDataEntity;
import com.api.api_interface.entity.MachineValuesEntity;
import com.api.api_interface.entity.PartValuesEntity;
import com.api.api_interface.entity.ProductionRawDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Component(value = "OpportunityTrendPL")
public class OpportunityTrendDayServiceImpl implements  OpportunityTrendDayService{
    @Autowired
    private  UserServiceImpl userServiceImpl;
    private List totalDays;
    @Autowired
    private OpportunityTrendDayServiceImpl opportunityTrendDayServiceImpl;
    private List<MachineValuesEntity> machine;
    private  List<PartValuesEntity> part;
    private List<DowntimeRawDataEntity> downData;
    private List<ProductionRawDataEntity> production;

    @Autowired OEETrendDayWiseImpl oeeTrendDayWiseImpl;
    @Override
    public Map getOpportunityTrendValues() {
        String s = userServiceImpl.getFromDate();
        String e = userServiceImpl.getToDate();
        LocalDate start = LocalDate.parse(s);
        LocalDate end = LocalDate.parse(e);
        List<String> days = new ArrayList<>();
        while (!start.isAfter(end)) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
//            System.out.println(formatter.format(start));
            days.add(String.valueOf(start));
            start = start.plusDays(1);
        }
        opportunityTrendDayServiceImpl.totalDays =days;
        machine = userServiceImpl.machineValues;
        part = userServiceImpl.partValues;
        downData = userServiceImpl.downtimeFilter;
        production =userServiceImpl.productionFilter;
        List dayWiseDowntime =oeeTrendDayWiseImpl.dayWiseMachineValues;
        List totalDays = oeeTrendDayWiseImpl.totalDays;
        //int allTime = userServiceImpl.getAllTime();
        int allTime = 1440;


        List opportunityTrendDay = new ArrayList();
        AtomicReference<Float> GrantTotalPL = new AtomicReference<>((float) 0);

        dayWiseDowntime.forEach((n)->{
            AtomicReference<Float> MachinePLB= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLP= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLU= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLPer= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLQ= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLT= new AtomicReference<>((float) 0);
            List Machine=new ArrayList();

            AtomicReference<Float> MachinePLBDuration= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLPDuration= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLUDuration= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLPerDuration= new AtomicReference<>((float) 0);
            AtomicReference<Float> MachinePLQDuration= new AtomicReference<>((float) 0);
            float MachinePLTDuration=0;
            Map x = (Map) n;
            List a = (List) x.get("data");
            a.forEach((f)->{
                Map y = (Map) f;
                int PlannedDownTime = (int) y.get("Planned");
                int UnplannedDownTime = (int) y.get("Unplanned");
                int MachineOFFDownTime = (int) y.get("Machine_OFF");
                int All = (int) y.get("All");

                float NICTCorrectTPP = 0;
                float machineRate=1;
                float machineoffopportunity=0;
                float plannedopportunity=0;
                float unplannedopportunity=0;
                float qualityopportunity=0;
                String machineName="";
                for (PartValuesEntity partValues:part){
                    for (ProductionRawDataEntity product:production){
                        if (product.getMachine_id().equals(y.get("Machine_ID")) && partValues.getPart_id().equals(product.getPart_id()) && product.getCalendar_date().equals(x.get("date"))) {
                            for (MachineValuesEntity m:machine){
                                if (m.getMachine_id().equals(y.get("Machine_id"))){
                                    machineoffopportunity = (m.getMachine_offrate_per_hour()*MachineOFFDownTime)/60;
                                    plannedopportunity = (m.getRate_per_hour()*PlannedDownTime)/60;
                                    unplannedopportunity = (m.getRate_per_hour()*UnplannedDownTime)/60;
                                    MachinePLBDuration.set(MachinePLBDuration.get() +MachineOFFDownTime);
                                    MachinePLPDuration.set(MachinePLPDuration.get() +PlannedDownTime);
                                    MachinePLUDuration.set(MachinePLUDuration.get() +UnplannedDownTime);
                                }
                            }
                            int TCorrected = (int)product.getProduction()+(int)product.getCorrections();
                            qualityopportunity = qualityopportunity +(partValues.getPart_price()*product.getRejections());
                            float NICT = partValues.getNict()/60;
                            NICTCorrectTPP = ((TCorrected*NICT)+NICTCorrectTPP);
                            MachinePLQDuration.set(MachinePLQDuration.get() + (NICT * product.getRejections()));
                        }
                    }
                }
                for (MachineValuesEntity m: machine){
                    if (m.getMachine_id().equals(y.get("Machine_ID"))){
                        machineRate = m.getRate_per_hour();
                        machineName=m.getMachine_name();
                    }
                }
                float t=(allTime-All)-NICTCorrectTPP;
                float performance = (t)/(60*machineRate) ;
                MachinePLPerDuration.set(MachinePLPerDuration.get() + t);

                //Machine Wise P&L
                float MachinePLTotalTmp = (machineoffopportunity)+(plannedopportunity)+(unplannedopportunity)+(performance)+(qualityopportunity);

                GrantTotalPL.set(GrantTotalPL.get() + MachinePLTotalTmp);
                MachinePLB.set(MachinePLB.get() + machineoffopportunity);
                MachinePLP.set(MachinePLP.get() + plannedopportunity);
                MachinePLU.set(MachinePLU.get() + unplannedopportunity);
                MachinePLPer.set(MachinePLPer.get() + performance);
                MachinePLQ.set(MachinePLQ.get() + qualityopportunity);
                MachinePLT.set(MachinePLT.get() + MachinePLTotalTmp);
            });
            MachinePLTDuration= (MachinePLBDuration.get())+(MachinePLPDuration.get())+(MachinePLUDuration.get())+(MachinePLQDuration.get())+(MachinePLPerDuration.get());
            Map tmp =new HashMap();
            tmp.put("date",x.get("date"));
            tmp.put("business",MachinePLB);
            tmp.put("planned",MachinePLP);
            tmp.put("unplanned",MachinePLU);
            tmp.put("performance",MachinePLPer);
            tmp.put("quality",MachinePLQ);
            tmp.put("total",MachinePLT);
            tmp.put("businessDuration",MachinePLBDuration);
            tmp.put("plannedDuration",MachinePLPDuration);
            tmp.put("unplannedDuration",MachinePLUDuration);
            tmp.put("performanceDuration",MachinePLPerDuration);
            tmp.put("qualityDuration",MachinePLQDuration);
            tmp.put("totalDuration",MachinePLTDuration);
            opportunityTrendDay.add(tmp);
        });

        Map out = new HashMap();
        out.put("data",opportunityTrendDay);
        out.put("grand_total",GrantTotalPL);

        return out;
    }
}
