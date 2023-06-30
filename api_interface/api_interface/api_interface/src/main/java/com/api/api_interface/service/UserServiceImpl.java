package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import com.api.api_interface.repo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements OverallValuesService
{
    // @Autowired
    // private  ThreadPoolExecutorUtil threadPoolExecutorUtil;

    @Autowired
    private MachineValuesRepository machineValuesRepository;
    @Autowired
    private DowntimeRawDataRepository downtimeRawDataRepository;
    @Autowired
    private ProductionRawDataRepository productionRawDataRepository;
    @Autowired
    private PartValuesRepository partValuesRepository;
    @Autowired
    private DowntimeReasonRepository downtimeReasonRepository;
    @Autowired
    private InactiveMachineValuesRepository inactiveMachineValuesRepository;
    @Autowired
    private OverallTargetValuesRepository overallTargetValuesRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    //Encapsulation Variable
    private List<MachineValuesEntity> machineValues;
    private List<DowntimeRawDataEntity> downtimeRaw;
    private List<ProductionRawDataEntity> productionRaw;
    private List<DowntimeRawDataEntity> downtimeFilter;
    private List<ProductionRawDataEntity> productionFilter;
    private List<PartValuesEntity> partValues;
    private List<DowntimeReasonEntity> downtimeReason;
    private HashMap machineWiseOrdering;
    private  Map machineWiseOverallValues;
    private  Map machineWiseDowntime;
    private Map overallGraphValues;
    private List<InactiveMachineValuesEntity> inactiveMachineData;
    private List<OverallTargetValuesEntity> overallTargetValues;

    //Filter variables
    private String fromDate="2022-08-10";
    private String toDate="2022-08-15";
    private String fromTime="00:00:00";
    private String toTime="00:00:00";

    private  float oee;
    private  float ooe;
    private float teep;

    private float oeePercentage;

    private float ooePercentage;
    private  float teepPercentage;

    private int allTime;

    @Override
    public Map getOverallValues(){

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            Date firstParsedDate = dateFormat1.parse(userServiceImpl.fromDate.concat(" ").concat(userServiceImpl.fromTime));
            Date secondParsedDate = dateFormat1.parse(userServiceImpl.toDate.concat(" ").concat(userServiceImpl.toTime));
            long diff = secondParsedDate.getTime() - firstParsedDate.getTime();
            userServiceImpl.allTime =(int)(diff/(1000*60));
        }catch (Exception e){
            System.out.println("Difference Error");
        }
        // Function for get the Machine Datas.....
        machineValues = userServiceImpl.getMachineData();

        //Function for get Part Data.........
        partValues = userServiceImpl.getPartData();

        // Function for get Downtime Raw Data........
        downtimeRaw = userServiceImpl.getDowntimeRawData();
        // Function for get Production Raw Data.......
        productionRaw = userServiceImpl.getProductionData();
        //Function for Downtime Reason........
        downtimeReason = userServiceImpl.getDowntimeReasonData();


        inactiveMachineData = userServiceImpl.inactiveMachineValues();
        //Function for Filter for downtime reason mapping data.......
        downtimeFilter = userServiceImpl.filterRawDataDowntime();
        // Function for Filter for Production Info data........
        productionFilter = userServiceImpl.filterRawDataProduction();
        //Machine wise respective of Part wise Order. 
        // This is not apply anywhere.......
        machineWiseOrdering = userServiceImpl.machineWiseDataOrdering();

        //Function for Machine-wise OEE,OOE,TEEP.......
        machineWiseDowntime = userServiceImpl.machineWiseDowntime();
        // List<DowntimeRawDataEntity> machineWiseDowntime = userServiceImpl.machineDowntime();

        machineWiseOverallValues = userServiceImpl.machineWiseOverall();

        //Function for get Overall Target values.......
        overallTargetValues=userServiceImpl.getOverallTargetValues();


        //Function for Overall OEE,OOE,TEEP......
        overallGraphValues = userServiceImpl.overallAverage();

        

        return overallGraphValues;
    }

    public Map overallAverage(){
        Map machineWise = userServiceImpl.machineWiseOverallValues;
        List<OverallTargetValuesEntity> target = userServiceImpl.overallTargetValues;
        Map<String, Float> overallPercentage = new HashMap<>();
        machineWise.forEach((k,v) ->{
            Map machineVal = (Map) v;
                userServiceImpl.setOee((float) machineVal.get("OEE"));
                userServiceImpl.setOoe((float) machineVal.get("OOE"));
                userServiceImpl.setTeep((float) machineVal.get("TEEP"));
        });
        userServiceImpl.setOeePercentage((userServiceImpl.oee*100)/machineWise.size());
        userServiceImpl.setOoePercentage((userServiceImpl.ooe*100)/machineWise.size());
        userServiceImpl.setTeepPercentage((userServiceImpl.teep*100)/machineWise.size());

        overallPercentage.put("OEE",userServiceImpl.getOeePercentage());
        overallPercentage.put("OOE",userServiceImpl.getOoePercentage());
        overallPercentage.put("TEEP", userServiceImpl.getTeepPercentage());

         for (OverallTargetValuesEntity p : target) {
             System.out.println(p.getOverall_teep());
             overallPercentage.put("Target_OEE", p.getOverall_oee());
             overallPercentage.put("Target_OOE", p.getOverall_ooe());
             overallPercentage.put("Target_TEEP", p.getOverall_teep());
         }
         System.out.println(overallPercentage);
        return overallPercentage;
    }

    public Map machineWiseOverall() {
        Map downtime = userServiceImpl.machineWiseDowntime;
        List<PartValuesEntity> part = userServiceImpl.partValues;
        List<ProductionRawDataEntity> productionData = userServiceImpl.productionFilter;
       //  ObjectMapper obj = new ObjectMapper();
       // try{
       //     System.out.println(obj.writerWithDefaultPrettyPrinter().writeValueAsString(downtime));
       // }
       // catch (Exception e){
       //     e.printStackTrace();
       // }
       Map<String, Map> machineWiseOverall = new HashMap<>();
        downtime.forEach((k,v) -> {
            Map downtime1 = (Map) v;
            int planned= Math.round((Float) downtime1.get("Planned"));
            int unplanned=Math.round((Float) downtime1.get("Unplanned"));
            int machineoff=Math.round((Float) downtime1.get("Machine_OFF"));

            // System.out.println(planned);
            // System.out.println(unplanned);
            // System.out.println(machineoff);
            // System.out.println("\n");

                int allTime = userServiceImpl.allTime;
                // System.out.println(allTime);

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
                                // float nict = (float)((partDetails.getNict()) / 60);
                                // System.out.println(nict);

                                int correctedTPP = (product.getProduction()) + (product.getCorrections());
                                //For Performance.......
                                tmpCorrectedTPP_NICT = tmpCorrectedTPP_NICT + (correctedTPP * (float)((partDetails.getNict()) / 60));
                                //For Quality........
                                tmpCorrectedTPP = tmpCorrectedTPP + correctedTPP;
                                tmpReject = tmpReject + product.getRejections();
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
                // System.out.println(performance);
                // System.out.println(quality);
                // System.out.println(availability);
                // System.out.println("");
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
       //    ObjectMapper obj = new ObjectMapper();
       // try{
       //     System.out.println(obj.writerWithDefaultPrettyPrinter().writeValueAsString(machineWiseOverall));
       // }
       // catch (Exception e){
       //     e.printStackTrace();
       // }
        return machineWiseOverall;
    }

    @Override
    public List<DowntimeRawDataEntity> getDowntimeRawData(){
        DowntimeDataThread downtimeRawThread=new DowntimeDataThread(downtimeRawDataRepository,userServiceImpl.fromDate,userServiceImpl.toDate);
        downtimeRawThread.run();
        return downtimeRawThread.getValue();
    }

    @Override
    public List<InactiveMachineValuesEntity> inactiveMachineValues(){
        InactiveMachineDataThread inactiveMachineRawThread=new InactiveMachineDataThread(inactiveMachineValuesRepository);
        inactiveMachineRawThread.run();
        return inactiveMachineRawThread.getValue();
    }

    @Override
    public List<MachineValuesEntity> getMachineData(){
        MachineDataThread machineValueThread=new MachineDataThread(machineValuesRepository);
        machineValueThread.run();
        return machineValueThread.getValue();

        //Thread Executor Threadpoolexecutor.....
        // threadPoolExecutorUtil.executeTask(machineValueThread);
    }

    @Override
    public List<PartValuesEntity> getPartData(){
        PartDataThread partValueThread=new PartDataThread(partValuesRepository);
        partValueThread.run();
        return partValueThread.getValue();
    }

    @Override
    public List<DowntimeReasonEntity> getDowntimeReasonData(){
        DowntimeReasonThread downtimeReasonThread=new DowntimeReasonThread(downtimeReasonRepository);
        downtimeReasonThread.run();
        return downtimeReasonThread.getValue();
    }

    @Override
    public List<ProductionRawDataEntity> getProductionData(){
        ProductionDataThread productionRawThread=new ProductionDataThread(productionRawDataRepository,userServiceImpl.fromDate,userServiceImpl.toDate);
        productionRawThread.run();
        return productionRawThread.getValue();
    }

    @Override
    public List<OverallTargetValuesEntity> getOverallTargetValues(){
        OverallTargetValuesThread overallTargetValuesThread=new OverallTargetValuesThread(overallTargetValuesRepository);
        overallTargetValuesThread.run();
        return overallTargetValuesThread.getValue();
    }

    public List<DowntimeRawDataEntity> filterRawDataDowntime(){
        List<DowntimeRawDataEntity> downData = userServiceImpl.downtimeRaw;
        Set<DowntimeRawDataEntity> set = new LinkedHashSet<DowntimeRawDataEntity>();
        List<InactiveMachineValuesEntity> inactive = userServiceImpl.inactiveMachineData;
        set.addAll(downData);
        downData.clear();
        downData.addAll(set);
        Iterator<DowntimeRawDataEntity> i = downData.iterator();
        while (i.hasNext()) {
            DowntimeRawDataEntity s = i.next(); 
           // Do something
            if (s.getSplit_duration()<0) {
                i.remove();
            }
            else{
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
                try {
                    //From Date.......
                    Date fdate = formatDate.parse(s.getShift_date());
                    Date fdate1 = formatDate.parse(userServiceImpl.fromDate);
                    //From Time.......
                    Date ftime = formatTime.parse(s.getStart_time());
                    Date ftime1 = formatTime.parse(userServiceImpl.fromTime);

                    int flag=0;
                    for (InactiveMachineValuesEntity in_active : inactive) {
                        if (in_active.getMachine_id().equals(s.getMachine_id())) {
                            if (in_active.getStatus().equals(0)) {
                                String sv = in_active.getLast_updated_on();
                                String[] sp=sv.split("\\s");
                                Date fdate2 = formatDate.parse(sp[0]);
                                Date ftime2 = formatTime.parse(sp[1]);
                                if (fdate.compareTo(fdate2) >=0 && in_active.getMachine_id().equals(s.getMachine_id()) && ftime.compareTo(ftime2)>0) {
                                    flag=1;
                                    i.remove();
                                }
                            }
                        }
                    }
                    if ((int)flag==(int)(0)) {
                        //From date Filter
                        //Date Filter..........
                        if (fdate.compareTo(fdate1)<0) {
                            i.remove();
                        }
                        else{
                            //Time Filter.......
                            if ((fdate.compareTo(fdate1) == 0) && (ftime1.compareTo(ftime)<0)) {
                                i.remove();
                            }
                        }

                        //To date Filter
                        //To Date.......
                        Date tdate = formatDate.parse(s.getShift_date());
                        Date tdate1 = formatDate.parse(userServiceImpl.toDate);
                        //To Time.......
                        Date ttime = formatTime.parse(s.getStart_time());
                        Date ttime1 = formatTime.parse(userServiceImpl.toTime);

                        if (tdate.compareTo(tdate1)>0) {
                            i.remove();
                        }
                        else{
                            //Time Filter.......
                            if ((tdate.compareTo(tdate1) == 0) && (ttime1.compareTo(ttime)>0)) {
                                i.remove();
                            }
                        }    
                    }
                    
                }catch (Exception e) {
                    System.out.println("Exception");
                }
            }
        }
        return downData;
    }

    public List<ProductionRawDataEntity> filterRawDataProduction(){
        List<ProductionRawDataEntity> productionData = userServiceImpl.productionRaw;
        List<InactiveMachineValuesEntity> inactive = userServiceImpl.inactiveMachineData;
        Set<ProductionRawDataEntity> set = new LinkedHashSet<ProductionRawDataEntity>();
         
        set.addAll(productionData);
        productionData.clear();
        productionData.addAll(set);

       //  ObjectMapper obj = new ObjectMapper();
       // // ObjectNode obj1 = obj.createObjectNode();
       // try{
       //     System.out.println(obj.writerWithDefaultPrettyPrinter().writeValueAsString(productionData));
       // }
       // catch (Exception e){
       //     e.printStackTrace();
       // }

        Iterator<ProductionRawDataEntity> i = productionData.iterator();
        while (i.hasNext()) {
            ProductionRawDataEntity s = i.next(); 
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
            try {
                //From Date.......
                Date fdate = formatDate.parse(s.getShift_date());
                Date fdate1 = formatDate.parse(userServiceImpl.fromDate);
                //From Time.......
                Date ftime = formatTime.parse(s.getStart_time());
                Date ftime1 = formatTime.parse(userServiceImpl.fromTime);

                int flag=0;
                for (InactiveMachineValuesEntity in_active : inactive) {
                    if (in_active.getMachine_id().equals(s.getMachine_id())) {
                        if (in_active.getStatus().equals(0)) {
                            String sv = in_active.getLast_updated_on();
                            String[] sp=sv.split("\\s");
                            Date fdate2 = formatDate.parse(sp[0]);
                            Date ftime2 = formatTime.parse(sp[1]);
                            if (fdate.compareTo(fdate2) >=0 && in_active.getMachine_id().equals(s.getMachine_id()) && ftime.compareTo(ftime2)>0) {
                                flag=1;
                                // System.out.println("Inactive Machine Present!");
                                i.remove();
                            }
                        }
                    }
                }
                if ((int)flag==(int)(0)) {
                    //From date Filter
                    //Date Filter..........
                    
                    if (fdate.compareTo(fdate1)<0) {
                        // System.out.println("Old date Data present!");
                        i.remove();
                    }
                    else{
                        //Time Filter.......
                        if ((fdate.compareTo(fdate1) == 0) && (ftime1.compareTo(ftime)<0)) {
                            i.remove();
                            // System.out.println("Old hour Data present!");
                        }
                    }

                    //To date Filter
                    //To Date.......
                    Date tdate = formatDate.parse(s.getShift_date());
                    Date tdate1 = formatDate.parse(userServiceImpl.toDate);
                    //To Time.......
                    Date ttime = formatTime.parse(s.getStart_time());
                    Date ttime1 = formatTime.parse(userServiceImpl.toTime);

                    if (tdate.compareTo(tdate1)>0) {
                        // System.out.println("Future Data present!");
                        i.remove();
                    }
                    else{
                        //Time Filter.......
                        if ((tdate.compareTo(tdate1) == 0) && (ttime1.compareTo(ttime)>0)) {
                            i.remove();
                            // System.out.println("Future time data present!");
                        }
                    }
                }
            }catch (Exception e) {
                System.out.println("Exception");
            }
        
        }
        return productionData;
    }

    public List<DowntimeRawDataEntity> machineDowntime(){
        List<DowntimeRawDataEntity> downData = userServiceImpl.downtimeFilter;
        Set<DowntimeRawDataEntity> set = new LinkedHashSet<DowntimeRawDataEntity>();
        set.addAll(downData);
        downData.clear();
        downData.addAll(set);
        // System.out.println(downData.size());
        return downData;
    }

    public HashMap machineWiseDataOrdering(){
        List<DowntimeRawDataEntity> downData = userServiceImpl.downtimeFilter;
        List<MachineValuesEntity> machine = userServiceImpl.machineValues;
        List<PartValuesEntity> part = userServiceImpl.partValues;
        
//        Map<String ,List> machineWiseOrdering = new HashMap<>();
        // Map<String ,Map> machineWiseOrdering = new HashMap<>();
        // List<Map> machineWiseOrdering = Collections.<Map>emptyList();
        HashMap<String,HashMap> machineWiseOrdering=new HashMap<String,HashMap>();
        for (MachineValuesEntity m : machine)
        { 
            //List<MachineValuesEntity> machineWiseOrderingTemp[];
            //List<MachineValuesEntity> machineWiseOrderingTemp = Collections.<MachineValuesEntity>emptyList();
            //List<String> machineWiseOrderingTemp = new ArrayList<>();
            HashMap<String,ArrayList> machineWiseOrderingTemp=new HashMap<String,ArrayList>();
            for (PartValuesEntity p : part ) {

                List<HashMap> partWiseOrdering = new ArrayList<>();
                // HashMap<String,HashMap> partWiseOrdering=new HashMap<String,HashMap>();


                for (DowntimeRawDataEntity r : downData ) {
                    if ((m.getMachine_id().equals(r.getMachine_id())) && (p.getPart_id().equals(r.getPart_id()))) {
                        HashMap<String,String> map=new HashMap<String,String>();
                        map.put("machine_id",r.getMachine_id());  
                        map.put("downtime_reason_id",r.getDowntime_reason_id());    
                        map.put("tool_id",r.getTool_id());   
                        map.put("part_id",r.getPart_id());
                        map.put("shift_date",r.getShift_date());
                        map.put("start_time",r.getStart_time()); 
                        map.put("end_time",r.getEnd_time());
                        map.put("split_duration",String.valueOf(r.getSplit_duration())); 
                        map.put("calendar_date",r.getCalendar_date());
                        partWiseOrdering.add(map);
                        // partWiseOrdering.put(r.getPart_id(),map);

                    }
                }
                // System.out.println(partWiseOrdering.getClass());
                machineWiseOrderingTemp.put(p.getPart_id(), (ArrayList) partWiseOrdering);
                //System.out.println(partWiseOrdering);
                // List x=null;
                //Map<String ,List> x = new HashMap<>();
                //x.put("part_id",partWiseOrdering);
            }
            // List y;
            //Map<String ,List> y = new HashMap<>();
            //y.put("machine_id",machineWiseOrderingTemp);
            // System.out.println(machineWiseOrderingTemp.getClass());
            machineWiseOrdering.put(m.getMachine_id(), machineWiseOrderingTemp);
        }
       //System.out.println(machineWiseOrdering.getClass());
        return machineWiseOrdering;
    }

    public Map machineWiseDowntime() {
        HashMap downData = userServiceImpl.machineWiseOrdering;
        List<DowntimeReasonEntity> downReason = userServiceImpl.downtimeReason;
        List<DowntimeRawDataEntity> downtimeData = userServiceImpl.downtimeFilter;
        List<MachineValuesEntity> machineVal = userServiceImpl.machineValues;

        Map<String, Map> machineWiseDowntime = new HashMap<>();
        for (MachineValuesEntity machine :machineVal ) {
            float machineoff=0;
            float planned=0;
            float unplanned=0;
            for (DowntimeRawDataEntity d : downtimeData ) {
                int id = Integer.parseInt(d.getDowntime_reason_id());
                float duration = d.getSplit_duration();
                String reason="";
                String category="";
                if (machine.getMachine_id().equals(d.getMachine_id())) {
                    for (DowntimeReasonEntity r : downReason) {
                        if (id == Integer.parseInt(r.getDowntime_reason_id())){
                            reason=r.getDowntime_reason();
                            category=r.getDowntime_category();
                        }
                    }
                    
                    if (category.equals("Unplanned")) {
                        unplanned=unplanned+ d.getSplit_duration();
                    }
                    else if(reason.equals("Machine_OFF") && category.equals("Planned")){
                        machineoff=machineoff+d.getSplit_duration();
                    }
                    else{
                        planned=planned+d.getSplit_duration();
                    }
                }

                // Map<String, String> map = new HashMap<>();
                // map.put("planned","1");
                // map.put("unplaned","2");
                // map.put("machine_off","3");

                // Set<Map.Entry<String, String> > set= map.entrySet();

                // List<Map.Entry<String, String> > list= new ArrayList<>(set);

                // System.out.println(list);

                // Iterator machine = mapping.entrySet().iterator();
                // while (machine.hasNext()) {
                //     Map.Entry machineVal = (Map.Entry)machine.next();
                // }
            }

            Map<String, Float> machineWise = new HashMap<>();
            machineWise.put("Planned",planned);
            machineWise.put("Unplanned",unplanned);
            machineWise.put("Machine_OFF",machineoff);

            machineWiseDowntime.put(machine.getMachine_id(),machineWise);
        }

//        Iterator machine = downData.entrySet().iterator();
//        while (machine.hasNext()) {
//            Map.Entry machineVal = (Map.Entry)machine.next();
//
//            Iterator part = downData.entrySet().iterator();
////            System.out.println(machineVal.getValue());
////            for (int i=0;i<5;i++){
////                System.out.println(dow[i]);
////            }
//            machine.remove();
//        }
        return machineWiseDowntime;
    }


    public float getOee() {
        return oee;
    }

    public float getOeePercentage() {
        return oeePercentage;
    }

    public void setOeePercentage(float oeePercentage) {
        this.oeePercentage = oeePercentage;
    }

    public float getOoePercentage() {
        return ooePercentage;
    }

    public void setOoePercentage(float ooePercentage) {
        this.ooePercentage = ooePercentage;
    }

    public float getTeepPercentage() {
        return teepPercentage;
    }

    public void setTeepPercentage(float teepPercentage) {
        this.teepPercentage = teepPercentage;
    }

    public void setOee(float oee) {
        this.oee = this.oee + oee;
    }

    public float getOoe() {
        return ooe;
    }

    public void setOoe(float ooe) {
        this.ooe = this.ooe + ooe;
    }

    public float getTeep() {
        return teep;
    }

    public void setTeep(float teep) {
        this.teep = this.teep + teep;
    }

}