package com.vitortgonzaga.fincore.account;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    @Test
    void shouldCreateAnActiveAccount(){

        AccountRepository accountRepository = mock(AccountRepository.class);
        AccountService accountService = new AccountService(accountRepository);
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account createdAccount = accountService.create("Vitor");

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();

        assertThat(savedAccount.getOwnerName()).isEqualTo("Vitor");
        assertThat(savedAccount.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getCreatedAt()).isNotNull();
        assertThat(savedAccount).isSameAs(createdAccount);
    }

    @Test
    void shouldNotCallRepositoryWhenOwnerNameIsBlank(){

        AccountRepository accountRepository = mock(AccountRepository.class);
        AccountService accountService = new AccountService(accountRepository);

        assertThatThrownBy(() -> accountService.create(" ")).isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(accountRepository);
    }

    @Test
    void shouldFindAccountById(){
        AccountRepository accountRepository = mock(AccountRepository.class);
        AccountService accountService = new AccountService(accountRepository);

        Account createdAccount = new Account("Vitinho");
        UUID accountId = createdAccount.getId();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(createdAccount));

        Account foundAccount = accountService.findById(accountId);

        assertThat(foundAccount).isSameAs(createdAccount);
        verify(accountRepository).findById(accountId);

    }

    @Test
    void shouldThrowWhenAccountIsNotFound(){
        AccountRepository accountRepository = mock(AccountRepository.class);
        AccountService accountService = new AccountService(accountRepository);

        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findById(accountId))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage("Account not found: " + accountId);

        verify(accountRepository).findById(accountId);
    }
}
