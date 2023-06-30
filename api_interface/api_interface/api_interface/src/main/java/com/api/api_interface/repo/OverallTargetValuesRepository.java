package com.api.api_interface.repo;

import com.api.api_interface.entity.OverallTargetValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OverallTargetValuesRepository extends JpaRepository<OverallTargetValuesEntity,Float>
{
    @Query(value = "SELECT * FROM `settings_financial_metrics_goals` ORDER BY `last_updated_on` DESC LIMIT 1", nativeQuery = true)
    public List<OverallTargetValuesEntity> findProducts();
}
