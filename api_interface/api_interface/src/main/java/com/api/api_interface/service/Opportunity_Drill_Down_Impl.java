package com.api.api_interface.service;

import com.api.api_interface.entity.DowntimeReasonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.KeyStore;
import java.util.*;

@Service
@Component(value = "Opportunity_drill_down_graph")
public class Opportunity_Drill_Down_Impl implements Opportunity_Drill_Down{


//    this object getting quality reasons and total object
    @Autowired
    private  UserServiceImpl user_service;

//    this object  get downtime reasons and total
    @Autowired
    private AvailabilityOpportunityServiceImpl availability_obj;

//    this object get performance opportunity graph grand total
    @Autowired
    private PerformanceOpportunityServiceImpl performance_obj;



    @Override
    public Map get_opportuntiy_drill_down_values() {

//        quality reaosns graph get reasons wise total and reasons array
        List Quality_reason_wise_total = user_service.reaon_wise_total_quality;
        List Quality_reasons = user_service.quality_reasons_array;

//        Availability opportunity get total and reaosns array
        List downtime_total = availability_obj.get_availability_reason_wise_total;
        List downtime_reasons = availability_obj.get_availability_reasons;

//        performance opportunity get grand total
        int performance_total = performance_obj.performance_grand_total;


//      for loop
//        for (Object r:downtime_reasons){
//            System.out.println("downtime reason:\t"+r.getClass());
//        }

//        quality reasons and total add the hashmap function
        HashMap<String ,Integer> opportuntiy_drill_down_grapp = new HashMap();
        for (int i=0;i<Quality_reasons.size();i++){
            opportuntiy_drill_down_grapp.put(Quality_reasons.get(i).toString(),Integer.parseInt(Quality_reason_wise_total.get(i).toString()));
        }

//        downtime reasons and total adding map function
        for (int j=0;j<downtime_reasons.size();j++){
            opportuntiy_drill_down_grapp.put(downtime_reasons.get(j).toString(),Integer.parseInt(downtime_total.get(j).toString()));
        }

//        next performance opportunity graph adding
        opportuntiy_drill_down_grapp.put("PerFormance",performance_total);

//        next sorted code for all reasons
        List<Map.Entry<String,Integer>> mylist = new ArrayList<>(opportuntiy_drill_down_grapp.entrySet());

        Collections.sort(mylist, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue()-o1.getValue();
            }

        });

        List final_reason = new ArrayList();
        List final_value = new ArrayList();
        for (Map.Entry<String, Integer> e:mylist){
            final_reason.add(e.getKey());
            final_value.add(e.getValue());
            //System.out.println(e.getKey()+"="+e.getValue()+"\n");
        }

        System.out.println("final reasons:\t"+final_reason+"\n final values:\t"+final_value);
        Map final_map_arr = new HashMap<>();
        final_map_arr.put("Reason",final_reason);
        final_map_arr.put("Total",final_value);


        return final_map_arr;
    }
}
