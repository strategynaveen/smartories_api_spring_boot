package com.api.api_interface.repo;

import com.api.api_interface.entity.ProductionRawDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductionRawDataRepository extends JpaRepository<ProductionRawDataEntity,Long>
{
	@Query(value = "SELECT * FROM pdm_production_info WHERE calendar_date>= :fdate and calendar_date<= :tdate and production !='Null'",nativeQuery = true)
	// @Query(value = "SELECT * FROM pdm_production_info",nativeQuery = true)
   	public List<ProductionRawDataEntity> findAllFiltered_data(@Param("fdate") String from_date, @Param("tdate") String to_date);
}
