package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Component(value = "oeeTrendDay")
public class OEETrendDayWiseImpl implements OEETrendDayWise{
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private  OEETrendDayWiseImpl ooeTrendDayWiseImpl;

    private  List<MachineValuesEntity> machine;
    private  List<PartValuesEntity> part;
    private  List<DowntimeRawDataEntity> downData;
    private  List<DowntimeReasonEntity> downReason;
    private  List<ProductionRawDataEntity> production;
    protected List totalDays;
    protected List dayWiseMachineValues;
    @Override
    public List getOEETrendValues() {
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
        ooeTrendDayWiseImpl.totalDays =days;
        machine = userServiceImpl.machineValues;
        part = userServiceImpl.partValues;
        downData = userServiceImpl.downtimeFilter;
        downReason = userServiceImpl.downtimeReason;
        production = userServiceImpl.productionFilter;
        List downtime= ooeTrendDayWiseImpl.oeeTrend();
        //int allTime = userServiceImpl.getAllTime();
        int allTime=1440;

        List final_List = new ArrayList();

        downtime.forEach((n)->{

            List MachineWiseData = new ArrayList();
            AtomicInteger pv= new AtomicInteger();
            AtomicInteger qv= new AtomicInteger();
            AtomicInteger av= new AtomicInteger();
            AtomicInteger ov= new AtomicInteger();

            Map x = (Map) n;
            List a = (List) x.get("data");
            a.forEach((f)->{
//                Map b = (Map) f;
//                System.out.println(b.get("Planned"));
//                b.forEach((k,v)->{
                    Map y = (Map) f;

                    int PlannedDownTime = (int) y.get("Planned");
                    int UnplannedDownTime = (int) y.get("Unplanned");
                    int MachineOFFDownTime = (int) y.get("Machine_OFF");
                    int All = (int) y.get("All");

                    int TotalCTPP_NICT = 0;
                    int TotalCTPP = 0 ;
                    int TotalReject = 0 ;
                    for (PartValuesEntity part: part){
                        int tmpCorrectedTPP_NICT=0;
                        int tmpCorrectedTPP=0;
                        int tmpReject=0;
                        for (ProductionRawDataEntity product:production){
                            if (product.getMachine_id().equals(y.get("Machine_ID")) && part.getPart_id().equals(product.getPart_id()) && product.getCalendar_date().equals(x.get("date"))){
                                int corrected_tpp = (int)product.getProduction()+(int)product.getCorrections();
                                int CorrectedTPP_NICT = (part.getNict()/60)* corrected_tpp;
                                // For Find Performance.....
                                tmpCorrectedTPP_NICT = tmpCorrectedTPP_NICT+CorrectedTPP_NICT;
                                //For Find Quality.......
                                tmpCorrectedTPP = tmpCorrectedTPP+corrected_tpp;
                                tmpReject = tmpReject+product.getRejections();
                            }
                        }
                        TotalCTPP_NICT =TotalCTPP_NICT+tmpCorrectedTPP_NICT;
                        TotalCTPP =TotalCTPP+tmpCorrectedTPP;
                        TotalReject = TotalReject+tmpReject;
                    }
                    float performance = ((TotalCTPP_NICT)/(allTime-(PlannedDownTime+UnplannedDownTime+MachineOFFDownTime)));

                    if (performance<0) {
                        performance = 0;
                    }
                float quality=0;
                    if (TotalCTPP>0){
                         quality = ((TotalCTPP - TotalReject)/(TotalCTPP));
                    }
                    else{
                         quality=0;
                    }

                    float availability = ((allTime-PlannedDownTime-UnplannedDownTime-MachineOFFDownTime)/(allTime-PlannedDownTime-MachineOFFDownTime));
                    // Machine Wise Availability TEEP.......
                    float availTEEP = ((allTime-PlannedDownTime-UnplannedDownTime-MachineOFFDownTime)/(allTime-PlannedDownTime));
                    // Machine Wise Availability OOE.....
                    float availOOE = ((allTime-PlannedDownTime-UnplannedDownTime-MachineOFFDownTime)/(allTime-MachineOFFDownTime));

                    float oee = (performance*quality*availability);
                    // Machine Wise TEEP.....
                    float teep = (performance*quality*availTEEP);
                    // Machine Wise OOE.....
                    float ooe = (performance*quality*availOOE);

                    Map<String, Float> temp = new HashMap<>();
                    temp.put("Performance",performance);
                    temp.put("Quality",quality);
                    temp.put("Availability_OEE",availability);
                    temp.put("Availability_TEEP",availTEEP);
                    temp.put("Availability_OOE",availOOE);
                    temp.put("OEE",oee);
                    temp.put("OOE",ooe);
                    temp.put("TEEP",teep);

                    MachineWiseData.add(temp);

                    pv.set((int)(pv.get() + performance));
                    qv.set((int)(qv.get() + quality));
                    av.set((int)(av.get() + availability));
                    ov.set((int)(ov.get() + ooe));

                });

                float ovv = pv.get()*qv.get()*av.get();
                Map<String, String> tmp = new HashMap<>();
                try {
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date firstParsedDate = dateFormat1.parse(String.valueOf(x.get("date")));
                }
                catch (Exception er){
                    er.printStackTrace();
                }

                tmp.put("date",String.valueOf(x.get("date")));
                tmp.put("availability",String.valueOf(av.get()));
                tmp.put("performance",String.valueOf(pv.get()));
                tmp.put("quality",String.valueOf(qv.get()));
                tmp.put("oee",String.valueOf(ovv));

            final_List.add(tmp);
//            });
        });
        return final_List;
    }
    public List oeeTrend(){
       List<MachineValuesEntity> m = ooeTrendDayWiseImpl.machine;
       List<PartValuesEntity> p = ooeTrendDayWiseImpl.part;
       List<DowntimeRawDataEntity> d = ooeTrendDayWiseImpl.downData;
       List totalDaysVal = ooeTrendDayWiseImpl.totalDays;
       List<DowntimeReasonEntity> reason = ooeTrendDayWiseImpl.downReason;

       List dayWise = new ArrayList();
       for (Object day : totalDaysVal){
           List machineWise = new ArrayList();
           for (MachineValuesEntity machine:m){
               float tmpMachineOFFDown = 0;
               float tmpPlannedDown = 0;
               float tmpUnplannedDown = 0;
               for (DowntimeRawDataEntity raw : d){
                   if (raw.getCalendar_date().equals(day)){
                       if (machine.getMachine_id().equals(raw.getMachine_id())){
                           for (DowntimeReasonEntity r:reason){
                               if (raw.getDowntime_reason_id().equals(r.getDowntime_reason_id())){
                                   if (r.getDowntime_category().equals("Unplanned")){
                                       tmpUnplannedDown = tmpUnplannedDown+ raw.getSplit_duration();
                                   }
                                   else if (r.getDowntime_category().equals("Planned") && r.getDowntime_reason().equals("Machine_OFF")) {
                                       tmpMachineOFFDown = tmpMachineOFFDown+ raw.getSplit_duration();
                                   }
                                   else {
                                       tmpPlannedDown = tmpPlannedDown+ raw.getSplit_duration();
                                   }

                               }
                           }

                       }
                   }
               }
               float total = tmpPlannedDown+tmpUnplannedDown+tmpMachineOFFDown;
               Map tmp = new HashMap();
               tmp.put("Machine_ID",machine.getMachine_id());
               tmp.put("Planned",(int)tmpPlannedDown);
               tmp.put("Unplanned",(int)tmpUnplannedDown);
               tmp.put("Machine_OFF",(int)tmpMachineOFFDown);
               tmp.put("All",(int) total);

               machineWise.add(tmp);

           }
           Map y = new HashMap();
           y.put("date",day);
           y.put("data",machineWise);
           dayWise.add(y);
       }
       ooeTrendDayWiseImpl.dayWiseMachineValues = dayWise;
        return dayWise;
    }
}
