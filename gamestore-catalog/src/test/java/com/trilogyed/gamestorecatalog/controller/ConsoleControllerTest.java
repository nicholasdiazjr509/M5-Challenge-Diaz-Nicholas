package com.trilogyed.gamestorecatalog.controller;

import com.trilogyed.gamestorecatalog.model.Console;
import com.trilogyed.gamestorecatalog.repository.ConsoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConsoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConsoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the consoleRep layer.
    // Therefore, mock the consoleRep layer.
    @MockBean
    private ConsoleRepository consoleRepo;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    @Test
    public void shouldReturnNewConsoleOnPostRequest() throws Exception {

        //perform the call, pass arguments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console...
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(12);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("199.89"));
        inConsole.setId(15);

        //Mock "out"put Console...
        Console outConsole = new Console();
        outConsole.setMemoryAmount("250GB");
        outConsole.setQuantity(12);
        outConsole.setManufacturer("Sega");
        outConsole.setModel("Nintendo");
        outConsole.setProcessor("AMD");
        outConsole.setPrice(new BigDecimal("199.89"));
        outConsole.setId(15);

        // the following mocks the consoleRep layer's method "createConsoleViewModel"
        // So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.

        // Could also have written this as the below:
        // doReturn(outConsole).when(this.consoleRep).createConsole(inConsole);
        when(this.consoleRepo.save(inConsole)).thenReturn(outConsole);

        mockMvc.perform(
                        post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(outConsole))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturnConsoleById() throws Exception{
        //Arrange
        //Mock "out"put Console...
        Console outConsole = new Console();
        outConsole.setMemoryAmount("250GB");
        outConsole.setQuantity(12);
        outConsole.setManufacturer("Sega");
        outConsole.setModel("Nintendo");
        outConsole.setProcessor("AMD");
        outConsole.setPrice(new BigDecimal("199.89"));
        outConsole.setId(15);

        // So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(consoleRepo.findById(15L)).thenReturn(Optional.of(outConsole));

        // Note the way we're passing argument in the Get...
        // Note how to expect a certain value in the returned JSON object.
        //Act & Assert
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(15));
    }

    @Test
    public void shouldReturn204StatusWithGoodUpdate() throws Exception {
        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console...
        Console inConsole = new Console();
        inConsole.setMemoryAmount("300GB");
        inConsole.setQuantity(12);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo II");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("249.99"));
        inConsole.setId(15L);


        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        //Remember: we are testing the code of the CONTROLLER methods.
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldReturn404StatusWithBadIdUpdateRequest() throws Exception {
        Console  inConsole = new Console();
        inConsole.setMemoryAmount("300GB");
        inConsole.setQuantity(12);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo II");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("249.99"));
        inConsole.setId(0);//<--pretend this is a bad id that does not match any existing Console...

        //mock call to controller and force an exception
        doThrow(new IllegalArgumentException("Console not found. Unable to update")).when(consoleRepo).save(inConsole);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNotFound()); //Expected response status code.
    }

    @Test
    public void shouldDeleteConsoleReturnNoContent() throws Exception{
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        //Remember: we are testing the code of the CONTROLLER methods.
        doNothing().when(consoleRepo).deleteById(15L);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/console/{id}",15))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldReturnConsoleByManufacturer() throws Exception {

        List<Console > consoleViewModelList = new ArrayList<>();

        //Mock a list of Consoles...

        //1st Console...
        Console  outConsole1 = new Console ();
        outConsole1.setMemoryAmount("250GB");
        outConsole1.setQuantity(12);
        outConsole1.setManufacturer("Sony");
        outConsole1.setModel("PS4");
        outConsole1.setProcessor("AMD");
        outConsole1.setPrice(new BigDecimal("499.89"));
        outConsole1.setId(15);

        consoleViewModelList.add(outConsole1);

        //2nd Console...
        outConsole1 = new Console ();
        outConsole1.setMemoryAmount("200GB");
        outConsole1.setQuantity(12);
        outConsole1.setManufacturer("Sony");
        outConsole1.setModel("PS2");
        outConsole1.setProcessor("AMD");
        outConsole1.setPrice(new BigDecimal("249.99"));
        outConsole1.setId(16);

        consoleViewModelList.add(outConsole1);

        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        //Remember: we are testing the methods in the CONTROLLER.
        when(consoleRepo.findAllByManufacturer("Sony")).thenReturn(consoleViewModelList);

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console/manufacturer/{manufacturer}", "Sony")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(consoleViewModelList)));
    }

    @Test
    public void shouldReturnAllConsoles() throws Exception {

        List<Console > consoleViewModelList = new ArrayList<>();

        //Mock a list of Consoles...

        //1st Console...
        Console  outConsole1 = new Console ();
        outConsole1.setMemoryAmount("250GB");
        outConsole1.setQuantity(12);
        outConsole1.setManufacturer("Sony");
        outConsole1.setModel("PS4");
        outConsole1.setProcessor("AMD");
        outConsole1.setPrice(new BigDecimal("499.89"));
        outConsole1.setId(15);

        consoleViewModelList.add(outConsole1);

        //2nd Console...
        outConsole1 = new Console ();
        outConsole1.setMemoryAmount("200GB");
        outConsole1.setQuantity(12);
        outConsole1.setManufacturer("Sony");
        outConsole1.setModel("PS2");
        outConsole1.setProcessor("AMD");
        outConsole1.setPrice(new BigDecimal("249.99"));
        outConsole1.setId(16);

        consoleViewModelList.add(outConsole1);

        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        //Remember: we are testing the methodse in the CONTROLLER.
        when(consoleRepo.findAll()).thenReturn(consoleViewModelList);

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(consoleViewModelList)));

        when(consoleRepo.findAll()).thenReturn(null);

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNotFound());
    }

    //Testing bad cases...

    @Test
    public void shouldFailCreateConsoleWithInvalidQuantity() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with 0 quantity
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(51000);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("199.89"));

        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.consoleRepo.save(inConsole)).thenReturn (null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Mock "in"coming Console  with > 50,000 quantity
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(50001);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("199.89"));

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldFailCreateConsoleWithInvalidPrice() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with null price
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(null);

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.consoleRepo.save(inConsole)).thenReturn(null);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Mock "in"coming Console  with no price
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("1000.00"));

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Mock "in"coming Console  with no price
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(BigDecimal.ZERO);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;
    }

    @Test
    public void shouldFailCreateConsoleInvalidManufacturer() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with 0 quantity
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer(null);
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("10.99"));

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.consoleRepo.save(inConsole)).thenReturn(null);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;
    }

    @Test
    public void shouldFailCreateConsoleInvalidModel() throws Exception {
        //perform the call, pass arguments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with 0 quantity
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("10.99"));

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(this.consoleRepo.save(inConsole)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Mock "in"coming Console  with 0 quantity
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel(null);
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("10.99"));

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;
    }

    @Test
    public void shouldFailUpdateConsoleInvalidModel() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with 0 quantity
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("10.99"));
        inConsole.setId(15L);


        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Mock "in"coming Console  with 0 quantity
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel(null);
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("10.99"));
        inConsole.setId(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;
    }

    @Test
    public void shouldFailUpdateConsoleInvalidQuantity() throws Exception {
        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with 0 quantity
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(0);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("199.89"));
        inConsole.setId(15);

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Mock "in"coming Console  with > 50,000 quantity
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(50001);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("199.89"));

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;
    }

    @Test
    public void shouldFailUpdateConsoleInvalidPrice() throws Exception {

        //perform the call, pass argutments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with null price
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(null);
        inConsole.setId(15L);

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Mock "in"coming Console  with no price
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("1000.00"));
        inConsole.setId(15L);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Mock "in"coming Console  with no price
        inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer("Sega");
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(BigDecimal.ZERO);
        inConsole.setId(15L);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;
    }

    @Test
    public void shouldFailUpdateConsoleInvalidManufacturer() throws Exception {
        //perform the call, pass arguments (path variables & requestBody), use objectMapper to convert objects
        // from/to JSON format.

        //Mock "in"coming Console  with 0 quantity
        Console inConsole = new Console();
        inConsole.setMemoryAmount("250GB");
        inConsole.setQuantity(2);
        inConsole.setManufacturer(null);
        inConsole.setModel("Nintendo");
        inConsole.setProcessor("AMD");
        inConsole.setPrice(new BigDecimal("10.99"));
        inConsole.setId(15);

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/console")
                                .content(mapper.writeValueAsString(inConsole)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;
    }
    @Test
    public void shouldFailGetConsoleWithBadId() throws Exception{
        //Mock "out"put Console...
        Console outConsole = new Console();
        outConsole.setMemoryAmount("250GB");
        outConsole.setQuantity(12);
        outConsole.setManufacturer("Sega");
        outConsole.setModel("Nintendo");
        outConsole.setProcessor("AMD");
        outConsole.setPrice(new BigDecimal("199.89"));
        outConsole.setId(15);

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        // Remember: we are testing the code of the CONTROLLER methods.
        when(consoleRepo.findById(16L)).thenReturn(null);

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console/{id}", 16L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNotFound())
        ;

    }
    @Test
    public void shouldFailGetConsoleByManufacturerWithInvalidManufacturer() throws Exception {
        List<Console> console = new ArrayList<>();
    //Mock a list of Consoles...
        //1st Console...
        Console outConsole1 = new Console();
        outConsole1.setMemoryAmount("250GB");
        outConsole1.setQuantity(12);
        outConsole1.setManufacturer("Sony");
        outConsole1.setModel("PS4");
        outConsole1.setProcessor("AMD");
        outConsole1.setPrice(new BigDecimal("499.89"));
        outConsole1.setId(15);

        console.add(outConsole1);

        //2nd Console...
        outConsole1 = new Console();
        outConsole1.setMemoryAmount("200GB");
        outConsole1.setQuantity(12);
        outConsole1.setManufacturer("Sony");
        outConsole1.setModel("PS2");
        outConsole1.setProcessor("AMD");
        outConsole1.setPrice(new BigDecimal("249.99"));
        outConsole1.setId(16);

        console.add(outConsole1);

        //the following mocks the consoleRep layer's method "createConsoleViewModel"
        //So we are mocking (not executing the consoleRep layer) since we are testing the controller here.
        //Remember: we are testing the code of the CONTROLLER methods.
        when(consoleRepo.findAllByManufacturer("Sony")).thenReturn(null);

        mockMvc.perform( MockMvcRequestBuilders
                        .get("/console/manufacturer/{manufacturer}", "Sony")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNotFound());
    }
}