package com.vitortgonzaga.fincore.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import com.vitortgonzaga.fincore.TestcontainersConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void ShouldSaveAndFindAccount(){
        Account account = new Account("Vitor");
        Account savedAccount = accountRepository.save(account);

        entityManager.flush();
        entityManager.clear();

        UUID accountId = savedAccount.getId();

        Optional<Account> foundAccount = accountRepository.findById(accountId);
        Account persistedAccount = foundAccount.orElseThrow();

        assertThat(persistedAccount.getId()).isEqualTo(accountId);
        assertThat(persistedAccount.getOwnerName()).isEqualTo("Vitor");
        assertThat(persistedAccount.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(persistedAccount.getCreatedAt()).isNotNull();


    }

}
