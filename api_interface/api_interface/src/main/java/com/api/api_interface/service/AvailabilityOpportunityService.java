package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


public interface AvailabilityOpportunityService {
   Map getAvailabilityOpportunityValues();
}

