package com.api.api_interface.service;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Component("second")
public  interface MachineWiseDowntimeService{
   void machineWiseDowntime();
}