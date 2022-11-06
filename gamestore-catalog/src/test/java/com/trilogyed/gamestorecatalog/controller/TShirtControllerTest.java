package com.trilogyed.gamestorecatalog.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.gamestorecatalog.model.TShirt;
import com.trilogyed.gamestorecatalog.repository.TShirtRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
@WebMvcTest(TShirtController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TShirtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the tShirtRepo layer.
    // Therefore, mock the tShirtRepo layer.
    @MockBean
    private TShirtRepository tShirtRepo;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    @Test
    public void shouldCreateTShirt() throws Exception{
        //Object to JSON in String
        String outputJson;
        String inputJson;

        //Arrange
        TShirt inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        TShirt savedTShirt = new TShirt();
        savedTShirt.setQuantity(1);
        savedTShirt.setPrice( new BigDecimal("10.05"));
        savedTShirt.setDescription("Everybody Knows Your Name");
        savedTShirt.setColor("SkyBlue");
        savedTShirt.setSize("M");
        savedTShirt.setId(51);

        outputJson = mapper.writeValueAsString(savedTShirt);

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(savedTShirt);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnTShirtInfo() throws Exception{

        //Object to JSON in String
        String outputJson;

        //Arrange
        TShirt savedTShirt = new TShirt();
        savedTShirt.setQuantity(1);
        savedTShirt.setPrice( new BigDecimal("10.05"));
        savedTShirt.setDescription("Everybody Knows Your Name");
        savedTShirt.setColor("SkyBlue");
        savedTShirt.setSize("M");
        savedTShirt.setId(51);

        outputJson = mapper.writeValueAsString(savedTShirt);

        // Mocking DAO response
        // This is another way to mock using mockito
        // same as doReturn(Game).when(tShirtRepo).getGame(8);
        // We could also set up our mocks in a separate method, if we so chose
        when(tShirtRepo.findById(51L)).thenReturn(Optional.of(savedTShirt));

        //Act & Assert
        this.mockMvc.perform(get("/tshirt/{id}", 51))
                .andDo(print())
                .andExpect(status().isOk())
                //use the objectmapper output with the json method
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateTShirt() throws Exception{
        //Object to JSON in String
        String inputJson;

        //Arrange
        TShirt inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");
        inTShirt.setId(51);

        inputJson = mapper.writeValueAsString(inTShirt);

        TShirt savedTShirt = new TShirt();
        savedTShirt.setQuantity(18);
        savedTShirt.setPrice( new BigDecimal("15.05"));
        savedTShirt.setDescription("Everybody Knows Your Name");
        savedTShirt.setColor("SkyBlue");
        savedTShirt.setSize("M");
        savedTShirt.setId(51);


        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking  for the correct response status code

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteTShirt() throws Exception{
        //Object to JSON in String
        String inputJson=null;

        //Arrange
        TShirt inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");
        inTShirt.setId(51);

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking  for the correct response status code
        doNothing().when(tShirtRepo).deleteById(51L);

        //Act & Assert
        this.mockMvc.perform(delete("/tshirt/{id}",51))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldGetTShirtByColor() throws Exception{
        //Object to JSON in String
        String outputJson;
        //Arrange
        TShirt savedTShirt1 = new TShirt();
        savedTShirt1.setQuantity(1);
        savedTShirt1.setPrice( new BigDecimal("10.05"));
        savedTShirt1.setDescription("Everybody Knows Your Name");
        savedTShirt1.setColor("SkyBlue");
        savedTShirt1.setSize("L");
        savedTShirt1.setId(51);

        TShirt savedTShirt2 = new TShirt();
        savedTShirt2.setQuantity(9);
        savedTShirt2.setPrice( new BigDecimal("20.05"));
        savedTShirt2.setDescription("Everybody Loves Rayman");
        savedTShirt2.setColor("SkyBlue");
        savedTShirt2.setSize("M");
        savedTShirt2.setId(61);

        TShirt savedTShirt3 = new TShirt();
        savedTShirt3.setQuantity(17);
        savedTShirt3.setPrice( new BigDecimal("15.05"));
        savedTShirt3.setDescription("Bart Simpson");
        savedTShirt3.setColor("Yellow");
        savedTShirt3.setSize("S");
        savedTShirt3.setId(88);

        List<TShirt> foundList = new ArrayList<>();
        foundList.add(savedTShirt1);
        foundList.add(savedTShirt2);


        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.findAllByColor("SkyBlue")).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/tshirt/color/{color}","SkyBlue"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.findAllByColor("non-existent color")).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/tshirt/color/{color}","non-existent color"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetTShirtBySize() throws Exception{
        //Object to JSON in String
        String outputJson;

        //Arrange
        TShirt savedTShirt1 = new TShirt();
        savedTShirt1.setQuantity(1);
        savedTShirt1.setPrice( new BigDecimal("10.05"));
        savedTShirt1.setDescription("Everybody Knows Your Name");
        savedTShirt1.setColor("SkyBlue");
        savedTShirt1.setSize("L");
        savedTShirt1.setId(51);

        TShirt savedTShirt2 = new TShirt();
        savedTShirt2.setQuantity(9);
        savedTShirt2.setPrice( new BigDecimal("20.05"));
        savedTShirt2.setDescription("Everybody Loves Rayman");
        savedTShirt2.setColor("SkyBlue");
        savedTShirt2.setSize("M");
        savedTShirt2.setId(61);

        TShirt savedTShirt3 = new TShirt();
        savedTShirt3.setQuantity(17);
        savedTShirt3.setPrice( new BigDecimal("15.05"));
        savedTShirt3.setDescription("Bart Simpson");
        savedTShirt3.setColor("Yellow");
        savedTShirt3.setSize("S");
        savedTShirt3.setId(88);

        List<TShirt> foundList = new ArrayList<>();
        foundList.add(savedTShirt3);


        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.findAllBySize("S")).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/tshirt/size/{size}","S"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.findAllBySize("XL")).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/tshirt/size/{size}","XL"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAllTShirt() throws Exception{
        //Object to JSON in String
        String outputJson;

        //Arrange
        TShirt savedTShirt1 = new TShirt();
        savedTShirt1.setQuantity(1);
        savedTShirt1.setPrice( new BigDecimal("10.05"));
        savedTShirt1.setDescription("Everybody Knows Your Name");
        savedTShirt1.setColor("SkyBlue");
        savedTShirt1.setSize("L");
        savedTShirt1.setId(51);

        TShirt savedTShirt2 = new TShirt();
        savedTShirt2.setQuantity(9);
        savedTShirt2.setPrice( new BigDecimal("20.05"));
        savedTShirt2.setDescription("Everybody Loves Rayman");
        savedTShirt2.setColor("SkyBlue");
        savedTShirt2.setSize("M");
        savedTShirt2.setId(61);

        TShirt savedTShirt3 = new TShirt();
        savedTShirt3.setQuantity(17);
        savedTShirt3.setPrice( new BigDecimal("15.05"));
        savedTShirt3.setDescription("Bart Simpson");
        savedTShirt3.setColor("Yellow");
        savedTShirt3.setSize("S");
        savedTShirt3.setId(88);

        List<TShirt> foundList = new ArrayList<>();
        foundList.add(savedTShirt3);


        outputJson = mapper.writeValueAsString(foundList);

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.findAll()).thenReturn(foundList);

        //Act & Assert
        this.mockMvc.perform(get("/tshirt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.findAll()).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(get("/tshirt"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //test for bad incoming data...
    @Test
    public void shouldFailCreateTShirtWithBadData() throws Exception{
        //Object to JSON in String
        String outputJson = null;
        String inputJson=null;

        //Arrange
        TShirt inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize(null);

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor(null);
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription(null);
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("-1.00"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("1000.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( null);
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(-1);
        inTShirt.setPrice( new BigDecimal("1000.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(70000);
        inTShirt.setPrice( new BigDecimal("1000.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        when(tShirtRepo.save(inTShirt)).thenReturn(null);

        //Act & Assert
        this.mockMvc.perform(post("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldFailUpdateTShirtWithBadData() throws Exception{
        //Object to JSON in String
        String outputJson = null;
        String inputJson;

        //Arrange
        TShirt inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("");
        inTShirt.setId(31);

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking for the correct response status code
//        doNothing().when(tShirtRepo).save(inTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize(null);
        inTShirt.setId(31);

        inputJson = mapper.writeValueAsString(inTShirt);


        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking for the correct response status code
//        doNothing().when(tShirtRepo).save(inTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking  for the correct response status code
//        doNothing().when(tShirtRepo).save(inTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());


        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor(null);
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking  for the correct response status code
//        doNothing().when(tShirtRepo).save(inTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription("");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking for the correct response status code
//        doNothing().when(tShirtRepo).save(inTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("10.05"));
        inTShirt.setDescription(null);
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking for the correct response status code
        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("-1.00"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking for the correct response status code
//        doNothing().when(tShirtRepo).save(inTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( new BigDecimal("1000.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to tShirtRepo layer...
        //Nothing to mock!
        //Checking  for the correct response status code
//        doNothing().when(tShirtRepo).save(inTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(1);
        inTShirt.setPrice( null);
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Mock call to TShirt layer...
        //Nothing to mock!


        //Act & Assert
        this.mockMvc.perform(put("/tshirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(-1);
        inTShirt.setPrice( new BigDecimal("1000.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        inputJson = mapper.writeValueAsString(inTShirt);

        //Arrange
        inTShirt = new TShirt();
        inTShirt.setQuantity(70000);
        inTShirt.setPrice( new BigDecimal("1000.05"));
        inTShirt.setDescription("Everybody Knows Your Name");
        inTShirt.setColor("SkyBlue");
        inTShirt.setSize("M");

        outputJson = mapper.writeValueAsString(inTShirt);

    }

}