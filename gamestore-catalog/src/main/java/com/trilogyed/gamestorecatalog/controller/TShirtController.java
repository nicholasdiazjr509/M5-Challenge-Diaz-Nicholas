package com.trilogyed.gamestorecatalog.controller;

import com.trilogyed.gamestorecatalog.model.TShirt;
import com.trilogyed.gamestorecatalog.repository.TShirtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tshirt")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TShirtController {

    @Autowired
    TShirtRepository tShirtRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TShirt createTShirt(@RequestBody @Valid TShirt tShirt) {
        tShirt = tShirtRepo.save(tShirt);
        return tShirt;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TShirt getTShirtById(@PathVariable("id") Long tShirtId) {
        Optional<TShirt> tShirt = tShirtRepo.findById(tShirtId);
        if (tShirt == null) {
            throw new IllegalArgumentException("T-Shirt could not be retrieved for id " + tShirtId);
        } else {
            return (tShirt.get());
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTShirt(@RequestBody @Valid TShirt tShirt) {
        if (tShirt == null || tShirt.getId() < 1) {
            throw new IllegalArgumentException("Id in path must match id in view model");
        }else if (tShirt.getId() > 0) {
            tShirtRepo.save(tShirt);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTShirt(@PathVariable("id") Long tShirtId) {
        tShirtRepo.deleteById(tShirtId);
    }

    @GetMapping("/size/{size}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsBySize(@PathVariable("size") String size) {
        List<TShirt> tShirtsBySize = tShirtRepo.findAllBySize(size);
        if (tShirtsBySize == null || tShirtsBySize.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in size " + size);
        }
        return tShirtsBySize;
    }

    @GetMapping("/color/{color}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsByColor(@PathVariable("color") String color) {
        List<TShirt> tShirtsByColor = tShirtRepo.findAllByColor(color);
        if (tShirtsByColor == null || tShirtsByColor.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in " + color);
        }
        return tShirtsByColor;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getAllTShirts() {
        List<TShirt> allTShirts = tShirtRepo.findAll();
        if (allTShirts == null || allTShirts.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found.");
        }
        return allTShirts;
    }
}
