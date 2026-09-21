package com.vitortgonzaga.fincore.account;

import com.vitortgonzaga.fincore.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAccount() throws Exception{

        String requestBody = """
                {
                    "ownerName": "Vitor"
                }
                """;

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
    }
}
