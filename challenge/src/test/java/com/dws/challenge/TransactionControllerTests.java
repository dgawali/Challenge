package com.dws.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferMoney;
import com.dws.challenge.exception.InsufficientAmountException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.TransactionService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class TransactionControllerTests {

	private MockMvc mockMvc;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void transferMoneySuccess() throws Exception {
		Account accountFrom = new Account("Id-123");
		accountFrom.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(accountFrom);

		Account accountTo = new Account("Id-456");
		accountTo.setBalance(new BigDecimal(500));
		this.accountsService.createAccount(accountTo);

		TransferMoney transferMoney = new TransferMoney(accountFrom.getAccountId(), accountTo.getAccountId(),
				(new BigDecimal(200)));
		this.transactionService.transferMoney(transferMoney);

		assertThat(this.accountsService.getAccount("Id-123").getBalance().equals(800));
		assertThat(this.accountsService.getAccount("Id-456").getBalance().equals(700));
	}

	@Test
	void transferMoneyInsufficientBalance() throws Exception {
		Account accountFrom = new Account("Id-111");
		accountFrom.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(accountFrom);

		Account accountTo = new Account("Id-222");
		accountTo.setBalance(new BigDecimal(500));
		this.accountsService.createAccount(accountTo);

		TransferMoney transferMoney = new TransferMoney(accountFrom.getAccountId(), accountTo.getAccountId(),
				(new BigDecimal(2000)));

		try {
			this.transactionService.transferMoney(transferMoney);
			fail("Fail due to insufficient balance");
		} catch (InsufficientAmountException ex) {
			assertThat(ex.getMessage()).isEqualTo("Account id : " + transferMoney.getAccountFromId()
					+ " InsufficientAmount : " + transferMoney.getAmount());
		}
	}
}
