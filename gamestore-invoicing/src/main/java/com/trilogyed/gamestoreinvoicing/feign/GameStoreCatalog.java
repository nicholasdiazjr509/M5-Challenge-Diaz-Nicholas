package com.trilogyed.gamestoreinvoicing.feign;

import com.trilogyed.gamestoreinvoicing.model.Console;
import com.trilogyed.gamestoreinvoicing.model.Game;
import com.trilogyed.gamestoreinvoicing.model.TShirt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "gamestore-catalog")
public interface GameStoreCatalog {

/** console */
    @RequestMapping(value = "/console", method = RequestMethod.GET)
    public List<Console> getAllConsoles();

    @RequestMapping(value = "/console/{id}", method = RequestMethod.GET)
    public Console getConsoleById(@PathVariable("id") long consoleId);

    @RequestMapping(value = "/console", method = RequestMethod.POST)
    public Console addConsole(Console consoleViewModel);
//    thought maybe parameter should have @PathVariable? dont think so. or RequestBody!

    @RequestMapping(value = "/console", method = RequestMethod.PUT)
    public Console updateConsole(Console consoleViewModel);

    @RequestMapping(value = "/console/{id}", method = RequestMethod.DELETE)
    public void deleteConsole(@PathVariable("id") long consoleId);

/** game */
    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public List<Game> getAllGames();

    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public Game getGameById(@PathVariable("id") long gameId);

    @RequestMapping(value = "/game", method = RequestMethod.POST)
    public Game addGame(Game gameViewModel);

    @RequestMapping(value = "/game", method = RequestMethod.PUT)
    public Game updateGame(Game gameViewModel);

    @RequestMapping(value = "/game/{id}", method = RequestMethod.DELETE)
    public void deleteGame(@PathVariable("id") long gameId);


/** tshirt */
    @RequestMapping(value = "/tshirt", method = RequestMethod.GET)
    public List<TShirt> getAlltshirts();

    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.GET)
    public TShirt getTshirtById(@PathVariable("id") long tshirtId);

    @RequestMapping(value = "/tshirt", method = RequestMethod.POST)
    public TShirt addtshirt(TShirt tshirtViewModel);

    @RequestMapping(value = "/tshirt", method = RequestMethod.PUT)
    public TShirt updateConsole(TShirt tshirtViewModel);

    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.DELETE)
    public void deleteTshirt(@PathVariable("id") long thsirtId);

}

