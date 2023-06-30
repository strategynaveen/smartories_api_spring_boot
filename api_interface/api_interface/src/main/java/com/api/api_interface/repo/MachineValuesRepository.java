package com.api.api_interface.repo;

import com.api.api_interface.entity.MachineValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachineValuesRepository extends JpaRepository<MachineValuesEntity,String>
{
    // @Query(value = "SELECT * FROM settings_machine_current WHERE machine_id = :ids or machine_id = :id", nativeQuery = true)
    // public List<MachineValuesEntity> findProductsByIds(@Param("ids") String ids, @Param("id") String id);
}
