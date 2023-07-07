package org.fffd.l23o6.SumUpTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@WebMvcTest
public class TrainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTrainLists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/train"));
    }


    @Test
    public void getTrainById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/train/0"));
    }

    @Test
    public void getTrainAdminById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/train/0"));
    }

    @Configuration
    @EnableWebMvc
    static class Config{

    }
}
