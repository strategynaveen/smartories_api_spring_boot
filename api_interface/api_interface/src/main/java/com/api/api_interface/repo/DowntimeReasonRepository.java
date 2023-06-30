package com.api.api_interface.repo;

import com.api.api_interface.entity.DowntimeReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DowntimeReasonRepository extends JpaRepository<DowntimeReasonEntity,String>
{
    
}
