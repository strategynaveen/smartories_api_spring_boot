package com.api.api_interface.service;

import com.api.api_interface.entity.DowntimeRawDataEntity;
import com.api.api_interface.entity.ProductionRawDataEntity;

import java.util.List;

public  interface RawDataFilterServeice{
   List<DowntimeRawDataEntity> filterRawDataDowntime();
   List<ProductionRawDataEntity> filterRawDataProduction();
}
