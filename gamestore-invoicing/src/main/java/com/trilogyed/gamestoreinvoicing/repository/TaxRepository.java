package com.trilogyed.gamestoreinvoicing.repository;

import com.trilogyed.gamestoreinvoicing.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, String> {
}
