package com.trilogyed.gamestorecatalog.controller;

import com.trilogyed.gamestorecatalog.model.Console;
import com.trilogyed.gamestorecatalog.repository.ConsoleRepository;
//import com.trilogyed.gamestorecatalog.tShirtRepo.GameStoreCatalogServiceLayer;
//import com.trilogyed.gamestorecatalog.viewModel.ConsoleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/console")
@CrossOrigin(origins = {"http://localhost:7475"})
public class ConsoleController {

    @Autowired
    ConsoleRepository consoleRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @Valid Console createConsole(@RequestBody @Valid Console console ) {
        consoleRepo.save(console);
        return console ;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Console  getConsoleById(@PathVariable("id") long consoleId) {
        Optional<Console> console = consoleRepo.findById(consoleId);
        if (console == null) {
            throw new IllegalArgumentException("Console could not be retrieved for id " + consoleId);
        } else {
            return (console.get());
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateConsole(@RequestBody @Valid Console console) {
        if (console  == null || console .getId()< 1) {
            throw new IllegalArgumentException("Id in path must match id in view model");
        } else if (console .getId() > 0) {
            consoleRepo.save(console);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsole(@PathVariable("id") long consoleId) {
        consoleRepo.deleteById(consoleId);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    @ResponseStatus(HttpStatus.OK)
    public List<Console> getConsoleByManufacturer(@PathVariable("manufacturer") String manu) {
        List<Console> cvmByManufacturer = consoleRepo.findAllByManufacturer(manu);
        if (cvmByManufacturer == null || cvmByManufacturer.isEmpty()) {
            throw new IllegalArgumentException("No consoles, manufactured by " + manu + ", were found");
        } else
            return cvmByManufacturer;
    }


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Console > getAllConsoles() {
        List<Console > allConsoles = consoleRepo.findAll();
        if (allConsoles == null || allConsoles.isEmpty()) {
            throw new IllegalArgumentException("No consoles were found");
        } else
            return allConsoles;
    }
}
