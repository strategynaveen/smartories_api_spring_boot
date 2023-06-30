package com.api.api_interface.repo;

import com.api.api_interface.entity.DowntimeRawDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DowntimeRawDataRepository extends JpaRepository<DowntimeRawDataEntity,String>
{
//    @Query(value = "SELECT t.machine_id,t.downtime_reason_id,t.tool_id,t.part_id,t.shift_date,t.start_time,t.end_time,t.split_duration,t.calendar_date,r.downtime_category,r.downtime_reason FROM pdm_downtime_reason_mapping as t INNER JOIN settings_downtime_reasons as r ON r.downtime_reason_id = t.downtime_reason_id", nativeQuery = true)
    // @Query(value = "SELECT * FROM pdm_downtime_reason_mapping as t WHERE t.machine_event_id = :ids", nativeQuery = true)
    // public List<DowntimeRawDataEntity> findProductsByIds(@Param("ids") String ids);
//    public List<DowntimeRawDataEntity> findProductsByIds();
	@Query(value = "SELECT * FROM pdm_downtime_reason_mapping WHERE shift_date>= ?1 and shift_date<= ?2 ",nativeQuery = true)
	// @Query(value = "SELECT * FROM pdm_production_info",nativeQuery = true)
   	public List<DowntimeRawDataEntity> findProductsByIds(String from_date, String to_date);

}
