package com.trilogyed.gamestorecatalog.controller;

import com.trilogyed.gamestorecatalog.model.Game;
import com.trilogyed.gamestorecatalog.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = {"http://localhost:7475"})
public class GameController {

    @Autowired
    GameRepository gameRepo;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody @Valid Game game) {
        Game game2 = gameRepo.save(game);
        return game2;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Game getGameInfo(@PathVariable("id") long gameId) {
        Optional<Game> game = gameRepo.findById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found for id " + gameId);
        } else {
            return (game.get());
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGame(@RequestBody @Valid Game game) {
        if(game == null || game.getId()< 1) {
            throw new IllegalArgumentException("Game does not exist.");
        }else if (game.getId() > 0){
            gameRepo.save(game);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable("id") long gameId) {
        gameRepo.deleteById(gameId);
    }

    @GetMapping("/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGamesByTitle(@PathVariable("title") String title) {
        List<Game> gamesByTitle =   gameRepo.findAllByTitle(title);

        if (gamesByTitle == null || gamesByTitle.isEmpty()) {
            throw new IllegalArgumentException("No games were found with " + title);
        } else {
            return gamesByTitle;
        }
    }

    @GetMapping("/esrbrating/{esrb}")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGamesByEsrbRating(@PathVariable("esrb") String esrb) {
        List<Game> gamesByEsrbRating = gameRepo.findAllByEsrbRating(esrb);

        if (gamesByEsrbRating == null || gamesByEsrbRating.isEmpty()) {
            throw new IllegalArgumentException("No games were found with ESRB Rating " + esrb);
        } else {
            return gamesByEsrbRating;
        }
    }

    @GetMapping("/studio/{studio}")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGamesByStudio(@PathVariable("studio") String studio) {
        List<Game> gamesByStudio = gameRepo.findAllByStudio(studio);

        if (gamesByStudio == null || gamesByStudio.isEmpty()) {
            throw new IllegalArgumentException("No games were found from " + studio);
        } else {
            return gamesByStudio;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getAllGames() {
        List<Game> allGames = gameRepo.findAll();

        if (allGames == null || allGames.isEmpty()) {
            throw new IllegalArgumentException("No games were found.");
        } else {
            return allGames;
        }
    }
}
