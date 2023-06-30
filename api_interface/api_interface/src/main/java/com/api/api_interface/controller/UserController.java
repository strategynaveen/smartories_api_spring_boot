package com.api.api_interface.controller;

import com.api.api_interface.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages={"com.api.api_interface.entity", "com.api.api_interface.repo","com.api.api_interface.service"})
@RestController
@RequestMapping("/graph")
public class UserController {
    @Autowired
    @Qualifier(value = "first")
    private OverallValuesService overallValuesService;
//    @Autowired
//    @Qualifier(value = "second")
//    private MachineWiseOverallValuesService machineWiseOverallValuesService;

    @Autowired
    @Qualifier(value = "machineWiseOEE")
    private MachineWiseOEEService machineWiseOEEService;
    @Autowired
    private UserController userController;
    private String from_time;
    private String  to_time;
    private Map machineWiseOEE;

    @Autowired
    private MachineWiseDowntimeService machineWiseDowntimeService;

    @Autowired
    @Qualifier(value = "overallOEE")
    private OverallMonitoringValuesService overallMonitoringObj;

    @Autowired
    @Qualifier(value = "availabilityOpportunity")
    private AvailabilityOpportunityService availabilityOpportunityService;

    @Autowired
    @Qualifier(value = "performanceOpportunity")
    private  PerformanceOpportunityService performanceOpportunityService;

    @Autowired
    @Qualifier(value = "plOpportunity")
    private PLOpportunityService plOpportunityService;

    @Autowired
    @Qualifier(value = "machineWisePL")
    private  MachineWisePLOpportunity machineWisePLOpportunity;

    @Autowired
    @Qualifier(value = "oeeTrendDay")
    private OEETrendDayWise oeeTrendDayWise;

    @Autowired
    @Qualifier(value = "partWisePL")
    private PartWisePLOpportunityService partWisePLOpportunityService;

    @Autowired
    @Qualifier(value = "OpportunityTrendPL")
    private OpportunityTrendDayService opportunityTrendDayService;

     // quality reasons graph 
    
     @Autowired
     private UserServiceImpl service_obj;


    @Autowired
    @Qualifier(value = "Opportunity_drill_down_graph")
    private  Opportunity_Drill_Down_Impl opportunity_obj;
 

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    private  Map machineWiseMonitoring;
    private Map overallMonitoring;


   @GetMapping(path="/machineWiseMonitoring/{from_time}/{to_time}")
   public Map getMachineWiseMonitoring(@PathVariable String from_time,@PathVariable String to_time){
       userController.setFrom_time(from_time);
       userController.setTo_time(to_time);
       overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
       machineWiseDowntimeService.machineWiseDowntime();
       Map machineWise = machineWiseOEEService.getMachineWiseOEEValues();
       Map x = machineWiseOEEService.machineWiseOrderingForGraph();
       return machineWise;

   }

    @GetMapping(path="/overallMonitoringValues/{from_time}/{to_time}")
    public Map overallMonitoring(@PathVariable String from_time,@PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        machineWiseDowntimeService.machineWiseDowntime();
        userController.machineWiseMonitoring = machineWiseOEEService.getMachineWiseOEEValues();
        userController.overallMonitoring = overallMonitoringObj.overallAverage();
        return userController.overallMonitoring;
    }

    @GetMapping(path="/availabilityOpportunity/{from_time}/{to_time}")
    public Map getAvailabilityOpportunity(@PathVariable String from_time,@PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        return availabilityOpportunityService.getAvailabilityOpportunityValues();
    }

    @GetMapping(path="/performanceOpportunity/{from_time}/{to_time}")
    public Map performanceOpportunity(@PathVariable String from_time,@PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        machineWiseDowntimeService.machineWiseDowntime();
        return performanceOpportunityService.getPerformanceOpportunityValues();
    }

    @GetMapping(path="/plOpportunity/{from_time}/{to_time}")
    public Map plOpportunity(@PathVariable String from_time,@PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        machineWiseDowntimeService.machineWiseDowntime();
        return plOpportunityService.getPLOpprtunityValues();
    }

    @GetMapping(path="/machineWisePL/{from_time}/{to_time}")
    public Map machineWisePL(@PathVariable String from_time,@PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        machineWiseDowntimeService.machineWiseDowntime();
        return machineWisePLOpportunity.getMachineWisePLValues();
    }

    @GetMapping(path="/oeeTrend/{from_time}/{to_time}")
    public List oeeTrend(@PathVariable String from_time, @PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        machineWiseDowntimeService.machineWiseDowntime();
        return oeeTrendDayWise.getOEETrendValues();
    }

    @GetMapping(path="/partWisePLOpportunity/{from_time}/{to_time}")
    public List partWisePLOpportunity(@PathVariable String from_time, @PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        machineWiseDowntimeService.machineWiseDowntime();
        return partWisePLOpportunityService.getPartWisePLValues();
        //return "";
    }

    @GetMapping(path="/OpportunityPLTrend/{from_time}/{to_time}")
    public Map OpportunityPLTrend(@PathVariable String from_time, @PathVariable String to_time){
        userController.setFrom_time(from_time);
        userController.setTo_time(to_time);
        overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
        machineWiseDowntimeService.machineWiseDowntime();
        oeeTrendDayWise.getOEETrendValues();
        return opportunityTrendDayService.getOpportunityTrendValues();
       // return "Hello";
    }

     // quality opportunity graph naveen
    
//   quality reasons graph for full stack developer
@GetMapping(path = "qualityOpportunity/{from_date}/{to_date}")
public Map qualityOpportunity(@PathVariable String from_date , @PathVariable String to_date){
    userController.setFrom_time(from_date);
    userController.setTo_time(to_date);
    overallValuesService.getOverallValues(userController.getFrom_time(),userController.getTo_time());
    return  service_obj.qulaity_calculation();
// return "from date\n"+from_date+"to date:\t"+to_date;
}


// Opportunity DrillDown
    @GetMapping(path = "opportunity_drill_down/{from_date}/{to_date}")
    public Map opportunity_drill_down(@PathVariable String from_date, @PathVariable String to_date){
        userController.setFrom_time(from_date);
        userController.setTo_time(to_date);

        userController.qualityOpportunity(userController.getFrom_time(),userController.getTo_time());
        userController.getAvailabilityOpportunity(userController.getFrom_time(),userController.getTo_time());
        userController.performanceOpportunity(userController.getFrom_time() , userController.getTo_time());

        overallValuesService.getOverallValues(userController.getFrom_time(), userController.getTo_time());

        return opportunity_obj.get_opportuntiy_drill_down_values();

//        return "hello";
    }


}
