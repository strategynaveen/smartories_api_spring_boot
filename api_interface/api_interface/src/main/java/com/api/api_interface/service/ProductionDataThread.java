package com.api.api_interface.service;

import com.api.api_interface.entity.ProductionRawDataEntity;
import com.api.api_interface.repo.ProductionRawDataRepository;

import java.util.List;

public class ProductionDataThread implements Runnable
{
    private final ProductionRawDataRepository productionRawDataRepository;
    List<ProductionRawDataEntity> productionRaw;
    private String from_date ;
    private String to_date;
    ProductionDataThread(ProductionRawDataRepository productionRawDataRepository,String from_date,String to_date)
    {
        this.productionRawDataRepository = productionRawDataRepository;
        this.from_date=from_date;
        this.to_date=to_date;
    }

    @Override
    public void run()
    {
        // productionRaw=productionRawDataRepository.findAll();
        productionRaw=productionRawDataRepository.findAllFiltered_data(this.from_date,this.to_date);
    }
    public List<ProductionRawDataEntity> getValue() {
        return productionRaw;
    }
}
