package com.api.api_interface.controller;

import com.api.api_interface.service.OverallValuesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graph")
public class UserController {
    @Autowired
    private OverallValuesService overallValuesService;

    @GetMapping(path="/overallValues")
    public String getOverallValues(){
        ObjectMapper obj = new ObjectMapper();
         try{
             // System.out.println(obj.writerWithDefaultPrettyPrinter().writeValueAsString(overallValuesService.getOverallValues()));
             return (obj.writerWithDefaultPrettyPrinter().writeValueAsString(overallValuesService.getOverallValues()));
         }
         catch (Exception e){
            //return e.printStackTrace();
             return "";
         }
//        System.out.println(machineWiseOverallValues.getClass());
//        return (overallValuesService.getOverallValues());
    }

}
