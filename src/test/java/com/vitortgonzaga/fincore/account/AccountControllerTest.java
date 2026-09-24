package com.vitortgonzaga.fincore.account;

import com.vitortgonzaga.fincore.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldCreateAccount() throws Exception{

        String requestBody = """
                {
                    "ownerName": "Vitor"
                }
                """;

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ownerName").value("Vitor"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.status").value(AccountStatus.ACTIVE.name()));
    }

    @Test
    void shouldRejectAccountWithBlankOwnerName() throws Exception {
        String requestBody = """
                {
                    "ownerName": " "
                }
                """;

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldRejectUnknownFields() throws Exception {
        String requestBody = """
                {
                    "ownerName" : "Vitor",
                    "status" : "INACTIVE"
                }
                """;

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectOwnerNameLongerThan255Characters() throws Exception {
        String ownerName = "a".repeat(256);

        String requestBody = """
                {
                    "ownerName" = "%s"
                }
                """.formatted(ownerName);

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindAccountById() throws Exception{
        Account account = new Account("Vitor");
        Account savedAccount = accountRepository.save(account);
        UUID accountId = savedAccount.getId();

        mockMvc.perform(get("/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("Vitor"))
                .andExpect(jsonPath("$.status").value(AccountStatus.ACTIVE.name()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.id").value(savedAccount.getId().toString()));
    }
}
