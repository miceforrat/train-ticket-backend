package org.fffd.l23o6.SumUpTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@WebMvcTest
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    public void testListGet() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/station")).andReturn();
    }

    @Test
    public void testGetStation() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/station/3"));
    }

    @Test
    public void testAddStation() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/station")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"杭州\",}"));
    }


    @Configuration
    @EnableWebMvc
    static class Config{

    }
}
