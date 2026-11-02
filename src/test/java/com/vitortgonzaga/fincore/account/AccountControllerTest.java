package com.vitortgonzaga.fincore.account;

import com.vitortgonzaga.fincore.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void cleanDatabase() {
        accountRepository.deleteAll();
    }

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"))
                .andExpect(jsonPath("$.type").value("/errors/validation"))
                .andExpect(jsonPath("$.detail").value("Request has invalid fields"))
                .andExpect(jsonPath("$.errors[0].field").value("ownerName"))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Malformed request"))
                .andExpect(jsonPath("$.type").value("/errors/malformed-request"))
                .andExpect(jsonPath("$.detail").value("Request body is malformed or contains unknown fields"));
    }

    @Test
    void shouldRejectOwnerNameLongerThan255Characters() throws Exception {
        String ownerName = "a".repeat(256);

        String requestBody = """
                {
                    "ownerName": "%s"
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

    @Test
    void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception{
        UUID accountId = UUID.randomUUID();

        mockMvc.perform(get("/accounts/{id}", accountId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Account not found"))
                .andExpect(jsonPath("$.type").value("/errors/account-not-found"))
                .andExpect(jsonPath("$.detail").value("Account not found: " + accountId));
    }

    @Test
    void shouldFindAllAccounts() throws Exception{
        Account account = new Account("vitor");
        Account savedAccount = accountRepository.save(account);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ownerName").value("vitor"))
                .andExpect(jsonPath("$[0].status").value(AccountStatus.ACTIVE.name()))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].id").value(savedAccount.getId().toString()));
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoAccounts() throws Exception{
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenAccountIdIsInvalid() throws Exception{
        String invalidId = "not-a-valid-id";

        mockMvc.perform(get("/accounts/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Parameter"))
                .andExpect(jsonPath("$.type").value("/errors/invalid-parameter"))
                .andExpect(jsonPath("$.detail").value("Invalid value for parameter 'id'"));
    }
}
