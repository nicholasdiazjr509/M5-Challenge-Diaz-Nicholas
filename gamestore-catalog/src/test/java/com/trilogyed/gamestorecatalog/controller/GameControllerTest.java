package com.trilogyed.gamestorecatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.gamestorecatalog.model.Game;
import com.trilogyed.gamestorecatalog.repository.GameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the gameRepo layer.
    // Therefore, mock the gameRepo layer.
    @MockBean
    private GameRepository gameRepo;
    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    @Test
    public void shouldCreateGame() throws Exception{
        //Object to JSON in String
        String outputJson;
        String inputJson;

        //Arrange
        Game  inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(51L);

        inputJson = mapper.writeValueAsString(inGame);
        System.out.println(inputJson);

        Game savedGame = new Game ();
        savedGame.setTitle("Halo");
        savedGame.setEsrbRating("E10+");
        savedGame.setDescription("Puzzles and Math");
        savedGame.setPrice(new BigDecimal("23.99"));
        savedGame.setStudio("Xbox Game Studios");
        savedGame.setQuantity(5);
        savedGame.setId(51L);

        outputJson = mapper.writeValueAsString(savedGame);

        //Mock call to gameRepo layer...
        when(gameRepo.save(inGame)).thenReturn(savedGame);

        //Act & Assert
        this.mockMvc.perform(post("/game")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnGameInfo() throws Exception{

        //Object to JSON in String
        String outputJson = null;

        //Arrange
        Game  Game = new Game ();
        Game.setTitle("Halo");
        Game.setEsrbRating("E10+");
        Game.setDescription("Puzzles and Math");
        Game.setPrice(new BigDecimal("23.99"));
        Game.setStudio("Xbox Game Studios");
        Game.setQuantity(5);
        Game.setId(8);

        outputJson = mapper.writeValueAsString(Game);

        // Mocking DAO response
        // This is another way to mock using mockito
        // same as doReturn(Game).when(gameRepo).getGame(8);
        // We could also set up our mocks in a separate method, if we so chose
        when(gameRepo.findById(8L)).thenReturn(Optional.of(Game));

        //Act & Assert
        this.mockMvc.perform(get("/game/8"))
                .andDo(print())
                .andExpect(status().isOk())
                //use the objectmapper output with the json method
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldFailGetGameBadIdReturns404() throws Exception {

        long idForGameThatDoesNotExist = 100;

        //Arrange
        Game  game = new Game();
        when(gameRepo.findById(idForGameThatDoesNotExist)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game/" + idForGameThatDoesNotExist))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateGame() throws Exception{
        //Object to JSON in String
        String inputJson=null;

        //Arrange
        Game  inGame = new Game ();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(62);

        inputJson = mapper.writeValueAsString(inGame);

        //Mock call to gameRepo layer...
        //Nothing to mock!
        //Checking for the correct response status code
        doNothing().when(gameRepo).save(inGame);

        //Act & Assert
        this.mockMvc.perform(put("/game")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
    @Test
    public void shouldDeleteGame() throws Exception{
        //Object to JSON in String
        String inputJson=null;

        //Arrange
        Game  inGame = new Game ();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(62);

        inputJson = mapper.writeValueAsString(inGame);

        //Mock call to gameRepo layer...
        //Nothing to mock!
        //Checking for the correct response status code
        doNothing().when(gameRepo).deleteById(62L);

        //Act & Assert
        this.mockMvc.perform(delete("/game/62"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldGetGamesByTitle() throws Exception{
        //Object to JSON in String
        String outputJson;

        //Arrange
        Game  savedGame1 = new Game ();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(new BigDecimal("23.99"));
        savedGame1.setStudio("Xbox Game Studios");
        savedGame1.setQuantity(5);
        savedGame1.setId(56);

        Game  savedGame2 = new Game ();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(new BigDecimal("23.99"));
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setId(51);

        Game  savedGame3 = new Game ();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E10+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(new BigDecimal("23.99"));
        savedGame3.setStudio("Xbox Game Studios");
        savedGame3.setQuantity(5);
        savedGame3.setId(77);

        List<Game > foundList = new ArrayList<>();
        foundList.add(savedGame1);

        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to gameRepo layer...
        when(gameRepo.findAllByTitle("Halo")).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/game/title/Halo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to gameRepo layer...
        when(gameRepo.findAllByTitle("not there")).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game/title/{title}}","not there"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetGamesByEsrbRating() throws Exception{
        //Object to JSON in String
        String outputJson;

        //Arrange
        Game savedGame1 = new Game ();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(new BigDecimal("23.99"));
        savedGame1.setStudio("Xbox Game Studios");
        savedGame1.setQuantity(5);
        savedGame1.setId(56);

        Game  savedGame2 = new Game ();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(new BigDecimal("23.99"));
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setId(51);

        Game savedGame3 = new Game();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E18+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(new BigDecimal("23.99"));
        savedGame3.setStudio("Xbox Game Studios");
        savedGame3.setQuantity(5);
        savedGame3.setId(77);

        List<Game> foundList = new ArrayList<>();
        foundList.add(savedGame1);
        foundList.add(savedGame2);

        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to gameRepo layer...
        when(gameRepo.findAllByEsrbRating("10+")).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/game/esrbrating/10+"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to gameRepo layer...
        when(gameRepo.findAllByEsrbRating("not there")).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game/esrbrating/{esrb}", "not there"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetGamesByStudio() throws Exception{
        //Object to JSON in String
        String outputJson;

        //Arrange
        Game savedGame1 = new Game();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(new BigDecimal("23.99"));
        savedGame1.setStudio("A&E");
        savedGame1.setQuantity(5);
        savedGame1.setId(56);

        Game savedGame2 = new Game ();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(new BigDecimal("23.99"));
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setId(51);

        Game savedGame3 = new Game();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E18+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(new BigDecimal("23.99"));
        savedGame3.setStudio("A&E");
        savedGame3.setQuantity(5);
        savedGame3.setId(77);

        List<Game> foundList = new ArrayList<>();
        foundList.add(savedGame1);
        foundList.add(savedGame3);

        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to gameRepo layer...
        when(gameRepo.findAllByStudio("A&E")).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/game/studio/{studio}","A&E"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to gameRepo layer...
        when(gameRepo.findAllByStudio("not there")).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game/studio/{studio}", "not there"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllGames() throws Exception{
        //Object to JSON in String
        String outputJson;

        //Arrange
        Game savedGame1 = new Game();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(new BigDecimal("23.99"));
        savedGame1.setStudio("A&E");
        savedGame1.setQuantity(5);
        savedGame1.setId(56L);

        Game savedGame2 = new Game();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(new BigDecimal("23.99"));
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setId(51L);

        Game savedGame3 = new Game();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E18+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(new BigDecimal("23.99"));
        savedGame3.setStudio("A&E");
        savedGame3.setQuantity(5);
        savedGame3.setId(77L);

        List<Game> foundList = new ArrayList<>();
        foundList.add(savedGame1);
        foundList.add(savedGame3);

        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to gameRepo layer...
        when(gameRepo.findAll()).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/game"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to gameRepo layer...
        when(gameRepo.findAll()).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/game"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void shouldFailCreateGameWithInvalidData() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Arrange
        //title...
        Game inGame = new Game();
        inGame.setTitle("");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //So we are mocking (not executing the gameRepo layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.gameRepo.save(inGame)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Esrb...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.


        //Desc...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Studio...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Price...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("-1.00"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("60000"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(null);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //quantity
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(0);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(50001);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldFailUpdateGameWithInvalidData() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Arrange
        //title...
        Game inGame = new Game();
        inGame.setTitle("");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(77);

        //So we are mocking (not executing the gameRepo layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.gameRepo.save(inGame)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Esrb...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(77);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.


        //Desc...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Studio...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("");
        inGame.setQuantity(5);
        inGame.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Price...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("-1.00"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("60000"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(null);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //quantity
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(0);
        inGame.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(50001);
        inGame.setId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldFailFindGamesWithInvalidData() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Arrange
        //invalid id...
        Game inGame = new Game();
        inGame.setTitle("something");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(new BigDecimal("23.99"));
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setId(77);

        //So we are mocking (not executing the gameRepo layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.gameRepo.findById(77L)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/game/77") //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.

        String badValue = "bad value";
        //So we are mocking (not executing the gameRepo layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.gameRepo.findAllByStudio(badValue)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/game/studio/{badValue}", badValue) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.

        when(this.gameRepo.findAllByEsrbRating(badValue)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/game/esrbrating/{badValue}", badValue) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.


        when(this.gameRepo.findAllByTitle(badValue)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/game/title/{badValue}", badValue) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.
    }
}