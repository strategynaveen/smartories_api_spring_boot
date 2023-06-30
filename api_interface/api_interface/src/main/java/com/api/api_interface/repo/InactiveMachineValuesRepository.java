package com.api.api_interface.repo;

import com.api.api_interface.entity.InactiveMachineValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InactiveMachineValuesRepository extends JpaRepository<InactiveMachineValuesEntity,String>
{
    @Query(value = "SELECT `machine_id`,status,MAX(`last_updated_on`) as last_updated_on  FROM `settings_machine_log` GROUP BY `machine_id`;", nativeQuery = true)
    public List<InactiveMachineValuesEntity> findProductsByIds();
}
