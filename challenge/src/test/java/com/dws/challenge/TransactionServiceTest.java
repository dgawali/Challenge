package com.dws.challenge;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dws.challenge.domain.Account;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.MoneyTransferAsyncService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransactionServiceTest {

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private MoneyTransferAsyncService moneyTransferAsyncService;

	@Test
	void transferMoney() throws Exception {
		Account accountFrom = new Account("Id-890");
		accountFrom.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(accountFrom);

		Account accountTo = new Account("Id-290");
		accountTo.setBalance(new BigDecimal(500));
		this.accountsService.createAccount(accountTo);

		CompletableFuture<Boolean> check = moneyTransferAsyncService.transferMoney(accountFrom.getAccountId(),
				accountTo.getAccountId(), (new BigDecimal(200)));

		assertTrue(check.get());
	}

}
