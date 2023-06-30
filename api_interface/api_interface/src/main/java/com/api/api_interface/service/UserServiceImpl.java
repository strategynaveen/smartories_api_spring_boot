package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import com.api.api_interface.repo.*;
import com.api.api_interface.repo.MachineValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Component("first")
public class UserServiceImpl implements OverallValuesService,RawDataFilterServeice,MachineWiseDowntimeService
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

    // quality reasons graph naveen
     // quality reasons repository object naveen
     @Autowired
     private Quality_Reason_repo quality_repo;
 

    @Autowired
    private UserServiceImpl userServiceImpl;

    //Encapsulation Variable
    protected List<MachineValuesEntity> machineValues;
    private List<DowntimeRawDataEntity> downtimeRaw;
    private List<ProductionRawDataEntity> productionRaw;
    protected List<DowntimeRawDataEntity> downtimeFilter;

    // quality reasons naveen

    //    quality reasons global variable
    private  List<Quality_ReasonEntity> quality_reasons;


    //    production expand data for quality reasons
    private  Map production_expand;


    public List<ProductionRawDataEntity> getProductionFilter() {
        return productionFilter;
    }

    public void setProductionFilter(List<ProductionRawDataEntity> productionFilter) {
        this.productionFilter = productionFilter;
    }

    protected List<ProductionRawDataEntity> productionFilter;

    public List<PartValuesEntity> getPartValues() {
        return partValues;
    }

    public void setPartValues(List<PartValuesEntity> partValues) {
        this.partValues = partValues;
    }

    protected List<PartValuesEntity> partValues;
    protected List<DowntimeReasonEntity> downtimeReason;
    private HashMap machineWiseOrdering;

    public Map getMachineWiseDowntime() {
        return machineWiseDowntime;
    }

    public void setMachineWiseDowntime(Map machineWiseDowntime) {
        this.machineWiseDowntime = machineWiseDowntime;
    }

    protected  Map machineWiseDowntime;
    private List<InactiveMachineValuesEntity> inactiveMachineData;
    private List<OverallTargetValuesEntity> overallTargetValues;
    //Function will remove
    private  Map machineWiseOverallValues;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    private String fromDate;
    private String toDate;
    private String fromTime;
    private String toTime;


    private  float oee;
    private  float ooe;
    private float teep;

    private float oeePercentage;

    private float ooePercentage;
    private  float teepPercentage;

     // global variable for  quality graph
    
     protected   Map  quality_reasons_print;

