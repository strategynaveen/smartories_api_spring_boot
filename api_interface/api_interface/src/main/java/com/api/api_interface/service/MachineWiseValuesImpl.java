package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Primary
@Component("second")
public class MachineWiseValuesImpl{
    @Autowired
    private  MachineWiseValuesImpl machineWiseValuesImpl;
}
