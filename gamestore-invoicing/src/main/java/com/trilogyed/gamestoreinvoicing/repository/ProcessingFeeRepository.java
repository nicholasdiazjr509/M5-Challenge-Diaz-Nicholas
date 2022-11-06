package com.trilogyed.gamestoreinvoicing.repository;

import com.trilogyed.gamestoreinvoicing.model.ProcessingFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingFeeRepository extends JpaRepository<ProcessingFee, String> {
}