//     gloable varaiable for opportunity drilldown
    protected List  reaon_wise_total_quality;

    protected  List quality_reasons_array;



    public int getAllTime() {
        return allTime;
    }

    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }

    private int allTime;

    @Override
    public void getOverallValues(String from_time_range,String to_time_range){
        String[] from_date_get = from_time_range.split("T");
        String[] to_date_get = to_time_range.split("T");
        userServiceImpl.setFromDate(from_date_get[0]);
        userServiceImpl.setToDate(to_date_get[0]);
        userServiceImpl.setFromTime(from_date_get[1]);
        userServiceImpl.setToTime(to_date_get[1]);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            Date firstParsedDate = dateFormat1.parse(userServiceImpl.getFromDate().concat(" ").concat(userServiceImpl.getFromTime()));
            Date secondParsedDate = dateFormat1.parse(userServiceImpl.getToDate().concat(" ").concat(userServiceImpl.getToTime()));
            long diff = secondParsedDate.getTime() - firstParsedDate.getTime();
            userServiceImpl.setAllTime((int)(diff/(1000*60)));
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
        machineWiseDowntime = userServiceImpl.getMachineWiseDowntime();
        // List<DowntimeRawDataEntity> machineWiseDowntime = userServiceImpl.machineDowntime();

        //Function for get Overall Target values.......
        overallTargetValues=userServiceImpl.getOverallTargetValues();

        // return machineWiseOrdering;


         // quality reasons graph naveen
        
        //        get quality reasons for graph
        quality_reasons = userServiceImpl.get_quality_reasons();

        //        just printing temporary
        quality_reasons_print = userServiceImpl.qulaity_calculation();
        //System.out.println(quality_reasons_print);
            // System.out.println("quality reasons:\n"+quality_reasons);

        //        production expand data
        production_expand = userServiceImpl.getproductionExpand();
        //System.out.println(production_expand);

//        opportunity drilldown graph naveen



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

    @Override
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
            if (s.getSplit_duration() < 0) {
                i.remove();
            } else {
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
                try {
                    //From Date.......
                    Date fdate = formatDate.parse(s.getShift_date());
                    Date fdate1 = formatDate.parse(userServiceImpl.fromDate);
                    //From Time.......
                    Date ftime = formatTime.parse(s.getStart_time());
                    Date ftime1 = formatTime.parse(userServiceImpl.fromTime);

                    int flag = 0;
                    for (InactiveMachineValuesEntity in_active : inactive) {
                        if (in_active.getMachine_id().equals(s.getMachine_id())) {
                            if (in_active.getStatus().equals(0)) {
                                String sv = in_active.getLast_updated_on();
                                String[] sp = sv.split("\\s");
                                Date fdate2 = formatDate.parse(sp[0]);
                                Date ftime2 = formatTime.parse(sp[1]);
                                if (fdate.compareTo(fdate2) >= 0 && in_active.getMachine_id().equals(s.getMachine_id()) && ftime.compareTo(ftime2) > 0) {
                                    flag = 1;
                                    i.remove();
                                }
                            }
                        }
                    }
                    if ((int) flag == (int) (0)) {
                        //From date Filter
                        //Date Filter..........
                        if (fdate.compareTo(fdate1) < 0) {
                            i.remove();
                        } else {
                            //Time Filter.......
                            if ((fdate.compareTo(fdate1) == 0) && (ftime1.compareTo(ftime) < 0)) {
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

                        if (tdate.compareTo(tdate1) > 0) {
                            i.remove();
                        } else {
                            //Time Filter.......
                            if ((tdate.compareTo(tdate1) == 0) && (ttime1.compareTo(ttime) > 0)) {
                                i.remove();
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Exception");
                }
            }
        }
        return downData;
    }

    @Override
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
        List<PartValuesEntity> part = userServiceImpl.getPartValues();

        HashMap<String,HashMap> machineWiseOrdering=new HashMap<String,HashMap>();
        for (MachineValuesEntity m : machine)
        {
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

    @Override
    public void machineWiseDowntime() {
        //HashMap downData = userServiceImpl.machineWiseOrdering;
        List<DowntimeReasonEntity> downReason = userServiceImpl.downtimeReason;
        List<DowntimeRawDataEntity> downtimeData = userServiceImpl.downtimeFilter;
        List<MachineValuesEntity> machineVal = userServiceImpl.machineValues;

        Map<String, Map> machineWiseDowntime = new HashMap<>();
        for (MachineValuesEntity machine :machineVal ) {
            float machineoff = 0;
            float planned = 0;
            float unplanned = 0;
            for (DowntimeRawDataEntity d : downtimeData) {
                int id = Integer.parseInt(d.getDowntime_reason_id());
                float duration = d.getSplit_duration();
                String reason = "";
                String category = "";
                if (machine.getMachine_id().equals(d.getMachine_id())) {
                    for (DowntimeReasonEntity r : downReason) {
                        if (id == Integer.parseInt(r.getDowntime_reason_id())) {
                            reason = r.getDowntime_reason();
                            category = r.getDowntime_category();
                        }
                    }

                    if (category.equals("Unplanned")) {
                        unplanned = unplanned + d.getSplit_duration();
                    } else if (reason.equals("Machine_OFF") && category.equals("Planned")) {
                        machineoff = machineoff + d.getSplit_duration();
                    } else {
                        planned = planned + d.getSplit_duration();
                    }
                }
            }

            Map<String, Float> machineWise = new HashMap<>();
            machineWise.put("Planned", planned);
            machineWise.put("Unplanned", unplanned);
            machineWise.put("Machine_OFF", machineoff);

            machineWiseDowntime.put(machine.getMachine_id(), machineWise);
        }
        userServiceImpl.setMachineWiseDowntime(machineWiseDowntime);
    }






     // quality reasons graph naveen
    
    // get quality reasons function
    public List<Quality_ReasonEntity> get_quality_reasons(){
        Quality_Data_thread quality_obj = new Quality_Data_thread(quality_repo);
        quality_obj.run();
        return  quality_obj.getValue();
    }

    // production expand
    public  Map getproductionExpand(){
        List<ProductionRawDataEntity> production_filter_list = userServiceImpl.productionFilter;
        //System.out.println("production size:\t"+production_filter_list.size());
        HashMap<String,Map> production_map = new HashMap<>();
        for (ProductionRawDataEntity p:production_filter_list){
            if (p.getReject_reason().isEmpty() != true){
                //System.out.println("reasoned value"+p.getReject_reason());

                String[] reasons = p.getReject_reason().split("&&");
                for(Integer i=0;i<reasons.length;i++){
                    HashMap<String,String> production_assign = new HashMap<>();
                    String[] reaosns_split = reasons[i].split("&");
                    String reason_count = reaosns_split[0];
                    String reason_str = reaosns_split[1].replace("_"," ");

                    production_assign.put("machine_id",p.getMachine_id());
                    production_assign.put("part_id",p.getPart_id());
                    production_assign.put("reject_count",reason_count);
                    production_assign.put("reject_reason",reason_str);
                    production_assign.put("total_reject",String.valueOf(p.getRejections()));
                    production_assign.put("total_correction",String.valueOf(p.getCorrections()));
                    production_assign.put("total_production",String.valueOf(p.getProduction()));
                    production_assign.put("shot_count",String.valueOf(p.getActual_shot_count()));
                    production_assign.put("start_time",String.valueOf(p.getStart_time()));
                    production_assign.put("end_time",String.valueOf(p.getEnd_time()));

                 production_map.put(String.valueOf(i),production_assign);
                }
            }

            //System.out.println("Rejection reasons is NULL"+p.getReject_reason());
        }

        return production_map;
    }


    // quality reasons graph  calculation function
    //    get quality reasons for browseing data
    public Map qulaity_calculation(){

        //        final mapping array
        Map final_arr = new HashMap();


        HashMap<String,Map> quality = new HashMap<>();
        List<Quality_ReasonEntity> quality_list = userServiceImpl.quality_reasons;
        Map production_list = userServiceImpl.getproductionExpand();

        List<PartValuesEntity> part_data = userServiceImpl.partValues;
        List<MachineValuesEntity> machine_data = userServiceImpl.machineValues;

        //HashMap<String,Map> quality_data = new HashMap<>();
        List quality_data = new ArrayList();
        HashMap<String,Map> quality_oppcost_data = new HashMap<>();
        HashMap<String,String> part_array = new HashMap<>();
        HashMap<Integer,List> total_reject = new HashMap<>();
        for (PartValuesEntity part:part_data){
            Integer tot_in = 0;
            HashMap<String,Map> tmp_reason = new HashMap<>();
//            HashMap<String,Map> tmp_actual_reason = new HashMap<>();
            List tmp_actual_reason= new ArrayList();

            HashMap<String,String> part_array_tmp = new HashMap<>();
            HashMap<String,Map> tmp_arr = new HashMap<>();
            HashMap<String,Map> tmp_actual_Data = new HashMap<>();
            ArrayList<AtomicReference<Integer>> reject_total = new ArrayList<>();
            for (Quality_ReasonEntity q:quality_list){

                AtomicReference<Integer> tmp_opportunity_cost = new AtomicReference<>(0);
                AtomicReference<Integer> tmp_total_reject = new AtomicReference<>(0);
                HashMap<String,String> tmp_actual = new HashMap<>();
                HashMap<String,Map> tamp_part = new HashMap<>();
                //System.out.println(production_list);
                production_list.forEach((k,v)->{
                    Map production_val = (Map) v;

                    HashMap<String,String> tmp_pass_part = new HashMap<>();
                    String quality_reason = String.valueOf(production_val.get("reject_reason")).replace(" ","_");
                    String quality_reason1 = q.getQuality_reason_name().replace(" ","_");
                    String part1 = production_val.get("part_id").toString().trim();
                    String part2 = part.getPart_id().trim();
        //          && (quality_reason == quality_reason1)
                    if ((part1.compareTo(part2) == 0) && (quality_reason1.compareTo(quality_reason)==0)){

                        //System.out.println("quality reasons:\t"+quality_reason1+"quality reasons:\n"+quality_reason);

                        int machine_rate_hour =1;
                        int machine_offrate_hour = 1;
                        for (MachineValuesEntity m:machine_data){
                            String machine = m.getMachine_id().trim();
                            String machine1 = production_val.get("machine_id").toString().trim();
                            if (machine.compareTo(machine1) == 0){
                                machine_rate_hour = Math.round(m.getRate_per_hour());
                                machine_offrate_hour = Math.round(m.getMachine_offrate_per_hour());
                            }
                        }
                        Float part_price = part.getPart_price();
                        Float material_price = part.getMaterial_price();
                        Integer part_weight = Math.round(part.getPart_weight());

                        //System.out.println(part_price+"part price");

                        String[] start_time_split = String.valueOf(production_val.get("start_time")).split(":");
                        String[] end_time_split = String.valueOf(production_val.get("end_time")).split(":");

                        Integer start_time_ms = Integer.parseInt(start_time_split[0])*3600 + Integer.parseInt(start_time_split[1])*60 + Integer.parseInt(start_time_split[2]);
                        Integer end_time_ms = Integer.parseInt(end_time_split[0])*3600 + Integer.parseInt(end_time_split[1])*60 + Integer.parseInt(end_time_split[2]);


                        Integer part_in_machine = (end_time_ms - start_time_ms)/60;

                        String ttp_str = production_val.get("total_production").toString();
                        String total_reject_str = production_val.get("total_reject").toString();
                        String correction_str = production_val.get("total_correction").toString();

                        Integer ttp = Integer.parseInt(ttp_str);

                        Integer reject = Integer.parseInt(total_reject_str);
                        Integer correction = Integer.parseInt(correction_str);

                        Integer total_correction = ttp + correction;

                        Integer umaterial_cost = Math.round(part_price) * total_correction *  (part_weight/1000);


                        Integer uproduction_cost = (part_in_machine/60)* machine_rate_hour;

                      // System.out.println("production  cost"+uproduction_cost+"part inmachine :\t"+part_in_machine+"\nmachine rate hour:\t"+machine_rate_hour+"part_weight:\t"+part_weight);

                        Integer utotal_part_produced_cost = umaterial_cost + uproduction_cost;

                        Integer good_revenu = Math.round(part_price) * (total_correction - reject);

                        Integer profit_loss = good_revenu - utotal_part_produced_cost;

                        //   unit part produced cost
                       Integer unitpart_production_cost = utotal_part_produced_cost/total_correction;

                        Integer t_reject  = reject;
                        //System.out.println("reject :\t"+t_reject+"\nunit production cost:\t"+unitpart_production_cost);
                        Integer opp_cost = t_reject * unitpart_production_cost;
                        //System.out.println("opportunity cost:\t"+opp_cost);
                        tmp_total_reject.set(tmp_total_reject.get() + t_reject);
                        tmp_opportunity_cost.set(tmp_opportunity_cost.get() + opp_cost);
                        //System.out.println("tmp_op_cost\t"+tmp_opportunity_cost.get());
                        tmp_pass_part.put("reject_reason",production_val.get("reject_reason").toString());
                        tmp_pass_part.put("part_id",part.getPart_id());
                        tmp_pass_part.put("unit_cost",unitpart_production_cost.toString());
                        tmp_pass_part.put("total_reject",t_reject.toString());
                        tmp_pass_part.put("opportunity_cost",opp_cost.toString());


                    }
                    tmp_reason.put(q.getQuality_reason_name(),tmp_pass_part);
                });

                //tmp_opportunity_cost.set(tmp_opportunity_cost.get());
                reject_total.add(tmp_total_reject);
                tmp_actual.put("reason",q.getQuality_reason_name());
                tmp_actual.put("total_reject",tmp_total_reject.get().toString());
                tmp_actual.put("total_cost",tmp_opportunity_cost.get().toString());
                tmp_actual.put("part_name",part.getPart_name());


//                tmp_actual_reason.put(q.getQuality_reason_name(),tmp_actual);
                tmp_actual_reason.add(tmp_actual);

            }
            //System.out.println("quality reasons:\n"+tmp_actual_reason);

            total_reject.put(total_reject.isEmpty() ? 0 : total_reject.size(),reject_total);
            //System.out.println("total reject array:\n"+total_reject);
            //  reason wise part
            tmp_arr.put(part.getPart_id(),tmp_reason);

            //quality_data.put("Quality_availability_data",tmp_arr);

            //  part wise reason cumculate
            // tmp_actual_Data.put(part.getPart_id(),tmp_actual_reason);
            Map tmp = new HashMap();
            tmp.put("Part",part.getPart_id());
            tmp.put("Reason",tmp_actual_reason);

            quality_data.add(tmp);

//            System.out.println("quality data list:\t"+quality_data);

           // part_array_tmp.put(part.getPart_id(),part.getPart_name());
           // part_array.put(part.getPart_id(),part_array_tmp);
            part_array.put(part.getPart_id(),part.getPart_name());

            //  incrment for total rejection count perpose
            quality_oppcost_data.put(part.getPart_id(),tmp_reason);
        }
               // System.out.println("total rejects\n"+quality_data+"\n part array:\n"+part_array);


        //quality reasons for list to mapping
        ArrayList<String> reason_array = new ArrayList<>();

        for (Quality_ReasonEntity q:quality_list){
            HashMap<String,String> quality_ordering = new HashMap<>();
            quality_ordering.put("reasons_id",q.getQuality_reason_id());
            quality_ordering.put("reasons_name",q.getQuality_reason_name());
            quality_ordering.put("status",q.getStatus());
            quality_ordering.put("last_updated_on",q.getLast_updated_on());
            quality_ordering.put("last_updated_by",q.getLast_updated_by());

            quality.put(q.getQuality_reason_id(),quality_ordering);
            reason_array.add(q.getQuality_reason_name());
                }

            Integer over_all_opportunity = 0;
            List<Integer> reason_wise_total = new ArrayList<>();
            for (int i=0;i<reason_array.size();i++){
                AtomicInteger tempcost = new AtomicInteger();
                int finalI = i;
                for (int j=0;j<quality_data.size();j++){
                      Map tmp_map = (Map) quality_data.get(j);
                      List tmp_list = (List) tmp_map.get("Reason");
                      for (int k =0 ;k<tmp_list.size();k++){
                          Map tmp_map1 = (Map) tmp_list.get(k);
                          String reason_arr_str = reason_array.get(i).toString();
                          String reaos_str = tmp_map1.get("reason").toString();
                          if (reason_arr_str.compareTo(reaos_str) == 0){
                              Integer tmp_total_cost = Integer.parseInt(tmp_map1.get("total_cost").toString());
                              tempcost.set(tempcost.get() + tmp_total_cost);
                          }
//                         System.out.println("Reasons for list :\t"+tmp_list.get(k));
                      }
                      //System.out.println("map get function for loop:\t"+tmp_list.size());

                }
                reason_wise_total.add(tempcost.get());
                over_all_opportunity = over_all_opportunity + tempcost.get();
            }



            final_arr.put("OppCost",quality_data);
            final_arr.put("Part",part_array);
            final_arr.put("Reason",reason_array);
            final_arr.put("grand_total",over_all_opportunity);
            final_arr.put("Total",reason_wise_total);

//            global variable value passing in opportunity drill down graph
        userServiceImpl.reaon_wise_total_quality = reason_wise_total;
        userServiceImpl.quality_reasons_array = reason_array;
//            System.out.println("Dai "+quality_oppcost_data);
//            System.out.println(final_arr);

//            System.out.println("\n\n"+reason_wise_total);
            return  final_arr;

        }




    // quality opportunity naveen


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