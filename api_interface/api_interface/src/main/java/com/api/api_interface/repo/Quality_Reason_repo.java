package com.api.api_interface.repo;

import com.api.api_interface.entity.Quality_ReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface Quality_Reason_repo extends JpaRepository<Quality_ReasonEntity ,String> {

}
