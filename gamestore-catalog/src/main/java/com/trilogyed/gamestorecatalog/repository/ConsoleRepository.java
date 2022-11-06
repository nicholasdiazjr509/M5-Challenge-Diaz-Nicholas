package com.trilogyed.gamestorecatalog.repository;

import com.trilogyed.gamestorecatalog.model.Console;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

@Repository
public interface ConsoleRepository extends JpaRepository<Console, Long> {
    List<Console> findAllByManufacturer(String manufacturer);


    List<Console> findAllById(long id);
}
